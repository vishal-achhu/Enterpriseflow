from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import List

app = FastAPI(title="EnterpriseFlow Analytics", version="1.0.0",
              description="Analytics microservice for operational metrics.")

class ForecastRequest(BaseModel):
    daily_orders: List[int] = Field(min_length=3, max_length=90)
    horizon_days: int = Field(default=7, ge=1, le=30)

class ForecastResponse(BaseModel):
    method: str
    average_daily_orders: float
    forecast: List[float]
    note: str

@app.get("/health")
def health():
    return {"status": "UP", "service": "analytics-python"}

@app.post("/forecast", response_model=ForecastResponse)
def forecast(request: ForecastRequest):
    # Transparent baseline forecast: moving average of the latest up-to-7 observations.
    # Replace with a validated time-series model when real historical data is available.
    window = request.daily_orders[-7:]
    avg = sum(window) / len(window)
    values = [round(avg, 2) for _ in range(request.horizon_days)]
    return ForecastResponse(
        method="7-observation moving-average baseline",
        average_daily_orders=round(avg, 2),
        forecast=values,
        note="Demonstration baseline, not a production demand-planning model."
    )
