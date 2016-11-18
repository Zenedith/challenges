package com.goeuro.importer.core.configuration;

public class ImporterFileProperties {

    private String filename;

    private Boolean watcher;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getWatcher() {
        return watcher;
    }

    public void setWatcher(Boolean watcher) {
        this.watcher = watcher;
    }
}
