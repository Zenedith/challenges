package com.goeuro.importer.core.domain;

import java.time.Instant;
import java.util.Objects;

public class Import {
    private final Instant startedAt;
    private final Instant finishedAt;
    private final String filename;
    private final Counters counters;

    private Import(Instant startedAt, Instant finishedAt, String filename, Counters counters) {
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.filename = filename;
        this.counters = counters;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public String getFilename() {
        return filename;
    }

    public Counters getCounters() {
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
        final Import other = (Import) obj;
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

    public static final class ImportBuilder {
        private Instant startedAt;
        private Instant finishedAt;
        private String filename;
        private Counters counters;

        private ImportBuilder() {
        }

        public static ImportBuilder anImport() {
            return new ImportBuilder();
        }

        public ImportBuilder withStartedAt(Instant startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public ImportBuilder withFinishedAt(Instant finishedAt) {
            this.finishedAt = finishedAt;
            return this;
        }

        public ImportBuilder withFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public ImportBuilder withCounters(Counters counters) {
            this.counters = counters;
            return this;
        }

        public Import build() {
            return new Import(startedAt, finishedAt, filename, counters);
        }
    }
}
