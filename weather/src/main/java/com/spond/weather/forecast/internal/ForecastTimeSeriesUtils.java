package com.spond.weather.forecast.internal;


import com.spond.weather.forecast.infrastructure.ExternalMetDataTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.ToDoubleFunction;

import static com.spond.weather.forecast.infrastructure.ExternalMetDataTypes.ForecastTimeSeries;
import static java.time.temporal.ChronoUnit.DAYS;

public class ForecastTimeSeriesUtils {


    public static BiFunction<ForecastTimeSeries, LocalDateTime, Boolean> beforeEventStartDate = (ts, est) -> ts.time()
                                                                                                               .truncatedTo(DAYS)
                                                                                                               .isBefore(est.truncatedTo(DAYS));

    public static BiFunction<ForecastTimeSeries, LocalDateTime, Boolean> beforeOrEqualsEventEndDate = (ts, edt) -> ts.time()
                                                                                                                     .truncatedTo(DAYS)
                                                                                                                     .compareTo(edt.truncatedTo(DAYS)) <= 0;

    public static OptionalDouble arithmeticMean(List<ForecastTimeSeries> tsLst,
                                                ToDoubleFunction<ExternalMetDataTypes.ForecastReading> extractor) {
        return tsLst.stream()
                    .mapToDouble(ts -> extractor.applyAsDouble(ts.data().instant().forecastReading()))
                    .average();
    }

}
