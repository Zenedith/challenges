package com.goeuro.importer.core.validator.route;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DuplicatedStationsOnRouteValidator implements RouteValidator {
    private static final Logger logger = LoggerFactory.getLogger(DuplicatedStationsOnRouteValidator.class);
    private static final String FOUND_DUPLICATES_FORMAT = "Found duplicated bus stations: \"%s\"";

    @Override
    public Route validate(Route route) {
        return Optional.of(route)
                .map(Route::getStations)
                .map(stations -> checkDuplicates(stations, route))
                .orElse(route);
    }

    private Route checkDuplicates(List<String> stations, Route route) {

        Set<String> uniqueStations = new HashSet<String>(stations.size());
        Set<String> duplicates = stations.stream()
                .filter(stationId -> !uniqueStations.add(stationId))
                .collect(Collectors.toSet());

        if (!duplicates.isEmpty()) {
            String message = String.format(
                    "Found duplicated bus stations: \"%s\" for route id: \"%s\"",
                    duplicates,
                    route.getRouteId()
            );
            logger.warn(message);
            return routeWithViolation(route, String.format(FOUND_DUPLICATES_FORMAT, duplicates));
        }

        return route;
    }
}
