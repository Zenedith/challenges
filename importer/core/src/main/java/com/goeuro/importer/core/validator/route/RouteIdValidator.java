package com.goeuro.importer.core.validator.route;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RouteIdValidator implements RouteValidator {
    private static final Logger logger = LoggerFactory.getLogger(RouteIdValidator.class);
    private static final String INVALID_ROUTE_ID_FORMAT = "Invalid route id: \"%s\"";

    @Override
    public Route validate(Route route) {
        return Optional.of(route)
                .map(Route::getRouteId)
                .map(routeId -> validateRouteId(routeId, route))
                .orElse(route);
    }

    private Route validateRouteId(String val, Route route) {
        try {
            int routeId = Integer.parseInt(val);
            if (routeId < 1) {
                String message = String.format("Invalid route id: \"%d\" - should be greater than 0", routeId);
                logger.warn(message);
                return routeWithViolation(route, String.format(INVALID_ROUTE_ID_FORMAT, routeId));
            }
        } catch (NumberFormatException e) {
            String message = String.format("Unable to parse route id from value: \"%s\"", val);
            logger.warn(message);
            return routeWithViolation(route, String.format(INVALID_ROUTE_ID_FORMAT, val));
        }

        return route;
    }
}
