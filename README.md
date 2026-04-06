# API Aluno Online

Solução de backend voltada para a administração escolar, permitindo o controle centralizado de informações sobre o corpo docente e discente.

## Recursos Principais

A API disponibiliza operações completas de **CRUD** para a gestão de duas entidades fundamentais:

* **Estudantes:** Gestão integral de matrículas, perfis, atualizações cadastrais e exclusões.
* **Docentes:** Registro e manutenção de dados relativos aos professores da instituição.

## Stack Tecnológica

* **Linguagem:** Java
* **Framework Principal:** Spring Boot (Ecossistema Spring)
* **Acesso a Dados:** Spring Data JPA
* **Armazenamento:** Suporte a Bancos Relacionais (PostgreSQL/MySQL)
* **Automação de Build:** Maven

## Arquitetura do Sistema

O projeto adota o padrão **MVC (Model-View-Controller)** para garantir modularidade e facilitar a manutenção do código:

* `controllers/`: Ponto de entrada da API, lida com as rotas e respostas HTTP.
* `services/`: Concentra as regras de negócio e validações da aplicação.
* `models/`: Define a estrutura das entidades e o mapeamento do banco de dados.
* `repositories/`: Interface de comunicação direta com a camada de persistência.
* `AlunoOnlineApplication.java`: Classe principal que inicializa o contexto do Spring Boot.

## 👁️ Visão Geral da Implementação

A aplicação utiliza as melhores práticas do ecossistema Java para oferecer uma API robusta e escalável.

### 1. Mapeamento Objeto-Relacional (Entities)
As entidades em `models/` são sincronizadas com o banco de dados via **JPA**. 
* **Persistência Automática:** Através das anotações `@Entity` e `@Id`, o sistema automatiza a criação de tabelas e chaves primárias.
* **Produtividade com Lombok:** Reduz o código repetitivo (*boilerplate*) gerando automaticamente métodos acessores e construtores.



### 2. Abstração de Dados (Repositories)
A utilização do `JpaRepository` elimina a complexidade de consultas manuais. A interface permite que operações como busca, salvamento e remoção sejam executadas de forma nativa, aumentando a segurança e performance das queries.

### 3. Lógica de Domínio (Services)
Esta camada atua como o "cérebro" do sistema. No fluxo de atualização, por exemplo, o serviço garante a integridade dos dados, verificando a existência do registro e aplicando apenas as mudanças necessárias antes de persistir no banco.

### 4. Gestão de Endpoints (Controllers)
Os controladores expõem as funcionalidades para o mundo externo, utilizando os métodos HTTP de forma semântica:
* `@PostMapping`: Para inserção de novos recursos.
* `@GetMapping`: Para recuperação de informações.
* `@PutMapping`: Para modificação de registros.
* `@DeleteMapping`: Para encerramento de registros.
* `@RequestBody`: Realiza o *parsing* automático de objetos JSON enviados pelo cliente para objetos Java.

## Prints do Projeto 👍

<div align-items="center">
 <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/33DFD9A5-A6B5-4483-BA5C-E564E99A764D.png" width=500 />
 <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/2765F32D-7361-4B2B-9E51-B7F7A8A004A4.png" width=500 />
  <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/D463999F-3D62-4769-A224-4F9C94F691FA.png" width=500 />
  <img src="https://github.com/Joaovictorasm/Aluno-Online/blob/main/assets/FD5D0DDF-5A9E-4227-83BE-CF31A332B812.png" width=500 />
</div>
