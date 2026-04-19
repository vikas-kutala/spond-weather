package com.spond.weather.forecast.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "met-location-forecast-api",
        url = "${app.external.met-no.base-url}",
        fallback = MetLocationForecastFeignClientFallback.class,
        dismiss404 = true,
        configuration = MetLocationForecastFeignClientConfig.class
)
public interface MetLocationForecastFeignClient {

    @GetMapping("/locationforecast/2.0/compact")
    ExternalMetDataTypes.MetLocationForecastResponse getLocationForecast(@RequestParam("lat") double lat,
                                                                         @RequestParam("lon") double lon);

}
