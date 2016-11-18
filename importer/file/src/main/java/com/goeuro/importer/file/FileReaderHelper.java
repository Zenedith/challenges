package com.goeuro.importer.file;

import com.goeuro.importer.core.exception.ImportException;
import com.goeuro.importer.core.exception.file.EmptyFileException;
import com.goeuro.importer.core.exception.file.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReaderHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileReaderHelper.class);
    static final String SEPARATOR = " ";

    public String readHeaderLine(Reader source) throws ImportException {
        try (BufferedReader reader = new BufferedReader(source)) {
            return reader.lines()
                    .findFirst()
                    .orElseThrow(EmptyFileException::new);
        } catch (IOException e) {
            String msg = String.format("Unknown parse exception - %s", e);
            logger.warn(msg);
            throw new ImportException(msg);
        }
    }

    public Reader createReader(String filename) throws FileNotFoundException {
        try {
            Path path = Paths.get(filename);
            checkRegularFile(filename, path);
            return Files.newBufferedReader(path, Charset.forName("UTF-8"));
        } catch (IOException e) {

            String msg = String.format("Unable to parse file: \"%s\"", filename);
            logger.warn(msg);
            throw new FileNotFoundException(msg, e);
        }
    }

    private void checkRegularFile(String filename, Path path) {
        boolean isRegularFile = Files.isRegularFile(path);
        if (!isRegularFile) {
            String msg = String.format("\"%s\" is not a regular file", filename);
            logger.warn(msg);
            throw new IllegalArgumentException(msg);
        }
    }

}