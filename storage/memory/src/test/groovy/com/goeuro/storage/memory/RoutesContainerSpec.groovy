package com.goeuro.storage.memory

import com.goeuro.importer.core.domain.Route
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RoutesContainerSpec extends Specification {

    @Subject
    def RoutesContainer routesContainer

    def route = Route.RouteBuilder.aRoute()
            .withRouteId('1')
            .withStations(['2', '3', '4'])
            .build()

    def setup() {
        routesContainer = new RoutesContainer(true)
        routesContainer.importRoute(route)
    }

    @Unroll
    def 'should return direct: #expected for departureStationId: #departureStationId and arrivalStationId : #arrivalStationId'() {
        when:
        def directRoute = routesContainer.isDirectRoute(departureStationId, arrivalStationId)

        then:
        directRoute == expected

        where:
        departureStationId | arrivalStationId | expected
        2                  | 3                | true
        2                  | 4                | true
        3                  | 4                | true
        1                  | 4                | false
        3                  | 2                | false
        4                  | 2                | false
        4                  | 3                | false
    }

    @Unroll
    def 'should return valid station for stationId: #stationId'() {
        when:
        def valid = routesContainer.isValidStationId(stationId)

        then:
        valid

        where:
        stationId << [2, 3, 4]
    }

    def 'should return false for not valid stationId'() {
        when:
        def valid = routesContainer.isValidStationId(5)

        then:
        !valid
    }

}
