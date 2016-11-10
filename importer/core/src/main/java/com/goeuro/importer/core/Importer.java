package com.goeuro.importer.core;

import com.goeuro.importer.core.configuration.ImporterProperties;
import com.goeuro.importer.core.domain.Import;
import com.goeuro.importer.core.exception.ImportException;

import java.util.List;

public interface Importer {
    void importRoutes(ImporterProperties properties) throws ImportException;

    void onDestroy();

    List<Import> getImports();
}
