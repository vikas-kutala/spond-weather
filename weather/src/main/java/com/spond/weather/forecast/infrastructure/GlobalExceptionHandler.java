package com.spond.weather.forecast.infrastructure;

import com.spond.weather.forecast.MetLocationForecastUnknownUnitsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MetLocationForecastServiceUnavailableException.class)
    public ResponseEntity<String> handleException(MetLocationForecastServiceUnavailableException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(e.getMessage());
    }

    @ExceptionHandler(value = MetLocationForecastUnknownUnitsException.class)
    public ResponseEntity<String> handleException(MetLocationForecastUnknownUnitsException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                             .body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Something went wrong", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try again later");
    }

}
