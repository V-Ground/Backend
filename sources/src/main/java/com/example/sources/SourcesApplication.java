package com.example.sources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SourcesApplication {

    private static final String CONFIGURATION_LOCATION = "spring.config.location=" +
            "classpath:application.yml" +
            "," +
            "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(SourcesApplication.class)
                .properties(CONFIGURATION_LOCATION)
                .run(args);
    }
}
