package com.goeuro.importer.file

import com.goeuro.importer.core.configuration.ImporterFileProperties
import com.goeuro.importer.core.configuration.ImporterLimitsProperties
import com.goeuro.importer.core.configuration.ImporterProperties
import com.goeuro.importer.core.exception.ImportException
import com.goeuro.importer.core.exception.file.EmptyFileException
import com.goeuro.importer.core.validator.route.*
import com.goeuro.importer.file.extractor.RouteExtractor
import com.goeuro.importer.file.extractor.RoutesCountExtractor
import com.goeuro.storage.memory.MemoryStorage
import com.google.common.collect.ImmutableSet
import spock.lang.Subject

class FileImporterIntegrationSpec extends AbstractIntegrationSpec {

    @Subject
    def FileImporter importer

    def limitRoutes = 200
    def limitStationsOnRoute = 400
    def validateStationId = true

    def fileProperties = new ImporterFileProperties()
    def importerLimitsProperties = new ImporterLimitsProperties()
    def importerProperties = ImporterProperties.ImporterPropertiesBuilder.anImporterProperties()
            .withFile(fileProperties)
            .withLimits(importerLimitsProperties)
            .build()

    def routeValidators = ImmutableSet.of(
            new MinStationsOnRouteCountValidator(),
            new MaxStationsOnRouteCountValidator(importerProperties),
            new DuplicatedStationsOnRouteValidator(),
            new RouteIdValidator(),
            new StationIdOnRouteValidator()
    )

    def routesCountExtractor = new RoutesCountExtractor()
    def routeExtractor = new RouteExtractor(routeValidators)
    def storage = new MemoryStorage()

    def setup() {
        importer = new FileImporter(fileReaderHelper, routesCountExtractor, routeExtractor, storage, limitRoutes, validateStationId)
        importerLimitsProperties.setStationsOnRoute(limitStationsOnRoute)
        importerLimitsProperties.setRoutes(limitRoutes)
    }

    def 'should parse valid routes data using extractors'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/validRoutes')

        when:
        importer.importRoutes(importerProperties)

        then:
        notThrown()
        storage.getStatus().routes == 10
        storage.isDirectRoute(153, 150)
    }

    def 'should throw error for empty file content'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/invalidRoutesWithEmptyContent')

        when:
        importer.importRoutes(importerProperties)

        then:
        EmptyFileException e = thrown()
        e.message == 'Empty file exception'
    }

    def 'should throw error on reached limitRoutes: #limitRoutes'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/validRoutes')
        importer = new FileImporter(fileReaderHelper, routesCountExtractor, routeExtractor, storage, 1, validateStationId)
        importerLimitsProperties.setRoutes(1)

        when:
        importer.importRoutes(importerProperties)

        then:
        ImportException e = thrown()
        e.message == 'Invalid count header value: 10 - should be less than 2'
    }

    def 'should return empty routes by skipping all routes using small limit for bus stations per route'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/validRoutes')
        importerLimitsProperties.setStationsOnRoute(2)

        when:
        importer.importRoutes(importerProperties)

        then:
        notThrown()
        storage.getStatus().routes == 0
    }

    def 'should return empty routes by skipping route with invalid route id'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/invalidRoutesWithInvalidRouteId')

        when:
        importer.importRoutes(importerProperties)

        then:
        notThrown()
        storage.getStatus().routes == 0
    }

    def 'should return empty routes by skipping route with invalid station id'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/invalidRoutesWithInvalidStationId')

        when:
        importer.importRoutes(importerProperties)

        then:
        notThrown()
        storage.getStatus().routes == 0
    }

    def 'should return empty routes by skipping route with just one station id'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/invalidRoutesWithOnlyOneStationInRoute')

        when:
        importer.importRoutes(importerProperties)

        then:
        notThrown()
        storage.getStatus().routes == 0
    }

    def 'should return empty routes by skipping route with duplicated station id'() {
        given:
        fileProperties.setFilename('src/integrationTest/resources/invalidRoutesWithDuplicatedStationIdInRoute')

        when:
        importer.importRoutes(importerProperties)

        then:
        notThrown()
        storage.getStatus().routes == 0
    }
}
