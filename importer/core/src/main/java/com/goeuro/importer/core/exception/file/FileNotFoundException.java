package com.goeuro.importer.core.exception.file;

import com.goeuro.importer.core.exception.ImportException;

public class FileNotFoundException extends ImportException {
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
