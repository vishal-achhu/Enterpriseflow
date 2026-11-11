# API Examples

## Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
 -H 'Content-Type: application/json' \
 -d '{"name":"Demo User","email":"demo@example.com","password":"StrongPassword123"}'
```

## Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
 -H 'Content-Type: application/json' \
 -d '{"email":"demo@example.com","password":"StrongPassword123"}'
```
Copy `accessToken` and set `TOKEN`.

## Create customer
```bash
curl -X POST http://localhost:8080/api/customers \
 -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
 -d '{"name":"Acme Retail","email":"ops@acme.test","company":"Acme"}'
```

## Create order
```bash
curl -X POST http://localhost:8080/api/orders \
 -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
 -d '{"customerId":1,"items":[{"description":"Implementation service","quantity":2,"unitPrice":1250.00}]}'
```

## Forecast
```bash
curl -X POST http://localhost:8080/api/analytics/forecast \
 -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
 -d '{"daily_orders":[8,10,9,13,12,15,14],"horizon_days":7}'
```
