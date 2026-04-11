# Guia de Desenvolvimento - Grupo 2

## Objetivo

Este documento define o fluxo de trabalho oficial do Grupo 2 e estabelece limites claros entre organizacao tecnica e implementacao de features.

## Regra central do grupo

Cada integrante implementa apenas a propria parte oficial.

O Tech Lead nao deve adiantar, gerar ou registrar no proprio historico implementacoes que pertencem aos outros membros.

Essa regra deve ser respeitada em:

- commits
- branches
- pull requests
- codigo
- documentacao tecnica

## Divisao oficial das responsabilidades

- Jose Gomes Cabral Neto: arquitetura, documentacao, integracao e revisao
- Saylon Batista: autenticacao
- Mauricio: playlist
- Israel: busca e avaliacao
- Lucas Henrique: historico
- todos: testes basicos da propria entrega

## Fluxo de trabalho do grupo

1. O Tech Lead prepara a base organizacional do repositorio.
2. Cada integrante cria sua branch a partir da base atualizada.
3. Cada integrante implementa apenas seu card ou sua parte oficial.
4. A entrega deve ser documentada de forma objetiva no PR.
5. O Tech Lead revisa aderencia ao escopo, clareza tecnica e consistencia com a arquitetura.
6. Ajustes solicitados em revisao devem permanecer no escopo da pessoa responsavel pela feature.

## Estrutura organizacional do projeto

As pastas abaixo representam organizacao do projeto, e nao autorizacao para o Tech Lead preencher implementacoes de feature:

```text
src/main/java/com/deefy/group2
├── config
├── dto
│   ├── request
│   └── response
├── exception
├── model
├── repository
│   └── fake
├── service
│   └── impl
└── util
```

## Padrao de branches

Padrao recomendado:

```text
docs/<tema>
chore/<tema>
feat/<tema>
fix/<tema>
```

Exemplos de uso:

```text
docs/readme-grupo2
docs/contrato-grupo2-grupo3
chore/base-projeto
feat/autenticacao
feat/playlist
feat/busca-avaliacao
feat/historico
fix/auth-validacao
```

## Padrao de commits

Formato:

```text
tipo(area): descricao curta
```

Exemplos:

```text
docs(guia): define fluxo de trabalho do grupo 2
docs(contrato): publica fronteira inicial com o grupo 3
chore(base): configura estrutura inicial do projeto
feat(auth): implementa autenticacao basica
feat(playlist): adiciona regra de dono da playlist
feat(search): implementa busca basica
feat(history): registra execucao de musica
fix(rating): corrige validacao de nota
```

## Como abrir pull request

Todo PR deve conter:

- titulo claro
- descricao curta do objetivo
- card oficial relacionado, quando aplicavel
- resumo do que foi alterado
- limite exato da entrega
- observacoes de risco, se existirem

O PR nao deve:

- misturar mais de uma feature sem necessidade
- invadir escopo de outro integrante
- incluir implementacao alheia para "adiantar o time"
- alterar contrato com outro grupo sem alinhamento previo

## O que cada integrante deve entregar

### Jose Gomes Cabral Neto

- organizacao do repositorio
- documentacao
- definicao de padroes
- integracao e alinhamento tecnico
- revisao

### Saylon Batista

- implementacao de autenticacao
- testes basicos da propria entrega

### Mauricio

- implementacao de playlist
- testes basicos da propria entrega

### Israel

- implementacao de busca
- implementacao de avaliacao
- testes basicos da propria entrega

### Lucas Henrique

- implementacao de historico
- testes basicos da propria entrega

## Como documentar a propria entrega

Cada integrante deve registrar no PR:

- objetivo da implementacao
- regra de negocio coberta
- principais classes alteradas
- testes incluidos
- limitacoes conhecidas, se houver

Se houver impacto de integracao com outro grupo, isso deve ser informado ao Tech Lead antes do merge.

## Regras de organizacao do codigo

- manter a arquitetura simples e explicavel
- evitar logica de negocio fora da camada de servico
- evitar controller inchado
- manter nomes claros e coerentes
- nao adicionar dependencias sem justificativa
- nao criar complexidade desnecessaria para a Sprint 1

## Regra explicita sobre implementacao individual

Nenhum integrante deve implementar a feature atribuida a outro membro.

Isso inclui:

- services de outra pessoa
- testes da feature de outra pessoa
- fake repositories completos que entreguem a feature de outra pessoa
- codigo de apoio que, na pratica, finalize a entrega alheia

## Regra explicita sobre o Tech Lead

O Tech Lead pode:

- organizar a base do projeto
- documentar
- registrar decisoes
- definir padroes
- revisar
- alinhar integracao

O Tech Lead nao pode:

- implementar as features dos demais integrantes
- adiantar services de feature
- gerar testes das features dos outros
- deixar no proprio historico de commits uma entrega que pertence a outro membro
