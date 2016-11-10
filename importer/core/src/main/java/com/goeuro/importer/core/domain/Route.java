package com.goeuro.importer.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Route {
    private final String routeId;
    private final List<String> stations;
    private final Set<String> violations;

    private Route(String routeId, List<String> stations, Set<String> violations) {
        this.routeId = routeId;
        this.stations = stations;
        this.violations = violations;
    }

    public String getRouteId() {
        return routeId;
    }

    public List<String> getStations() {
        return stations;
    }

    public Set<String> getViolations() {
        return violations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, stations, violations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Route other = (Route) obj;
        return Objects.equals(this.routeId, other.routeId)
                && Objects.equals(this.stations, other.stations)
                && Objects.equals(this.violations, other.violations);
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", stations=" + stations +
                ", violations=" + violations +
                '}';
    }

    public static final class RouteBuilder {
        private String routeId;
        private List<String> stations = Collections.emptyList();
        private Set<String> violations = new HashSet<>();

        private RouteBuilder() {
        }

        public static RouteBuilder aRoute() {
            return new RouteBuilder();
        }

        public RouteBuilder withRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public RouteBuilder withStations(List<String> stations) {
            this.stations = stations;
            return this;
        }

        public RouteBuilder withViolations(Set<String> violations) {
            this.violations = violations;
            return this;
        }

        public RouteBuilder addViolation(String violation) {
            this.violations.add(violation);
            return this;
        }

        public RouteBuilder from(Route route) {
            return aRoute().
                    withRouteId(route.getRouteId())
                    .withStations(route.getStations())
                    .withViolations(route.getViolations());
        }

        public Route build() {
            return new Route(routeId, stations, violations);
        }
    }
}
