package com.goeuro.service.rest

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
public class DirectControllerSpec extends Specification {

    def underTest = new DirectController()

    def mockMvc = MockMvcBuilders.standaloneSetup(underTest).build()

    def 'should get hello'() {
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!")));

    }
}