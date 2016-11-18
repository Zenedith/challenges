package com.goeuro.importer.core.configuration;

public class ImporterLimitsProperties {

    private Integer routes;
    private Integer stations;
    private Integer stationsOnRoute;

    public Integer getRoutes() {
        return routes;
    }

    public void setRoutes(Integer routes) {
        this.routes = routes;
    }

    public Integer getStations() {
        return stations;
    }

    public void setStations(Integer stations) {
        this.stations = stations;
    }

    public Integer getStationsOnRoute() {
        return stationsOnRoute;
    }

    public void setStationsOnRoute(Integer stationsOnRoute) {
        this.stationsOnRoute = stationsOnRoute;
    }
}
