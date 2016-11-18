package com.goeuro.service.configuration;

import com.goeuro.importer.core.Importer;
import com.goeuro.importer.core.configuration.ImporterLimitsProperties;
import com.goeuro.importer.core.configuration.ImporterProperties;
import com.goeuro.importer.core.configuration.ImporterValidationProperties;
import com.goeuro.importer.core.validator.RouteValidator;
import com.goeuro.importer.core.validator.route.DuplicatedStationsOnRouteValidator;
import com.goeuro.importer.core.validator.route.MaxStationsOnRouteCountValidator;
import com.goeuro.importer.core.validator.route.MinStationsOnRouteCountValidator;
import com.goeuro.importer.core.validator.route.RouteIdValidator;
import com.goeuro.importer.core.validator.route.StationIdOnRouteValidator;
import com.goeuro.importer.file.FileImporter;
import com.goeuro.importer.file.FileReaderHelper;
import com.goeuro.importer.file.extractor.RouteExtractor;
import com.goeuro.importer.file.extractor.RoutesCountExtractor;
import com.goeuro.router.Router;
import com.goeuro.service.configuration.properties.ImporterFileConfigurationProperties;
import com.goeuro.service.configuration.properties.ImporterLimitsConfigurationProperties;
import com.goeuro.service.configuration.properties.ImporterValidationConfigurationProperties;
import com.goeuro.storage.core.Storage;
import com.goeuro.storage.memory.MemoryStorage;
import com.google.common.collect.ImmutableSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.inject.Named;
import java.util.Optional;
import java.util.Set;

@Configuration
@EnableWebMvc
public class ApplicationConfiguration {

    @Bean("importerProperties")
    public ImporterProperties importerProperties(
            ImporterFileConfigurationProperties file,
            ImporterLimitsConfigurationProperties limits,
            ImporterValidationConfigurationProperties validation
    ) {
        return ImporterProperties.ImporterPropertiesBuilder.anImporterProperties()
                .withFile(file)
                .withLimits(limits)
                .withValidation(validation)
                .build();
    }


    @Bean("memoryStorage")
    public Storage memoryStorage() {
        return new MemoryStorage();
    }

    @Bean("fileReaderHelper")
    public FileReaderHelper fileReaderHelper() {
        return new FileReaderHelper();
    }

    @Bean("routesCountExtractor")
    public RoutesCountExtractor routesCountExtractor() {
        return new RoutesCountExtractor();
    }

    @Bean("routeValidators")
    public Set<RouteValidator> routeValidators(
            @Named("importerProperties") ImporterProperties importerProperties
    ) {
        return ImmutableSet.of(
                new MinStationsOnRouteCountValidator(),
                new MaxStationsOnRouteCountValidator(importerProperties),
                new DuplicatedStationsOnRouteValidator(),
                new RouteIdValidator(),
                new StationIdOnRouteValidator()
        );
    }

    @Bean("routeExtractor")
    public RouteExtractor routeExtractor(
            @Named("routeValidators") Set<RouteValidator> routeValidators
    ) {
        return new RouteExtractor(routeValidators);
    }

    @Bean("fileImporter")
    public Importer fileImporter(
            @Named("importerProperties") ImporterProperties importerProperties,
            @Named("fileReaderHelper") FileReaderHelper fileReaderHelper,
            @Named("routesCountExtractor") RoutesCountExtractor routesCountExtractor,
            @Named("routeExtractor") RouteExtractor routeExtractor,
            @Named("memoryStorage") Storage memoryStorage
    ) {
        Integer limitRoutes = Optional.of(importerProperties)
                .map(ImporterProperties::getLimits)
                .map(ImporterLimitsProperties::getRoutes)
                .orElseThrow(() -> new IllegalArgumentException("Unable to get importer.limits.routes from configuration"));

        Boolean validateStationId = Optional.of(importerProperties)
                .map(ImporterProperties::getValidation)
                .map(ImporterValidationProperties::getStations)
                .orElseThrow(() -> new IllegalArgumentException("Unable to get importer.validation.stations from configuration"));

        return new FileImporter(
                fileReaderHelper,
                routesCountExtractor,
                routeExtractor,
                memoryStorage,
                limitRoutes,
                validateStationId
        );
    }

    @Bean("router")
    public Router router(@Named("memoryStorage") Storage memoryStorage) {
        return new Router(memoryStorage);
    }
}
