package com.spond.weather.forecast;

public class MetLocationForecastUnknownUnitsException extends RuntimeException {
    public MetLocationForecastUnknownUnitsException() {
        super("MetLocationForecastAPI response has unknown forecast reading units!");
    }
}
