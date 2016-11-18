package com.goeuro.importer.file

import spock.lang.Subject

class RoutesFileReaderIntegrationSpec extends AbstractIntegrationSpec {

    @Subject
    def FileReaderHelper fileReaderHelper

    def Reader reader

    def setup() {
        fileReaderHelper = new FileReaderHelper()
    }

    def cleanup() {
        if (reader) {
            reader.close()
        }
    }

    def 'should create reader from valid path'() {
        when:
        reader = fileReaderHelper.createReader('src/integrationTest/resources/validRoutes')

        then:
        reader
    }

    def 'should throw error when file not exists'() {
        when:
        fileReaderHelper.createReader('no-exists')

        then:
        thrown(IllegalArgumentException)
    }

    def 'should read header'() {
        given:
        reader = createReaderFrom('validRoutes')

        when:
        def header = fileReaderHelper.readHeaderLine(reader)

        then:
        header == '10'
    }
}
