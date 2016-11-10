package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ImportStatusResponse.ImportStatusResponseBuilder.class)
public class ImportStatusResponse {
    private final String startedAt;
    private final String finishedAt;
    private final String filename;
    private final CountersResponse counters;

    private ImportStatusResponse(String startedAt, String finishedAt, String filename, CountersResponse counters) {
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.filename = filename;
        this.counters = counters;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public String getFilename() {
        return filename;
    }

    public CountersResponse getCounters() {
        return counters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startedAt, finishedAt, filename, counters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ImportStatusResponse other = (ImportStatusResponse) obj;
        return Objects.equals(this.startedAt, other.startedAt)
                && Objects.equals(this.finishedAt, other.finishedAt)
                && Objects.equals(this.filename, other.filename)
                && Objects.equals(this.counters, other.counters);
    }

    @Override
    public String toString() {
        return "Import{" +
                "startedAt='" + startedAt + '\'' +
                ", finishedAt='" + finishedAt + '\'' +
                ", filename='" + filename + '\'' +
                ", counters=" + counters +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class ImportStatusResponseBuilder {
        private String startedAt;
        private String finishedAt;
        private String filename;
        private CountersResponse counters;

        private ImportStatusResponseBuilder() {
        }

        public static ImportStatusResponseBuilder anImportStatusResponseBuilder() {
            return new ImportStatusResponseBuilder();
        }

        public ImportStatusResponseBuilder withStartedAt(String startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public ImportStatusResponseBuilder withFinishedAt(String finishedAt) {
            this.finishedAt = finishedAt;
            return this;
        }

        public ImportStatusResponseBuilder withFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public ImportStatusResponseBuilder withCounters(CountersResponse counters) {
            this.counters = counters;
            return this;
        }

        public ImportStatusResponse build() {
            return new ImportStatusResponse(startedAt, finishedAt, filename, counters);
        }
    }
}
