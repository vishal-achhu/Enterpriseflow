# Architecture Notes

## Service boundaries
- `backend-java` is the system of record and owns customer/order lifecycle rules.
- `analytics-python` is stateless and receives explicit metric payloads. It does not directly mutate business data.
- `frontend` communicates with the Java API; it never connects directly to PostgreSQL.
- SOAP is isolated in `integration` package as a legacy partner boundary.

## Data model
- `app_user`: authentication identity.
- `customer`: customer profile.
- `customer_order`: order header and lifecycle state.
- `order_item`: order line details.

## Production evolution
For a more mature design, introduce an outbox table and message broker (Kafka/RabbitMQ) for reliable asynchronous events; use Flyway for versioned schema changes; add tenant isolation, audit tables, role permissions, refresh token rotation, observability, and contract tests.
