# Projeto PoC - Webservices em Kotlin

Este repositório contém um projeto de **Prova de Conceito (PoC)** que utiliza **Kotlin** para a criação de webservices. O objetivo é validar conceitos e explorar funcionalidades específicas relacionadas ao desenvolvimento backend com Kotlin.

## Objetivo
Desenvolver e testar webservices simples para entender melhor:

- A estruturação de projetos em Kotlin.
- Uso de frameworks e bibliotecas.
- Boas práticas de desenvolvimento backend.

## Tecnologias
- **Kotlin**: Linguagem principal utilizada no projeto.
- **Frameworks/Bibliotecas**: Quarkus, Panache ORM

## Observação
Este projeto tem caráter exploratório e não é voltado para produção.


### Questões técnicas

#### Login é baseado em regras de acesso, sendo:
    ADMIN, USER, GUEST

**ADMIN** : tem todas as permissões. Incluindo criação, edição, deleção e afins.

**USER** : tem acesso a apenas endpoints que requerem autenticação.

**GUEST**: tem permissão apenas a endpoint sem nenhuma autenticação.

