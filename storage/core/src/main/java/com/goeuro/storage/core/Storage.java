package com.goeuro.storage.core;

import com.goeuro.importer.core.domain.Route;
import com.goeuro.storage.core.model.ConnectionType;
import com.goeuro.storage.core.model.StorageStatus;

public interface Storage {
    ConnectionType isConnection(int departureStationId, int arrivalStationId);

    boolean isDirectRoute(int departureStationId, int arrivalStationId);

    boolean isValidStationId(int stationId);

    StorageStatus getStatus();

    boolean importRoute(Route route);

    void prepare(Boolean validateStationId);
}
