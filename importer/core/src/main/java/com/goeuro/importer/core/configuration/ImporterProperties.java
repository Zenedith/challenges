package com.goeuro.importer.core.configuration;

import java.util.Objects;

public class ImporterProperties {

    private final ImporterLimitsProperties limits;

    private final ImporterFileProperties file;

    private final ImporterValidationProperties validation;

    private ImporterProperties(ImporterLimitsProperties limits, ImporterFileProperties file, ImporterValidationProperties validation) {
        this.limits = limits;
        this.file = file;
        this.validation = validation;
    }

    public ImporterLimitsProperties getLimits() {
        return limits;
    }

    public ImporterFileProperties getFile() {
        return file;
    }

    public ImporterValidationProperties getValidation() {
        return validation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(limits, file, validation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ImporterProperties other = (ImporterProperties) obj;
        return Objects.equals(this.limits, other.limits)
                && Objects.equals(this.file, other.file)
                && Objects.equals(this.validation, other.validation);
    }

    @Override
    public String toString() {
        return "ImporterProperties{" +
                "limits=" + limits +
                ", file=" + file +
                ", validation=" + validation +
                '}';
    }

    public static final class ImporterPropertiesBuilder {
        private ImporterLimitsProperties limits;
        private ImporterFileProperties file;
        private ImporterValidationProperties validation;

        private ImporterPropertiesBuilder() {
        }

        public static ImporterPropertiesBuilder anImporterProperties() {
            return new ImporterPropertiesBuilder();
        }

        public ImporterPropertiesBuilder withLimits(ImporterLimitsProperties limits) {
            this.limits = limits;
            return this;
        }

        public ImporterPropertiesBuilder withFile(ImporterFileProperties file) {
            this.file = file;
            return this;
        }

        public ImporterPropertiesBuilder withValidation(ImporterValidationProperties validation) {
            this.validation = validation;
            return this;
        }

        public ImporterProperties build() {
            return new ImporterProperties(limits, file, validation);
        }
    }
}
