Markdown
# 📑 Documentação Técnica: Módulo de Autenticação e Registro
> **Projeto:** Deefy - Grupo 2  
> **Objetivo:** Gestão de usuários e controle de acesso (RBAC)

---

# 1. Estrutura do Projeto (Package Hierarchy)
A organização segue o padrão de camadas para garantir o desacoplamento entre a API e as regras de negócio.

```plaintext
src/main/java/com/deefy/group2/
├── controller/
│   └── AuthController.java             # Ponto de entrada da API (REST)
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java           # Entrada para login
│   │   └── UserRegistrationRequest.java # Entrada para cadastro
│   └── response/
│       └── LoginResponse.java          # Resposta pós-autenticação
├── exception/
│   ├── GlobalExceptionHandler.java     # Tratamento centralizado de erros
│   ├── EmailJaCadastradoException.java # Erro de negócio (Cadastro)
│   └── CredenciaisInvalidasException.java # Erro de segurança (Login)
├── model/
│   ├── User.java                       # Entidade de Usuário
│   └── Perfil.java                     # Entidade de Perfil (RBAC)
├── repository/
│   ├── UserRepository.java             # Comunicação com tabela 'usuario'
│   └── PerfilRepository.java           # Comunicação com tabela 'perfil'
├── service/
│   ├── UserAuthenticationService.java  # Interface de Autenticação
│   ├── UserRegistrationService.java    # Interface de Registro
│   └── impl/
│       ├── UserAuthenticationServiceImpl.java # Lógica de login
│       └── UserRegistrationServiceImpl.java   # Lógica de cadastro
```
---
# 2. Detalhamento de Classes e Interfaces

## 2.1 Camada de Controller

`AuthController:` Gerencia as rotas `/api/v1/auth/registrar` e `/api/v1/auth/login`. É responsável por receber os dados e disparar as validações automáticas do Spring.

### 2.1.1 Endpoints de Autenticação

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `POST` | `/api/v1/auth/registrar` | Cria um novo usuário com perfil inicial 'Free' |
| `POST` | `/api/v1/auth/login` | Autentica o usuário e retorna um token de acesso |

### 2.1.2 Exemplo de Cadastro (Request)
```json
{
  "name": "Saylon Batista",
  "email": "saylon@email.com",
  "password": "senha_segura_123"
}
```

## 2.2 Camada de DTO (Data Transfer Objects)

`UserRegistrationRequest:` `Record` que valida o formato de e-mail, nome obrigatório e tamanho mínimo da senha no cadastro.

`LoginRequest:` Record utilizado para capturar as credenciais de acesso de forma segura.

`LoginResponse:` Define o formato do JSON de resposta, incluindo o token simbólico e o nome do usuário com seu respectivo perfil.

## 2.3 Camada de Service (Regras de Negócio)

`UserRegistrationService (Interface):` Define o contrato para a funcionalidade de registro.

`UserRegistrationServiceImpl:` Realizar a verificação de e-mails duplicados e atribui o perfil 'Free' automaticamente.

`UserAuthenticationService (Interface):` Define o contrato para o login.

`UserAuthenticationServiceImpl:` Valida credenciais no banco de dados e gera a resposta de sucesso com o token da Sprint 1.

## 2.4 Camada de Exception (Segurança e Feedback)

`GlobalExceptionHandler:` Captura erros e os traduz em mensagens amigáveis com códigos HTTP (400, 401, 500).

`EmailJaCadastradoException:` Disparada quando o e-mail já existe na base.

`CredenciaisInvalidasException:` Alarme de segurança para e-mail ou senha incorretos.

## 2.5 Camada de Model e Repository

`User & Perfil:` Entidades que representam o mapeamento para o PostgreSQL do Supabase.

`UserRepository & PerfilRepository:` Interfaces JpaRepository para operações de banco sem necessidade de SQL manual.

# 3. Qualidade e Cobertura de Testes

Classes de teste criadas:

`UserRegistrationServiceImplTest:` Garante a persistência correta e bloqueio de duplicatas.

`UserAuthenticationServiceImplTest:` Valida o login e o disparo de exceções em caso de falha.
