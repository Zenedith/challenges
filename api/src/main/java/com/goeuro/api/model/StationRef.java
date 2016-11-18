package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = StationRef.StationRefBuilder.class)
public class StationRef {
    private final String id;

    private StationRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StationRef other = (StationRef) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "StationRef{" +
                "id='" + id + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class StationRefBuilder {
        private String id;

        private StationRefBuilder() {
        }

        public static StationRefBuilder aStationRef() {
            return new StationRefBuilder();
        }

        public StationRefBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public StationRef build() {
            return new StationRef(id);
        }
    }
}
