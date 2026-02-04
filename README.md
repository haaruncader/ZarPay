# ZarPay – Backend

ZarPay is a Spring Boot backend for a digital wallet application that allows users to manage ZAR balances, perform deposits, transfer funds, and view a full transaction ledger.
This project focuses on clean REST API design, ledger-based accounting, and layered backend architecture.

---

**Features**
- User registration
- Automatic wallet creation
- Deposit funds into a wallet
- Transfer funds between users
- Ledger-based wallet balance calculation
- Transaction history per wallet
- Input validation and centralized error handling

---

**Architecture**
The backend follows a layered architecture:
- Controllers – expose REST API endpoints
- Services – enforce business rules and transactional logic
- Repositories – database access via Spring Data JPA
- DTOs – request/response models to isolate API contracts from entities
  
Balances are derived from the transaction ledger, not stored directly on wallets, ensuring auditability and consistency.

---

**Tech Stack**
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

---

**API Endpoints**

### Authentication
POST /api/auth/signup

### Wallet
GET /api/wallet/balance?email={email}
GET /api/wallet/transactions?email={email}

### Transactions
POST /api/deposit
POST /api/transfer


---

**Run Locally**

Prerequisites:
Java 17+
Maven
PostgreSQL

Start the application
mvn spring-boot:run

The backend will start on:
http://localhost:8080

The React frontend for ZarPay is available here:
https://github.com/haaruncader/zarpay-frontend

📌 Notes
Authentication is currently email-based (JWT can be added later)
CORS is configured for local frontend development
Designed as a portfolio / learning project with real-world structure


Haarun Cader
Final-year Computer Science and Business Computing student
