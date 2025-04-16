package com.VentaMex.apiVentaMex.configuration.app;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.postConfigurer(objectMapper -> {
                objectMapper.getFactory().setStreamWriteConstraints(
                        StreamWriteConstraints.builder()
                                .maxNestingDepth(10) // Ajusta según tus necesidades
                                .build()
                );
            });
        };
    }
}
