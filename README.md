# Ledgerly: Expense & Subscription Manager

A small full-stack project built with React, Spring Boot, Spring Data JPA, Spring Security, JWT, and MySQL. It has four modules only: User & Authentication, Expense Management, Subscription Management, and Dashboard & Analytics.

## Project flow

1. React sends a request to a REST controller.
2. Spring Security checks the JWT and loads the signed-in user.
3. The controller passes the user's ID and request data to a service.
4. The service validates ownership and applies the business rules.
5. A JPA repository reads or writes the MySQL data.
6. The controller returns a JSON response to React.

The user ID comes from the verified token, not from the request body. Expense and subscription lookups include both the record ID and user ID, so one user cannot read or change another user's records.

## Modules

- **User & Authentication:** register, sign in, JWT-protected profile, and sign out. Passwords are stored with BCrypt. Sign out clears the token in the browser; JWTs otherwise expire after 24 hours.
- **Expense Management:** create, list, read, update, and delete expenses. Lists can be filtered by category and date range.
- **Subscription Management:** create, list, read, update, and cancel subscriptions. Cancellation changes the status to `CANCELLED` so the record remains visible. Past billing dates advance by one month or year until the next date is today or later.
- **Dashboard & Analytics:** current-month expense total and category totals use database `SUM` and `GROUP BY` queries. Active subscriptions are normalized to monthly and yearly costs. Upcoming renewals include active subscriptions due from today through the next seven days.

## Database

There are exactly three entity tables:

- `users`: id, name, email, password
- `expenses`: id, amount, category, description, payment method, expense date, user foreign key
- `subscriptions`: id, name, amount, billing cycle, category, next billing date, status, user foreign key

Run [`database/schema.sql`](database/schema.sql) in MySQL Workbench. The script creates the database and its three tables, with foreign keys back to `users`. Hibernate is set to `validate`, so Java/JPA checks the tables instead of modifying the schema at startup.

## Run locally

Prerequisites: Java 17, Maven 3.9+, Node.js, npm, and MySQL.

1. Start MySQL and run `database/schema.sql`.
2. Start the backend from the project root:

   ```powershell
   cd backend
   mvn spring-boot:run
   ```

   Defaults expect MySQL at `localhost:3306`, user `root`, and an empty password. Override with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables if needed. Set `JWT_SECRET` to a private key of at least 32 characters outside local development.

3. In a second terminal, start React:

   ```powershell
   cd frontend
   npm install
   npm run dev
   ```

4. Open `http://localhost:5173` and register an account.

The frontend uses `http://localhost:8080/api` by default. Override it with `VITE_API_URL` if the backend is hosted elsewhere. CORS is configured for the local Vite URL.

## REST API

All endpoints except register and login require `Authorization: Bearer <token>`.

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/api/auth/register` | Create account and return a token |
| POST | `/api/auth/login` | Sign in and return a token |
| POST | `/api/auth/logout` | Sign out endpoint; browser removes its token |
| GET | `/api/auth/me` | Read the signed-in user's profile |
| POST / GET | `/api/expenses` | Create or list the signed-in user's expenses |
| GET / PUT / DELETE | `/api/expenses/{id}` | Read, update, or delete an owned expense |
| POST / GET | `/api/subscriptions` | Create or list the signed-in user's subscriptions |
| GET / PUT / DELETE | `/api/subscriptions/{id}` | Read, update, or cancel an owned subscription |
| GET | `/api/subscriptions/upcoming` | List renewals due in the next seven days |
| GET | `/api/dashboard` | Read the combined dashboard summary |

Expense filters: `category`, `fromDate`, and `toDate`, for example `/api/expenses?category=FOOD&fromDate=2026-10-01&toDate=2026-10-31`.

## Interview walkthrough

Start at `AuthController`: registration hashes the password and login returns a JWT. `JwtAuthenticationFilter` verifies that token on later requests. An expense request then follows `ExpenseController` → `ExpenseService` → `ExpenseRepository`; its owner ID is taken from the authenticated principal. Subscription cost and renewal rules live in `SubscriptionService`. `DashboardService` combines the expense aggregates with those subscription calculations. The three JPA entities map directly to the three database tables.