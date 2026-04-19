package com.spond.weather.forecast;

import com.spond.weather.forecast.infrastructure.ExternalMetDataTypes.MetLocationForecastResponse;
import com.spond.weather.forecast.infrastructure.MetLocationForecastFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Service
@Slf4j
public class LocationForecastService {

    private final MetLocationForecastFeignClient forecastFeignClient;

    private final Predicate<MetLocationForecastResponse> hasKnownWindSpeedUnit = r -> "m/s".equals(r.properties()
                                                                                                    .meta().units()
                                                                                                    .unitWindSpeed());

    private final Predicate<MetLocationForecastResponse> hasKnownAirTempUnit = r -> "celsius".equals(r.properties()
                                                                                                      .meta().units()
                                                                                                      .unitAirTemperature());

    private final Predicate<MetLocationForecastResponse> hasUnknownForecastUnits = not(hasKnownAirTempUnit.and(hasKnownWindSpeedUnit));

    @Cacheable(value = "locationForecastService",
            key = "#lat + '_' + #lon",
            unless = "#result == null")
    public MetLocationForecastResponse getLocationForecast(Double lat, Double lon) {
        log.debug("getLocationForecast() lat:{} lon:{}", lat, lon);

        MetLocationForecastResponse locationForecast = forecastFeignClient.getLocationForecast(lat, lon);
        if (hasUnknownForecastUnits.test(locationForecast)) {
            throw new MetLocationForecastUnknownUnitsException();
        }

        return locationForecast;
    }

}
