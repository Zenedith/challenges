package com.goeuro.service.rest

import com.goeuro.api.model.ImportStatusesCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity

public class ImportsControllerIntegrationSpec extends AbstractControllerIntegrationSpec {

    @Autowired
    private TestRestTemplate restTemplate;

    def 'should get imports'() {
        when:
        def ResponseEntity<ImportStatusesCollection> statusResponseResponseEntity = restTemplate.getForEntity('/api/imports', ImportStatusesCollection)

        then:
        statusResponseResponseEntity.statusCode.value() == 200
        statusResponseResponseEntity.body != null
        statusResponseResponseEntity.body.imports != null
        def importRow = statusResponseResponseEntity.body.imports.get(0)
        importRow.finishedAt
        importRow.startedAt
        importRow.filename == 'src/integrationTest/resources/example'
        importRow.counters != null
        importRow.counters.routes == 10
        importRow.counters.stations == 65
    }
}
