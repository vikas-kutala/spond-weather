package com.spond.weather.forecast.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableWireMock({
        @ConfigureWireMock(name = "metApi", baseUrlProperties = "${app.external.met-no.base-url}")
})
public class MetLocationForecastFeignClientFallbackTest {

    @Autowired
    MetLocationForecastFeignClient metLocationForecastFeignClient;

    @Test
    public void testServiceUnavailable() {
        stubFor(get("/locationforecast/2.0/compact?lat=90.0&lon=90.0").willReturn(serviceUnavailable()));
        assertThrows(MetLocationForecastServiceUnavailableException.class,
                     () -> metLocationForecastFeignClient.getLocationForecast(90.0, 90.0));
    }

    @Test
    public void testServerError() {
        stubFor(get("/locationforecast/2.0/compact?lat=90.0&lon=90.0").willReturn(serverError()));
        assertThrows(MetLocationForecastServiceUnavailableException.class,
                     () -> metLocationForecastFeignClient.getLocationForecast(90.0, 90.0));
    }
}
