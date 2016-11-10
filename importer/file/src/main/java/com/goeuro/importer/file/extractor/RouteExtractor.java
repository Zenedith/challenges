package com.goeuro.importer.file.extractor;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.exception.route.EmptyRouteDataException;
import com.goeuro.importer.core.exception.route.RouteImportException;
import com.goeuro.importer.core.validator.RouteValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteExtractor {
    private final Set<RouteValidator> routeValidators;

    public RouteExtractor(Set<RouteValidator> routeValidators) {
        this.routeValidators = routeValidators;
    }

    public Route extractSingleRoute(List<String> routeRow) throws RouteImportException {

        String routeId = parseRouteId(routeRow);
        List<String> stations = getStationsList(routeRow);

        Route route = Route.RouteBuilder.aRoute()
                .withRouteId(routeId)
                .withStations(stations)
                .build();

        routeValidators.forEach(validator -> validator.validate(route));
        return route;
    }

    private List<String> getStationsList(List<String> routeRow) {
        return routeRow.stream()
                .skip(1)
                .collect(Collectors.toList());
    }

    private String parseRouteId(List<String> routeRow) throws RouteImportException {
        return routeRow.stream()
                .findFirst()
                .orElseThrow(() -> new EmptyRouteDataException(routeRow.toString()));
    }
}
