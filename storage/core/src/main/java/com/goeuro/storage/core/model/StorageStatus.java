package com.goeuro.storage.core.model;

import java.util.Objects;

public class StorageStatus {
    private final int stations;
    private final int routes;

    private StorageStatus(int stations, int routes) {
        this.stations = stations;
        this.routes = routes;
    }

    public int getStations() {
        return stations;
    }

    public int getRoutes() {
        return routes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, routes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StorageStatus other = (StorageStatus) obj;
        return Objects.equals(this.stations, other.stations)
                && Objects.equals(this.routes, other.routes);
    }

    @Override
    public String toString() {
        return "StorageStatus{" +
                "stations=" + stations +
                ", routes=" + routes +
                '}';
    }

    public static final class StorageStatusBuilder {
        private int stations;
        private int routes;

        private StorageStatusBuilder() {
        }

        public static StorageStatusBuilder aStorageStatus() {
            return new StorageStatusBuilder();
        }

        public StorageStatusBuilder withStations(int stations) {
            this.stations = stations;
            return this;
        }

        public StorageStatusBuilder withRoutes(int routes) {
            this.routes = routes;
            return this;
        }

        public StorageStatus build() {
            return new StorageStatus(stations, routes);
        }
    }
}
