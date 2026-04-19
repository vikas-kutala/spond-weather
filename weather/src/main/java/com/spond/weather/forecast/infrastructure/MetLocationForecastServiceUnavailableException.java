package com.spond.weather.forecast.infrastructure;

public class MetLocationForecastServiceUnavailableException extends RuntimeException {
    public MetLocationForecastServiceUnavailableException() {
        super("The met.no service is currently unavailable. Please try again later.");
    }
}
