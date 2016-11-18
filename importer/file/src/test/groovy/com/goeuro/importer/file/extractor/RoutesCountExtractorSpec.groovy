package com.goeuro.importer.file.extractor

import com.goeuro.importer.core.exception.ImportException
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RoutesCountExtractorSpec extends Specification {

    @Subject
    def RoutesCountExtractor routesCountExtractor
    def limitRoutes = 2

    def setup() {
        routesCountExtractor = new RoutesCountExtractor()
    }

    def 'should get valid count from header'() {
        given:
        def validValue = limitRoutes

        when:
        def countFromHeader = routesCountExtractor.extractRoutesCountFromHeader(String.valueOf(validValue), limitRoutes)

        then:
        notThrown()
        countFromHeader == validValue
    }

    @Unroll
    def 'should throw error on invalid value (#value) in header'() {
        when:
        routesCountExtractor.extractRoutesCountFromHeader(value, limitRoutes)

        then:
        ImportException e = thrown()
        e.message == 'Unable to parse routes count from header value: "' + value + '"'

        where:
        value << [null, 'a', '1 2']
    }

    @Unroll
    def 'should throw error on invalid count value (#value) in header'() {
        when:
        routesCountExtractor.extractRoutesCountFromHeader(value, limitRoutes)

        then:
        ImportException e = thrown()
        e.message == 'Invalid count header value: ' + value + ' - should be greater than 0'

        where:
        value << ['-1', '0']
    }

    def 'should throw error on over limit routes count value in header'() {
        given:
        def overLimit = String.valueOf(limitRoutes + 1)

        when:
        routesCountExtractor.extractRoutesCountFromHeader(overLimit, limitRoutes)

        then:
        ImportException e = thrown()
        e.message == 'Invalid count header value: ' + overLimit + ' - should be less than ' + (limitRoutes + 1)
    }

}
