package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ImportStatusesCollection.ImportStatusesCollectionBuilder.class)
public class ImportStatusesCollection {
    private final List<ImportStatusResponse> imports;

    private ImportStatusesCollection(List<ImportStatusResponse> imports) {
        this.imports = imports;
    }

    public List<ImportStatusResponse> getImports() {
        return imports;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imports);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ImportStatusesCollection other = (ImportStatusesCollection) obj;
        return Objects.equals(this.imports, other.imports);
    }

    @Override
    public String toString() {
        return "ImportStatusesCollection{" +
                "imports=" + imports +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class ImportStatusesCollectionBuilder {
        private List<ImportStatusResponse> imports = Collections.emptyList();

        private ImportStatusesCollectionBuilder() {
        }

        public static ImportStatusesCollectionBuilder anImportStatusesCollection() {
            return new ImportStatusesCollectionBuilder();
        }

        public ImportStatusesCollectionBuilder withImports(List<ImportStatusResponse> imports) {
            this.imports = imports;
            return this;
        }

        public ImportStatusesCollection build() {
            return new ImportStatusesCollection(imports);
        }
    }
}
