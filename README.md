#  Barbearia API - Sistema de Gestão de Serviços

Esta é uma API REST robusta desenvolvida com **Spring Boot** para gerenciar agendamentos de barbearias ou salões de beleza. O sistema conta com controle de autenticação JWT, gestão de faturamento e regras automáticas de conflito de horários.

##  Tecnologias
* **Java 21**
* **Spring Boot 3**
* **Spring Security** (Autenticação JWT)
* **Spring Data JPA** (Persistência de dados)
* **MySQL/PostgreSQL** (Banco de dados)
* **Maven** (Gerenciador de dependências)

##  Funcionalidades de Segurança
* **JWT (JSON Web Token):** Autenticação stateless para garantir segurança e escalabilidade.
* **Controle de Acesso (RBAC):** * `ADMIN`: Acesso total, incluindo faturamento, gestão de clientes e serviços.
    * `CLIENTE`: Permissão para visualizar serviços e realizar seus próprios agendamentos.
* **CORS:** Configurado para permitir comunicações seguras com o Frontend Angular (`localhost:4200`).

##  Regras de Negócio Implementadas
* **Validação de Conflitos:** O sistema impede agendamentos no mesmo horário para o mesmo profissional.
* **Bloqueio de Agenda:** Funcionalidade para o administrador bloquear horários específicos para compromissos ou manutenção.
* **Cálculo de Faturamento:** Relatórios financeiros automáticos por período, dia e total.

* ##  Endpoints da API

| Método | Endpoint | Descrição | Acesso |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/login` | Realiza login e retorna o Token JWT | Público |
| **POST** | `/api/usuarios/registrar` | Cadastro de novos clientes | Público |
| **GET** | `/api/servicos` | Lista todos os serviços ativos | Público |
| **POST** | `/api/agendamentos` | Cria um novo agendamento | Autenticado |
| **GET** | `/api/agendamentos/faturamento` | Consulta faturamento (Total/Hoje/Período) | Admin |
| **DELETE** | `/api/agendamentos/{id}` | Cancela um agendamento | Admin / Dono |

