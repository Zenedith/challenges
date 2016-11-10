package com.goeuro.service.rest

import com.goeuro.service.BusRouteChallengeApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(
        classes = BusRouteChallengeApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class DirectControllerIntegrationSpec extends Specification {

    @Autowired
    private TestRestTemplate restTemplate;

    def 'should get hello'() {
        when:
        String body = this.restTemplate.getForObject("/", String.class);

        then:
        body == 'Greetings from Spring Boot!'

    }
}