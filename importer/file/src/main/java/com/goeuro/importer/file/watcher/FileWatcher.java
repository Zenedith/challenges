//http://stackoverflow.com/a/40003648

package com.goeuro.importer.file.watcher;

import com.google.common.base.Preconditions;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public abstract class FileWatcher {
    private Path folderPath;
    private String watchFile;
    private boolean shouldStop = false;

    public FileWatcher(String watchFile) {
        Preconditions.checkNotNull(watchFile);

        Path filePath = Paths.get(watchFile);

        boolean isRegularFile = Files.isRegularFile(filePath);

        if (!isRegularFile) {
            // Do not allow this to be a folder since we want to watch files
            throw new IllegalArgumentException(watchFile + " is not a regular file");
        }

        // This is always a folder
        folderPath = Optional.of(filePath)
                .map(Path::getParent)
                .orElseThrow(() -> new IllegalArgumentException("Unable to get parent director"));

        this.watchFile = Optional.of(watchFile)
                .map(str -> str.replace(folderPath.toString() + File.separator, ""))
                .orElseThrow(() -> new IllegalArgumentException("Unable to remove parent directory from filename"));
    }

    public void watchFile() throws Exception {
        shouldStop = false;
        // We obtain the file system of the Path
        FileSystem fileSystem = folderPath.getFileSystem();

        // We create the new WatchService using the try-with-resources block
        try (WatchService service = fileSystem.newWatchService()) {
            // We watch for modification events
            folderPath.register(service, ENTRY_MODIFY);

            // Start the polling loop
            while (!shouldStop) {
                // Wait for the next event
                WatchKey watchKey = service.take();

                watchKey.pollEvents().stream()
                        .filter(watchEvent -> watchEvent.kind() == ENTRY_MODIFY)
                        .map(WatchEvent::context)
                        .map(context -> (Path) context)
                        .filter(watchEventPath -> watchEventPath.toString().equals(watchFile))
                        .findFirst()
                        .ifPresent(watchEventPath -> onModified());

                if (!watchKey.reset()) {
                    // Exit if no longer valid
                    break;
                }

                if (shouldStop) {
                    break;
                }
            }
        }
    }

    public void stopWatching() {
        shouldStop = true;
    }

    protected abstract void onModified();

}
