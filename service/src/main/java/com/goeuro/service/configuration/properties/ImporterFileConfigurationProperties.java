package com.goeuro.service.configuration.properties;

import com.goeuro.importer.core.configuration.ImporterFileProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "importer.file")
public class ImporterFileConfigurationProperties extends ImporterFileProperties {

}
