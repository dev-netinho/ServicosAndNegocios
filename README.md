# Deefy Services — Grupo 2

Camada de **Serviços e Regras de Negócio** do projeto acadêmico-profissional **Deefy** da matéria **Programação Orientada à Objetos II**.

## Objetivo da Sprint 1
- Estruturar a camada de serviços
- Implementar autenticação básica
- Definir e aplicar regras centrais de playlist
- Criar base para busca, avaliação e histórico
- Publicar contratos para o Grupo 3
- Documentar arquitetura, fluxo e comandos

## Stack
- Java 21
- Spring Boot
- Maven
- Spring Web
- Spring Validation
- Springdoc OpenAPI
- JUnit 5 + Mockito

## Rotas de documentação
- JSON OpenAPI: `http://localhost:8080/api/docs`
- Swagger UI: `http://localhost:8080/api/docs-ui`

## Estrutura de pastas
```text
src/
 ├─ main/java/com/deefy/services
 │   ├─ config
 │   ├─ controller
 │   ├─ dto
 │   ├─ exception
 │   ├─ model
 │   ├─ repository
 │   ├─ service
 │   ├─ service/impl
 │   ├─ mapper
 │   └─ util
 └─ test/java/com/deefy/services/service
```

## Comandos principais
```bash
# rodar a aplicacao
mvn spring-boot:run

# compilar
mvn clean compile

# rodar testes
mvn test

# gerar pacote
mvn clean package
```

## Convenções de branch
- `main`: estável
- `develop`: integração do grupo
- `feature/<assunto>`: desenvolvimento de task
- `docs/<assunto>`: documentação

## Responsabilidades principais do Grupo 2
- autenticação e controle de acesso
- gestão de playlists
- busca por música
- avaliação
- histórico
- recomendações e notificações em formato inicial/stub na Sprint 1

## Documentos internos
- `docs/API_E_COMANDOS.md`
- `docs/ENTREGAS_E_RESPONSABILIDADES.md`
- `docs/TRELLO_POVOAMENTO.md`
- `docs/CONTRATO_GRUPO2_PARA_GRUPO3.md`
