import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class represents the panel that will be used in the File Watcher GUI.
 */
public final class FWPanel extends JPanel {
    // The extensions that can be monitored.
    private JComboBox<String> myExtensionDropdown;
    // The text fields that will be used in the GUI, myPathOrDate and myFileName are in the query window.
    private JTextField myDirectoryField, myPathOrDateText, myFileNameText;
    // The dropdowns that will be used in the GUI.
    private JComboBox<String> myQuerySelectionDropdown, myManualQueryComboBox, myEventActivityDropdown;
    // The various buttons that will be used in both of the GUI's.
    private JButton myWriteDbButton, myQueryButton, myDatabaseResetButton, myResetButton, myBrowseButton, myStartButton,
            myStopButton, myExportCSVButton, myManualQueryButton, myReturnWindowButton;
    // The buttons that have images associated with them.
    private JButton myImgStartButton, myImgStopButton, myImgDBButton, myImgClearButton;
    // The GridBagConstraint that will be used for a clean layout.
    private final GridBagConstraints myGBC;
    // The main panel, that is the primary panel being used.
    private final JPanel myMainPanel;
    // The query panel that appears after hitting the query button.
    private final JPanel myQueryPanel;
    // The label that will show data associated with what the user is querying.
    private JLabel myManualQueryLabel;

    /**
     * Constructor for the FWPanel class. This will set up the panel for the File
     * Watcher GUI.
     */
    public FWPanel() {
        setLayout(new BorderLayout());

        myMainPanel = new JPanel(new GridBagLayout());
        myGBC = new GridBagConstraints();
        myGBC.insets = new Insets(5, 5, 5, 5);
        myGBC.fill = GridBagConstraints.HORIZONTAL;

        createButtonBar();
        setUpExtensionBox();
        setUpDirectoryBox();
        setUpDirectoryButtons();
        myQueryPanel = setUpDatabaseBox();

        add(myMainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the query panel popup for the GUI.
     * @return The query panel popup for the GUI.
     */
    public final JPanel FWQueryPanel() {
        final JPanel queryPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints queryGBC = new GridBagConstraints();
        queryGBC.insets = new Insets(5, 5, 5, 5);

        final JLabel queryLabel = new JLabel("Query to Select: ");
        queryPanelGBC(queryGBC, 0, 0, 0.0);
        queryPanel.add(queryLabel, queryGBC);

        setUpQueryFilterOptions();
        
        queryPanelGBC(queryGBC, 1, 0, 1.0);
        queryPanel.add(myQuerySelectionDropdown, queryGBC);

        queryPanelGBC(queryGBC, 2, 0, 0.0);
        queryPanel.add(myExportCSVButton, queryGBC);

        queryPanelGBC(queryGBC,3, 0, 0.0);
        queryPanel.add(myReturnWindowButton, queryGBC);

        queryPanelGBC(queryGBC, 4, 0, 0.0);
        queryPanel.add(myDatabaseResetButton, queryGBC);

        queryPanelGBC(queryGBC, 0, 1, 0.0);
        queryPanel.add(myManualQueryLabel, queryGBC);

        queryPanelGBC(queryGBC, 1, 1, 0.0);
        queryPanel.add(myManualQueryComboBox, queryGBC);
        
        queryPanelGBC(queryGBC, 2, 1, 1.0);
        queryGBC.gridwidth = 2;
        queryPanel.add(myPathOrDateText, queryGBC);
        
        queryPanelGBC(queryGBC, 4, 1, 0.0);
        queryPanel.add(myManualQueryButton, queryGBC);

        queryPanelGBC(queryGBC, 2, 1, 0.0);
        queryPanel.add(myEventActivityDropdown, queryGBC);

        queryPanelGBC(queryGBC, 2, 1, 1.0);
        queryGBC.gridwidth = 2;
        queryPanel.add(myFileNameText,queryGBC);

        return queryPanel;
    }

    /**
     * Sets up the visual portions of the query popup window.
     */
    private final void setUpQueryFilterOptions(){
        myQuerySelectionDropdown = new JComboBox<>(
                new String[] { "Choose query", "Manually query", "Query 1 - All events from today",
                        "Query 2 - Top 5 frequently modified file types",
                        "Query 3 - Most Common Events for Each Extension", });
        myExportCSVButton = createModernButton("Export to CSV");
        myDatabaseResetButton = createModernButton("Reset Database");
        myReturnWindowButton = createModernButton("Return to Main Window");
        myManualQueryComboBox = new JComboBox<>(
                new String[] { "Choose file detail", "File Name", "File Extension", "Path to File Location",
                        "Type of Activity", "Between Two Dates" });
        myManualQueryComboBox.setVisible(false);

        myEventActivityDropdown = new JComboBox<>(new String[] {"Choose Activity Type","CREATED", "DELETED", "MODIFIED"});
        myEventActivityDropdown.setVisible(false);

        myFileNameText = new JTextField(0);
        myFileNameText.setVisible(false);

        myPathOrDateText = new JTextField(0);
        myPathOrDateText.setVisible(false);
        myPathOrDateText.setEditable(false);
        myManualQueryLabel = new JLabel("");
        myManualQueryLabel.setVisible(false);
        myManualQueryButton = createModernButton("");
        myManualQueryButton.setVisible(false);
    }

    /**
     * Helper method for adjusting the GridBag
     * @param theGBC The GridBagConstraint to be adjusted.
     * @param theX The X value to be adjusted for the gridbag.
     * @param theY The Y value to be adjusted for the gridbag.
     * @param theWeightx The weightx value to be adjusted for the gridbag.
     */
    private final void queryPanelGBC(final GridBagConstraints theGBC, final int theX, final int theY, final double theWeightx) {
        theGBC.fill = GridBagConstraints.HORIZONTAL;
        theGBC.gridx = theX;
        theGBC.gridy = theY;
        theGBC.weightx = theWeightx;
    }

    /**
     * Creates the buttons for the image icons.
     */
    private final void createButtonBar() {
        ImageIcon startImageIcon = new ImageIcon(getClass().getResource("/startWatching.png"));
        final ImageIcon stopImageIcon = new ImageIcon(getClass().getResource("/stopWatching.png"));
        final ImageIcon dbImageIcon = new ImageIcon(getClass().getResource("/startDB.png"));
        final ImageIcon clearImageIcon = new ImageIcon(getClass().getResource("/clearData.png"));

        // Force fixing the scaling as for some reason it was larger than its siblings.
        startImageIcon = new ImageIcon(startImageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        myImgStartButton = new JButton(startImageIcon);
        myImgStartButton.setEnabled(false);
        myImgStopButton = new JButton(stopImageIcon);
        myImgStopButton.setEnabled(false);
        myImgDBButton = new JButton(dbImageIcon);
        myImgClearButton = new JButton(clearImageIcon);
    }

    /**
     * Sets up the extension box for the panel.
     */
    private final void setUpExtensionBox() {
        final JLabel monitorLabel = new JLabel("Monitor by extension");
        adjustGridBagConstraints(0, 1, GridBagConstraints.RELATIVE, 0.0);
        myMainPanel.add(monitorLabel, myGBC);

        myExtensionDropdown = new JComboBox<>(
                new String[] { "All extensions", "Custom extension", "test", "docx", "pdf", "txt", "png", "jpg",
                        "jpeg", "gif", "mp3", "mp4", "wav", "avi", "mov", "csv" });
        adjustGridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1.0);
        myMainPanel.add(myExtensionDropdown, myGBC);
    }

    /**
     * Sets up the directory box for the panel.
     */
    private final void setUpDirectoryBox() {
        final JLabel directoryLabel = new JLabel("Directory to monitor");
        adjustGridBagConstraints(0, 2, GridBagConstraints.RELATIVE, 0.0);
        myMainPanel.add(directoryLabel, myGBC);

        myDirectoryField = new JTextField(0); // Increase the size of the text field
        adjustGridBagConstraints(1, 2, GridBagConstraints.REMAINDER, 1.0);
        myGBC.fill = GridBagConstraints.HORIZONTAL; // Make the text field fill the available space
        myMainPanel.add(myDirectoryField, myGBC);
    }

    /**
     * Sets up the directory buttons for the panel.
     */
    private final void setUpDirectoryButtons() {
        myStartButton = createModernButton("Start");
        myStopButton = createModernButton("Stop");
        myBrowseButton = createModernButton("Browse");
        
        // Create a panel to hold the browse, start, and stop buttons so they are equal
        // size.
        final JPanel buttonSet1 = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonSet1.add(myBrowseButton);
        buttonSet1.add(myStartButton);
        buttonSet1.add(myStopButton);
        // Disabling both buttons for until the user has a directory and extension to
        // monitor.
        myStopButton.setEnabled(false);
        myStartButton.setEnabled(false);

        // Make JPanel take up remainder of row space and fill horizontally
        myGBC.fill = GridBagConstraints.HORIZONTAL;
        adjustGridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1);
        myMainPanel.add(buttonSet1, myGBC);
    }

    /**
     * Sets up the database box for the panel. Returns a JPanel containing only the lower buttons
     * (write to db, query, reset)
     */
    private final JPanel setUpDatabaseBox() {
        myWriteDbButton = createModernButton("Write to database");
        myQueryButton = createModernButton("Query");
        myQueryButton.setEnabled(false);
        myResetButton = createModernButton("Reset");
        // adjustGridBagConstraints(0, 6, 1, 1);
        // myGBC.fill = GridBagConstraints.HORIZONTAL; // Reset fill

        // Create a panel to hold the write to database and query buttons so they are
        // equal size.
        final JPanel buttonSet2 = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonSet2.add(myWriteDbButton);
        buttonSet2.add(myQueryButton);
        // Create a panel to hold the buttonSet2 and the reset button
        final JPanel databasePanel = new JPanel(new GridBagLayout());
        //databasePanel.
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Force all horizontal room to be used as space for button
        
        databasePanel.add(buttonSet2, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Make the buttons take up entire row
        databasePanel.add(myResetButton, gbc);

        return databasePanel;
    }

    /**
     * Creates a "modern design" button with the given text.
     * @param text The text being passed in that will appear on the button.
     * @return The button with the given text in the modern design.
     */
    private final JButton createModernButton(String text) {
        final JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    /**
     * Gets the query popup label that will be used in the window.
     * @return The query popup label that will be used in the window.
     */
    public final JLabel getMyManualQueryLabel() {
        return myManualQueryLabel;
    }

    /**
     * Gets the event activity dropdown to be returned.
     * @return The event activity dropdown.
     */
    public final JComboBox<String> getEventActivityDropdown(){
        return myEventActivityDropdown;
    }

    /**
     * Gets the JTextField for the file name filter.
     * @return JTextField for file name filter.
     */
    public final JTextField getFileNameText(){
        return myFileNameText;
    }

    /**
     * Gets the file extension query textbox.
     * @return The file extension query textbox.
     */
    public final JTextField getFileExtensionText() {
        return myPathOrDateText;
    }

    /**
     * Gets the file extension filter button.
     * @return The file extension filter button.
     */
    public final JButton getFileExtensionFilterButton(){
        return myManualQueryButton;
    }

    /**
     * Gets the extension box.
     * @return The extension box.
     */
    public final JComboBox<String> getExtensionBox() {
        return myExtensionDropdown;
    }

    /**
     * Gets the query extension box.
     * @return The query extension box.
     */
    public final JComboBox<String> getQueryPopupSelection() {
        return myQuerySelectionDropdown;
    }

    /**
     * Gets the manual query dropdown.
     * @return The manual query dropdown.
     */
    public final JComboBox<String> getManualQueryComboBox() {
        return myManualQueryComboBox;
    }

    /**
     * Gets the directory textbox.
     * @return The directory textbox.
     */
    public final JTextField getMyDirectoryField() {
        return myDirectoryField;
    }

    /**
     * Gets the start button on the panel.
     * @return The start button on the panel.
     */
    public final JButton getStartButton() {
        return myStartButton;
    }

    /**
     * Gets the stop button.
     * @return The stop button.
     */
    public final JButton getStopButton() {
        return myStopButton;
    }

    /**
     * Gets the directory browse button.
     * @return The directory browsing button.
     */
    public final JButton getBrowseButton() {
        return myBrowseButton;
    }

    /**
     * Gets the reset button
     * @return The reset button.
     */
    public final JButton getResetButton() {
        return myResetButton;
    }

    /**
     * Gets the query button.
     * @return The query button.
     */
    public final JButton getQueryButton() {
        return myQueryButton;
    }

    /**
     * Gets the button that will reset the database.
     * @return The button that will reset the database.
     */
    public final JButton getDatabaseResetButton() {
        return myDatabaseResetButton;
    }

    /**
     * Gets the button that will return the user to the main window.
     * @return The button that will return the user to the main window.
     */
    public final JButton getReturnWindowButton(){
        return myReturnWindowButton;
    }

    /**
     * Gets the button that will export the database query list as a CSV.
     * @return The button that will export the database query list as a CSV.
     */
    public final JButton getCSVButton() {
        return myExportCSVButton;
    }

    /**
     * Gets the Panel that contains the write, query, and reset buttons
     * @return The Panel that contains the write, query, and reset buttons.
     */
    public final JPanel getQueryPanel() {
        return myQueryPanel;
    }

    /**
     * Gets the button to start the database.
     * @return The button to start the database.
     */
    public final JButton getMyWriteDBButton() {
        return myWriteDbButton;
    }

    /**
     * Gets the start button that has a picture.
     * @return The start button with an image.
     */
    public final JButton getMyImgStarButton() {
        return myImgStartButton;
    }

    /**
     * Gets the stop button that has a picture.
     * @return The stop button with an image.
     */
    public final JButton getMyImgStopButton() {
        return myImgStopButton;
    }

    /**
     * Gets the button to query the database that has a picture.
     * @return The button to query the database with an image.
     */
    public final JButton getMyImgDBButton() {
        return myImgDBButton;
    }

    /**
     * Gets the clear button that has a picture.
     * @return The clear button with an image.
     */
    public final JButton getMyImgClearButton() {
        return myImgClearButton;
    }

    /**
     * Helper constructor to clean up code above, adjusting gridbag with X, Y,
     * width, and weightx values.
     * 
     * @param theX       The X value to be adjusted for the gridbag.
     * @param theY       The Y value to be adjusted for the gridbag.
     * @param theWidth   The width value to be adjusted for the gridbag. MUST USE
     *                   GridBagConstraints ENUM.
     * @param theWeightx The weightx value to be adjusted for the gridbag.
     */
    private final void adjustGridBagConstraints(int theX, int theY, int theWidth, double theWeightx) {
        myGBC.gridx = theX;
        myGBC.gridy = theY;
        myGBC.gridwidth = theWidth;
        myGBC.weightx = theWeightx;
    }
}
