package com.goeuro.importer.core.domain;

import java.util.Objects;

public class Counters {
    private final int routes;
    private final int stations;

    private Counters(int routes, int stations) {
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
        final Counters other = (Counters) obj;
        return Objects.equals(this.routes, other.routes)
                && Objects.equals(this.stations, other.stations);
    }

    @Override
    public String toString() {
        return "Counters{" +
                "routes=" + routes +
                ", stations=" + stations +
                '}';
    }

    public static final class CountersBuilder {
        private int routes;
        private int stations;

        private CountersBuilder() {
        }

        public static CountersBuilder aCounters() {
            return new CountersBuilder();
        }

        public CountersBuilder withRoutes(int routes) {
            this.routes = routes;
            return this;
        }

        public CountersBuilder withStations(int stations) {
            this.stations = stations;
            return this;
        }

        public Counters build() {
            return new Counters(routes, stations);
        }
    }
}
