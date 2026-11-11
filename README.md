# EnterpriseFlow — Intelligent Business Operations & Integration Platform

A portfolio-grade, multi-service business application demonstrating enterprise full-stack engineering.

## What it demonstrates
- **Java 21 + Spring Boot 3**: core business API, validation, layered architecture, JPA, transactions, JWT security.
- **Python + FastAPI**: analytics/forecasting microservice with typed request/response models.
- **TypeScript + React + Vite**: typed operations dashboard.
- **PostgreSQL**: relational data model and Flyway migrations.
- **Redis**: caching / rate-limit-ready infrastructure.
- **REST + SOAP**: REST APIs plus an isolated SOAP partner adapter boundary (contract-specific SOAP invocation is an integration task because no partner WSDL was supplied).
- **Docker Compose**: local multi-service environment.
- **Cloud readiness**: containerized services, environment-based configuration, health checks, CI workflow.

> This repository is an implementation starter with working core flows and clear extension points. Before describing it as production-ready, complete the hardening checklist below, configure real credentials, and run the tests in your environment.

## Architecture

```text
 React + TypeScript (5173)
          |
          | HTTPS / REST
          v
 Java Spring Boot API (8080) ---- PostgreSQL (5432)
          |                            |
          |                            +-- Flyway schema migrations
          |
          +---- Redis (6379)
          |
          +---- Python Analytics API (8000)
          |
          +---- SOAP Partner Gateway (configurable WSDL endpoint)
```

## Main workflows
1. Create and manage customers.
2. Create orders with line items and calculate totals.
3. Move orders through `DRAFT -> CONFIRMED -> FULFILLED` or `CANCELLED`.
4. View operational KPIs and order summaries.
5. Request a lightweight demand forecast from the Python service.
6. Trigger a SOAP partner status lookup through the integration adapter.

## Repository layout

```text
enterpriseflow/
├── backend-java/       Spring Boot business API
├── analytics-python/   FastAPI analytics microservice
├── frontend/           React + TypeScript dashboard
├── infra/              Docker and deployment examples
├── docs/               Architecture and API notes
└── .github/workflows/  CI starter
```

## Run locally

Requirements: Docker Desktop / Docker Engine and Docker Compose.

```bash
cp .env.example .env
docker compose up --build
```

Open:
- Frontend: http://localhost:5173
- Java API: http://localhost:8080/api
- Java health: http://localhost:8080/actuator/health
- Python API docs: http://localhost:8000/docs

The seeded demo account is not included. Register through the API or add a seed user for local demos. For a quick API smoke test, create a customer and then an order using the endpoints below.

## API overview

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Obtain JWT |
| GET/POST | `/api/customers` | List/create customers |
| GET/POST | `/api/orders` | List/create orders |
| PATCH | `/api/orders/{id}/status` | Change order status |
| GET | `/api/dashboard/summary` | Operational KPIs |
| POST | `/api/analytics/forecast` | Call Python analytics |
| GET | `/api/integrations/soap/partner-status/{reference}` | SOAP integration demo |

Authenticated business endpoints expect `Authorization: Bearer <token>`.

## Environment configuration

See `.env.example`. Never commit real secrets. Replace all development secrets before deployment.

## Engineering decisions worth explaining in interviews
- The Java service owns transactional business data and domain rules.
- Python is separated as a specialized analytics service rather than mixing unrelated responsibilities into the monolith.
- PostgreSQL is the system of record; Redis is an optional fast cache.
- REST is used for modern resource-oriented APIs; SOAP is isolated behind an adapter for legacy enterprise partners.
- DTOs separate external API contracts from persistence entities.
- Flyway migrations make schema changes repeatable.
- Docker Compose provides a reproducible local environment.

## Cloud deployment path
A practical deployment target is AWS:
1. Build each service as a Docker image.
2. Push images to Amazon ECR.
3. Deploy Java and Python services to ECS Fargate.
4. Use Amazon RDS for PostgreSQL and ElastiCache for Redis.
5. Store secrets in AWS Secrets Manager.
6. Put the frontend behind S3 + CloudFront (or deploy the container to a managed app platform).
7. Add CloudWatch logs/metrics, HTTPS, health checks, and managed backups.

The included `infra/aws-deployment.md` is a deployment checklist, not an already deployed cloud environment.

## Before publishing to GitHub
- [ ] Change all secrets and configure a production JWT secret.
- [ ] Add integration tests against a disposable PostgreSQL instance.
- [ ] Add pagination and filtering to list endpoints.
- [ ] Add refresh-token rotation, email verification, password reset, and account lockout.
- [ ] Configure CORS for the real frontend origin.
- [ ] Add idempotency keys for order creation and partner requests.
- [ ] Add structured logs, tracing, metrics, and alerting.
- [ ] Implement a real SOAP partner WSDL and contract tests.
- [ ] Add role-based permissions and audit history.
- [ ] Run dependency, container, and static-analysis scans.
- [ ] Deploy to a cloud account and document the live URL only after successful deployment.

## Resume-ready project description (use only after you run and verify it)
**EnterpriseFlow — Enterprise Business Operations & Integration Platform**  
Built a multi-service business platform using Java Spring Boot, Python FastAPI, React, TypeScript, and PostgreSQL. Implemented JWT-secured REST APIs for customer and order workflows, a Python analytics microservice, Redis infrastructure, and a SOAP integration adapter for legacy partner connectivity. Containerized services with Docker Compose and documented an AWS deployment architecture.
