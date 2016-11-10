package com.goeuro.service.rest.validator;

import com.goeuro.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StationIdValidator {
    private static final Logger logger = LoggerFactory.getLogger(StationIdValidator.class);

    private final Router router;
    private final boolean validateStationId;

    @Autowired
    public StationIdValidator(
            Router router,
            @Value("${importer.validation.stations}") boolean validateStationId
    ) {
        this.router = router;
        this.validateStationId = validateStationId;
    }

    public boolean validate(String value) {
        if (!validateStationId) {
            return true;
        }

        if (value == null) {
            return false;
        }

        try {
            Integer stationId = Integer.parseInt(value);

            if (stationId < 1) {
                return false;
            }

            return router.isValidStationId(stationId);
        } catch (NumberFormatException e) {
            logger.info(e.getMessage(), e);
        }
        return false;
    }

}