package com.goeuro.router

import com.goeuro.storage.core.Storage
import com.goeuro.storage.core.model.ConnectionType
import spock.lang.Specification
import spock.lang.Subject

class RouterSpec extends Specification {

    @Subject
    def Router router

    def storage = Mock(Storage)

    def setup() {
        router = new Router(storage)
    }

    def 'should call storage for valid station by id'() {
        when:
        def valid = router.isValidStationId(2)

        then:
        1 * storage.isValidStationId(2) >> true

        valid
    }

    def 'should call storage for direct route'() {
        when:
        def direct = router.isDirectRoute(2, 3)

        then:
        1 * storage.isDirectRoute(2, 3) >> true

        direct
    }

    def 'should call storage for connection route'() {
        when:
        def connectionType = router.isConnection(2, 3)

        then:
        1 * storage.isConnection(2, 3) >> ConnectionType.DIRECT

        connectionType == ConnectionType.DIRECT
    }
}
