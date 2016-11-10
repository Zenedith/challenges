package com.goeuro.importer.file.extractor;

import com.goeuro.importer.core.exception.ImportException;
import com.goeuro.importer.core.exception.InvalidCountInHeaderValueException;
import com.goeuro.importer.core.exception.UnableToImportRoutesCountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoutesCountExtractor {
    private static final Logger logger = LoggerFactory.getLogger(RoutesCountExtractor.class);

    public int extractRoutesCountFromHeader(String header, Integer limitRoutes) throws ImportException {
        try {
            int count = Integer.parseInt(header);

            if (count < 1) {
                String msg = String.format("Invalid count header value: %d - should be greater than 0", count);
                logger.warn(msg);
                throw new InvalidCountInHeaderValueException(msg);
            }

            if (count > limitRoutes) {
                String msg = String.format("Invalid count header value: %d - should be less than %d", count, limitRoutes + 1);
                logger.warn(msg);
                throw new InvalidCountInHeaderValueException(msg);
            }

            return count;
        } catch (NumberFormatException e) {
            String msg = String.format("Unable to parse routes count from header value: \"%s\"", header);
            logger.warn(msg);
            throw new UnableToImportRoutesCountException(msg, e);
        }
    }
}
