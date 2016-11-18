package com.goeuro.importer.core.exception;

public class ImportException extends Exception {
    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportException(String message) {
        super(message);
    }
}
