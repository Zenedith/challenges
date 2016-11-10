package com.goeuro.importer.core.validator;

import com.goeuro.importer.core.domain.Route;

public interface RouteValidator {
    Route validate(Route route);

    default Route routeWithViolation(Route route, String violation) {
        return Route.RouteBuilder.aRoute().from(route).addViolation(violation).build();
    }
}
