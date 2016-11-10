package com.goeuro.importer.file

import com.goeuro.importer.file.extractor.RoutesCountExtractor
import com.goeuro.importer.file.extractor.RouteExtractor
import com.goeuro.storage.core.Storage
import spock.lang.Specification
import spock.lang.Subject

class FileImporterSpec extends Specification {

    @Subject
    def FileImporter importer

    def fileReaderHelper = Stub(FileReaderHelper)
    def reader = Stub(Reader)
    def routesCountExtractor = Mock(RoutesCountExtractor)
    def routeExtractor = Mock(RouteExtractor)
    def storage = Mock(Storage)

    def limitRoutes = 2
    def validateStationId = true

    def setup() {
        importer = new FileImporter(fileReaderHelper, routesCountExtractor, routeExtractor, storage, limitRoutes, validateStationId)
        fileReaderHelper.createReader(_) >> reader
    }

}
