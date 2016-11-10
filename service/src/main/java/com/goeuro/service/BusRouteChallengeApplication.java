package com.goeuro.service;

import com.goeuro.service.configuration.properties.ImporterFileConfigurationProperties;
import com.goeuro.service.configuration.properties.ImporterLimitsConfigurationProperties;
import com.goeuro.service.configuration.properties.ImporterValidationConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        {
                ImporterFileConfigurationProperties.class,
                ImporterLimitsConfigurationProperties.class,
                ImporterValidationConfigurationProperties.class
        }
)
public class BusRouteChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusRouteChallengeApplication.class, args);
    }
}
