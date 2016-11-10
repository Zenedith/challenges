package com.goeuro.importer.core.validator.route;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MinStationsOnRouteCountValidator implements RouteValidator {
    private static final Logger logger = LoggerFactory.getLogger(MinStationsOnRouteCountValidator.class);
    private static final String INVALID_BUS_STATIONS_COUNT_FORMAT = "Invalid bus stations count: \"%d\"";

    @Override
    public Route validate(Route route) {
        return Optional.of(route)
                .map(Route::getStations)
                .map(List::size)
                .map(size -> checkMinSize(size, route))
                .orElse(route);
    }

    private Route checkMinSize(Integer count, Route route) {
        if (count < 2) {
            String message = String.format(
                    "Invalid bus stations count: %d for route id: \"%s\" - required minimum 2 bus stations per route",
                    count,
                    route.getRouteId()
            );
            logger.warn(message);
            return routeWithViolation(route, String.format(INVALID_BUS_STATIONS_COUNT_FORMAT, count));
        }

        return route;
    }
}
