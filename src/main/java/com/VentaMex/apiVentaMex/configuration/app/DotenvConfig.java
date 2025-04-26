package com.VentaMex.apiVentaMex.configuration.app;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(dotenvEntry -> {
            System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue());
        });
    }
}
