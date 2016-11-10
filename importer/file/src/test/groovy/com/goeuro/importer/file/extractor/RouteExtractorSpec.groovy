package com.goeuro.importer.file.extractor

import com.goeuro.importer.core.configuration.ImporterLimitsProperties
import com.goeuro.importer.core.configuration.ImporterProperties
import com.goeuro.importer.core.domain.Route
import com.goeuro.importer.core.validator.route.*
import com.google.common.collect.ImmutableSet
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RouteExtractorSpec extends Specification {

    @Subject
    def RouteExtractor routeExtractor

    def importerLimitsProperties = new ImporterLimitsProperties()

    def importerProperties = ImporterProperties.ImporterPropertiesBuilder.anImporterProperties()
            .withLimits(importerLimitsProperties)
            .build()

    def routeValidators = ImmutableSet.of(
            new MinStationsOnRouteCountValidator(),
            new MaxStationsOnRouteCountValidator(importerProperties),
            new DuplicatedStationsOnRouteValidator(),
            new RouteIdValidator(),
            new StationIdOnRouteValidator()
    )

    def setup() {
        routeExtractor = new RouteExtractor(routeValidators)
        importerLimitsProperties.setStationsOnRoute(10)
    }

    def 'should extract valid route'() {
        given:
        def routeData = ['1', '153', '150', '148', '106', '17', '20', '160', '140', '24']

        when:
        def Route route = routeExtractor.extractSingleRoute(routeData)

        then:
        notThrown()
        route.routeId == '1'
        route.stations == ['153', '150', '148', '106', '17', '20', '160', '140', '24']
        route.violations.isEmpty()
    }

    def 'should add violation for min stations on route count'() {
        given:
        def routeData = ['1', '153']

        when:
        def Route route = routeExtractor.extractSingleRoute(routeData)

        then:
        notThrown()
        route.routeId == '1'
        route.stations == ['153']
        route.violations == ['Invalid bus stations count: "1"'] as Set
    }

    def 'should add violation for max stations on route count'() {
        given:
        importerLimitsProperties.setStationsOnRoute(8)

        def routeData = ['1', '153', '150', '148', '106', '17', '20', '160', '140', '24']

        when:
        def Route route = routeExtractor.extractSingleRoute(routeData)

        then:
        notThrown()
        route.routeId == '1'
        route.stations == ['153', '150', '148', '106', '17', '20', '160', '140', '24']
        route.violations == ['Invalid bus stations count: "9"'] as Set
    }

    @Unroll
    def 'should add violation for invalid route id: #routeId'() {
        given:
        def routeData = [routeId, '2', '3', '4', '5', '6', '7']

        when:
        def Route route = routeExtractor.extractSingleRoute(routeData)

        then:
        notThrown()
        route.routeId == routeId
        route.stations == ['2', '3', '4', '5', '6', '7']
        route.violations == ['Invalid route id: "' + routeId + '"'] as Set


        where:
        routeId << ['0', '-1', 'a', ' ', '']
    }

    @Unroll
    def 'should add violation for invalid station id: #stationId'() {
        given:
        def routeData = ['1', stationId, '3', '4', '5', '6', '7', '8', '9', '10']

        when:
        def Route route = routeExtractor.extractSingleRoute(routeData)

        then:
        notThrown()
        route.routeId == '1'
        route.stations == [stationId, '3', '4', '5', '6', '7', '8', '9', '10']
        route.violations == ['Invalid station id: "' + stationId + '"'] as Set

        where:
        stationId << ['0', '-1', 'a', ' ', '']
    }

    def 'should add violation for duplicated stations in route'() {
        given:
        def routeData = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '7']

        when:
        def Route route = routeExtractor.extractSingleRoute(routeData)

        then:
        notThrown()
        route.routeId == '1'
        route.stations == ['2', '3', '4', '5', '6', '7', '8', '9', '7']
        route.violations == ['Found duplicated bus stations: "[7]"'] as Set
    }
}
