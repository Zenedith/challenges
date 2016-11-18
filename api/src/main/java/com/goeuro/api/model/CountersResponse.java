package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = CountersResponse.CountersResponseBuilder.class)
public class CountersResponse {
    private final int routes;
    private final int stations;

    private CountersResponse(int routes, int stations) {
        this.routes = routes;
        this.stations = stations;
    }

    public int getRoutes() {
        return routes;
    }

    public int getStations() {
        return stations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(routes, stations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CountersResponse other = (CountersResponse) obj;
        return Objects.equals(this.routes, other.routes)
                && Objects.equals(this.stations, other.stations);
    }

    @Override
    public String toString() {
        return "CountersResponse{" +
                "routes=" + routes +
                ", stations=" + stations +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class CountersResponseBuilder {
        private int routes;
        private int stations;

        private CountersResponseBuilder() {
        }

        public static CountersResponseBuilder aCounters() {
            return new CountersResponseBuilder();
        }

        public CountersResponseBuilder withRoutes(int routes) {
            this.routes = routes;
            return this;
        }

        public CountersResponseBuilder withStations(int stations) {
            this.stations = stations;
            return this;
        }

        public CountersResponse build() {
            return new CountersResponse(routes, stations);
        }
    }
}
