package com.goeuro.service.configuration.properties;

import com.goeuro.importer.core.configuration.ImporterLimitsProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "importer.limits")
public class ImporterLimitsConfigurationProperties extends ImporterLimitsProperties {

}
