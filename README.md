# API Aluno Online

Solução de backend voltada para a administração escolar, permitindo o controle centralizado de informações sobre o corpo docente e discente.

## Recursos Principais

A API disponibiliza operações de **CRUD** para a gestão das entidades da escola:

* **Alunos:** Cadastro, consulta, atualização e exclusão de estudantes.
* **Professores:** Registro e manutenção dos dados dos professores da instituição.
* **Disciplinas:** Cadastro de disciplinas vinculadas a um professor.
* **Matrículas:** Matrícula de alunos em disciplinas, lançamento de notas, trancamento e histórico do aluno (com média por disciplina).

Além disso, a API conta com:

* 🔒 **Login com token JWT** (Spring Security, stateless).
* 📖 **Documentação interativa** com Swagger (OpenAPI).
* 🗄️ **Versionamento do banco de dados** com Flyway.

## Stack Tecnológica

* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot 4
* **Acesso a Dados:** Spring Data JPA
* **Banco de Dados:** PostgreSQL
* **Migrations:** Flyway
* **Segurança:** Spring Security + JWT (java-jwt da Auth0)
* **Validação:** Bean Validation (`@Valid`, `@NotBlank`)
* **Documentação:** springdoc-openapi (Swagger UI)
* **Testes:** JUnit 5, MockMvc e H2 em memória
* **Automação de Build:** Maven (com Maven Wrapper)

## Arquitetura do Sistema

O código segue a organização usada no curso **Spring Avançado**, separada em três pacotes principais:

```
br.com.alunoonline.api
├── controller/        → rotas HTTP (Aluno, Professor, Disciplina, MatriculaAluno, Autenticacao, Usuario)
├── domain/            → regras de negócio, separadas por assunto
│   ├── aluno/         → Aluno, AlunoRepository, AlunoService
│   ├── professor/     → Professor, ProfessorRepository, ProfessorService
│   ├── disciplina/    → Disciplina, DisciplinaRepository, DisciplinaService
│   ├── matricula/     → MatriculaAluno, repository, service e os records Dados*
│   └── usuario/       → Usuario (UserDetails), UsuarioRepository e os records Dados*
└── infra/             → infraestrutura
    ├── exception/     → TratadorDeErros (@RestControllerAdvice)
    ├── security/      → SecurityConfigurations, SecurityFilter, TokenService, AutenticacaoService
    └── springdoc/     → SpringDocConfigurations (Swagger)
```

* **controller:** recebe as requisições e devolve as respostas.
* **domain:** cada pasta reúne a entidade, o repositório, o service e os DTOs (`record Dados...`) daquele assunto.
* **infra:** código de apoio que não é regra de negócio: segurança, tratamento de erros e documentação.
* `resources/db/migration/`: scripts SQL do Flyway.

## 🚀 Como Rodar

**Pré-requisitos:** Java 21+ e PostgreSQL rodando em `localhost:5432` com um banco chamado `aluno_online`.

1. Ajuste usuário e senha do banco em `Java-Project/src/main/resources/application.properties`.
2. Na pasta `Java-Project`, suba a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows (cmd/PowerShell), use `mvnw.cmd spring-boot:run`.

Para rodar os testes (não precisam do PostgreSQL, usam H2 em memória):

```bash
./mvnw test
```

### Rodando em produção (profile `prod`)

O arquivo `application-prod.properties` lê os dados sensíveis de variáveis de ambiente. Gere o `.jar` e rode com o profile `prod`:

```bash
./mvnw clean package
```

```bash
java -Dspring.profiles.active=prod -DDATASOURCE_URL=jdbc:postgresql://localhost:5432/aluno_online -DDATASOURCE_USERNAME=postgres -DDATASOURCE_PASSWORD=sua_senha -DJWT_SECRET=um_segredo_forte -jar target/api-0.0.1-SNAPSHOT.jar
```

## 🔒 Segurança (Spring Security + JWT)

A API é **stateless**: em vez de sessão, cada requisição leva um **token JWT** no cabeçalho.

| Rota | Acesso |
|---|---|
| `POST /usuarios` (cadastro) | Público |
| `POST /login` | Público |
| Swagger (`/swagger-ui/**`, `/v3/api-docs/**`) | Público |
| Todas as outras | Precisa do token |

**Passo a passo:**

1. **Cadastre um usuário** (a senha é salva criptografada com BCrypt):

```http
POST /usuarios
{ "login": "aluno@email.com", "senha": "123456" }
```

2. **Faça login** e copie o token (vale por 2 horas):

```http
POST /login
{ "login": "aluno@email.com", "senha": "123456" }
```

Resposta: `{ "token": "eyJhbGciOiJIUzI1NiIs..." }`

3. **Use o token** nas outras requisições, no cabeçalho:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

No Insomnia/Postman, escolha **Auth → Bearer Token** e cole o token.

**Como funciona por dentro:**

* `SecurityConfigurations`: define as rotas livres, desliga o CSRF e deixa a API stateless.
* `SecurityFilter`: roda em toda requisição, lê o cabeçalho `Authorization` e valida o token.
* `TokenService`: gera e valida o token JWT, assinado com o segredo `api.security.token.secret` (variável `JWT_SECRET`).
* `AutenticacaoService`: busca o usuário no banco durante o login.

**Respostas de erro:**

* `401 Unauthorized`: sem token, token inválido/expirado ou login e senha errados.
* `400 Bad Request`: dados inválidos. A resposta mostra o campo e a mensagem, por exemplo `[{"campo": "senha", "mensagem": "Senha é obrigatória"}]`.
* `404 Not Found`: registro não encontrado.

## 📖 Documentação (Swagger)

Com a aplicação rodando, acesse:

* **Swagger UI:** http://localhost:8080/swagger-ui/index.html
* **OpenAPI (JSON):** http://localhost:8080/v3/api-docs

Faça o login em `POST /login`, clique em **Authorize**, cole o token e teste os endpoints direto pelo navegador.

## 🗄️ Banco de Dados (Flyway)

As tabelas são criadas e versionadas pelo **Flyway**, a partir dos scripts em `src/main/resources/db/migration`:

* `V1__criar_tabelas.sql`: cria `aluno`, `professores`, `disciplinas` e `matricula_aluno`.
* `V2__criar_tabela_usuarios.sql`: cria `usuarios` (login e senha criptografada).

Regras:
* Toda mudança no banco vira um **novo arquivo** (`V2__descricao.sql`, `V3__...`). Nunca edite uma migration que já rodou.
* O Hibernate está em `ddl-auto=validate`: ele não altera o banco, só confere se as entidades batem com as tabelas.
* Se o banco já tinha as tabelas (criadas antes pelo `ddl-auto=update`), o Flyway marca a V1 como aplicada automaticamente (`baseline-on-migrate`).

## 📌 Endpoints

### Autenticação
| Método | Rota | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastra um usuário (público) |
| POST | `/login` | Faz login e devolve o token JWT (público) |

### Alunos (`/alunos`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/alunos/create` | Cria um aluno |
| GET | `/alunos/` | Lista todos os alunos |
| GET | `/alunos/{id}` | Busca aluno por id |
| PUT | `/alunos/{id}` | Atualiza aluno |
| DELETE | `/alunos/{id}` | Remove aluno |

### Professores (`/professores`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/professores/create` | Cria um professor |
| GET | `/professores/` | Lista todos os professores |
| GET | `/professores/{id}` | Busca professor por id |
| PUT | `/professores/{id}` | Atualiza professor |
| DELETE | `/professores/{id}` | Remove professor |

### Disciplinas (`/disciplina`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/disciplina/create` | Cria uma disciplina |
| GET | `/disciplina/foundall` | Lista todas as disciplinas |
| GET | `/disciplina/{id}` | Busca disciplina por id |
| GET | `/disciplina/professor/{professorId}` | Lista disciplinas de um professor |
| DELETE | `/disciplina/delete/{id}` | Remove disciplina |

### Matrículas (`/matricula-aluno`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/matricula-aluno/create` | Matricula um aluno em uma disciplina |
| PATCH | `/matricula-aluno/update-grades/{id}` | Lança/atualiza as notas |
| PATCH | `/matricula-aluno/update-status/{id}` | Tranca a matrícula |
| GET | `/matricula-aluno/historico-aluno/{id}` | Histórico do aluno com notas e médias |

## 👁️ Visão Geral da Implementação

A aplicação utiliza as melhores práticas do ecossistema Java para oferecer uma API robusta e escalável.

### 1. Mapeamento Objeto-Relacional (Entities)
As entidades em `models/` são mapeadas para o banco de dados via **JPA**.
* **Mapeamento:** Através das anotações `@Entity`, `@Table` e `@Id`, cada classe corresponde a uma tabela criada pelas migrations do Flyway.
* **DTOs com `record`:** os dados de entrada e saída (ex.: `DadosAtualizacaoNotas`, `DadosHistoricoAluno`) são `records` imutáveis.
* **Produtividade com Lombok:** Reduz o código repetitivo (*boilerplate*) gerando automaticamente métodos acessores e construtores.

### 2. Abstração de Dados (Repositories)
A utilização do `JpaRepository` elimina a complexidade de consultas manuais. A interface permite que operações como busca, salvamento e remoção sejam executadas de forma nativa, aumentando a segurança e performance das queries.

### 3. Lógica de Domínio (Services)
Esta camada atua como o "cérebro" do sistema. No fluxo de atualização, por exemplo, o serviço garante a integridade dos dados, verificando a existência do registro e aplicando apenas as mudanças necessárias antes de persistir no banco.

### 4. Gestão de Endpoints (Controllers)
Os controladores expõem as funcionalidades para o mundo externo, utilizando os métodos HTTP de forma semântica:
* `@PostMapping`: Para inserção de novos recursos.
* `@GetMapping`: Para recuperação de informações.
* `@PutMapping` / `@PatchMapping`: Para modificação de registros.
* `@DeleteMapping`: Para encerramento de registros.
* `@RequestBody`: Realiza o *parsing* automático de objetos JSON enviados pelo cliente para objetos Java.

### 5. Segurança (infra/security)
O `SecurityConfigurations` define uma `SecurityFilterChain` **stateless**. O `SecurityFilter` valida o token JWT gerado pelo `TokenService` a cada requisição, e as senhas são guardadas com **BCrypt**.

### 6. Tratamento de Erros (infra/exception)
O `TratadorDeErros` (`@RestControllerAdvice`) centraliza os erros: validação vira `400` com a lista de campos, registro inexistente vira `404` e falha de login vira `401`.

## Prints do Projeto 👍

<div align-items="center">
 <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/33DFD9A5-A6B5-4483-BA5C-E564E99A764D.png" width=500 />
 <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/2765F32D-7361-4B2B-9E51-B7F7A8A004A4.png" width=500 />
  <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/D463999F-3D62-4769-A224-4F9C94F691FA.png" width=500 />
  <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/FD5D0DDF-5A9E-4227-83BE-CF31A332B812.png" width=500 />
</div>
