package ru.tinkoff.edu.java.scrapper.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfiguration {
    @Bean
    public Counter counter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("messages.processed");
    }
}
