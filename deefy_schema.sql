-- ============================================================
-- Deefy — Script de Criação do Banco de Dados
-- Grupo 1: Banco de Dados e Persistência
-- Baseado nas regras de negócio do Grupo 2: Serviços e Negócio
-- ============================================================

-- ============================================================
-- EXTENSÕES (caso use PostgreSQL)
-- ============================================================
-- CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- ============================================================
-- TABELA: usuario
-- Representa quem utiliza o sistema.
-- Regras:
--   - email único
--   - tipo_usuario: COMUM ou ADMIN
-- ============================================================
CREATE TABLE usuario (
    id              BIGSERIAL       PRIMARY KEY,
    nome            VARCHAR(150)    NOT NULL,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    senha           VARCHAR(255)    NOT NULL,
    tipo_usuario    VARCHAR(20)     NOT NULL DEFAULT 'COMUM'
                        CHECK (tipo_usuario IN ('COMUM', 'ADMIN')),
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usuario_email ON usuario (email);


-- ============================================================
-- TABELA: musica
-- Unidade central do escopo atual do sistema.
-- Regras:
--   - campos obrigatórios: titulo, artista, genero, duracao
--   - campos opcionais para integração futura com Deezer:
--     preview_url, capa_url, id_externo
--   - pesquisável por título, artista e gênero
-- ============================================================
CREATE TABLE musica (
    id              BIGSERIAL       PRIMARY KEY,
    titulo          VARCHAR(300)    NOT NULL,
    artista         VARCHAR(200)    NOT NULL,
    genero          VARCHAR(100)    NOT NULL,
    duracao         INT             NOT NULL CHECK (duracao > 0),
    preview_url     VARCHAR(500),
    capa_url        VARCHAR(500),
    id_externo      VARCHAR(100)    UNIQUE,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_musica_titulo   ON musica (titulo);
CREATE INDEX idx_musica_artista  ON musica (artista);
CREATE INDEX idx_musica_genero   ON musica (genero);


-- ============================================================
-- TABELA: playlist
-- Pertence a um usuário e agrupa músicas.
-- Regras:
--   - toda playlist tem um dono (usuario_id)
--   - visibilidade: publica (TRUE) ou privada (FALSE)
--   - apenas o dono pode editar
-- ============================================================
CREATE TABLE playlist (
    id              BIGSERIAL       PRIMARY KEY,
    usuario_id      BIGINT          NOT NULL
                        REFERENCES usuario (id) ON DELETE CASCADE,
    nome            VARCHAR(200)    NOT NULL,
    publica         BOOLEAN         NOT NULL DEFAULT FALSE,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_playlist_usuario ON playlist (usuario_id);


-- ============================================================
-- TABELA: playlist_musica
-- Associação N:N entre playlist e música.
-- Regras:
--   - campo "ordem" permite reordenação das faixas
--   - par (playlist_id, musica_id) deve ser único
-- ============================================================
CREATE TABLE playlist_musica (
    id              BIGSERIAL       PRIMARY KEY,
    playlist_id     BIGINT          NOT NULL
                        REFERENCES playlist (id) ON DELETE CASCADE,
    musica_id       BIGINT          NOT NULL
                        REFERENCES musica (id) ON DELETE CASCADE,
    ordem           INT             NOT NULL DEFAULT 0,
    adicionado_em   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_playlist_musica UNIQUE (playlist_id, musica_id)
);

CREATE INDEX idx_playlist_musica_playlist ON playlist_musica (playlist_id);
CREATE INDEX idx_playlist_musica_musica   ON playlist_musica (musica_id);
CREATE INDEX idx_playlist_musica_ordem    ON playlist_musica (playlist_id, ordem);


-- ============================================================
-- TABELA: historico_execucao
-- Registra o que o usuário ouviu e quando.
-- Regras:
--   - permite múltiplos registros do mesmo usuário
--     para a mesma música em momentos diferentes
--   - base para recomendações futuras
-- ============================================================
CREATE TABLE historico_execucao (
    id                  BIGSERIAL   PRIMARY KEY,
    usuario_id          BIGINT      NOT NULL
                            REFERENCES usuario (id) ON DELETE CASCADE,
    musica_id           BIGINT      NOT NULL
                            REFERENCES musica (id) ON DELETE CASCADE,
    data_hora_execucao  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_historico_usuario ON historico_execucao (usuario_id);
CREATE INDEX idx_historico_musica  ON historico_execucao (musica_id);
CREATE INDEX idx_historico_data    ON historico_execucao (data_hora_execucao);


-- ============================================================
-- TABELA: avaliacao
-- Sistema de avaliação de músicas por usuário.
-- Regras:
--   - nota entre 1 e 5
--   - um usuário avalia uma música apenas uma vez
--     (pode atualizar a nota existente)
-- ============================================================
CREATE TABLE avaliacao (
    id              BIGSERIAL   PRIMARY KEY,
    usuario_id      BIGINT      NOT NULL
                        REFERENCES usuario (id) ON DELETE CASCADE,
    musica_id       BIGINT      NOT NULL
                        REFERENCES musica (id) ON DELETE CASCADE,
    nota            SMALLINT    NOT NULL CHECK (nota BETWEEN 1 AND 5),
    avaliado_em     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_avaliacao_usuario_musica UNIQUE (usuario_id, musica_id)
);

CREATE INDEX idx_avaliacao_usuario ON avaliacao (usuario_id);
CREATE INDEX idx_avaliacao_musica  ON avaliacao (musica_id);


-- ============================================================
-- DADOS DE TESTE
-- ============================================================

-- Usuários
INSERT INTO usuario (nome, email, senha, tipo_usuario) VALUES
    ('Admin Deefy',      'admin@deefy.com',    '$2a$10$hashAdmin',   'ADMIN'),
    ('João Silva',       'joao@email.com',      '$2a$10$hashJoao',    'COMUM'),
    ('Maria Oliveira',   'maria@email.com',     '$2a$10$hashMaria',   'COMUM');

-- Músicas
INSERT INTO musica (titulo, artista, genero, duracao, id_externo) VALUES
    ('Bohemian Rhapsody', 'Queen',           'Rock',       354, 'deezer_001'),
    ('Blinding Lights',   'The Weeknd',      'Pop',        200, 'deezer_002'),
    ('Shape of You',      'Ed Sheeran',      'Pop',        234, 'deezer_003'),
    ('Hotel California',  'Eagles',          'Rock',       391, 'deezer_004'),
    ('Levitating',        'Dua Lipa',        'Pop',        203, 'deezer_005'),
    ('Smells Like Teen',  'Nirvana',         'Rock',       301, 'deezer_006'),
    ('As It Was',         'Harry Styles',    'Pop',        167, 'deezer_007'),
    ('Flowers',           'Miley Cyrus',     'Pop',        200, 'deezer_008');

-- Playlists
INSERT INTO playlist (usuario_id, nome, publica) VALUES
    (2, 'Meus Favoritos', TRUE),
    (2, 'Rock Clássico',  FALSE),
    (3, 'Pop Hits 2024',  TRUE);

-- Playlist_Musica (com ordenação)
INSERT INTO playlist_musica (playlist_id, musica_id, ordem) VALUES
    (1, 1, 1),
    (1, 2, 2),
    (1, 5, 3),
    (2, 1, 1),
    (2, 4, 2),
    (2, 6, 3),
    (3, 2, 1),
    (3, 3, 2),
    (3, 7, 3),
    (3, 8, 4);

-- Histórico de execução (com repetições permitidas)
INSERT INTO historico_execucao (usuario_id, musica_id, data_hora_execucao) VALUES
    (2, 1, '2024-01-10 09:00:00'),
    (2, 2, '2024-01-10 09:06:00'),
    (2, 1, '2024-01-11 14:30:00'),
    (2, 4, '2024-01-12 08:00:00'),
    (3, 3, '2024-01-10 10:00:00'),
    (3, 5, '2024-01-10 10:04:00'),
    (3, 7, '2024-01-11 20:00:00');

-- Avaliações
INSERT INTO avaliacao (usuario_id, musica_id, nota) VALUES
    (2, 1, 5),
    (2, 2, 4),
    (2, 4, 5),
    (3, 3, 4),
    (3, 5, 5),
    (3, 7, 3);
