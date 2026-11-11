EnterpriseFlow — Enterprise Business Operations & Integration Platform
<p align="center">
  <strong>A multi-service full-stack business operations application</strong><br/>
  Java 21 · Spring Boot 3 · Python · FastAPI · React · TypeScript · PostgreSQL
</p><p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white" />
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-3-6DB33F?logo=springboot&logoColor=white" />
  <img alt="Python" src="https://img.shields.io/badge/Python-3.x-3776AB?logo=python&logoColor=white" />
  <img alt="FastAPI" src="https://img.shields.io/badge/FastAPI-API-009688?logo=fastapi&logoColor=white" />
  <img alt="React" src="https://img.shields.io/badge/React-TypeScript-61DAFB?logo=react&logoColor=black" />
  <img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white" />
  <img alt="Docker" src="https://img.shields.io/badge/Docker_Compose-2496ED?logo=docker&logoColor=white" />
</p>EnterpriseFlow is a portfolio project demonstrating how a business application can separate its user interface, core business API, analytics capability, and relational data store. It supports customer and order workflows, JWT-based authentication, operational dashboard summaries, and a lightweight forecast endpoint.
Project status and accuracy: This repository is a local development/portfolio implementation, not a production deployment. The Python forecast is a moving-average baseline. Redis is included in Compose but is not currently used by application logic. The SOAP area is an adapter boundary; a real partner WSDL and live SOAP invocation have not been configured. AWS deployment is documented as a blueprint only.

Contents
- Product overview
- Visual tour
- Architecture
- Features
- Technology choices
- Repository structure
- Run locally
- Demo workflow
- API reference
- Example requests
- Data model
- Testing and verification
- Security notes
- Known limitations and roadmap
- Interview talking points
Product overview
Enterprise teams often manage customer records, orders, and operational reporting across disconnected tools. EnterpriseFlow provides a single workspace for these common workflows. The React client calls a Java Spring Boot API; the Java service owns customer/order business rules and PostgreSQL persistence. A separate Python FastAPI service provides a simple demand forecast.
Core use cases
- Register and authenticate a workspace user.
- Create and review customer records.
- Create orders with line items and totals.
- Track order lifecycle states: DRAFT, CONFIRMED, FULFILLED, or CANCELLED.
- Review dashboard KPIs and recent operational records.
- Request a 7-day baseline forecast from the Python analytics service.
Visual tour
Dashboard layout preview

This is an illustrative documentation preview based on the current React component structure, not a captured screenshot of a running application. Replace it with a browser screenshot after launching the project and adding demo records.
System architecture

Order request lifecycle

Architecture
Component	Responsibility	Main technologies
Web client	Login, dashboard, customer/order views, forecast action	React, TypeScript, Vite
Core API	Authentication, business rules, customer/order operations	Java 21, Spring Boot 3
Analytics API	Moving-average baseline forecast	Python, FastAPI
Relational store	Users, customers, orders, line items	PostgreSQL 16
Schema management	Versioned database changes	Flyway
Runtime	Local multi-container orchestration	Docker Compose


Request path: Browser → React client → Spring Boot REST API → PostgreSQL. Forecast requests are forwarded by the Java service to the FastAPI analytics service. The browser does not connect directly to PostgreSQL.
Features
Authentication
- User registration and login endpoints.
- JWT access token returned after successful authentication.
- Password hashing and protected business endpoints.
Customer and order management
- Customer listing and creation.
- Order listing and creation.
- Order status update endpoint.
- Order header and line-item persistence.
- Dashboard summary for customer count, order count, open orders, and tracked revenue.
Analytics
The Python service accepts a sequence of recent daily order counts and a forecast horizon. It returns a simple moving-average baseline. This is a transparent baseline method, not a trained machine-learning model.
Integration boundary
The repository contains a SOAP partner adapter boundary to isolate future legacy-system integration. A real SOAP client invocation requires a partner WSDL, authentication details, and contract-specific request/response mapping; those are not supplied in this starter.
Technology choices
- Spring Boot: structured Java application, dependency injection, REST controllers, and integration with Spring Data JPA.
- Python/FastAPI: separate analytics service with typed request/response schemas and interactive OpenAPI docs.
- React/TypeScript: component-based UI with compile-time type checking.
- PostgreSQL: relational persistence and transactional consistency for customer/order data.
- Flyway: repeatable, versioned schema migrations.
- Docker Compose: consistent local service orchestration.
Repository structure
EnterpriseFlow/
├── backend-java/          # Spring Boot business API
├── analytics-python/      # FastAPI forecast service
├── frontend/              # React + TypeScript UI
├── docs/
│   ├── images/             # README diagrams and illustrative preview
│   ├── architecture.md
│   └── api-examples.md
├── infra/                  # Cloud deployment blueprint
├── .github/workflows/      # CI starter
├── docker-compose.yml
├── .env.example
└── README.md
Clone and run the project
You can run EnterpriseFlow with Docker Compose (the simplest option) or run each service directly on your computer. The instructions below use Windows PowerShell, with macOS/Linux alternatives where useful.
Prerequisites
For either method
- Git, if cloning from GitHub.
- An internet connection for downloading dependencies.
Docker method
- Docker Desktop on Windows/macOS, or Docker Engine and the Docker Compose plugin on Linux.
- Docker Desktop must be open and its engine running before you start the commands.
Manual / no-Docker method
- Java Development Kit (JDK) 21.
- Apache Maven 3.9+.
- Python 3.10 or newer (Python 3.12 recommended).
- Node.js 20+ and npm.
- PostgreSQL 16 (or a compatible supported PostgreSQL version).
1. Clone this repository to your computer
First, create or open the GitHub repository containing this project. Copy its HTTPS clone URL from Code → HTTPS. Replace the placeholder below with your actual URL:
git clone https://github.com/<your-github-username>/EnterpriseFlow.git
cd EnterpriseFlow
For example, if your GitHub username is octocat, the command would be git clone https://github.com/octocat/EnterpriseFlow.git.

If you have not uploaded the project to GitHub yet, download the project ZIP, extract it, and open a terminal in the extracted EnterpriseFlow folder. The folder you enter must contain docker-compose.yml, .env.example, and this README.md.

To pull the latest changes later:
git pull origin main
If your repository's default branch is named master or something else, use that branch name instead of main.
Option A — Run with Docker Compose (recommended)
1. Open PowerShell/Terminal in the repository root (the folder containing docker-compose.yml).
2. Create your local environment file:
   Windows PowerShell
   Copy-Item .env.example .env
   macOS/Linux
   cp .env.example .env
3. Open .env and review the local database password and JWT secret. Use a long random JWT secret. Do not commit .env or real secrets to Git.
4. Start and build the services:
   docker compose up --build
   Keep this terminal open to view logs. The first build can take several minutes.
5. Open the application and service endpoints:
   Service	URL
   React frontend	http://localhost:5173
   Java Spring Boot API	http://localhost:8080
   Java health check	http://localhost:8080/actuator/health
   Python FastAPI docs	http://localhost:8000/docs
   PostgreSQL	localhost:5432
6. Register a user once through the Java API. The current frontend has a sign-in form but no registration screen. In a second PowerShell window, run:
   $body = @{
     name = "Demo User"
     email = "demo@example.com"
     password = "StrongPassword123"
   } | ConvertTo-Json
   
   Invoke-RestMethod -Method Post `
     -Uri "http://localhost:8080/api/auth/register" `
     -ContentType "application/json" `
     -Body $body
7. Open http://localhost:5173 and sign in with the account you registered.
Stop the services with Ctrl+C in the Compose terminal, then run:
docker compose down
To also remove the PostgreSQL volume and all locally persisted project data (destructive):
docker compose down -v
Option B — Run without Docker
In this mode, PostgreSQL, the Python analytics API, the Java API, and the React frontend run as separate local processes. Start PostgreSQL first, then the Python API, Java API, and frontend. Keep each service running in its own terminal window.
1. Create the PostgreSQL database
Start your local PostgreSQL server. Open pgAdmin or psql and create the database and user. Example SQL:
CREATE USER enterpriseflow WITH PASSWORD 'change_me_local';
CREATE DATABASE enterpriseflow OWNER enterpriseflow;
Use a password you control. If the role already exists, update its password rather than creating it again. Ensure PostgreSQL is listening on port 5432 or adjust the connection settings below.
2. Configure environment variables
Create .env from the example for reference:

Windows PowerShell
Copy-Item .env.example .env
macOS/Linux
cp .env.example .env
The Compose file reads .env automatically, but processes started manually do not automatically load it. Set the variables in the terminal used to start the Java service. For PowerShell:
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/enterpriseflow"
$env:SPRING_DATASOURCE_USERNAME="enterpriseflow"
$env:SPRING_DATASOURCE_PASSWORD="change_me_local"
$env:JWT_SECRET="replace-this-with-a-long-random-secret-at-least-32-characters"
$env:JWT_EXPIRATION_MS="3600000"
$env:ANALYTICS_BASE_URL="http://localhost:8000"
Change the password to the one assigned to your PostgreSQL user. Flyway applies the project's database migration when the Java application starts.
3. Start the Python analytics service
Open Terminal 1 at the repository root:
cd analytics-python
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn app.main:app --reload --host 127.0.0.1 --port 8000
On macOS/Linux, activate the environment with source .venv/bin/activate instead. The API documentation should be available at http://localhost:8000/docs.
4. Start the Java Spring Boot API
Open Terminal 2 at the repository root and enter the Java service directory:
cd backend-java
Set the Java service environment variables in this same terminal (repeat the PowerShell assignments from step 2), then start the application:
mvn spring-boot:run
If Maven is not installed, install Maven 3.9+ and confirm mvn -version works. Wait for Spring Boot to finish starting. Check http://localhost:8080/actuator/health.
5. Start the React frontend
Open Terminal 3 at the repository root:
cd frontend
npm install
$env:VITE_API_BASE_URL="http://localhost:8080/api"
npm run dev
On macOS/Linux, set the variable with export VITE_API_BASE_URL=http://localhost:8080/api before npm run dev. Open http://localhost:5173.
6. Register and sign in
With PostgreSQL and all three application services running, use the registration request from Option A (step 6), then sign in through the frontend.
7. Stop manually started services
Press Ctrl+C in each service terminal. Deactivate the Python virtual environment with deactivate if desired. PostgreSQL can be stopped separately through its service manager or pgAdmin installation controls.
Local ports
Component	Default port
React/Vite frontend	5173
Java/Spring Boot API	8080
Python/FastAPI analytics	8000
PostgreSQL	5432
Redis container (not currently used by app logic)	6379


Troubleshooting
- git is not recognized: Install Git, reopen the terminal, and check git --version.
- docker compose cannot connect: Start Docker Desktop and wait for the engine to become ready.
- Port already in use: Stop the process using that port or change the host port mapping in docker-compose.yml.
- Java database connection fails: Confirm PostgreSQL is running, the database/user exist, and the SPRING_DATASOURCE_* values match.
- Flyway or schema validation fails: Check the startup logs and confirm the database is empty or has the expected migration history. Avoid manually creating tables that Flyway owns.
- Python command not found: Confirm Python is installed and available on PATH; on some Windows installations use py -3 instead of python.
- PowerShell blocks virtual-environment activation: Use Set-ExecutionPolicy -Scope CurrentUser RemoteSigned if permitted by your machine's policy, or activate the environment using a supported shell.
- Frontend cannot reach the API: Confirm the Java service is running and VITE_API_BASE_URL points to http://localhost:8080/api.
Important: This is a development/portfolio setup. Do not expose the services publicly with the sample credentials or development secrets. Replace secrets and apply production security, monitoring, backups, and deployment configuration before hosting.

Demo workflow
1. Register a user through POST /api/auth/register.
2. Sign in from the frontend.
3. Create one or more customer records using the customer API.
4. Create an order linked to a customer.
5. Update an order status and refresh the dashboard.
6. Use Run forecast to call the Python analytics service.
7. Inspect the returned forecast values and method label.
Example forecast input
{
  "daily_orders": [8, 10, 9, 13, 12, 15, 14],
  "horizon_days": 7
}
The frontend currently uses this example series when the forecast action is selected. Treat any returned values as a baseline demonstration, not validated business demand predictions.
API reference
Base URL: http://localhost:8080/api
Method	Endpoint	Purpose
POST	/auth/register	Register a user
POST	/auth/login	Authenticate and obtain JWT
GET / POST	/customers	List or create customers
GET / POST	/orders	List or create orders
PATCH	/orders/{id}/status	Update order status
GET	/dashboard/summary	Retrieve operational KPIs
POST	/analytics/forecast	Request forecast from Python service


Authenticated routes expect:
Authorization: Bearer <access-token>
For request payloads and additional examples, see [`docs/api-examples.md`](docs/api-examples.md).
Data model
The primary relational entities include:
- app_user: authentication identity.
- customer: customer profile information.
- customer_order: order header, customer association, and lifecycle status.
- order_item: individual items and quantities associated with an order.
The Java backend uses Spring Data JPA for persistence mapping, while Flyway manages schema migrations.
Testing and verification
The repository includes a Python analytics test and a GitHub Actions CI starter. Before presenting the project as verified, run the services and tests in your own environment.

Suggested checks:
# Python analytics tests (from analytics-python, in a Python environment)
pytest

# Java tests (from backend-java, with Java 21 and Maven available)
./mvnw test
If the Maven wrapper is not present in your checkout, use an installed Maven command (mvn test). Also verify the full Docker Compose startup, registration/login, customer/order CRUD, and forecast call manually. No runtime test results are claimed by this README.
Security notes
- Keep secrets in local environment variables or a secret manager.
- Use a strong JWT signing secret outside local development.
- Enforce HTTPS in hosted environments.
- Add rate limiting, audit logging, authorization roles, and refresh-token rotation before production use.
- Review CORS, validation, error handling, and database backup policies before deployment.
Known limitations and roadmap
- [ ] Add a real SOAP partner WSDL and test live SOAP request/response mapping.
- [ ] Integrate Redis for a defined cache or rate-limiting use case (currently infrastructure only).
- [ ] Add role-based access control and tenant isolation.
- [ ] Add API integration and end-to-end tests.
- [ ] Add audit logs and observability.
- [ ] Evaluate stronger forecasting approaches on representative historical data.
- [ ] Deploy to AWS; current AWS notes are a blueprint, not a live deployment.
