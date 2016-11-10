package com.goeuro.service.rest

import com.goeuro.api.model.DirectRouteInformationResponse
import com.goeuro.api.model.ErrorInfoResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity

public class DirectControllerIntegrationSpec extends AbstractControllerIntegrationSpec {

    @Autowired
    private TestRestTemplate restTemplate;

    def 'should get direct information for two stations'() {
        given:
        def departureStationId = 150
        def arrivalStationId = 148

        when:
        def ResponseEntity<DirectRouteInformationResponse> directRouteInformationEntity = restTemplate.getForEntity(
                '/api/direct?dep_sid={dep_sid}&arr_sid={arr_sid}',
                DirectRouteInformationResponse,
                departureStationId,
                arrivalStationId
        )

        then:
        directRouteInformationEntity.statusCode.value() == 200
        directRouteInformationEntity.body != null
        with(directRouteInformationEntity.body) { DirectRouteInformationResponse it ->
            it.departureStationId == departureStationId
            it.arrivalStationId == arrivalStationId
            it.directBusRoute
        }
    }

    def 'should get error on invalid station id'() {
        when:
        def ResponseEntity<ErrorInfoResponse> errorResponse = restTemplate.getForEntity(
                '/api/direct?dep_sid={dep_sid}&arr_sid={arr_sid}',
                ErrorInfoResponse,
                'a',
                '2'
        )

        then:
        errorResponse.statusCode.value() == 400
        errorResponse.body != null
        errorResponse.body.code == 'InvalidStationIdException'
        errorResponse.body.message == 'Unable to parse station id'
    }

}