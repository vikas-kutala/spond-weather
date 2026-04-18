package com.spond.weather.forecast;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {

    @GetMapping("/api/v1/weather/forecasts")
    public String getForecast(@DecimalMin("-90.0") @DecimalMax("90.0") @NotNull Double lat,
                              @DecimalMin("-90.0") @DecimalMax("90.0") @NotNull Double lon) {
        return "test - " + lat + ":" + lon;
    }

}
