package com.spond.weather.forecast.infrastructure;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetLocationForecastFeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate ->
                requestTemplate.header("User-Agent", "github.com/vikas-kutala");
    }
}
