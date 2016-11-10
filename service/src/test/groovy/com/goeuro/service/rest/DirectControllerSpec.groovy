package com.goeuro.service.rest

import com.goeuro.router.Router
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
public class DirectControllerSpec extends Specification {

    def router = Stub(Router)

    def underTest = new DirectController(router)

    def mockMvc = MockMvcBuilders.standaloneSetup(underTest).build()

    def 'should get direct information for two stations'() {
        given:
        router.isDirectRoute(1, 2) >> true

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get('/api/direct')
                        .param('dep_sid', '1')
                        .param('arr_sid', '2')
                        .accept(MediaType.APPLICATION_JSON)
        )

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.dep_sid').value(1))
                .andExpect(jsonPath('$.arr_sid').value(2))
                .andExpect(jsonPath('$.direct_bus_route').value(true))
    }
}
