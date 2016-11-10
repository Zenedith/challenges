package com.goeuro.service.configuration.properties;

import com.goeuro.importer.core.configuration.ImporterValidationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "importer.validation")
public class ImporterValidationConfigurationProperties extends ImporterValidationProperties {

}
