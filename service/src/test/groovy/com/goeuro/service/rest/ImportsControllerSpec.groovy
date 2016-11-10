package com.goeuro.service.rest

import com.goeuro.importer.core.Importer
import com.goeuro.importer.core.domain.Counters
import com.goeuro.importer.core.domain.Import
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.Instant

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
public class ImportsControllerSpec extends Specification {

    def importer = Stub(Importer)

    def underTest = new ImportsController(importer)

    def mockMvc = MockMvcBuilders.standaloneSetup(underTest).build()

    def 'should get imports'() {
        given:
        Counters counters = Counters.CountersBuilder.aCounters()
                .withRoutes(1)
                .withStations(2)
                .build()

        Import importStatus = Import.ImportBuilder.anImport()
                .withCounters(counters)
                .withStartedAt(Instant.now())
                .withFinishedAt(Instant.now().plusSeconds(1))
                .withFilename('filename')
                .build()

        importer.getImports() >> [importStatus]

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get('/api/imports').accept(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.imports').isArray())
                .andExpect(jsonPath('$.imports[0].finishedAt').isString())
                .andExpect(jsonPath('$.imports[0].startedAt').isString())
                .andExpect(jsonPath('$.imports[0].filename').value('filename'))
                .andExpect(jsonPath('$.imports[0].counters.routes').value(1))
                .andExpect(jsonPath('$.imports[0].counters.stations').value(2))
    }
}
