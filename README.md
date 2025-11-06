# CabinetPro-Lite 🛠️  
*A Spring Boot + PostgreSQL backend project built as part of my Java Foundations Professional Certificate.*

---

## 🎯 Overview
**CabinetPro-Lite** is a backend project developed to demonstrate **clean architecture**, **transactional service design**, and the **DAO pattern** using **Spring Boot 3** and **PostgreSQL**.

It’s inspired by real cabinet-making workflows — managing **customers** and their **projects** in a structured, atomic, and scalable way.

This project was created as part of my learning journey for the **Java Foundations Professional Certificate (JetBrains Academy)** — applying the fundamentals of Java, JDBC, and enterprise-grade backend development.

---

## 🧱 Tech Stack
| Layer | Technology | Purpose |
|--------|-------------|----------|
| Backend | **Java 17**, **Spring Boot 3.2** | Core business logic, REST API |
| Database | **PostgreSQL (Dockerized)** | Data persistence |
| Data Access | **Pure JDBC + DAO pattern** | Manual SQL control (no JPA) |
| Transactions | **Spring @Transactional** | Managed at the Service layer |
| Testing | **Postman / MockMvc / Testcontainers** | API and integration testing |

---

## 🧩 Architecture
```
com.cabinetpro.lite
 ├─ controller/    → REST endpoints
 ├─ service/       → Business logic + @Transactional
 ├─ dao/           → Direct database access via JDBC
 ├─ model/         → Entities (Customer, Project)
 ├─ dto/           → Data Transfer Objects
 ├─ config/        → (Optional) DB configuration or connection utilities
 └─ CabinetProLiteApplication.java
```

---

### **Key Concept**  
All transactions are handled at the **service layer** for atomic consistency,  
while DAO classes manage raw SQL operations with `DataSourceUtils`.

---

## 🚀 Setup & Run

### Prerequisites
- **Java 17+**
- **Maven 3.8+**
- **Docker Desktop** (for PostgreSQL)

### Steps
```bash
# 1️⃣ Clone the project
git clone https://github.com/Dela-Hashem/cabinetpro-lite.git
cd cabinetpro-lite

# 2️⃣ Run PostgreSQL with Docker
docker run --name cabinetpro_db   -e POSTGRES_USER=cabinetuser   -e POSTGRES_PASSWORD=cabinetpass   -e POSTGRES_DB=cabinetpro   -p 5432:5432 -d postgres:16

# 3️⃣ Build & Run the app
mvn clean spring-boot:run
```

Then open your browser:  
👉 **http://localhost:8080**

---

## 📬 REST API Examples

### ➕ Create Customer
**POST** `/api/customers`
```json
{
  "fullName": "Maryam A.",
  "phone": "0400 555 123",
  "email": "maryam@example.com"
}
```

### ➕ Create Customer with First Project (atomic transaction)
**POST** `/api/customers/with-project`
```json
{
  "customer": {
    "fullName": "Zac H.",
    "phone": "0400 111 222",
    "email": "zac@example.com"
  },
  "project": {
    "title": "Laundry Fitout",
    "address": "Nollamara WA"
  }
}
```

### 🔍 Search Customers
**GET** `/api/customers/search?q=mary`

### 📋 List Projects by Customer
**GET** `/api/projects/by-customer/{customerId}`

---

## 📘 Educational Context
This project was built as a **practical component** of my  
🎓 *Java Foundations Professional Certificate (JetBrains Academy)*  
to apply:
- Object-oriented design principles  
- JDBC connections and connection pooling (HikariCP)  
- Service–DAO separation  
- Transaction management with Spring  
- Real-world CRUD operations  

---

## 🧑‍💻 Author
**Dela Hashem**  
📍 Perth, Western Australia  
🔗 [GitHub Profile](https://github.com/Dela-Hashem)

---

## 🏁 Future Enhancements
- Add authentication & role-based access control  
- Integrate React frontend (for full-stack version)  
- Add Docker Compose for full environment deployment  

---

⭐ **If you like this project, consider starring it on GitHub — it supports my certification journey and helps others discover it.**
