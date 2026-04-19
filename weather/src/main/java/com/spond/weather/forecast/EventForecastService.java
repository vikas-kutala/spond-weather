package com.spond.weather.forecast;

import com.spond.weather.forecast.infrastructure.DtoTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static com.spond.weather.forecast.infrastructure.DtoTypes.AirTemperature;
import static com.spond.weather.forecast.infrastructure.DtoTypes.EventForecastDto;
import static com.spond.weather.forecast.infrastructure.DtoTypes.WindSpeed;
import static com.spond.weather.forecast.infrastructure.ExternalMetDataTypes.ForecastReading;
import static com.spond.weather.forecast.infrastructure.ExternalMetDataTypes.ForecastTimeSeries;
import static com.spond.weather.forecast.infrastructure.ExternalMetDataTypes.MetLocationForecastResponse;
import static com.spond.weather.forecast.internal.ForecastTimeSeriesUtils.arithmeticMean;
import static com.spond.weather.forecast.internal.ForecastTimeSeriesUtils.beforeEventStartDate;
import static com.spond.weather.forecast.internal.ForecastTimeSeriesUtils.beforeOrEqualsEventEndDate;
import static com.spond.weather.forecast.internal.RoundingUtils.roundAirTemp;
import static com.spond.weather.forecast.internal.RoundingUtils.roundWindSpeed;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventForecastService {

    final Function<List<ForecastTimeSeries>, DtoTypes.EventForecastDto> toEventForecastDto = forecast -> {
        if (isEmpty(forecast)) {
            return EventForecastDto.empty();
        }

        double airTemp = arithmeticMean(forecast, ForecastReading::airTemperature).getAsDouble();
        double windSpeed = arithmeticMean(forecast, ForecastReading::windSpeed).getAsDouble();

        return new EventForecastDto(new AirTemperature(roundAirTemp.apply(airTemp), DtoTypes.AirTempUnit.CELSIUS),
                                    new WindSpeed(roundWindSpeed.apply(windSpeed), DtoTypes.WindSpeedUnit.MT_PER_SEC));

    };

    private final LocationForecastService locationForecastService;

    public EventForecastDto getEventForecast(Double lat,
                                             Double lon,
                                             LocalDateTime eventStartTime,
                                             LocalDateTime eventEndTime) {

        MetLocationForecastResponse locationForecast = locationForecastService.getLocationForecast(lat, lon);
        if (locationForecast == null) {
            return EventForecastDto.empty();
        }

        List<ForecastTimeSeries> eventDatesForecast = locationForecast.properties().forecastTimeSeries().stream()
                                                                      .dropWhile(ts -> beforeEventStartDate.apply(ts, eventStartTime))
                                                                      .takeWhile(ts -> beforeOrEqualsEventEndDate.apply(ts, eventEndTime))
                                                                      .toList();

        int eventStartTimeIndex = findBracketingIndex(eventDatesForecast, eventStartTime, 0);
        int eventEndTimeIndex = findBracketingIndex(eventDatesForecast, eventEndTime, eventStartTimeIndex);

        eventDatesForecast.subList(eventStartTimeIndex, eventEndTimeIndex + 1).forEach(ts -> log.debug(ts.toString()));

        return toEventForecastDto.apply(eventDatesForecast.subList(eventStartTimeIndex, eventEndTimeIndex + 1));
    }

    int findBracketingIndex(List<ForecastTimeSeries> forecast, LocalDateTime timestamp, int fromIndex) {
        for (int i = fromIndex; i < forecast.size() - 1; i++) {
            LocalDateTime currentTsTime = forecast.get(i).time();
            LocalDateTime nextTsTimestamp = forecast.get(i + 1).time();

            if (timestamp.isBefore(nextTsTimestamp) && (timestamp.isAfter(currentTsTime) || timestamp.isEqual(currentTsTime))) {
                return i;
            }
        }
        return fromIndex;
    }
}
