package com.goeuro.router;

import com.goeuro.storage.core.Storage;
import com.goeuro.storage.core.model.ConnectionType;

public class Router {
    private final Storage storage;

    public Router(Storage storage) {
        this.storage = storage;
    }

    public boolean isDirectRoute(int departureStationId, int arrivalStationId) {
        return storage.isDirectRoute(departureStationId, arrivalStationId);
    }

    public ConnectionType isConnection(int departureStationId, int arrivalStationId) {
        return storage.isConnection(departureStationId, arrivalStationId);
    }

    public boolean isValidStationId(int stationId) {
        return storage.isValidStationId(stationId);
    }
}
