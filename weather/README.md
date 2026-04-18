# spond-weather
take home test for spond backend eng position

---

# Running Locally 

```shell
$ export JAVA_HOME=/path/to/jdk-25              # set JAVA_HOME to point to JDK-25
$ ./mvnw clean install                          # build and run tests
$ ./mvnw spring-boot:run                        # run the app
```
Access the endpoints on swagger with [[spond-weather-api]](http://localhost:8080/swagger-ui/index.html)

---

# High-Level Design

```mermaid 

flowchart
    IN((Request))
    FCTR[ForecastController]
    FSRV[ForecastService]
    FLFS[MetLocationForecastService]
    FMAF[MetLocationForecastFeignClient]
    EMA[external:met-api]
    IN --> FCTR
    subgraph spond-weather
        FCTR --> FSRV
        FSRV --> FLFS
        FLFS -- in-memory cache --> FLFS
        FMAF -- circuit-breaker --> FMAF
        FLFS --> FMAF
    end

    FMAF --> EMA
```

- ForecastController: Validate and round the latitude/longitude to 2-decimals(~1 sq.km) before forwarding requests to ForecastService 
- ForecastService: Call MetLocationForecastService to get forecast for latitude/longitude, match the event timestamps and return arithmetic mean of air-temp & wind-speeds
- MetLocationForecastService: Check for cache-hits based on rounded latitude & longitude, else make the call with feign-client
- MetLocationForecastFeignClient: If the circuit-breaker is `OPEN`, call fallback method, else make an API call to `met.no`
---

# Improvements & Next Steps

- Use distributed cache like Redis instead of Caffeine
- Depending on product requirements, replace simple rounding up of latitude & longitude approximation with standard GIS libraries like ApacheSIS/Proj4j for much higher accuracy
 
---

# AI Usage

- No AI usage for the design, planning, implementation & testing of the project
- Gemini to know more about latitude and longitude, specifically to improve my cacheability ideas