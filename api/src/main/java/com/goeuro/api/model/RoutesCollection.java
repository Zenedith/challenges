package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = RoutesCollection.RoutesCollectionBuilder.class)
public class RoutesCollection {

    private final StationRef departureStation;
    private final StationRef arrivalStation;
    private final List<RouteResponse> routes;

    private RoutesCollection(StationRef departureStation, StationRef arrivalStation, List<RouteResponse> routes) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.routes = routes;
    }

    public StationRef getDepartureStation() {
        return departureStation;
    }

    public StationRef getArrivalStation() {
        return arrivalStation;
    }

    public List<RouteResponse> getRoutes() {
        return routes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureStation, arrivalStation, routes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RoutesCollection other = (RoutesCollection) obj;
        return Objects.equals(this.departureStation, other.departureStation)
                && Objects.equals(this.arrivalStation, other.arrivalStation)
                && Objects.equals(this.routes, other.routes);
    }

    @Override
    public String toString() {
        return "RoutesCollection{" +
                "departureStation=" + departureStation +
                ", arrivalStation=" + arrivalStation +
                ", routes=" + routes +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class RoutesCollectionBuilder {
        private StationRef departureStation;
        private StationRef arrivalStation;
        private List<RouteResponse> routes = Collections.emptyList();

        private RoutesCollectionBuilder() {
        }

        public static RoutesCollectionBuilder aRoutesCollection() {
            return new RoutesCollectionBuilder();
        }

        public RoutesCollectionBuilder withDepartureStation(StationRef departureStation) {
            this.departureStation = departureStation;
            return this;
        }

        public RoutesCollectionBuilder withArrivalStation(StationRef arrivalStation) {
            this.arrivalStation = arrivalStation;
            return this;
        }

        public RoutesCollectionBuilder withRoutes(List<RouteResponse> routes) {
            this.routes = routes;
            return this;
        }

        public RoutesCollection build() {
            return new RoutesCollection(departureStation, arrivalStation, routes);
        }
    }
}
