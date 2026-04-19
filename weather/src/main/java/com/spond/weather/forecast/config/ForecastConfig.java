package com.spond.weather.forecast.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = com.spond.weather.forecast.infrastructure.MetLocationForecastFeignClient.class)
@EnableCaching
public class ForecastConfig {

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        return new CaffeineCacheManager();
    }
}
