# 🛒 Sistema de Gestão de Vendas e Estoque

Um sistema de Ponto de Venda (PDV) completo e robusto, desenvolvido com foco em boas práticas de arquitetura de software, segurança de dados e experiência do usuário. 

![Java Badge](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Badge](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![React Badge](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
## ✨ Funcionalidades

### 🏠 **Dashboard Principal**
- Interface moderna com cards interativos
- Navegação intuitiva para todos os módulos

### 👥 **Gestão de Clientes**
- Cadastro de clientes
- Busca dinâmica por ID ou nome
- Validação de e-mail em tempo real

### 📦 **Gestão de Produtos**
- Cadastro de produtos com descrição, valor e estoque
- Controle automático de quantidade
- Busca por ID ou descrição

### 🛍️ **Ponto de Venda (PDV)**
- Carrinho de compras interativo
- Seleção de clientes e produtos
- Controle de estoque em tempo real
- Cálculo automático de subtotais
- Aplicação de descontos por item

### 📊 **Central de Relatórios**
- Relatórios por cliente
- Relatórios por produto
- Relatórios por período personalizado
- Busca detalhada por ID do pedido
- Visualização em tabela e detalhes completos

## 🏗️ Arquitetura e Decisões Técnicas

### Backend
O backend foi estruturado utilizando o padrão **MVC (Model-View-Controller)** com separação estrita de responsabilidades:

1. **Controllers Magros (Thin Controllers):** Responsáveis unicamente por expor as rotas da API e lidar com o tráfego HTTP
2. **Service Layer (Business Logic):** Orquestra validações, cálculos financeiros e garante atomicidade com `@Transactional`
3. **Data Transfer Objects (DTOs):** Utiliza `Records` do Java moderno para segurança dos dados
4. **Tratamento Global de Exceções:** `@ControllerAdvice` para retornar mensagens JSON limpas
5. **Spring Boot:** Esse projeto utiliza o spring boot para facilitar a injeção de dependências e por conter o tomcat embutido.

### Frontend
- **Component-Based Architecture:** React com componentes reutilizáveis
- **UI Framework:** Bootstrap 5 + React Bootstrap para design responsivo
- **Feedback Visual:** SweetAlert2 para notificações elegantes

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21**
- **Spring Boot 3.5.1**
- **PostgreSQL**
- **Maven**

### Frontend
- **React 19.2.0**
- **Vite 7.3.1**

## ⚙️ Como executar o projeto

### Pré-requisitos
- Java 21+ instalado
- Node.js 18+ e NPM instalados
- Docker Compose ou PostgreSQL instalados

### Rodando o Backend (Spring Boot)
1. Clone o repositório
2. Navegue até a pasta [back-end](cci:9://file:///home/gui-ubuntu/projects/java/desafio-sgtc-ana/back-end:0:0-0:0)
3. Configure as variáveis do banco em `application.yml` ou execute `docker-compose up -d`
4. Execute com sua IDE ou via Maven: `./mvnw spring-boot:run`

### Configurando o Banco de Dados
1. Após clonar, abra seu gestor de banco preferido (DBeaver, pgAdmin)
2. Navegue até `back-end/src/main/resources/schema.sql`
3. Execute os scripts de criação do banco

### Rodando o Frontend (React)
1. Navegue até a pasta [front-end](cci:9://file:///home/gui-ubuntu/projects/java/desafio-sgtc-ana/front-end:0:0-0:0)
2. Instale as dependências:
   ```bash
   npm install
   ```
3. Inicie o projeto: `npm run dev`
