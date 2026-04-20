package com.spond.weather.forecast;

import com.spond.weather.forecast.infrastructure.DtoTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.spond.weather.forecast.infrastructure.DtoTypes.EventForecastDto;
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

    private final LocationForecastService locationForecastService;

    public EventForecastDto getEventForecast(Double lat,
                                             Double lon,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime) {

        MetLocationForecastResponse locationForecast = locationForecastService.getLocationForecast(lat, lon);
        if (locationForecast == null) {
            return EventForecastDto.empty();
        }

        // Consider only the forecast for event start & end dates
        List<ForecastTimeSeries> eventDatesForecast = locationForecast.properties().forecastTimeSeries().stream()
                                                                      .dropWhile(ts -> beforeEventStartDate.apply(ts, startTime))
                                                                      .takeWhile(ts -> beforeOrEqualsEventEndDate.apply(ts, endTime))
                                                                      .toList();

        // find indices of forecast time-series range that spans entire event duration
        int startTimeIndex = findBracketingIndex(eventDatesForecast, startTime, 0);
        int endTimeIndex = findBracketingIndex(eventDatesForecast, endTime, startTimeIndex);
        List<ForecastTimeSeries> eventForecast = eventDatesForecast.subList(startTimeIndex, endTimeIndex + 1);

        eventForecast.forEach(ts -> log.debug(ts.toString()));

        return aggregateAndMapToEventForecastDto(lat, lon, startTime, endTime, eventForecast);
    }

    static int findBracketingIndex(List<ForecastTimeSeries> forecast, LocalDateTime timestamp, int fromIndex) {
        for (int i = fromIndex; i < forecast.size() - 1; i++) {
            LocalDateTime currentTsTime = forecast.get(i).time();
            LocalDateTime nextTsTime = forecast.get(i + 1).time();

            if (timestamp.isBefore(nextTsTime) && (timestamp.isAfter(currentTsTime) || timestamp.isEqual(currentTsTime))) {
                return i;
            }
        }
        return fromIndex;
    }

    EventForecastDto aggregateAndMapToEventForecastDto(Double lat, Double lon, LocalDateTime start,
                                                       LocalDateTime end, List<ForecastTimeSeries> forecast) {
        if (isEmpty(forecast)) {
            return EventForecastDto.empty();
        }

        double airTemp = arithmeticMean(forecast, ForecastReading::airTemperature).getAsDouble();
        double windSpeed = arithmeticMean(forecast, ForecastReading::windSpeed).getAsDouble();

        return new EventForecastDto(lat, lon, start, end,
                                    new DtoTypes.AirTemperature(roundAirTemp.apply(airTemp), DtoTypes.AirTempUnit.CELSIUS),
                                    new DtoTypes.WindSpeed(roundWindSpeed.apply(windSpeed), DtoTypes.WindSpeedUnit.MT_PER_SEC));
    }

}
