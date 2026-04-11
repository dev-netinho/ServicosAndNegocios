# Deefy - Grupo 2 | Servicos e Negocio

Repositorio de base organizacional do Grupo 2 do projeto Deefy, da disciplina de Programacao Orientada a Objetos II.

## Visao geral do sistema

O Deefy e uma plataforma profissional de streaming de audio com escopo inicial focado em musica, desenvolvida para Web e com margem futura para evolucao.

O projeto geral da turma esta dividido em quatro grupos:

1. Banco de Dados e Persistencia
2. Servicos e Negocio
3. API REST e Integracao com Deezer
4. Frontend e Player

## Papel do Grupo 2

O Grupo 2 e responsavel exclusivamente pela camada de servicos e regras de negocio.

Escopo funcional do grupo:

- autenticacao e controle de acesso
- gerenciamento de playlists
- servico de busca
- avaliacao
- historico de execucao
- publicacao de contratos e interfaces para integracao com o Grupo 3

## Escopo da Sprint 1 do Grupo 2

Na Sprint 1, este repositorio deve priorizar:

- organizacao inicial do projeto
- documentacao tecnica do grupo
- padroes de desenvolvimento
- definicao de fluxo de trabalho
- contrato documental com o Grupo 3
- registro de decisoes tecnicas, riscos e impedimentos
- preparacao neutra da base Spring Boot + Maven

Esta base nao deve ser usada para adiantar implementacoes individuais dos integrantes.

## Stack escolhida

- Java 17
- Spring Boot
- Maven
- Spring Web
- Spring Validation
- Springdoc OpenAPI

## Estrutura organizacional do projeto

As pastas abaixo devem ser tratadas como estrutura organizacional para receber as implementacoes dos membros, e nao como autorizacao para o Tech Lead preencher features:

```text
deefy-group2-services/
├── docs/
│   ├── contrato-grupo2-grupo3.md
│   ├── decisoes-sprint1.md
│   └── guia-desenvolvimento.md
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── deefy/
    │   │           └── group2/
    │   │               ├── config/
    │   │               ├── dto/
    │   │               │   ├── request/
    │   │               │   └── response/
    │   │               ├── exception/
    │   │               ├── model/
    │   │               ├── repository/
    │   │               │   └── fake/
    │   │               ├── service/
    │   │               │   └── impl/
    │   │               └── util/
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── com/
                └── deefy/
                    └── group2/
                        └── service/
                            └── impl/
```

## Como o grupo deve trabalhar

- cada integrante implementa apenas a propria responsabilidade oficial
- o Tech Lead cuida de arquitetura, documentacao, integracao, revisao e organizacao tecnica
- o repositorio deve manter separacao clara entre base organizacional e implementacao de feature
- qualquer alinhamento entre grupos deve ser registrado documentalmente
- qualquer alteracao relevante de direcao tecnica deve entrar em `docs/decisoes-sprint1.md`

## Convencoes de branch

Padrao recomendado:

```text
docs/<tema>
chore/<tema>
feat/<tema>
fix/<tema>
```

Exemplos:

```text
docs/guia-grupo2
docs/contrato-grupo2-grupo3
chore/base-spring
feat/autenticacao
feat/playlist
feat/busca-avaliacao
feat/historico
```

## Convencoes de commit

Padrao recomendado:

```text
tipo(area): descricao curta
```

Exemplos:

```text
docs(readme): ajusta escopo do grupo 2
docs(contrato): publica fronteira inicial com o grupo 3
chore(base): configura projeto spring boot
feat(auth): inicia autenticacao basica
fix(playlist): corrige validacao de acesso
```

## Limites de atuacao do Grupo 2 e do Tech Lead

O Grupo 2 responde pela camada de servicos e negocio.

O Tech Lead, neste repositorio, pode atuar legitimamente em:

- organizacao do repositorio
- estrutura inicial do projeto
- documentacao
- alinhamento tecnico
- contrato com o Grupo 3
- definicao de padroes
- registro de decisoes, riscos e impedimentos
- revisao tecnica

O Tech Lead nao deve implementar no proprio historico as features individuais dos demais integrantes.

## Integrantes e responsabilidades

- Jose Gomes Cabral Neto: arquitetura, documentacao, integracao e revisao
- Saylon Batista: autenticacao
- Mauricio: playlist
- Israel: busca e avaliacao
- Lucas Henrique: historico
- todos: testes basicos da propria entrega

## Documentos principais

- `docs/guia-desenvolvimento.md`
- `docs/contrato-grupo2-grupo3.md`
- `docs/decisoes-sprint1.md`
