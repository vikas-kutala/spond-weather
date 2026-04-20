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
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.spond.weather.forecast.infrastructure.DtoTypes.EventForecastDto;
import static com.spond.weather.forecast.internal.RoundingUtils.roundCoordinate;

@RestController
@RequiredArgsConstructor
public class EventForecastController {

    private final BiFunction<LocalDateTime, LocalDateTime, Boolean> validateEventTimes = (start, end) -> {
        LocalDateTime now = LocalDateTime.now();
        return start.isAfter(now.plusDays(7L)) || end.isBefore(now) || start.isAfter(end);
    };

    private final Function<LocalDateTime, LocalDateTime> effectiveStartTime = start -> start.isAfter(LocalDateTime.now()) ? start : LocalDateTime.now();

    private final EventForecastService eventForecastService;

    @GetMapping("/api/v1/weather/forecasts")
    public ResponseEntity<EventForecastDto> getForecast(@DecimalMin("-90.0") @DecimalMax("90.0") @NotNull @RequestParam Double lat,
                                                        @DecimalMin("-180.0") @DecimalMax("180.0") @NotNull @RequestParam Double lon,
                                                        @RequestParam @NotNull LocalDateTime eventStartTime,
                                                        @RequestParam @NotNull LocalDateTime eventEndTime) {

        if (validateEventTimes.apply(eventStartTime, eventEndTime)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(eventForecastService.getEventForecast(roundCoordinate.apply(lat),
                                                                       roundCoordinate.apply(lon),
                                                                       effectiveStartTime.apply(eventStartTime),
                                                                       eventEndTime));
    }

}
