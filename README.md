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

