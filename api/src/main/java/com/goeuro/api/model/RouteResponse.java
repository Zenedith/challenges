package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = RouteResponse.RouteResponseBuilder.class)
public class RouteResponse {

    private final String id;
    private final RouteType type;

    private RouteResponse(String id, RouteType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public RouteType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RouteResponse other = (RouteResponse) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
        return "RouteResponse{" +
                "id='" + id + '\'' +
                ", type=" + type +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class RouteResponseBuilder {
        private String id;
        private RouteType type;

        private RouteResponseBuilder() {
        }

        public static RouteResponseBuilder aRouteResponse() {
            return new RouteResponseBuilder();
        }

        public RouteResponseBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public RouteResponseBuilder withType(RouteType type) {
            this.type = type;
            return this;
        }

        public RouteResponse build() {
            return new RouteResponse(id, type);
        }
    }
}
