# Deefy Grupo 2 — Notas de integração (busca + base Spring)

Este documento resume o que foi configurado e implementado para o projeto **Serviços e Negócio** ficar **rodável e alinhado ao banco**, para o resto do grupo poder continuar sem depender de contexto solto. Complementa o `README.md` e o `docs/guia-desenvolvimento.md`.

---

## 1. Convenções de Git

- Trabalho alinhado à branch **`staging`** (combinado com o grupo).
- Features pessoais: branches no padrão do guia, por exemplo `feat/busca-avaliacao`.
- **Não commitar** ficheiros com passwords. O ficheiro `application-local.properties` está no `.gitignore`.

---

## 2. O que precisam de instalar

| Ferramenta | Notas |
|------------|--------|
| **JDK 17+** | O `pom.xml` usa `java.version` 17; JDK 21 também funciona. |
| **Maven** | **Opcional**: o projeto tem **Maven Wrapper** (`mvnw` / `mvnw.cmd`). Preferir `.\mvnw.cmd` no Windows. |
| **PostgreSQL local** | **Não é obrigatório** se usarem o Supabase do grupo. |

---

## 3. Base de dados (Supabase / Grupo 1)

- O schema oficial está descrito em **`deefy_schema.sql`** na raiz (tabela **`musica`**, colunas `titulo`, `artista`, `genero`, `duracao`, etc.).
- A entidade JPA **`com.deefy.group2.model.Music`** mapeia `musica` com nomes Java em inglês (`title`, `artist`, `genre`) e `@Column` para os nomes reais em SQL.
- **`spring.jpa.hibernate.ddl-auto=none`** — o Hibernate **não cria nem altera** tabelas; quem manda no schema é o Grupo 1 / scripts SQL.

### 3.1 Ligação JDBC (importante)

- Para a API Java usamos **JDBC** (user + password da base), **não** a anon key nem a service_role do Supabase.
- Em redes **só IPv4**, a ligação **direta** `db.<ref>.supabase.co` pode falhar com `UnknownHostException`. O painel do Supabase recomenda o **Session pooler (Shared pooler)**.
- No repositório, o modelo em **`application-local.properties.example`** usa:
  - host do pooler (ex.: `aws-1-sa-east-1.pooler.supabase.com`);
  - utilizador no formato **`postgres.<PROJECT_REF>`** (não só `postgres`);
  - `sslmode=require` na URL JDBC.

**Cada um** deve:

1. Copiar `application-local.properties.example` → `src/main/resources/application-local.properties`.
2. Preencher a **password** da base (Supabase → Project Settings → Database).
3. Ajustar URL/user se o projeto Supabase do grupo mudar.

### 3.2 Variáveis de ambiente (alternativa)

Em vez do ficheiro local, podem definir no SO (o Spring Boot faz bind automático):

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

---

## 4. Como correr o projeto

Na raiz do repositório (PowerShell):

```powershell
cd <caminho>\ServicosAndNegocios
$env:JAVA_HOME = "<caminho_do_JDK>"   # se necessário
.\mvnw.cmd spring-boot:run
```

- **Porta padrão:** `8080`. Se aparecer *Port 8080 was already in use*, fechem outra instância (`Ctrl+C` noutro terminal) ou mudem `server.port` em `application-local.properties`.
- **Testes:** `.\mvnw.cmd test`

---

## 5. API de busca de músicas (implementado)

### 5.1 Endpoint

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/api/v1/music/search` | Busca / listagem parcial do catálogo |

**Query parameters (nomes em inglês, todos opcionais):**

| Parâmetro | Exemplo | Efeito |
|-----------|---------|--------|
| `title` | `shape` | `LIKE` case-insensitive em `titulo` |
| `artist` | `Queen` | idem em `artista` |
| `genre` | `Rock` | idem em `genero` |

- **Nenhum filtro preenchido** (ou só strings vazias): devolve **até 50** músicas, ordenadas por título (pré-visualização do catálogo / Swagger).
- **Um ou mais filtros:** condições em **AND** (todas têm de ser verdadeiras). Uso interno: **Spring Data JPA Specifications** (`MusicSpecifications`), com escape de `%` e `_` no texto pesquisado.

### 5.2 Formato da resposta (JSON)

```json
{
  "results": [
    {
      "id": 1,
      "title": "...",
      "artist": "...",
      "genre": "...",
      "durationSeconds": 354,
      "previewUrl": null,
      "coverUrl": null,
      "externalId": "...",
      "createdAt": "..."
    }
  ],
  "count": 1
}
```

Lista válida com zero itens: `"results": []`, `"count": 0`.

### 5.3 Documentação interativa (OpenAPI)

- Swagger UI: `http://localhost:8080/api/docs-ui`
- OpenAPI JSON: `http://localhost:8080/api/docs`

---

## 6. Estrutura de código relevante

```text
src/main/java/com/deefy/group2/
├── DeefyGroup2Application.java
├── controller/
│   └── MusicSearchController.java      # GET /api/v1/music/search
├── dto/response/
│   ├── MusicResponseDto.java
│   └── MusicSearchResponseDto.java
├── model/
│   └── Music.java                      # @Table(name = "musica")
├── repository/
│   ├── MusicRepository.java            # JpaRepository + JpaSpecificationExecutor
│   └── MusicSpecifications.java        # filtros dinâmicos AND + LIKE
├── service/
│   ├── MusicSearchService.java
│   └── impl/
│       └── MusicSearchServiceImpl.java

src/test/java/.../MusicSearchServiceImplTest.java   # testes unitários (Mockito)
```

Dependências principais no `pom.xml`: **Spring Web**, **Validation**, **Spring Data JPA**, **PostgreSQL**, **Springdoc OpenAPI**, **spring-boot-starter-test**.

---

## 7. Ficheiros de configuração

| Ficheiro | Função |
|----------|--------|
| `application.properties` | Config geral versionada (porta, JPA, Springdoc, import opcional do local). **Sem secrets.** |
| `application-local.properties` | **Local + gitignored** — URL/user/password da base. |
| `application-local.properties.example` | Modelo **sem password**, seguro para commit. |
| `.gitignore` | Inclui `application-local.properties`, `/target/`, IDE, etc. |

---

## 8. Maven Wrapper

Foram adicionados **`mvnw`**, **`mvnw.cmd`** e **`.mvn/wrapper/maven-wrapper.properties`** para quem não tiver Maven no PATH. A primeira execução descarrega o Maven para a pasta do utilizador (`.m2/wrapper`).

---

## 9. Integração com outros grupos

- **Grupo 3 / Frontend:** consomem esta API por **HTTP + JSON**; o contrato atual da busca é o descrito na secção 5. Ajustes devem ser alinhados e, se possível, refletidos no OpenAPI.
- **Grupo 1:** alterações na tabela `musica` (nomes/tipos de colunas) exigem atualizar a entidade `Music` e, se necessário, DTOs e testes.

---

## 10. Problemas frequentes

| Sintoma | O que verificar |
|---------|------------------|
| `mvn` não reconhecido | Usar `.\mvnw.cmd` em vez de `mvn`. |
| `Failed to configure a DataSource` | Falta `application-local.properties` ou variáveis `SPRING_DATASOURCE_*`. |
| `UnknownHostException` … `supabase` | Preferir **connection string do pooler** IPv4; testar `nslookup` ao host indicado no painel. |
| `Port 8080 was already in use` | Parar outro Spring Boot ou mudar `server.port`. |
| Swagger sem resultados | Parâmetros são `title`, `artist`, `genre` (não `titulo`/`genero`). Sem filtros, a API devolve até 50 linhas se a tabela tiver dados. |
| Filtro “não filtra” | Foi corrigido com `MusicSpecifications`; se regressar, verificar se a query string está a chegar ao controller. |

---

## 11. Responsabilidades (lembrar do guia do grupo)

Pelo `docs/guia-desenvolvimento.md`, cada um implementa a sua parte. A **busca básica** neste repositório entra no escopo combinado para **Israel (busca e avaliação)**; **avaliação** ainda pode ser acrescentada em módulos à parte (`/api/v1/...`) sem colidir com este endpoint, desde que respeitem pacotes e revisão do Tech Lead.

---

## 12. Segurança

- **Nunca** colocar passwords ou service_role keys em ficheiros versionados nem no `README`.
- Se uma password foi exposta (chat, commit, print), **alterar na consola Supabase** e atualizar só o `application-local.properties` local.

---

*Última atualização deste guia: alinhado ao estado do projeto com Spring Boot 3.5, JPA + PostgreSQL (Supabase pooler), busca com Specifications e Maven Wrapper.*
