package com.spond.weather.forecast.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "met-location-forecast-api",
        url = "",
        fallback = MetLocationForecastFeignClientFallback.class,
        dismiss404 = true
)
public interface MetLocationForecastFeignClient {
}
