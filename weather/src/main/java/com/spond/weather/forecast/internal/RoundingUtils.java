package com.spond.weather.forecast.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class RoundingUtils {

    public static Function<Double, Double> roundCoordinate = c -> BigDecimal.valueOf(c).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

    public static Function<Double, Double> roundAirTemp = t -> BigDecimal.valueOf(t).setScale(1, RoundingMode.HALF_EVEN).doubleValue();

    public static Function<Double, Double> roundWindSpeed = s -> BigDecimal.valueOf(s).setScale(1, RoundingMode.HALF_EVEN).doubleValue();

}
