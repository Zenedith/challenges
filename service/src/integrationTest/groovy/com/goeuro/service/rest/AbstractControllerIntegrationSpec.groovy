package com.goeuro.service.rest

import com.goeuro.service.BusRouteChallengeApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(
        classes = BusRouteChallengeApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ActiveProfiles("integration")
class AbstractControllerIntegrationSpec extends Specification {
}
