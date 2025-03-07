import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents a table that will display the events that have occurred
 * to files.
 */
public class FWEventTable extends JPanel {
    /** JTable to display the events that have occurred. */
    private JTable myEventTable;
    /** DefaultTableModel to hold and act as manager for JTable. */
    private DefaultTableModel myTableModel;
    /** ArrayList to hold the data for the JTable. */
    private ArrayList<FileEvent> myData;
    /** The sorter for handling file event sorting */
    private TableRowSorter<DefaultTableModel> sorter;
    /** Array of default column widths for the JTable. */
    private int[] myDefaultColumnWidths = { 100, 250, 50, 25, 100, 100 }; // Default column widths for the table

    /**
     * Constructor for the FWEventTable. This will create the table and set up the
     * panel.
     */
    public FWEventTable() {
        super(new BorderLayout()); // Ensure that the panel is using a BorderLayout.
        String[] myColumnNames = { "File Name", "File Path", "Event Type", "Extension", "Date", "Time" };

        myTableModel = new DefaultTableModel(myColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myData = new ArrayList<>();

        myEventTable = new JTable(myTableModel);
        myEventTable.getTableHeader().setReorderingAllowed(true); // Allow column reordering

        sorter = new TableRowSorter<>(myTableModel);
        
        myEventTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(myEventTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Add preferred (default) widths to columns
        for (int i = 0; i < myColumnNames.length; i++) {
            myEventTable.getColumnModel().getColumn(i).setPreferredWidth(myDefaultColumnWidths[i]);
        }
        
        myEventTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds a FileEvent to the table.
     * 
     * @param theEvent The FileEvent to add to the table.
     */
    public void addEvent(FileEvent theEvent) {
        myData.add(theEvent);

        myTableModel.addRow(new Object[] {
                theEvent.getFileName(),
                theEvent.getFilePath(),
                theEvent.getEventType(),
                theEvent.getExtension(),
                theEvent.getEventDate(),
                theEvent.getEventTime()
        });
        
        //int rowIndex = myTableModel.getRowCount() - 1;
        //myTableModel.fireTableRowsInserted(rowIndex, rowIndex);
    }

    /**
     * Returns the data in the table.
     * 
     * @return The data in the table.
     */
    public ArrayList<FileEvent> getData() {
        return myData;
    }

    /**
     * Updates the table with the current data.
     */
    public void updateTable() {
        myTableModel.setRowCount(0);
        for (FileEvent event : myData) {
            myTableModel.addRow(new Object[] { 
                event.getFileName(),
                event.getFilePath(),
                event.getEventType(),
                event.getExtension(),
                event.getEventDate(),
                event.getEventTime()
            });
        }
    }

    /**
     * Clears the table of all data and empties the myData array of FileEvents.
     */
    public void clearTable() {
        myData.clear();
        myTableModel.setRowCount(0);
    }

    public void filterTable(String theFilter){
        myTableModel.setRowCount(0);
        if(!theFilter.equals("All Extensions")){
            for (FileEvent event : myData) {
                if(event.getExtension().contains(theFilter)){
                    myTableModel.addRow(new Object[] { event.getFileName(), event.getFilePath(), event.getEventType(),
                            event.getExtension(), event.getEventDate(), event.getEventTime() });
                }
            }
        }
    }
}