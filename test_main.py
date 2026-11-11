from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_health():
    assert client.get("/health").json()["status"] == "UP"

def test_forecast():
    response = client.post("/forecast", json={"daily_orders":[2,4,6,8],"horizon_days":3})
    assert response.status_code == 200
    assert response.json()["forecast"] == [5.0,5.0,5.0]
