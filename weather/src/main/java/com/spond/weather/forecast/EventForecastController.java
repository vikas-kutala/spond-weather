package com.spond.weather.forecast;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.spond.weather.forecast.internal.RoundingUtils.roundCoordinate;
import static com.spond.weather.forecast.infrastructure.DtoTypes.EventForecastDto;

@RestController
@RequiredArgsConstructor
public class EventForecastController {

    private final EventForecastService eventForecastService;

    @GetMapping("/api/v1/weather/forecasts")
    public ResponseEntity<EventForecastDto> getForecast(@DecimalMin("-90.0") @DecimalMax("90.0") @NotNull @RequestParam Double lat,
                                                        @DecimalMin("-90.0") @DecimalMax("90.0") @NotNull @RequestParam Double lon,
                                                        @RequestParam LocalDateTime eventStartTime,
                                                        @RequestParam LocalDateTime eventEndTime) {

        return ResponseEntity.ok(eventForecastService.getEventForecast(roundCoordinate.apply(lat),
                                                                       roundCoordinate.apply(lon),
                                                                       eventStartTime,
                                                                       eventEndTime));
    }

}
