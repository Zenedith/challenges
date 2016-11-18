package com.goeuro.importer.file;

import com.codahale.metrics.annotation.Timed;
import com.goeuro.importer.core.Importer;
import com.goeuro.importer.core.configuration.ImporterFileProperties;
import com.goeuro.importer.core.configuration.ImporterProperties;
import com.goeuro.importer.core.domain.Counters;
import com.goeuro.importer.core.domain.Import;
import com.goeuro.importer.core.domain.Route;
import com.goeuro.importer.core.exception.ImportException;
import com.goeuro.importer.core.exception.file.FileNotFoundException;
import com.goeuro.importer.core.exception.route.RouteImportException;
import com.goeuro.importer.file.extractor.RouteExtractor;
import com.goeuro.importer.file.extractor.RoutesCountExtractor;
import com.goeuro.importer.file.watcher.FileWatcher;
import com.goeuro.storage.core.Storage;
import com.goeuro.storage.core.model.StorageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FileImporter implements Importer {
    private static final Logger logger = LoggerFactory.getLogger(FileImporter.class);

    private final FileReaderHelper fileReaderHelper;
    private final RoutesCountExtractor routesCountExtractor;
    private final RouteExtractor routeExtractor;
    private final Storage storage;
    private final Integer limitRoutes;
    private final Boolean validateStationId;

    private FileWatcher fileWatcher;

    private final List<Import> imports = new LinkedList<>();

    public FileImporter(
            FileReaderHelper fileReaderHelper,
            RoutesCountExtractor routesCountExtractor,
            RouteExtractor routeExtractor,
            Storage storage,
            Integer limitRoutes,
            Boolean validateStationId
    ) {
        this.fileReaderHelper = fileReaderHelper;
        this.routesCountExtractor = routesCountExtractor;
        this.routeExtractor = routeExtractor;
        this.storage = storage;
        this.limitRoutes = limitRoutes;
        this.validateStationId = validateStationId;
    }

    @Override
    @Timed
    @Async("importThreadPoolTaskExecutor")
    public void importRoutes(ImporterProperties importerProperties) throws ImportException {
        logger.info("start import routes async");

        String filename = Optional.of(importerProperties)
                .map(ImporterProperties::getFile)
                .map(ImporterFileProperties::getFilename)
                .orElseThrow(() -> new FileNotFoundException("Unable to get filename from configuration"));

        logger.info("importRoutes from filename: {}", filename);

        importFromFile(filename);
        setupWatcherIfEnabled(importerProperties, filename);
    }

    @Override
    public List<Import> getImports() {
        return imports;
    }

    @Override
    public void onDestroy() {
        if (fileWatcher != null) {
            fileWatcher.stopWatching();
        }
    }

    private void setupWatcherIfEnabled(ImporterProperties importerProperties, String filename) {
        Optional.of(importerProperties)
                .map(ImporterProperties::getFile)
                .map(ImporterFileProperties::getWatcher)
                .ifPresent(enable -> {
                    if (enable) {
                        startWatching(filename);
                    }
                });
    }

    private void startWatching(String filename) {
        fileWatcher = new FileWatcher(filename) {
            @Override
            protected void onModified() {
                try {
                    importFromFile(filename);
                } catch (ImportException e) {
                    logger.error("failed to import routes from file", e);
                }
            }
        };
        try {
            fileWatcher.watchFile();
        } catch (Exception e) {
            logger.error("Unable to start watching file", e);
        }
    }

    @Timed
    private void importFromFile(String filename) throws ImportException {
        logger.info("importFromFile: {}", filename);

        Instant startTime = Instant.now();
        int count = getRoutesCountFromHeader(filename);

        storage.prepare(validateStationId);
        Reader source = fileReaderHelper.createReader(filename);

        try (BufferedReader reader = new BufferedReader(source)) {
            reader.lines()
                    .skip(1)
                    .limit(count)
                    .map(line -> Arrays.asList(line.split(FileReaderHelper.SEPARATOR)))
                    .map(list -> {
                        try {
                            return routeExtractor.extractSingleRoute(list);
                        } catch (RouteImportException e) {
                            logger.info("Skip route \"{}\" because of error: {}", list, e.getMessage());
                            return null;
                        }
                    })
                    .filter(route -> route != null)
                    .filter(route -> route.getViolations().isEmpty())
                    .map(this::logProcessingRoute)
                    .forEach(storage::importRoute);
        } catch (IOException e) {
            String msg = String.format("Unknown import exception - %s", e);
            logger.warn(msg);
            throw new ImportException(msg);
        }

        StorageStatus storageStatus = storage.getStatus();
        addImportStatusOnTop(filename, storageStatus, startTime);
    }

    private void addImportStatusOnTop(String filename, StorageStatus storageStatus, Instant startTime) {
        logger.info("addImportStatusOnTop: filename: {}, storageStatus: {}, startTime: {}", filename, storageStatus, startTime);
        Counters counters = Counters.CountersBuilder.aCounters()
                .withRoutes(storageStatus.getRoutes())
                .withStations(storageStatus.getStations())
                .build();

        Import importStatus = Import.ImportBuilder.anImport()
                .withCounters(counters)
                .withStartedAt(startTime)
                .withFinishedAt(Instant.now())
                .withFilename(filename)
                .build();
        imports.add(0, importStatus);
        logger.info("addImportStatus: {}", importStatus);
    }

    private Route logProcessingRoute(Route route) {
        logger.info("processing route with id: {}", route.getRouteId());
        return route;
    }

    @Timed
    private int getRoutesCountFromHeader(String filename) throws ImportException {
        Reader reader = fileReaderHelper.createReader(filename);
        logger.info("getRoutesCountFromHeader");
        String header = fileReaderHelper.readHeaderLine(reader);
        return routesCountExtractor.extractRoutesCountFromHeader(header, limitRoutes);
    }
}
