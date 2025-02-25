import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FWEventTable extends JPanel {
    
    private JTable myEventTable;
    
    private DefaultTableModel myTableModel;

    private ArrayList<FileEvent> myData;
    /**
     * Constructor for the event table.
     */
    public FWEventTable() {
        super(new BorderLayout());
        String[] myColumnNames = { "File Name", "File Path", "Event Type", "File Extension", "Time" };
        myTableModel = new DefaultTableModel();
        myTableModel.setColumnIdentifiers(myColumnNames);
        myData = new ArrayList<>();

        myEventTable = new JTable(myTableModel);
        JScrollPane scrollPane = new JScrollPane(myEventTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    /**
     * Adds an event to the table.
     * @param theEvent The event to add.
     */
    public void addEvent(FileEvent theEvent) {
        myData.add(theEvent);
        myTableModel.addRow(new Object[] {
                theEvent.getFileName(),
                theEvent.getFilePath(),
                theEvent.getEventType(),
                theEvent.getExtension(),
                theEvent.getEventTime()
        });
    }
}
