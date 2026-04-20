package com.spond.weather.forecast;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.spond.weather.forecast.EventForecastService.findBracketingIndex;
import static com.spond.weather.forecast.infrastructure.ExternalMetDataTypes.ForecastTimeSeries;
import static org.assertj.core.api.Assertions.assertThat;

class EventForecastServiceTest {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.parse("2026-04-20T10:00:00");
    private static final List<ForecastTimeSeries> TIME_SERIES = List.of(
            ts(TIMESTAMP),
            ts(TIMESTAMP.plusHours(1)),
            ts(TIMESTAMP.plusHours(2)),
            ts(TIMESTAMP.plusDays(3)),
            ts(TIMESTAMP.plusDays(4))
    );

    private static ForecastTimeSeries ts(LocalDateTime time) {
        return new ForecastTimeSeries(time, null);
    }

    @Test
    void whenTimestampBetweenTwoEntries() {
        int idx = findBracketingIndex(TIME_SERIES, TIMESTAMP.plusMinutes(90), 0);
        assertThat(idx).isEqualTo(1);
    }

    @Test
    void whenTimestampBetweenFirstTwoEntries() {
        int idx = findBracketingIndex(TIME_SERIES, TIMESTAMP.plusMinutes(15), 0);
        assertThat(idx).isEqualTo(0);
    }

    @Test
    void whenTimestampBetweenLastTwoEntries() {
        int idx = findBracketingIndex(TIME_SERIES, TIMESTAMP.plusDays(3).plusMinutes(30), 0);
        assertThat(idx).isEqualTo(3);
    }

    @Test
    void whenTimestampEqualsFirstEntry() {
        int idx = findBracketingIndex(TIME_SERIES, TIMESTAMP, 0);
        assertThat(idx).isEqualTo(0);
    }
}