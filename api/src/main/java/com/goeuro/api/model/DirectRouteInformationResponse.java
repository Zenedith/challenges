package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = DirectRouteInformationResponse.DirectRouteInformationResponseBuilder.class)
public class DirectRouteInformationResponse {
    @JsonProperty(value = "dep_sid")
    private final int departureStationId;

    @JsonProperty(value = "arr_sid")
    private final int arrivalStationId;

    @JsonProperty(value = "direct_bus_route")
    private final boolean directBusRoute;

    private DirectRouteInformationResponse(int departureStationId, int arrivalStationId, boolean directBusRoute) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.directBusRoute = directBusRoute;
    }

    public int getDepartureStationId() {
        return departureStationId;
    }

    public int getArrivalStationId() {
        return arrivalStationId;
    }

    public boolean isDirectBusRoute() {
        return directBusRoute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureStationId, arrivalStationId, directBusRoute);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DirectRouteInformationResponse other = (DirectRouteInformationResponse) obj;
        return Objects.equals(this.departureStationId, other.departureStationId)
                && Objects.equals(this.arrivalStationId, other.arrivalStationId)
                && Objects.equals(this.directBusRoute, other.directBusRoute);
    }

    @Override
    public String toString() {
        return "DirectRouteInformationResponse{" +
                "departureStationId=" + departureStationId +
                ", arrivalStationId=" + arrivalStationId +
                ", directBusRoute=" + directBusRoute +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class DirectRouteInformationResponseBuilder {
        @JsonProperty(value = "dep_sid")
        private int departureStationId;

        @JsonProperty(value = "arr_sid")
        private int arrivalStationId;

        @JsonProperty(value = "direct_bus_route")
        private boolean directBusRoute;

        private DirectRouteInformationResponseBuilder() {
        }

        public static DirectRouteInformationResponseBuilder aDirectRouteInformation() {
            return new DirectRouteInformationResponseBuilder();
        }
        public DirectRouteInformationResponseBuilder withDepartureStationId(int departureStationId) {
            this.departureStationId = departureStationId;
            return this;
        }

        public DirectRouteInformationResponseBuilder withArrivalStationId(int arrivalStationId) {
            this.arrivalStationId = arrivalStationId;
            return this;
        }

        public DirectRouteInformationResponseBuilder withDirectBusRoute(boolean directBusRoute) {
            this.directBusRoute = directBusRoute;
            return this;
        }

        public DirectRouteInformationResponse build() {
            return new DirectRouteInformationResponse(departureStationId, arrivalStationId, directBusRoute);
        }
    }
}
