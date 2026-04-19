package com.spond.weather.forecast.infrastructure;

public class DtoTypes {

    public enum AirTempUnit {CELSIUS}

    public enum WindSpeedUnit {MT_PER_SEC}

    public record EventForecastDto(AirTemperature airTemperature,
                                   WindSpeed windSpeed) {

        private static final EventForecastDto EMPTY = new EventForecastDto(null, null);

        public static EventForecastDto empty() {
            return EMPTY;
        }
    }

    public record AirTemperature(Double value,
                                 AirTempUnit unit) {
    }

    public record WindSpeed(Double value,
                            WindSpeedUnit unit) {
    }
}
