package com.goeuro.service

import com.goeuro.importer.core.configuration.ImporterProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
class BusRouteChallengeApplicationSpec extends Specification {

    @Autowired
    private WebApplicationContext context

    @Autowired
    private ImporterProperties ImporterProperties

    def "should boot up without errors"() {
        expect:
        context != null
    }

    def "should load configuration"() {
        expect:
        ImporterProperties != null
        ImporterProperties.file != null
        ImporterProperties.limits != null
        ImporterProperties.validation != null
    }
}

