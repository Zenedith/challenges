package com.goeuro.storage.memory;

import com.codahale.metrics.annotation.Timed;
import com.goeuro.importer.core.domain.Route;
import com.goeuro.storage.core.model.StorageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RoutesContainer {
    private static final Logger logger = LoggerFactory.getLogger(RoutesContainer.class);

    private final Set<Integer> stations;
    private final Map<Integer, Set<Integer>> routesWithConnectedStations;

    private final AtomicInteger stationsCount = new AtomicInteger();
    private final AtomicInteger routesCount = new AtomicInteger();
    private final boolean validateStationId;

    public RoutesContainer(boolean validateStationId) {
        this.validateStationId = validateStationId;
        stations = new HashSet<Integer>();
        routesWithConnectedStations = new HashMap<Integer, Set<Integer>>();
    }

    @Timed
    public boolean importRoute(Route route) {
        convertRoute(route);

        if (validateStationId) {
            route.getStations().forEach(stationId -> stations.add(Integer.parseInt(stationId)));
        }

        stationsCount.addAndGet(route.getStations().size());
        routesCount.incrementAndGet();
        return true;
    }

    @Timed
    public StorageStatus getStatus() {
        return StorageStatus.StorageStatusBuilder.aStorageStatus()
                .withStations(stationsCount.get())
                .withRoutes(routesCount.get())
                .build();
    }

    public boolean isDirectRoute(int departureStationId, int arrivalStationId) {
        logger.info("isDirectRoute departureStationId: {}, arrivalStationId: {}", departureStationId, arrivalStationId);
        return Optional.of(routesWithConnectedStations)
                .map(map -> map.get(departureStationId))
                .map(connectedStations -> connectedStations.contains(arrivalStationId))
                .orElse(false);
    }

    public boolean isValidStationId(Integer stationId) {
        logger.info("isValidStationId stationId: {}", stationId);
        return stations.contains(stationId);
    }

    private void convertRoute(Route route) {
        int stationsCount = route.getStations().size();

        for (int i = 0; i < stationsCount; ++i) {
            String sourceStation = route.getStations().get(i);

            for (int j = i + 1; j < stationsCount; ++j) {
                String directConnection = route.getStations().get(j);
                addDirectConnection(Integer.parseInt(sourceStation), Integer.parseInt(directConnection));
            }
        }
    }

    private void addDirectConnection(Integer sourceStation, Integer directConnection) {
        Set<Integer> set = getSourceStationSet(sourceStation);
        set.add(directConnection);
    }

    private Set<Integer> getSourceStationSet(Integer sourceStation) {
        return Optional.of(routesWithConnectedStations)
                .map(map -> map.get(sourceStation))
                .orElseGet(() -> {
                    Set<Integer> set = new HashSet<Integer>();
                    routesWithConnectedStations.put(sourceStation, set);
                    return set;
                });
    }
}
