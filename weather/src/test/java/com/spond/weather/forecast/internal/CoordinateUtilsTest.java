package com.spond.weather.forecast.internal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.spond.weather.forecast.internal.RoundingUtils.roundCoordinate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CoordinateUtilsTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            0, 0
            -20.0110, -20.02
            20.0110, 20.01
            98.98784, 98.98
            """
    )
    public void testRoundCoordinate(Double input, Double expected) {
        assertThat(roundCoordinate.apply(input)).isEqualTo(expected);
    }
}
