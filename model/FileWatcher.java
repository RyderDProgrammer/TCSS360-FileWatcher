import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

public class FileWatcher implements Runnable {
    /** The directory to watch */
    private final Path myDirectory;
    /** Flag to control the running state of the watcher */
    private volatile boolean myRunning = true;
    /**
     * Constructor for FileWatcher.
     * @param theDirectoryPath
     */
    public FileWatcher(String theDirectoryPath) {
        this.myDirectory = Paths.get(theDirectoryPath);
    }

    /**
     * Stops the file watcher.
     */
    public void stopWatching() {
        this.myRunning = false;
        System.out.println("Stopping file watcher.");
    }

    /**
     * Starts the file watcher.
     */
    @Override
    public void run() {
        // Check if the directory exists
        if (!Files.exists(myDirectory)) {
            System.out.println(" ERROR: Directory does not exist: " + myDirectory);
            return;
        }

        System.out.println(" Watching directory: " + myDirectory);

        // Register the directory with the watch service
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            myDirectory.register(watchService, 
                StandardWatchEventKinds.ENTRY_CREATE, 
                StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);
        
            while (myRunning) {
                WatchKey key = watchService.take(); // Blocks until event occurs

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path filePath = myDirectory.resolve((Path) event.context());

                    System.out.println(" Detected Event: " + filePath + " -> " + kind.name());

                    // Process event
                    EventType eventType = getEventType(kind);
                    FileEvent fileEvent = new FileEvent(
                        filePath.getFileName().toString(),
                        filePath.toString(),
                        eventType.toString(),
                        getFileExtension(filePath.toString()),
                        LocalDateTime.now().toString()
                    );

                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines the event type based on the kind of event.
     * @param kind The kind of event.
     * @return The corresponding EventType.
     */
    private EventType getEventType(WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) return EventType.FILECREATED;
        if (kind == StandardWatchEventKinds.ENTRY_DELETE) return EventType.FILEDELETED;
        return EventType.FILEMODIFIED;
    }

    /**
     * Gets the file extension from the file name.
     * @param fileName The name of the file.
     * @return The file extension.
     */
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
    }
}
