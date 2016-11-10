package com.goeuro.storage.memory;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.storage.core.Storage;
import com.goeuro.storage.core.model.ConnectionType;
import com.goeuro.storage.core.model.StorageStatus;

import java.util.Optional;

public class MemoryStorage implements Storage {
    private RoutesContainer routesContainer;

    public MemoryStorage() {
        prepare(false);
    }

    @Override
    public boolean isDirectRoute(int departureStationId, int arrivalStationId) {
        return routesContainer.isDirectRoute(departureStationId, arrivalStationId);
    }

    @Override
    public ConnectionType isConnection(int departureStationId, int arrivalStationId) {
        return Optional.of(routesContainer)
                .map(routesContainer -> routesContainer.isDirectRoute(departureStationId, arrivalStationId))
                //for now only direct connections are supported
                .map(isDirect -> isDirect ? ConnectionType.DIRECT : ConnectionType.NONE)
                .orElse(ConnectionType.NONE);
    }

    @Override
    public boolean isValidStationId(int stationId) {
        return routesContainer.isValidStationId(stationId);
    }

    @Override
    public StorageStatus getStatus() {
        return routesContainer.getStatus();
    }

    @Override
    public boolean importRoute(Route route) {
        return routesContainer.importRoute(route);
    }

    @Override
    public void prepare(Boolean validateStationId) {
        routesContainer = new RoutesContainer(validateStationId);
    }
}
