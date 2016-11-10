package com.goeuro.service.rest

import com.goeuro.importer.core.Importer
import com.goeuro.importer.core.domain.Import
import com.goeuro.router.Router
import com.goeuro.service.exception.InvalidStationIdException
import com.goeuro.service.exception.RoutesUnavailableException
import com.goeuro.service.rest.validator.StationIdValidator
import com.google.common.collect.ImmutableList
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
public class RoutesControllerSpec extends Specification {

    def router = Stub(Router)
    def importer = Stub(Importer)
    def stationIdValidator = Stub(StationIdValidator)

    def underTest = new RoutesController(router, importer, stationIdValidator)

    def mockMvc = MockMvcBuilders.standaloneSetup(underTest).build()

    def 'should get routes for two stations'() {
        given:
        importer.getImports() >> ImmutableList.of(Import.ImportBuilder.anImport().build())
        stationIdValidator.validate('1') >> true
        stationIdValidator.validate('2') >> true
        router.isDirectRoute(1, 2) >> true

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get('/api/routes')
                        .param('departureStation.id', '1')
                        .param('arrivalStation.id', '2')
                        .accept(MediaType.APPLICATION_JSON)
        )

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.departureStation.id').value(1))
                .andExpect(jsonPath('$.arrivalStation.id').value(2))
                .andExpect(jsonPath('$.routes').isArray())
                .andExpect(jsonPath('$.routes[0].id').value('should be route id'))
                .andExpect(jsonPath('$.routes[0].type').value('DIRECT'))
    }

    def 'should throw InvalidStationIdException for invalid station id'() {
        given:
        importer.getImports() >> ImmutableList.of(Import.ImportBuilder.anImport().build())
        stationIdValidator.validate('3') >> false
        stationIdValidator.validate('2') >> true
        router.isDirectRoute(3, 2) >> true

        when:
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get('/api/routes')
                        .param('departureStation.id', '3')
                        .param('arrivalStation.id', '2')
                        .accept(MediaType.APPLICATION_JSON)
        )

        then:
        NestedServletException e = thrown()
        e.getCause() instanceof InvalidStationIdException
    }

    def 'should throw RoutesUnavailableException for empty imports'() {
        given:
        importer.getImports() >> []

        when:
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get('/api/routes')
                        .param('departureStation.id', '3')
                        .param('arrivalStation.id', '2')
                        .accept(MediaType.APPLICATION_JSON)
        )

        then:
        NestedServletException e = thrown()
        e.getCause() instanceof RoutesUnavailableException
    }
}
