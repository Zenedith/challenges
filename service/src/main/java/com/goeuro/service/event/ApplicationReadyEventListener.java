package com.goeuro.service.event;

import com.goeuro.importer.core.Importer;
import com.goeuro.importer.core.configuration.ImporterProperties;
import com.goeuro.importer.core.exception.ImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    @Autowired
    private ImporterProperties importerProperties;

    @Autowired
    private Importer importer;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        logger.info("on application event: ApplicationReadyEvent");

        try {
            importer.importRoutes(importerProperties);
        } catch (ImportException e) {
            logger.error("failed to import routes", e);
        }
    }

    @PreDestroy
    public void onDestroy() {
        logger.error("On destroy");
        importer.onDestroy();
    }
}
