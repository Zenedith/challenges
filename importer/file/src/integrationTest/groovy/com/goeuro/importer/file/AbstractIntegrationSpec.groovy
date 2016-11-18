package com.goeuro.importer.file

import spock.lang.Specification

class AbstractIntegrationSpec extends Specification {

    def FileReaderHelper fileReaderHelper = new FileReaderHelper()

    def Reader createReaderFrom(def filename) {
        fileReaderHelper.createReader('src/integrationTest/resources/' + filename)
    }
}
