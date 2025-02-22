import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is responsible for inserting file events into the database.
 */
public class FileEventDAO {
    public static void insertFileEvent(FileEvent theEvent) {

        // get connection. If connection is null, return
        Connection conn = DatabaseConnection.getMyConnection();
        if (conn == null) {
            System.out.println(" Database is not connected!");
            return;
        }
        // SQL statement to insert a file event
        String sql = "INSERT INTO file_events (file_name, file_path, event_type, file_extension, event_time) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, theEvent.getFileName());
            pstmt.setString(2, theEvent.getFilePath());
            pstmt.setString(3, theEvent.getEventType());
            pstmt.setString(4, theEvent.getExtension());
            pstmt.setString(5, theEvent.getEventTime().toString());

            // execute the insert statement
            int rowsInserted = pstmt.executeUpdate();

            // check if the insert was successful
            if (rowsInserted > 0) {
                System.out.println(
                        " File event inserted: " + theEvent.getFileName() + " (" + theEvent.getEventType() + ")");
            } else {
                System.out.println("File event NOT inserted.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Error inserting file event.");
        }
    }


    public static void insertFileEvents(List<FileEvent> events) {
    if (events.isEmpty()) {
        return; // No events to insert
    }

    Connection conn = DatabaseConnection.getMyConnection();
    if (conn == null) {
        System.out.println("Database is not connected! Events cannot be stored.");
        return;
    }

    String sql = "INSERT INTO file_events (file_name, file_path, event_type, file_extension, event_time) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        for (FileEvent event : events) {
            pstmt.setString(1, event.getFileName());
            pstmt.setString(2, event.getFilePath());
            pstmt.setString(3, event.getEventType());
            pstmt.setString(4, event.getExtension());
            pstmt.setString(5, event.getEventTime());
            pstmt.addBatch(); // Add to batch for efficient execution
        }
        
        int[] rowsInserted = pstmt.executeBatch(); // Execute batch insert
        System.out.println(rowsInserted.length + " file events inserted into the database.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error inserting file events.");
    }
}



    
}
