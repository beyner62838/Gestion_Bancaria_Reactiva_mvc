# Technical Test – Banking Management API

This API allows you to manage clients, accounts, and banking transactions in a simple and organized way.  
The project is built with **Java + Spring Boot**, uses **PostgreSQL** as the database engine, and follows a clean structure suitable for technical tests, academic projects, or backend practice.

---

## 📌 Overview

The application exposes REST endpoints to:

- Create, update, delete, and list clients.
- Register and manage bank accounts associated with each client.
- Record transactions such as deposits and withdrawals.
- Validate basic business rules (account existence, available balance, etc.).

It’s lightweight, easy to deploy, and straightforward to test using Postman, Insomnia, or cURL.

---

## 🧰 Technologies Used

- **Java 17+**
- **Spring Boot 3**
- **Maven**
- **PostgreSQL**
- **Lombok**
- **Spring Data JPA**
- **Spring Web**

---

## ⚙️ Prerequisites

Before running the project, ensure you have installed:

- Java 17 or higher  
- Maven  
- PostgreSQL  
- Git  

---

## 📥 Installation & Setup

### 1️⃣ Clone the repository

```bash
git clone https://github.com/beyner62838/PruebaTecnica1.git
cd PruebaTecnica1
```

### 2️⃣ Create the Database

Create a PostgreSQL database named:

```
prueba_tecnica
```

### 3️⃣ Configure `application.properties`

Located at:  
`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/prueba_tecnica
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4️⃣ Run the project

```bash
mvn clean install
mvn spring-boot:run
```

Server will run at:

```
http://localhost:8080
```

---

## 🗂️ Database Structure

> Tables are automatically generated via JPA/Hibernate.

If you prefer manual creation:

```sql
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    lastname VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20),
    balance DECIMAL(12,2),
    client_id INTEGER REFERENCES clients(id)
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    type VARCHAR(20),
    amount DECIMAL(12,2),
    date TIMESTAMP,
    account_id INTEGER REFERENCES accounts(id)
);
```

---

## 🚀 API Endpoints

### 🧑‍💼 **Clients**

#### ➕ Create a client  
```
POST /clientes
```

**Example body:**
```json
{
  "name": "John",
  "lastname": "Doe",
  "email": "john.doe@gmail.com"
}
```

#### 📄 List all clients  
```
GET /clientes
```

#### 🔍 Get client by ID  
```
GET /clientes/{id}
```

#### ✏️ Update client  
```
PUT /clientes/{id}
```

#### 🗑️ Delete client  
```
DELETE /clientes/{id}
```

---

### 💳 **Accounts**

#### ➕ Create an account  
```
POST /cuentas
```

**Example body:**
```json
{
  "number": "123456789",
  "balance": 0,
  "clientId": 1
}
```

#### 📄 List all accounts  
```
GET /cuentas
```

---

### 💸 **Transactions**

#### ➕ Register a transaction  
```
POST /transacciones
```

**Example body:**
```json
{
  "type": "DEPOSIT",
  "amount": 50000,
  "accountId": 1
}
```

Transaction types allowed:

- `DEPOSIT`
- `WITHDRAWAL`

#### 📄 List all transactions  
```
GET /transacciones
```

---

## 🧪 Example cURL Tests

### Deposit

```bash
curl -X POST http://localhost:8080/transacciones -H "Content-Type: application/json" -d '{"type":"DEPOSIT","amount":20000,"accountId":1}'
```

### Withdrawal

```bash
curl -X POST http://localhost:8080/transacciones -H "Content-Type: application/json" -d '{"type":"WITHDRAWAL","amount":10000,"accountId":1}'
```

---

## 🗺️ Project Status

- ✔ Functional CRUD operations  
- ✔ Basic validations  
- ✔ Automatic database generation  


---

## 📌 Roadmap / Future Improvements

- Add JWT authentication  
- Add Swagger/OpenAPI documentation  
- Implement pagination  
- Add unit tests  
- Improve error handling responses  

---

## 👨‍💻 Author

**Breyner José Pertuz Castro**  
GitHub: https://github.com/beyner62838  
