import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class is the main GUI for the FileWatcher application. It creates the
 * main frame and sets up the menu bar, buttons, and event table.
 */
public final class FWGUI implements ActionListener {
    // Main frame for the entire window.
    private final JFrame myFrame;
    // Menu bar for the primary window.
    private JMenuBar myMenuBar;
    // Timer to be updated as the user watches a directory.
    private int runningTime = 0;
    // Timer to update the time label.
    private Timer myTimer;
    // Label to display the time & the manual query information.
    private JLabel myTimeLabel, myManualQueryLabel;
    // Start button in menu.
    private JMenuItem myMenuStart;
    // Stop button in menu.
    private JMenuItem myMenuStop;
    // Event table to display file events.
    private final FWEventTable myEventTable;
    // Event table to display the file events in the query window.
    private final FWEventTable myQueryTable;
    // Event table for querying the database, used as storage
    private FWEventTable myQueryResults;
    // Extension types combobox for querying.
    private JComboBox<String> myExtensionComboBox;
    // Different dropdowns in the query window.
    private JComboBox<String> myAutomaticQueryComboBox, myManualQueryComboBox, myEventActivityDropdown;
    // Directory field in the main window for monitoring directories, extension
    // field for extensions.
    // PathOrDate and FileName are in query window for displaying information.
    private JTextField myDirectoryField, myExtensionField, myPathOrDateText, myFileNameText;
    // All the buttons to be used throughout both of the windows.
    private JButton myDirectoryStartButton, myDirectoryStopButton, myWriteDbButton, myDirectoryBrowseButton,
            myResetDirectoryButton, myQueryButton, myDatabaseResetButton, myExportCSVButton, myManualQueryButton,
            myReturnWindowButton;
    // Buttons that have an image associated with them.
    private JButton myImgStartButton, myImgStopButton, myImgDBButton, myImgClearButton;
    // The main panel of the GUI.
    private final FWPanel myMainPanel;
    // The panel of the query window popup.
    private final FWPanel myQueryPanel;
    // Boolean value for displaying if the program is monitoring.
    private boolean myIsMonitoring;
    // Directory watch service to monitor directories.
    private DirectoryWatchService myDirectoryWatchService;
    // Boolean values for if the database is active and if filtering is turned on.
    private boolean myDatabaseActive, myFilteringOn;
    // Displaying if the database is connected.
    private JLabel myDatabaseConnectionLabel;
    // Debug menu options for adding items.
    private JMenuItem add10Item, add100Item, myAdd1OfEachItem;
    // Connecting and disconnecting from the database JMenuItems.
    private JMenuItem myConnectDbItem, myDisconnectDbItem;
    // Array of checkboxes for the query window.
    private JPanel myQueryCheckBoxPanel;
    // Query window popup frame.
    private FWFrame myQueryFrame;
    // JCheckBox array for the query window.
    private JCheckBox[] myExtensionCheckBox;
    // Secondary panel inside the query window for displaying information.
    private final JPanel mySecondQueryPanel;
    // Query option for the file menu dropdown.
    private JMenuItem myFileQueryItem;
    // Instance of the GUI.
    private static FWGUI myFWGUIInstance;
    // Constant string for the custom extension.
    private static final String MY_CUSTOM_EXTENSION_STRING = "Custom extension";
    // Constant to set the split pane resize weight.
    private final double MY_SPLIT_PANE_RESIZE_WEIGHT = 0.2;


    /*
     * Constructor for the GUI. This will create the GUI and set up the menu bar.
     */
    public FWGUI() {
        myFrame = new FWFrame().frameOutline();
        myFrame.setLayout(new BorderLayout());

        // Create the main panel and query panel information.
        myMainPanel = new FWPanel();
        myQueryPanel = new FWPanel();
        mySecondQueryPanel = myMainPanel.getQueryPanel();
        myEventTable = new FWEventTable();
        myQueryTable = new FWEventTable();
        // Default monitoring is false.
        myIsMonitoring = false;
        // Methods to help break up the constructor and make it easier to read.
        setUpButtons();
        createMenuBar();
        timeAndDbLabel();
        setUpDocumentListeners();
        setUpFileViewer();

        // Set up the exit listener
        setUpExitListener();

        myFrame.add(myMainPanel, BorderLayout.NORTH);

        myFrame.setVisible(true);
    }

    /**
     * Sets up all the buttons that will be used in the program.
     */
    private final void setUpButtons() {
        myExtensionComboBox = myMainPanel.getExtensionBox();
        myExtensionComboBox.setPreferredSize(new Dimension(200, myExtensionComboBox.getPreferredSize().height));

        // Adding action listener into the extension combobox.
        myExtensionComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object theDropdowObject = myExtensionComboBox.getSelectedItem();
                if (theDropdowObject.equals(MY_CUSTOM_EXTENSION_STRING)) {
                    myExtensionComboBox.setEditable(true);
                } else {
                    myExtensionComboBox.setEditable(false);
                }
            }
        });
        myExtensionComboBox.addActionListener(this);
        // Cleaned up duplicated and repeated lines.
        myDirectoryStartButton = addButtonActionListener(myMainPanel.getStartButton());
        myDirectoryStopButton = addButtonActionListener(myMainPanel.getStopButton());
        myDirectoryBrowseButton = addButtonActionListener(myMainPanel.getBrowseButton());
        myResetDirectoryButton = addButtonActionListener(myMainPanel.getResetButton());
        myQueryButton = addButtonActionListener(myMainPanel.getQueryButton());
        myWriteDbButton = addButtonActionListener(myMainPanel.getMyWriteDBButton());
        myWriteDbButton.setEnabled(false);

        myImgStartButton = addButtonActionListener(myMainPanel.getMyImgStarButton());
        myImgStartButton.setToolTipText("Start Monitoring");
        myImgStopButton = addButtonActionListener(myMainPanel.getMyImgStopButton());
        myImgStopButton.setToolTipText("Stop Monitoring");
        myImgDBButton = addButtonActionListener(myMainPanel.getMyImgDBButton());
        myImgDBButton.setToolTipText("Write to Database");
        myImgClearButton = addButtonActionListener(myMainPanel.getMyImgClearButton());
        myImgClearButton.setToolTipText("Reset");
    }

    /**
     * Helper method for the setUpButtons method. This will add an action listener
     * to the button passed in and return the button.
     */
    private final JButton addButtonActionListener(JButton theButton) {
        theButton.addActionListener(this);
        return theButton;
    }

    /**
     * This method will create the menu bar for the GUI. Cleaned up original version
     * to make it more readable.
     */
    private final void createMenuBar() {
        myMenuBar = new JMenuBar();
        createFileMenu();
        createWatcherMenu();
        createDatabaseMenu();
        createEmailMenu();
        createAboutMenu();
        createImgButtons();
        myFrame.setJMenuBar(myMenuBar);
    }

    /**
     * Creates the first drop down menu choice for the GUI.
     */
    private final void createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        myTimeLabel = new JLabel("Time not started.");
        myDatabaseConnectionLabel = new JLabel("Database not connected.");
        myMenuStart = new JMenuItem("Start");
        myMenuStop = new JMenuItem("Stop");
        myFileQueryItem = new JMenuItem("Query Database");
        myFileQueryItem.setEnabled(false);
        myFileQueryItem.addActionListener(this);
        final JMenuItem closeItem = new JMenuItem("Close");
        myMenuStart.setEnabled(false);
        myMenuStop.setEnabled(false);
        fileMenu.add(myMenuStart);
        fileMenu.add(myMenuStop);
        fileMenu.add(myFileQueryItem);
        fileMenu.add(closeItem);
        closeItem.addActionListener(this);
        myMenuBar.add(fileMenu);
    }

    /**
     * Creates the second drop down menu choice for the GUI.
     */
    private final void createWatcherMenu() {
        final JMenu watcherMenu = new JMenu("Debug");
        add10Item = new JMenuItem("Add 10 Events");
        add10Item.addActionListener(this);
        watcherMenu.add(add10Item);
        add100Item = new JMenuItem("Add 100 Events");
        watcherMenu.add(add100Item);
        add100Item.addActionListener(this);
        myAdd1OfEachItem = new JMenuItem("Add one of each file type");
        watcherMenu.add(myAdd1OfEachItem);
        myAdd1OfEachItem.addActionListener(this);
        watcherMenu.add(myAdd1OfEachItem);
        myMenuBar.add(watcherMenu);
    }

    /**
     * Creates the third drop down menu choice for the GUI.
     */
    private final void createDatabaseMenu() {
        final JMenu databaseMenu = new JMenu("Database");

        myConnectDbItem = new JMenuItem("Connect to Database");
        myDisconnectDbItem = new JMenuItem("Disconnect Database");
        // Disabling until database connection is established.
        myDisconnectDbItem.setEnabled(false);
        databaseMenu.add(myConnectDbItem);
        databaseMenu.add(myDisconnectDbItem);
        myConnectDbItem.addActionListener(this);
        myDisconnectDbItem.addActionListener(this);

        myMenuBar.add(databaseMenu);
    }

    /**
     * Creates the fourth drop down menu choice for the GUI.
     */
    private final void createAboutMenu() {
        final JMenu aboutMenu = new JMenu("About");
        final JMenuItem aboutHelpItem = new JMenuItem("About");
        aboutHelpItem.addActionListener(this);
        aboutMenu.add(aboutHelpItem);
        myMenuBar.add(aboutMenu);
    }

    /**
     * Creates the image buttons for the GUI and pushes them all the way to the end
     * of the menu bar.
     */
    private final void createImgButtons() {
        myMenuBar.add(Box.createHorizontalGlue());
        myMenuBar.add(myImgStartButton);
        myMenuBar.add(myImgStopButton);
        myMenuBar.add(myImgDBButton);
        myMenuBar.add(myImgClearButton);
    }

    private final void createEmailMenu() {
        final JMenu emailMenu = new JMenu("Email");
        final JMenuItem sendEmailItem = new JMenuItem("Send File via Email");
        sendEmailItem.addActionListener(this);
        emailMenu.add(sendEmailItem);
        myMenuBar.add(emailMenu);

    }

    /**
     * This method will keep track of the time that the user has been monitoring
     * files.
     */
    private final void timeAndDbLabel() {
        myTimer = new Timer(1000, (ActionEvent e) -> {
            runningTime++;
            timerLabelExtended();
        });
        myMenuStart.addActionListener(this);
        myMenuStop.addActionListener(this);
        // Create a panel for the time label
        final JPanel bottomGuiPanel = new JPanel(new BorderLayout());

        final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(myDatabaseConnectionLabel);

        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(myTimeLabel);

        bottomGuiPanel.add(leftPanel, BorderLayout.WEST);
        bottomGuiPanel.add(rightPanel, BorderLayout.EAST);
        myFrame.add(bottomGuiPanel, BorderLayout.SOUTH);
    }

    /**
     * This method will extend the timer label to show the time in days, hours,
     * minutes, and seconds.
     */
    private final void timerLabelExtended() {
        final int days = runningTime / 86400;
        final int hours = (runningTime % 86400) / 3600;
        final int minutes = (runningTime % 3600) / 60;
        final int seconds = runningTime % 60;
        final String timeFormatted = String.format("Time Running: %02d Days: %02d Hours: %02d Minutes: %02d Seconds",
                days, hours, minutes, seconds);
        myTimeLabel.setText(timeFormatted);
    }

    /**
     * Adds document listeners for the directory, database, and extension fields.
     */
    private final void setUpDocumentListeners() {
        final DocumentListener theListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFields();
            }
        };

        myDirectoryField = myMainPanel.getMyDirectoryField();
        myDirectoryField.getDocument().addDocumentListener(theListener);
        myExtensionField = (JTextField) myExtensionComboBox.getEditor().getEditorComponent();
        // Was starting out blank for some reason.
        myExtensionField.setText(("All extensions"));
        myExtensionField.getDocument().addDocumentListener(theListener);
    }

    /**
     * This method will set up the file viewer for the GUI.
     */
    private final void setUpFileViewer() {

        // Create a JSplitPane to divide the space between the main panel and the event
        // table
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, myMainPanel, myEventTable);
        final JSplitPane outerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane, mySecondQueryPanel);

        splitPane.setResizeWeight(MY_SPLIT_PANE_RESIZE_WEIGHT);
        splitPane.setDividerSize(0);
        outerSplitPane.setResizeWeight(1); // Give maximum space to event table (1)
        outerSplitPane.setDividerSize(0);

        // Add the outer JSplitPane to the frame
        myFrame.add(outerSplitPane, BorderLayout.CENTER);
    }

    /**
     * Sets up the exit listener for the GUI.
     */
    private final void setUpExitListener() {
        myFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        myFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                handleExit();
            }
        });
    }

    /**
     * Method for handling the exiting of the program and ensuring that if the user
     * has unsaved events, they are prompted to save them before exiting.
     */
    private final void handleExit() {
        final List<FileEvent> unsavedEvents = myEventTable.getData();

        if (!unsavedEvents.isEmpty()) {
            final int choice = JOptionPane.showConfirmDialog(
                    myFrame,
                    "You have unsaved file events. Would you like to save them to the database before exiting?",
                    "Unsaved Data",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.CANCEL_OPTION) {
                return; // Stop exit, let user stay
            }

            if (choice == JOptionPane.YES_OPTION) {
                if (DatabaseConnection.getMyConnection() == null) {
                    int dbChoice = JOptionPane.showConfirmDialog(
                            myFrame,
                            "Database is not connected. Would you like to connect now?",
                            "Database Not Connected",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (dbChoice == JOptionPane.CANCEL_OPTION) {
                        return; // Stop exit
                    }

                    if (dbChoice == JOptionPane.YES_OPTION) {
                        if (!DatabaseConnection.connect()) {
                            JOptionPane.showMessageDialog(
                                    myFrame,
                                    "Failed to connect to the database. Events will not be saved.",
                                    "Database Connection Error",
                                    JOptionPane.ERROR_MESSAGE);
                            System.exit(0);
                        }
                    } else {
                        System.exit(0);
                    }
                }
                FileEventDAO.insertFileEvents(unsavedEvents);
            }
        }
        System.exit(0);
    }

    /**
     * This method will handle the actions of the user when they click on the menu
     * items, different actions will be taken depending on the menu item clicked.
     */
    @Override
    public final void actionPerformed(final ActionEvent theEvent) {
        final Object source = theEvent.getSource();
        final String command = theEvent.getActionCommand();
        // Table was necessary to expand its scope in order to be used.
        myQueryResults = new FWEventTable();

        // Start Monitoring
        if (source.equals(myMenuStart) || source.equals(myDirectoryStartButton) || source.equals(myImgStartButton)) {
            startMonitoring();
        }
        // Stop Monitoring
        else if (source.equals(myMenuStop) || source.equals(myDirectoryStopButton) || source.equals(myImgStopButton)) {
            stopMonitoring();
        }
        // Close Application
        else if (command.equals("Close")) {
            System.exit(0);
        }
        // Show About Dialog
        else if (command.equals("About")) {
            showAboutDialog();
        }
        // Connect to Database
        else if (command.equals("Connect to Database")) {
            handleDatabaseConnection(true);
        }
        // Disconnect from Database
        else if (command.equals("Disconnect Database")) {
            handleDatabaseConnection(false);
        }
        // Extension Selection
        else if (source.equals(myExtensionComboBox)) {
            handleExtensionSelection();
        }
        // Send File via Email
        else if (command.equals("Send File via Email")) {
            sendFileViaEmail();
        } // Browse Directory
        else if (source.equals(myDirectoryBrowseButton)) {
            browseDirectory(myDirectoryField);
        } // Clear Fields
        else if (source.equals(myResetDirectoryButton) || source.equals(myImgClearButton)) {
            clearFields();
        } // Export Query Results to CSV
        else if (source.equals(myExportCSVButton)) {
            exportQueryResultsToCSV();
        } // Write to Database
        else if (source.equals(myWriteDbButton) || source.equals(myImgDBButton)) {
            writeToDatabaseHelper();
        } // Query window popup
        else if (source.equals(myQueryButton) || source.equals(myFileQueryItem)) {
            queryWindow();
            myQueryTable.clearTable();
        } // User is using pre-designed queries.
        else if (source.equals(myAutomaticQueryComboBox)) {
            handleAutomaticQueryComboBox();
        } // User wants to filter for specific things on their own.
        else if (source.equals(myManualQueryComboBox)) {
            handleManualQueryComboBox();
        } // If the user chooses a manual query option, this will run through the choices.
        else if (source.equals(myManualQueryButton)) {
            manualQueryHelper();
        } // User changing the activity, so clear out table.
        else if (source.equals(myEventActivityDropdown)) {
            myQueryTable.clearTable();
            if (!myEventActivityDropdown.getSelectedItem().toString().equals("Choose Activity Type")) {
                myQueryResults = FileEventDAO.manualQueryResults("event_type",
                        myEventActivityDropdown.getSelectedItem().toString());
            }
        } // Resetting database along with warning.
        else if (source.equals(myDatabaseResetButton)) {
            final int choice = JOptionPane.showConfirmDialog(
                    myQueryPanel,
                    "This will remove all data from the database, resetting it. Are you sure you want to continue?",
                    "Reset Database",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                FileEventDAO.resetEntireDatabase();
                myQueryTable.clearTable();
            }
        } else if (source.equals(myReturnWindowButton)){ // Closing the query window upon button press.
            myQueryFrame.dispose();
        } // Adding 10 items debug menu.
        else if (source.equals(add10Item)) {
            addTestEvents(10);
        } // Adding 100 items debug menu.
        else if (source.equals(add100Item)) {
            addTestEvents(100);
        } // Adding one of each item debug menu.
        else if (source.equals(myAdd1OfEachItem)) {
            runDummyInsertion();
        } // If filtering is enabled then update the table.
        if (myFilteringOn) {
            myEventTable.filterByExtension('.' + myExtensionComboBox.getSelectedItem().toString());
        } // If the query returns more than 0 results, then update the table.
        if (myQueryResults.getData().size() != 0) {
            for (FileEvent event : myQueryResults.getData()) {
                myQueryTable.addEvent(event);
            }
            queryFrameFixSize();
        }
    }

    /**
     * Helper method for the manual query portion of the query window popup
     * depending on the users choice it will display and use different data.
     */
    private final void manualQueryHelper() {
        if (myManualQueryLabel.getText().equals("File Pathway: ")) {
            browseDirectory(myPathOrDateText);
            if (!myPathOrDateText.getText().isEmpty()) {
                // Removing old data to prevent duplication.
                myQueryTable.clearTable();
                myQueryResults = FileEventDAO.manualQueryResults("file_path", myPathOrDateText.getText());
            }
        } else if (myManualQueryLabel.getText().equals("File Name to Search: ")) {
            if (!myFileNameText.getText().equals("")) {
                // Removing old data to prevent duplication.
                myQueryTable.clearTable();
                myQueryResults = FileEventDAO.manualQueryResults("file_name", myFileNameText.getText());
            }
        } else if (myManualQueryLabel.getText().equals("Choose Dates: ")) {
            final String selectedDateTime = dateRangePicker();
            myPathOrDateText.setText(selectedDateTime);
            if (!selectedDateTime.isEmpty()) {
                // Removing old data to prevent duplication.
                myQueryTable.clearTable();
                myQueryResults = FileEventDAO.manualQueryResults("event_date", selectedDateTime);
            }
        }
    }

    /**
     * Helper method for the "automatic" predefined queries and displaying them.
     */
    private final void handleAutomaticQueryComboBox() {
        resetQueryFrame();
        myManualQueryComboBox.setVisible(false);
        queryFrameFixSize();
        if (myAutomaticQueryComboBox.getSelectedItem().equals("Query 1 - All events from today")) {
            myQueryResults = FileEventDAO.fileEventsFromToday();
        } else if (myAutomaticQueryComboBox.getSelectedItem()
                .equals("Query 2 - Top 5 frequently modified file types")) {
            myQueryResults = FileEventDAO.topFiveExtensions();
        } else if (myAutomaticQueryComboBox.getSelectedItem()
                .equals("Query 3 - Most Common Events for Each Extension")) {
            myQueryResults = FileEventDAO.mostCommonEventsPerExtension();
        } else if (myAutomaticQueryComboBox.getSelectedItem().equals("Manually query")) {
            changeManualLabelAndButton("Choose Way to Query Database: ", null);
            myManualQueryComboBox.setVisible(true);
            queryFrameFixSize();
            myQueryFrame.pack();
        }
    }

    /**
     * Helper method for sending files via email, this will prompt the user to enter
     * an email and then select a file to send.
     */
    private final void sendFileViaEmail() {
        final String recipient = JOptionPane.showInputDialog(myFrame, "Enter recipient email:", "Send Email",
                JOptionPane.QUESTION_MESSAGE);
        if (recipient != null && !recipient.isEmpty()) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                EmailSender.sendEmailWithAttachment(recipient, "File Watcher Report",
                        "Please find the attached file.", filePath);
                JOptionPane.showMessageDialog(myFrame, "Email Sent Successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Helper method for dis/connecting to the database and displaying that
     * information in a popup as well as text in the bottom right.
     * 
     * @param theConnectionValue The boolean value of what you want the connection
     *                           to be.
     */
    private final void handleDatabaseConnection(boolean theConnectionValue) {
        final boolean success;
        if (theConnectionValue) {
            success = DatabaseConnection.connect();
            if (success) {
                myDatabaseActive = true;
                myConnectDbItem.setEnabled(false);
                myDisconnectDbItem.setEnabled(true);
                setDatabaseConnected(true);
                JOptionPane.showMessageDialog(myFrame, "Connected to the database successfully!",
                        "Database Connection", JOptionPane.INFORMATION_MESSAGE);
                myDatabaseConnectionLabel.setText("Database connected.");
            } else {
                JOptionPane.showMessageDialog(myFrame, "Failed to connect to the database.",
                        "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            myDatabaseActive = false;
            myConnectDbItem.setEnabled(true);
            myDisconnectDbItem.setEnabled(false);
            DatabaseConnection.disconnect();
            setDatabaseConnected(false);
            JOptionPane.showMessageDialog(myFrame, "Disconnected from the database.",
                    "Database Disconnection", JOptionPane.INFORMATION_MESSAGE);
            myDatabaseConnectionLabel.setText("Database not connected.");
        }
    }

    /**
     * Helper method for addressing what the user is filtering the main GUI table
     * with.
     */
    private final void handleExtensionSelection() {
        if (!myExtensionField.getText().isEmpty()
                && !myExtensionComboBox.getSelectedItem().equals(MY_CUSTOM_EXTENSION_STRING)) {
            checkFields();
            myFilteringOn = true;
            if (myExtensionComboBox.getSelectedItem().equals("All extensions")) {
                myEventTable.updateTable();
                myFilteringOn = false;
            }
        }
    }

    /**
     * Helper method for updating the query window labels and buttons based on the
     * user's selection.
     */
    private final void handleManualQueryComboBox() {
        myQueryTable.clearTable();
        resetQueryFrame();
        final Object shortenedManualCombo = myManualQueryComboBox.getSelectedItem();
        if (shortenedManualCombo.equals("File Extension")) {
            changeManualLabelAndButton("File Extensions to View: ", null);
            myQueryCheckBoxPanel.setVisible(true);
            myQueryFrame.pack(); // Resizes the frame to fit the components
            myQueryFrame.queryFrameSize(.8, .3);
        } else if (shortenedManualCombo.equals("Path to File Location")) {
            changeManualLabelAndButton("File Pathway: ", "Browse Directories");
            myPathOrDateText.setVisible(true);
        } else if (shortenedManualCombo.equals("File Name")) {
            changeManualLabelAndButton("File Name to Search: ", "Search");
            myFileNameText.setVisible(true);
        } else if (shortenedManualCombo.equals("Type of Activity")) {
            changeManualLabelAndButton("Type of Activity to choose: ", null);
            myEventActivityDropdown.setVisible(true);
        } else if (shortenedManualCombo.equals("Between Two Dates")) {
            changeManualLabelAndButton("Choose Dates: ", "Open Calendar");
            myPathOrDateText.setVisible(true);
        }
        queryFrameFixSize();
    }

    /**
     * Helper method for displaying the date and time popup.
     * 
     * @return String representation of the selected date and time.
     */
    private final String dateRangePicker() {
        final JPanel panel = new JPanel(new GridLayout(2, 2));

        // Start Date Picker
        final JLabel startLabel = new JLabel("Start Date:");
        final JSpinner startSpinner = new JSpinner(new SpinnerDateModel());
        final JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd");
        startSpinner.setEditor(startEditor);

        // End Date Picker
        final JLabel endLabel = new JLabel("End Date:");
        final JSpinner endSpinner = new JSpinner(new SpinnerDateModel());
        final JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd");
        endSpinner.setEditor(endEditor);

        // Add components to the panel
        panel.add(startLabel);
        panel.add(startSpinner);
        panel.add(endLabel);
        panel.add(endSpinner);

        final int option = JOptionPane.showConfirmDialog(null, panel, "Select Date Range",
                JOptionPane.OK_CANCEL_OPTION);
        myQueryTable.clearTable();

        if (option == JOptionPane.OK_OPTION) {
            final String startDate = startEditor.getFormat().format(startSpinner.getValue());
            final String endDate = endEditor.getFormat().format(endSpinner.getValue());
            return startDate + " to " + endDate; // Return selected date range as a formatted string
        }

        return ""; // Return empty string if canceled
    }

    /**
     * Helper method for displaying the manual query information.
     * 
     * @param theLabel  The label to display.
     * @param theButton The button to display.
     */
    private final void changeManualLabelAndButton(final String theLabel, final String theButton) {
        myManualQueryLabel.setVisible(true);
        myManualQueryLabel.setText(theLabel);
        if (theButton != null) {
            myManualQueryButton.setVisible(true);
            myManualQueryButton.setText(theButton);
        }
    }

    /**
     * Resetting the query frame back to all the options for when it was first
     * opened up.
     */
    private final void resetQueryFrame() {
        myQueryTable.clearTable();
        myQueryCheckBoxPanel.setVisible(false);
        myManualQueryButton.setVisible(false);
        myPathOrDateText.setVisible(false);
        myPathOrDateText.setText("");
        myFileNameText.setVisible(false);
        myFileNameText.setText("");
        myManualQueryLabel.setVisible(false);
        myEventActivityDropdown.setVisible(false);
        // Erasing the selected checkboxes as a precaution
        for (final JCheckBox checkBox : myExtensionCheckBox) {
            checkBox.setSelected(false);
        }
    }

    /**
     * Helper method to repaint and revalidate the query frame.
     */
    private final void queryFrameFixSize() {
        myQueryFrame.revalidate();
        myQueryFrame.repaint();
    }

    /**
     * Query popup window and all the things that go into making it visually appear.
     */
    private final void queryWindow() {
        myQueryFrame = new FWFrame();
        myQueryFrame.queryFrameSize(.8, .3);
        final ImageIcon icon = new ImageIcon(getClass().getResource("/appIcon.png"));
        myQueryFrame.setIconImage(icon.getImage());
        myQueryFrame.setLocationRelativeTo(null);
        myQueryFrame.setTitle("Query Window");
        myQueryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myQueryFrame.setLayout(new BorderLayout());

        final JPanel queryGUI = myQueryPanel.FWQueryPanel();
        myQueryFrame.add(queryGUI, BorderLayout.NORTH);

        myQueryCheckBoxPanel = setUpQueryCheckBoxes();

        myQueryCheckBoxPanel.setVisible(false);

        final JSplitPane middleQueryPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, myQueryCheckBoxPanel,
                myQueryTable);
        middleQueryPane.setResizeWeight(MY_SPLIT_PANE_RESIZE_WEIGHT);
        middleQueryPane.setDividerSize(2);

        // Create Export Button
        myExportCSVButton = myQueryPanel.getCSVButton();
        myExportCSVButton.addActionListener(this);

        final JSplitPane queryPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, queryGUI, middleQueryPane);
        queryPane.setResizeWeight(MY_SPLIT_PANE_RESIZE_WEIGHT);
        queryPane.setDividerSize(0);

        // Add the JSplitPane to the frame
        myQueryFrame.add(queryPane, BorderLayout.CENTER);

        setUpQueryFiltering();

        myQueryFrame.setVisible(true);
    }

    /**
     * Helper method to add action listeners to all buttons and comboboxes and set
     * them up.
     */
    private final void setUpQueryFiltering() {
        myAutomaticQueryComboBox = myQueryPanel.getQueryPopupSelection();
        myAutomaticQueryComboBox.addActionListener(this);

        myManualQueryComboBox = myQueryPanel.getManualQueryComboBox();
        myManualQueryComboBox.addActionListener(this);

        myDatabaseResetButton = myQueryPanel.getDatabaseResetButton();
        myDatabaseResetButton.addActionListener(this);

        myReturnWindowButton = myQueryPanel.getReturnWindowButton();
        myReturnWindowButton.addActionListener(this);

        myManualQueryButton = myQueryPanel.getFileExtensionFilterButton();
        myManualQueryButton.addActionListener(this);

        myEventActivityDropdown = myQueryPanel.getEventActivityDropdown();
        myEventActivityDropdown.addActionListener(this);

        myFileNameText = myQueryPanel.getFileNameText();

        myPathOrDateText = myQueryPanel.getFileExtensionText();

        myManualQueryLabel = myQueryPanel.getMyManualQueryLabel();
    }

    /**
     * Helper method for writing the information to the database.
     */
    private final void writeToDatabaseHelper() {
        if (DatabaseConnection.getMyConnection() == null) {
            final int choice = JOptionPane.showConfirmDialog(
                    myFrame,
                    "Database is not connected. Would you like to connect now?",
                    "Database Not Connected",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.CANCEL_OPTION) {
                return; // Stop execution if the user cancels
            }

            if (choice == JOptionPane.YES_OPTION) {
                final boolean success = DatabaseConnection.connect();
                if (!success) {
                    JOptionPane.showMessageDialog(
                            myFrame,
                            "Failed to connect to the database. Events will not be saved.",
                            "Database Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop execution if connection fails
                }
                setDatabaseConnected(true);
                myDatabaseConnectionLabel.setText("Database connected.");
                // disable menu item to connect to database
                myConnectDbItem.setEnabled(false);
                myDisconnectDbItem.setEnabled(true);

            } else {
                return; // Stop execution if the user chooses "No"
            }
        }
        // Write all stored events to the database
        int rowsInserted = 0;
        for (FileEvent event : myEventTable.getData()) {
            FileEventDAO.insertFileEvent(event);
            rowsInserted++;
        }
        JOptionPane.showMessageDialog(myFrame, rowsInserted + " events written to the database.",
                "Database Write", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Setting up all the query window checkboxes for the user to use.
     * 
     * @return The JPanel with the updated queryboxes set up.
     */
    private final JPanel setUpQueryCheckBoxes() {
        final JPanel queryCheckboxesPanel = new JPanel();
        final String[] extensions = { "test", "docx", "pdf", "txt", "png", "jpg", "jpeg", "gif", "mp3", "mp4", "wav",
                "avi",
                "mov", "csv" };
        final List<String> extensionList = new ArrayList<>();

        myExtensionCheckBox = new JCheckBox[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            myExtensionCheckBox[i] = new JCheckBox(extensions[i]);

            myExtensionCheckBox[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final JCheckBox curCheckBox = (JCheckBox) e.getSource();
                    final String curExtension = curCheckBox.getText();
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (!extensionList.contains(curExtension)) {
                            extensionList.add(curExtension);
                        }
                    } else {
                        extensionList.remove(curExtension);
                    }
                    final FWEventTable tempQueryResults = FileEventDAO.querySpecificExtensions(extensionList);
                    myQueryTable.clearTable();
                    for (final FileEvent event : tempQueryResults.getData()) {
                        myQueryTable.addEvent(event);
                    }
                    queryFrameFixSize();
                }
            });
            queryCheckboxesPanel.add(myExtensionCheckBox[i]);
        }
        return queryCheckboxesPanel;
    }

    /**
     * Creates a date string in the format of yyyy-MM-dd HH:mm:ss
     * 
     * @return String representation of the current date
     */
    private final String createDateString() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    /**
     * Creates a time string in the format of HH:mm:ss
     * 
     * @return String representation of the current time.
     */
    private final String createTimeString() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * Method for if the user has hit the start button and all the correct fields
     * are filled.
     */
    private final void startMonitoring() {
        myIsMonitoring = true;
        try {
            myDirectoryWatchService = new DirectoryWatchService(myDirectoryField.getText(), this);
            myDirectoryWatchService.start();
        } catch (final IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "\"" + myDirectoryField.getText() + "\" is not a valid directory",
                    "Invalid Directory Error", JOptionPane.ERROR_MESSAGE);
            myIsMonitoring = false;
            return;
        }
        runningTime = 0;
        myTimeLabel.setText("Time not started.");
        myTimer.start();
        reverseButtonState(false);
        // Disable ability to modify directory and extension fields while monitoring
        myDirectoryField.setEditable(false);
        myExtensionField.setEditable(false);
        myExtensionComboBox.setEditable(false);
    }

    /**
     * Method for if the user has hit the stop button.
     */
    private final void stopMonitoring() {
        myTimer.stop();
        myIsMonitoring = false;
        reverseButtonState(true);
        myDirectoryWatchService.stop();
        // Enable ability to modify directory and extension fields while not monitoring
        myDirectoryField.setEditable(true);
        myExtensionField.setEditable(true);
        myExtensionComboBox.setEditable(true);
    }

    /**
     * Method to show the about dialog box.
     */
    private final void showAboutDialog() {
        JOptionPane.showMessageDialog(myFrame,
                "Program Usage: This application watches file system changes.\n" +
                        "Version: 1.0\n" +
                        "Developers: Manjinder Ghuman, Ryder Deback, Brendan Tucker",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method to browse the directory and set the text field to the directory
     * chosen.
     */
    private final void browseDirectory(JTextField theField) {
        final JFileChooser direcChooser = new JFileChooser();
        direcChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        direcChooser.setAcceptAllFileFilterUsed(false);

        final int directoryValue = direcChooser.showOpenDialog(null);
        if (directoryValue == JFileChooser.APPROVE_OPTION) {
            theField.setText(direcChooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Method to reset the timer and set the label to "Time Not Started."
     */
    private final void resetTimer() {
        if (myTimer != null) {
            myTimer.stop();
        }
        runningTime = 0;
        myTimeLabel.setText("Time Not Started.");
    }

    /**
     * Method to clear all the fields in the GUI and reset the buttons.
     */
    private final void clearFields() {
        myEventTable.clearTable();
        myDirectoryField.setText("");
        myExtensionComboBox.setSelectedItem("All extensions");
        resetTimer();
        DatabaseConnection.disconnect();
        if(myDatabaseActive){
            handleDatabaseConnection(false);
        }
        myDatabaseActive = false;
        if (myDirectoryWatchService != null)
            myDirectoryWatchService.stop();
        myIsMonitoring = false;
        checkFields();
    }

    /**
     * Helper method to reverse the state of the buttons.
     * 
     * @param theValue The value to set the buttons to.
     */
    private final void reverseButtonState(boolean theValue) {
        myMenuStart.setEnabled(theValue);
        myDirectoryStartButton.setEnabled(theValue);
        myImgStartButton.setEnabled(theValue);
        myMenuStop.setEnabled(!theValue);
        myDirectoryStopButton.setEnabled(!theValue);
        myImgStopButton.setEnabled(!theValue);
    }

    private final void addTestEvents(int theAmount) {
        for (int i = 0; i < theAmount; i++) {
            myEventTable.addEvent(new FileEvent("DebugTestFile.test", "C:\\Users\\test\\subfolder\\subfolder",
                    "TESTEVENT", ".test", createDateString(), createTimeString()));
        }
    }

    /**
     * Helper method for inserting one of each event or "dummy" data to the table.
     */
    private final void runDummyInsertion() {
        final String dummyDate = LocalDate.now().toString();
        final String dummyDatePlus3 = LocalDate.now().plusDays(3).toString();
        final String dummyDatePlus10 = LocalDate.now().plusDays(10).toString();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        final String dummyTime = LocalTime.now().format(formatter).toString();
        final String dummyTimePlus6 = LocalTime.now().plusHours(6).format(formatter).toString();
        final String dummyTimeMinus6 = LocalTime.now().plusHours(-6).format(formatter).toString();

        final List<FileEvent> dummyDataList = Arrays.asList(
                new FileEvent("Dummy DUMB", "C:\\Users\\test\\subfolder\\subfolder", "TRASH", ".dumb", dummyDate,
                        dummyTime),
                new FileEvent("Dummy TEST", "C:\\Users\\test\\subfolder\\subfolder", "CREATED", ".test", dummyDate,
                        dummyTimePlus6),
                new FileEvent("Dummy DOCX", "C:\\Users\\test\\subfolder\\subfolder", "CREATED", ".docx", dummyDatePlus3,
                        dummyTime),
                new FileEvent("Dummy PDF", "C:\\Users\\test\\subfolder\\subfolder", "CREATED", ".pdf", dummyDate,
                        dummyTimePlus6),
                new FileEvent("Dummy TXT", "C:\\Users\\test\\subfolder\\subfolder", "DELETED", ".txt", dummyDatePlus10,
                        dummyTime),
                new FileEvent("Dummy PNG", "C:\\Users\\test\\subfolder\\subfolder", "DELETED", ".png", dummyDatePlus10,
                        dummyTimePlus6),
                new FileEvent("Dummy JPG", "C:\\Users\\test\\subfolder\\subfolder", "DELETED", ".jpg", dummyDatePlus3,
                        dummyTimeMinus6),
                new FileEvent("Dummy JPEG", "C:\\Users\\test\\subfolder\\subfolder", "DELETED", ".jpeg", dummyDatePlus3,
                        dummyTime),
                new FileEvent("Dummy GIF", "C:\\Users\\test\\subfolder\\subfolder", "MODIFIED", ".gif", dummyDatePlus10,
                        dummyTime),
                new FileEvent("Dummy MP3", "C:\\Users\\test\\subfolder\\subfolder", "MODIFIED", ".mp3", dummyDatePlus10,
                        dummyTimePlus6),
                new FileEvent("Dummy MP4", "C:\\Users\\test\\subfolder\\subfolder", "CREATED", ".mp4", dummyDate,
                        dummyTimePlus6),
                new FileEvent("Dummy WAV", "C:\\Users\\test\\subfolder\\subfolder", "CREATED", ".wav", dummyDatePlus3,
                        dummyTimeMinus6),
                new FileEvent("Dummy AVI", "C:\\Users\\test\\subfolder\\subfolder", "DELETED", ".avi", dummyDate,
                        dummyTimeMinus6),
                new FileEvent("Dummy MOV", "C:\\Users\\test\\subfolder\\subfolder", "MODIFIED", ".mov", dummyDatePlus3,
                        dummyTimeMinus6),
                new FileEvent("Dummy CSV", "C:\\Users\\test\\subfolder\\subfolder", "MODIFIED", ".csv", dummyDatePlus10,
                        dummyTime));

        // Loop through the list and add each event to the table
        for (final FileEvent theEvent : dummyDataList) {
            addDummyData(theEvent);
        }
    }

    /**
     * Helper method for the dummy insertion to clean up the code.
     * 
     * @param theFileEvent The file event to add to the table.
     */
    private final void addDummyData(final FileEvent theFileEvent) {
        // Adding the event to the event table
        myEventTable.addEvent(
                new FileEvent(theFileEvent.getFileName(), theFileEvent.getFilePath(), theFileEvent.getEventType(),
                        theFileEvent.getExtension(), theFileEvent.getEventDate(), theFileEvent.getEventTime()));
    }

    /**
     * Checks if the required fields are filled out correctly and enables/disables
     * Start buttons accordingly.
     */
    private final void checkFields() {
        // Prevent the pressing of the stop buttons if DirectoryWatchService isnt
        // monitoring
        if (!myIsMonitoring) {
            myMenuStop.setEnabled(false);
            myDirectoryStopButton.setEnabled(false);
            myImgStopButton.setEnabled(false);
        }
        final boolean hasDirectory = !myDirectoryField.getText().trim().isEmpty();
        final boolean hasExtension = !myExtensionField.getText().trim().isEmpty()
                && !myExtensionField.getText().equals(MY_CUSTOM_EXTENSION_STRING);
        final boolean hasDatabase = myDatabaseActive;

        final boolean enableStart = (hasDirectory && hasExtension) || hasDatabase;

        myDirectoryStartButton.setEnabled(enableStart);
        myMenuStart.setEnabled(enableStart);
        myImgStartButton.setEnabled(enableStart);

        // If conditions are not met, disable the start buttons
        if (!enableStart) {
            myDirectoryStartButton.setEnabled(false);
            myMenuStart.setEnabled(false);
            myImgStartButton.setEnabled(false);
        }
        // Do not enable start buttons if already monitoring
        if (myIsMonitoring) {
            myDirectoryStartButton.setEnabled(false);
            myMenuStart.setEnabled(false);
            myImgStartButton.setEnabled(false);
        }
    }

    /**
     * Exports the query results to a CSV file.
     */
    private final void exportQueryResultsToCSV() {
        final List<FileEvent> events = myQueryTable.getData();

        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(myQueryFrame, "No query results available for export. Run a query first.",
                    "Export Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        final int userSelection = fileChooser.showSaveDialog(myFrame);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.endsWith(".csv")) {
            filePath += ".csv";
        }

        try (final PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("File Watcher Query Results");
            writer.println(
                    "Export Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println();
            writer.println("File Name,File Path,Event Type,Extension,Event Date,Event Time");

            for (final FileEvent event : events) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        event.getFileName(),
                        event.getFilePath(),
                        event.getEventType(),
                        event.getExtension(),
                        event.getEventDate(),
                        event.getEventTime());
            }

            JOptionPane.showMessageDialog(myFrame, "Export successful: " + filePath, "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(myFrame, "Error writing to file.", "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Returns true if GUI is monitoring a directory. Used by DirectoryWatchService
     * to check if it should continue running.
     * 
     * @return true if monitoring, false otherwise
     */
    public final boolean isMonitoring() {
        return myIsMonitoring;
    }

    /**
     * Sets the database connection status in the GUI.
     * 
     * @param theValue true if connected, false otherwise
     */
    public final void setDatabaseConnected(final boolean theValue) {
        myWriteDbButton.setEnabled(theValue);
        myQueryButton.setEnabled(theValue);
        myFileQueryItem.setEnabled(theValue);
    }

    /**
     * Returns the event table for the GUI.
     * 
     * @return The event table for the GUI
     */
    public final FWEventTable getEventTable() {
        return myEventTable;
    }

    /**
     * Returns the instance of the GUI.
     * 
     * @return The instance of the GUI
     */
    public static final FWGUI getMyFWGUIInstance() {
        return myFWGUIInstance;
    }

    /**
     * Returns the main panel for the GUI.
     * 
     * @return The main panel for the GUI
     */
    public final FWPanel getMainPanel() {
        return myMainPanel;
    }

    /**
     * Returns the frame for the GUI.
     * 
     * @return The frame for the GUI
     */
    public final JFrame getFrame() {
        return myFrame;
    }

    /**
     * Returns the stop button for the directory.
     * 
     * @return The stop button for the directory
     */
    public final JButton getStopButton() {
        return myDirectoryStopButton;
    }

}
