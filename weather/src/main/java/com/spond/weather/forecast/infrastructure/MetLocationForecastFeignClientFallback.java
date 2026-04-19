package com.spond.weather.forecast.infrastructure;

import org.springframework.stereotype.Component;

@Component
public class MetLocationForecastFeignClientFallback implements MetLocationForecastFeignClient {

    @Override
    public ExternalMetDataTypes.MetLocationForecastResponse getLocationForecast(double lat, double lon) {
        throw new MetLocationForecastServiceUnavailableException();
    }
}
