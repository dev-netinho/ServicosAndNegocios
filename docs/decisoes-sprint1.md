# Decisoes Tecnicas - Sprint 1

Data de consolidacao: 10 de abril de 2026

## D01 - Stack escolhida

Decisao:

Adotar Spring Boot + Maven com Java 17 como base tecnica do Grupo 2.

Justificativa:

- atende ao perfil da disciplina
- facilita organizacao em camadas
- oferece estrutura adequada para servicos e negocio
- e suficientemente profissional e explicavel para a Sprint 1

## D02 - Escopo funcional limitado a musica

Decisao:

Manter o projeto focado apenas em musica nesta etapa.

Justificativa:

- respeita o escopo definido para o sistema
- evita expansao prematura para podcast
- reduz risco de dispersao tecnica

## D03 - Arquitetura simples e explicavel

Decisao:

Adotar uma estrutura em camadas simples, com separacao organizacional entre `config`, `dto`, `exception`, `model`, `repository`, `service`, `service/impl` e `util`.

Justificativa:

- facilita manutencao
- facilita apresentacao
- reduz acoplamento inicial
- cria base clara para o trabalho dos integrantes

## D04 - Base inicial focada em organizacao e documentacao

Decisao:

Na fase inicial do repositorio, priorizar organizacao, documentacao, padroes, contrato com o Grupo 3 e preparacao tecnica minima.

Justificativa:

- isso faz parte da atuacao legitima do Tech Lead
- reduz risco de invadir o trabalho individual dos integrantes
- prepara uma base segura para as implementacoes posteriores

## D05 - Nao adiantar features individuais no historico do Tech Lead

Decisao:

Evitar qualquer implementacao de feature individual no historico de commits do Tech Lead.

Justificativa:

- a professora pode analisar o historico de commits
- o Tech Lead nao deve parecer responsavel por entregas individuais dos colegas
- a divisao de responsabilidade do grupo precisa ser preservada com clareza

## D06 - OpenAPI apenas como preparacao tecnica

Decisao:

Manter apenas a preparacao minima para OpenAPI, sem transformar a Sprint 1 em sprint de controller ou de API REST.

Justificativa:

- melhora a base tecnica do projeto
- nao invade a responsabilidade do Grupo 3
- preserva foco em organizacao e alinhamento

## D07 - Contrato inicial com o Grupo 3 em formato documental

Decisao:

A relacao inicial entre Grupo 2 e Grupo 3 sera registrada documentalmente na Sprint 1.

Justificativa:

- define a fronteira entre servico e REST
- reduz ambiguidade entre grupos
- evita que a base inicial dependa de implementacoes prontas

## D08 - Dependencias minimas no `pom.xml`

Decisao:

Manter no `pom.xml` apenas a base tecnica minima e neutra:

- Spring Boot
- Spring Web
- Spring Validation
- Springdoc OpenAPI

Justificativa:

- reduz ruido tecnico
- evita exagero de dependencias
- reforca o objetivo de uma base segura e profissional

## D09 - Estrutura de pastas tratada como organizacao, nao como implementacao

Decisao:

Manter a estrutura de diretorios do projeto pronta para o time, mas sem preencher codigo de feature na base do Tech Lead.

Justificativa:

- o repositorio precisa estar preparado
- a organizacao do projeto e responsabilidade legitima do Tech Lead
- isso nao deve ser confundido com implementacao funcional

## Impedimentos e riscos da Sprint 1

### I01 - Dependencia dos demais grupos

Situacao:

O Grupo 2 depende do andamento dos outros grupos para integracao completa do projeto.

Impacto:

- a persistencia real depende do Grupo 1
- a camada REST depende do Grupo 3
- a experiencia final de uso depende do Grupo 4

### I02 - Risco de mistura de responsabilidades no historico

Situacao:

Se o Tech Lead adiantar feature alheia, o historico de commits pode distorcer a divisao real das entregas.

Impacto:

- perda de clareza sobre autoria
- risco academico e organizacional
- dificuldade de defesa da distribuicao de tarefas

### I03 - Risco de aumento indevido de escopo

Situacao:

Itens como JWT complexo, recomendacao, notificacoes e refinamentos avancados podem dispersar a sprint.

Impacto:

- atraso nas entregas principais
- dificuldade de apresentacao
- perda de foco no que foi solicitado

## Registro final da base da Sprint 1

Esta base inicial deve ser defendida como:

- organizacao tecnica do repositorio
- documentacao profissional
- definicao de fluxo de trabalho
- alinhamento entre grupos
- registro formal de decisoes, riscos e limites

Ela nao deve ser apresentada como pacote de implementacao das features individuais.
