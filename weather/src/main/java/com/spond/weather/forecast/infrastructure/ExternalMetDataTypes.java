package com.spond.weather.forecast.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class ExternalMetDataTypes {

    public record MetLocationForecastResponse(Properties properties) {
    }

    public record Properties(Meta meta,
                             @JsonProperty("timeseries") List<ForecastTimeSeries> forecastTimeSeries) {
    }

    public record Meta(
            @JsonProperty("updated_at") LocalDateTime utcUpdatedAt,
            Units units
    ) {
    }

    public record Units(
            @JsonProperty("air_temperature") String unitAirTemperature,
            @JsonProperty("wind_speed") String unitWindSpeed
    ) {
    }

    public record ForecastTimeSeries(LocalDateTime time,
                                     Data data) {
    }

    public record Data(Instant instant) {
    }

    public record Instant(@JsonProperty("details") ForecastReading forecastReading) {
    }

    public record ForecastReading(
            @JsonProperty("air_temperature") double airTemperature,
            @JsonProperty("wind_speed") double windSpeed
    ) {
    }
}
