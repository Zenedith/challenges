package com.goeuro.service.rest

import com.goeuro.api.model.ErrorInfoResponse
import com.goeuro.api.model.RouteType
import com.goeuro.api.model.RoutesCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

public class RoutesControllerIntegrationSpec extends AbstractControllerIntegrationSpec {

    @Autowired
    private TestRestTemplate restTemplate;

    @Unroll
    def 'should get routes for two stations using filter type: #filter'() {
        given:
        def departureStationId = '150'
        def arrivalStationId = '148'

        when:
        def ResponseEntity<RoutesCollection> routesCollection = restTemplate.getForEntity(
                '/api/routes?departureStation.id={dep_sid}&arrivalStation.id={arr_sid}',
                RoutesCollection,
                departureStationId,
                arrivalStationId
        )

        then:
        routesCollection.statusCode.value() == 200
        routesCollection.body != null
        with(routesCollection.body) { RoutesCollection it ->
            it.departureStation.id == departureStationId
            it.arrivalStation.id == arrivalStationId
            !it.routes.isEmpty()
            it.routes[0].id == 'should be route id'
            it.routes[0].type == RouteType.DIRECT
        }

        where:
        filter << [null, 'DIRECT']
    }

    def 'should throw InvalidStationIdException on invalid station id'() {
        when:
        def ResponseEntity<ErrorInfoResponse> errorReponse = restTemplate.getForEntity(
                '/api/routes?departureStation.id={dep_sid}&arrivalStation.id={arr_sid}',
                ErrorInfoResponse,
                'a',
                '2'
        )

        then:
        errorReponse.statusCode.value() == 400
        errorReponse.body != null
        errorReponse.body.code == 'InvalidStationIdException'
        errorReponse.body.message == 'Invalid station id: a'
    }

}