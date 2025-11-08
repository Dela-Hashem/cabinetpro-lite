# CabinetPro-Lite 🧰

**Lean backend for cabinet-making operations — built with Java 21 + Spring Boot 3 + PostgreSQL + Docker.**

A practical backend that tracks customers, projects, materials, tasks, and invoices.
Designed for real cabinet workflows — quoting, invoicing, and project management.

---

## ⚙️ Tech Stack

| Layer        | Technology                        |
| ------------ | --------------------------------- |
| Language     | Java 21                           |
| Framework    | Spring Boot 3.x                   |
| Build        | Maven                             |
| Database     | PostgreSQL (Dockerized)           |
| Connection   | JDBC + `DataSourceUtils` (no JPA) |
| Transactions | `@Transactional` in Service layer |
| Tests        | JUnit 5 + Testcontainers          |
| PDF Engine   | Thymeleaf + openhtmltopdf         |
| Packaging    | Docker + docker-compose           |
| REST Docs    | Postman Collection included       |

---

## 🧱 Architecture

```
controller → service → dao(jdbc) → db
```

* **Controller**: thin REST endpoints (`ResponseEntity<>`)
* **Service**: business logic, transaction boundaries
* **DAO**: plain JDBC via `DataSourceUtils`
* **DTO/model**: request + response isolation

Example:

```
CustomerController
 └── CustomerService
      └── CustomerDaoJdbc
```

---

## 📦 Modules / Entities

* **Customer** – name, phone, email
* **Project** – customer_id, title, address, status
* **Material** – project_id, name, qty, unit_price
* **TaskItem** – project_id, title, done
* **Invoice** – subtotal, gst, total, status, issued_at

---

## 📾 Invoices

* Generates totals from materials (`qty × unit_price`)
* Applies Australian GST 10 %
* Produces printable PDF via Thymeleaf template (`invoice.html`)
* Endpoint:

  ```
  GET /api/invoices/{id}/pdf   → application/pdf
  ```

---

## 🐳 Docker Setup

```bash
docker-compose up --build
```

Services:

* `app` → Spring Boot (port 8080)
* `db`  → PostgreSQL (port 5432)

Default credentials:

```yaml
POSTGRES_DB: cabinetpro
POSTGRES_USER: cabinetuser
POSTGRES_PASSWORD: cabinetpass
```

---

## 🧪 Local Testing

Import the Postman collection:
`CabinetPro-Lite.postman_collection.json`

Example flow:

1. Create customer → `/api/customers`
2. Create project → `/api/projects`
3. Add materials → `/api/projects/{id}/materials`
4. Generate invoice → `/api/projects/{id}/invoices`
5. Download PDF → `/api/invoices/{id}/pdf`

---

## 📄 SQL Schema

Full schema in `/db/init.sql`
Executed automatically by Docker at first boot.

---

## 🧬 Build & Run manually

```bash
mvn clean package -DskipTests
java -jar target/cabinetpro-lite-1.0.0.jar
```

---

## 🚀 Next Steps (Roadmap)

* [ ] Persist invoice PDF path to disk
* [ ] Add authentication (API key / JWT)
* [ ] Implement search + pagination
* [ ] Integrate metrics (Micrometer + Prometheus)
* [ ] Simple React front-end

---

## 🧑‍💻 Author

**Dela Hashemi** — Software developer, Perth WA
Practical builder of both software and cabinetry.
