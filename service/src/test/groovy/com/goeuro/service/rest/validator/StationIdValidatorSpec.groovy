package com.goeuro.service.rest.validator

import com.goeuro.router.Router
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class StationIdValidatorSpec extends Specification {

    @Subject
    def StationIdValidator validator

    def router = Stub(Router)
    def validateStationId = true

    def setup() {
        validator = new StationIdValidator(router, validateStationId)
    }

    @Unroll
    def 'should return false for invalid station id: #id'() {
        when:
        def result = validator.validate(id)

        then:
        !result

        where:
        id << [null, '', '-1', '0', 'a', '!']
    }
}
