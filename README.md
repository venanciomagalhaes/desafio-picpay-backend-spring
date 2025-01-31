
# Desafio PicPay Simplificado - Spring Boot

Este projeto é uma **API REST** desenvolvida com **Spring Boot** para simular funcionalidades de um sistema de pagamentos, como o **PicPay**. Ele oferece endpoints para o gerenciamento de **usuários**, **categorias de usuários** e **transferências financeiras** entre usuários.

## Tecnologias Usadas

- **Spring Boot 3.4.1**: Framework para desenvolvimento ágil de aplicações Java, com integração simplificada e suporte a Java 21.
- **Spring Data JPA**: Abstração para manipulação de dados em bancos relacionais.
- **Spring Boot Starter Web**: Facilita a criação de APIs RESTful com Spring.
- **Spring Boot Starter Validation**: Suporte para validação de dados de entrada usando **Bean Validation**.
- **Flyway**: Ferramenta para migração e versionamento de banco de dados.
- **MySQL Connector/J**: Driver JDBC para conexão com o banco de dados MySQL.
- **Spring Boot Starter Test**: Pacote para criação e execução de testes automatizados.
- **Spring Boot Starter HATEOAS**: Suporte para construção de APIs RESTful seguindo os princípios de HATEOAS.
- **Lombok**: Reduz o código boilerplate com anotações automáticas para getters, setters e construtores.
- **Spring Boot Starter Mail**: Suporte para envio de emails via SMTP.
- **Spring Boot Docker Compose**: Integração com containers Docker para ambientes consistentes.
- **Springdoc OpenAPI Starter WebMVC UI**: Geração e visualização da documentação da API usando OpenAPI.

---

## Endpoints da API

A documentação Swagger está disponível nas URLs: `/swagger-ui/index.html#` e `/v3/api-docs`.

### 1. **Usuários**

- **Listar todos os usuários**: `GET /api/v1/users`
- **Criar um novo usuário**: `POST /api/v1/users`
- **Buscar um usuário por ID**: `GET /api/v1/users/{id}`
- **Atualizar um usuário**: `PUT /api/v1/users/{id}`
- **Excluir um usuário**: `DELETE /api/v1/users/{id}`

### 2. **Categorias de Usuários**

- **Listar categorias de usuários**: `GET /api/v1/categories-users`
- **Criar uma nova categoria**: `POST /api/v1/categories-users`
- **Buscar categoria por ID**: `GET /api/v1/categories-users/{id}`
- **Atualizar categoria**: `PUT /api/v1/categories-users/{id}`
- **Excluir categoria**: `DELETE /api/v1/categories-users/{id}`

### 3. **Transferências**

- **Realizar transferência**: `POST /api/v1/transfer`

---

## Como Rodar

É necessário ter o GIT, Docker e Docker Compose em sua máquina

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/desafio-picpay.git
   ```

2. Entre na pasta do projeto:
   ```bash
   cd desafio-picpay
   ```

3. Compile e execute a aplicação com Docker:
   ```bash
   docker compose build && docker compose up -d
   ```

Os serviços MySQL para produção e teste estarão disponíveis em `localhost:1224` e `localhost:1225`, respectivamente.

---
