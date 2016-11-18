package com.goeuro.service.rest;

import com.codahale.metrics.annotation.Timed;
import com.goeuro.api.model.CountersResponse;
import com.goeuro.api.model.ImportStatusResponse;
import com.goeuro.api.model.ImportStatusesCollection;
import com.goeuro.importer.core.Importer;
import com.goeuro.importer.core.domain.Import;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ImportsController {
    private static final Logger logger = LoggerFactory.getLogger(ImportsController.class);

    private final Importer importer;

    @Autowired
    public ImportsController(Importer importer) {
        this.importer = importer;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/imports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Timed
    @ApiOperation(value = "getImportStatuses", nickname = "getImportStatuses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ImportStatusesCollection.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ImportStatusesCollection getImportStatuses() {
        logger.info("getImportStatuses");

        List<ImportStatusResponse> collection = Optional.of(importer)
                .map(Importer::getImports)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapImportStatus)
                .collect(Collectors.toList());

        return ImportStatusesCollection.ImportStatusesCollectionBuilder.anImportStatusesCollection()
                .withImports(collection)
                .build();
    }

    private ImportStatusResponse mapImportStatus(Import imp) {
        CountersResponse counters = CountersResponse.CountersResponseBuilder.aCounters()
                .withRoutes(imp.getCounters().getRoutes())
                .withStations(imp.getCounters().getStations())
                .build();

        return ImportStatusResponse.ImportStatusResponseBuilder.anImportStatusResponseBuilder()
                .withCounters(counters)
                .withFinishedAt(imp.getFinishedAt().toString())
                .withStartedAt(imp.getStartedAt().toString())
                .withFilename(imp.getFilename())
                .build();
    }
}
