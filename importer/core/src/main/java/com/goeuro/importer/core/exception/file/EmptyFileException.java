package com.goeuro.importer.core.exception.file;

import com.goeuro.importer.core.exception.ImportException;

public class EmptyFileException extends ImportException {
    public EmptyFileException() {
        super("Empty file exception");
    }
}
