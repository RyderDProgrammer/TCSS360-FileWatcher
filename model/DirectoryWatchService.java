import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class DirectoryWatchService {

    /** The directory to watch */
    private Path myDirectory;
    /** The watch service to monitor the directory */
    private WatchService myWatchService;
    /** Flag to check if watcher should be running. */
    private boolean myRunning = false;
    /** The key to the watch service */
    private WatchKey myKey;
    /** The event table to update with file events */
    private FWEventTable myEventTable;
    /** The GUI watch service will work on; used to check the "myIsMonitoring" variable via a getter */
    private FWGUI myGUI;
    /** The thread that will run the watch service */
    private Thread myWatchThread;

    /**
     * Constructor for DirectoryWatchService.
     * @param directory The directory to watch
     * @param theGUI The GUI instance it will be connected to
     * @throws IOException Thrown when chosen directory is invalid
     */
    public DirectoryWatchService(String directory, FWGUI theGUI) throws IOException {
        myDirectory = Path.of(directory);
        if (!Files.isDirectory(myDirectory) || myDirectory.toString() == "") { //Guard against invalid directory
            throw new IOException();
        } 

        //Initialize remaining DirectoryWatchService variables if valid
        myWatchService = FileSystems.getDefault().newWatchService();
        myGUI = theGUI;
        myEventTable = theGUI.getEventTable();
    }

    /**
     * Starts the watch service. This method will run in a separate thread to avoid blocking the GUI.
     * It will watch for file creation, deletion, and modification events in the specified directory.
     * 
     * @throws IOException Thrown when chosen directory is invalid
     * @throws ClosedWatchServiceException Thrown when the watch service is closed
     * @throws InterruptedException Thrown when the thread is interrupted
     */
    public void start() throws IOException {

        //Exit if watch service is already running
        if (myRunning) {
            return;
        }
        myRunning = true;

        //Run the file watching service in a separate thread to avoid blocking the GUI, this is an extra precaution
        // in addition to using the take method of myWatchService to block until an event is available.
        myWatchThread = new Thread(() -> {
            try {
                myKey = myDirectory.register(myWatchService, StandardWatchEventKinds.ENTRY_CREATE,
                                                             StandardWatchEventKinds.ENTRY_DELETE, 
                                                             StandardWatchEventKinds.ENTRY_MODIFY);
            } catch (IOException e) {
                myRunning = false;            
                return;
            }

            //Loop to watch for events until stop() is called
            while (myRunning && myGUI.isMonitoring()) {
                try {
                    WatchKey key = myWatchService.take(); // Forces the thread to wait until new events occur
                    for (WatchEvent<?> event : key.pollEvents()) {
                        myEventTable.addEvent(new FileEvent(event.context().toString(), myDirectory.toString() + "\\" + event.context().toString(), eventTypeFormatter(event.kind()), getExtension(event).toString(), createDateString()));
                    }
                    key.reset(); // Reset the key to receive further events /* throws when program stopped while take() method is waiting for new event */
                } catch (InterruptedException e) { stop(); } 
                catch (ClosedWatchServiceException e) { stop();}
            }
        });
        myWatchThread.start();

    }

    /**
     * Stops the watch service and closes it.
     */
    public void stop() {
        myRunning = false;
        

        //THROWS A CLOSEDWATCHSERVICEEXCEPTION ^^^^^^

        try { //Close the watch service and key if they are not null
            if (myKey != null) { myKey.cancel(); }
            if (myWatchService != null) { myWatchService.close(); }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "I/O Error: Could not close watch service!" , "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (myWatchThread != null) {
            myWatchThread.interrupt(); // Stop the thread watching for file events
        }
        myWatchThread = null; 
    }

    public void invalidDirectoryError() {
        JOptionPane.showMessageDialog(null,  "\"" + myDirectory.toString() + "\" is not a valid directory" , "Invalid Directory Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Gets the file extension from the event; includes the dot.
     * @param theEvent
     * @return
     */
    private Path getExtension(WatchEvent<?> theEvent) {
        String fileName = theEvent.context().toString();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return null; // No extension found or folder
        }
        return Path.of(fileName.substring(lastIndexOfDot));
    }

    /**
     * Creates a date string in the format of yyyy-MM-dd HH:mm:ss
     * @return  String representation of the current date and time
     */
    private String createDateString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * Formats the event type to a more readable string.
     * @param theEvent One of the three StandardWatchEventKinds enum
     * @return  Formatted event type string
     */
    private String eventTypeFormatter(WatchEvent.Kind<?> theEvent) {
        String eventType = theEvent.toString();
        if (eventType.equals("ENTRY_CREATE")) {
            return "CREATED";
        } else if (eventType.equals("ENTRY_DELETE")) {
            return "DELETED";
        } else if (eventType.equals("ENTRY_MODIFY")) {
            return "MODIFIED";
        }
        return null;
    }

}