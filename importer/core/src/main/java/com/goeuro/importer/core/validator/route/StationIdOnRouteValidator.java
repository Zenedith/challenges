package com.goeuro.importer.core.validator.route;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class StationIdOnRouteValidator implements RouteValidator {
    private static final Logger logger = LoggerFactory.getLogger(StationIdOnRouteValidator.class);
    private static final String INVALID_STATION_ID_FORMAT = "Invalid station id: \"%s\"";

    @Override
    public Route validate(Route route) {
        return Optional.of(route)
                .map(Route::getStations)
                .map(stations -> validateStationIds(stations, route))
                .orElse(route);
    }

    private Route validateStationIds(List<String> routeRow, Route route) {
        Route.RouteBuilder routeBuilder = Route.RouteBuilder.aRoute().from(route);
        routeRow.forEach(stationId -> validateStationId(route.getRouteId(), routeBuilder, stationId));
        return routeBuilder.build();
    }

    private void validateStationId(String routeId, Route.RouteBuilder routeBuilder, String val) {
        try {
            int stationId = Integer.parseInt(val);
            if (stationId < 1) {
                String violation = String.format("Unable to parse station id: \"%s\" for route id: \"%s\"", stationId, routeId);
                logger.warn(violation);
                routeBuilder.addViolation(String.format(INVALID_STATION_ID_FORMAT, stationId));
            }

        } catch (NumberFormatException e) {
            String violation = String.format("Unable to parse station id: \"%s\" for route id: \"%s\"", val, routeId);
            logger.warn(violation);
            routeBuilder.addViolation(String.format(INVALID_STATION_ID_FORMAT, val));
        }
    }
}
