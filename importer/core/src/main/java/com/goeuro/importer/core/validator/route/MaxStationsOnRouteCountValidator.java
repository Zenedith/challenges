package com.goeuro.importer.core.validator.route;

import com.goeuro.importer.core.configuration.ImporterLimitsProperties;
import com.goeuro.importer.core.configuration.ImporterProperties;
import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MaxStationsOnRouteCountValidator implements RouteValidator {
    private static final Logger logger = LoggerFactory.getLogger(MaxStationsOnRouteCountValidator.class);
    private static final String INVALID_BUS_STATIONS_COUNT_FORMAT = "Invalid bus stations count: \"%d\"";

    private final ImporterProperties importerProperties;

    public MaxStationsOnRouteCountValidator(ImporterProperties importerProperties) {
        this.importerProperties = importerProperties;
    }

    @Override
    public Route validate(Route route) {
        return Optional.of(route)
                .map(Route::getStations)
                .map(List::size)
                .map(size -> checkMaxSize(size, route))
                .orElse(route);
    }

    private Route checkMaxSize(Integer count, Route route) {

        int limitStationsOnRoute = Optional.of(importerProperties)
                .map(ImporterProperties::getLimits)
                .map(ImporterLimitsProperties::getStationsOnRoute)
                .orElse(0);

        if (count > limitStationsOnRoute) {
            String message = String.format(
                    "Invalid bus stations count: %d for route id: \"%s\" - should be less than %d bus stations per route",
                    count,
                    route.getRouteId(),
                    limitStationsOnRoute + 1
            );

            logger.warn(message);
            return routeWithViolation(route, String.format(INVALID_BUS_STATIONS_COUNT_FORMAT, count));
        }

        return route;
    }
}
