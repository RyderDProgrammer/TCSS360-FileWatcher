import java.awt.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;

public class FWPanel extends JPanel {
    // Combo box for selecting the extension to monitor.
    private JComboBox<String> extensionDropdown;
    // Text field for the directory to monitor.
    private JTextField directoryField;
    // Combo box for selecting the extension to query or write to the database.
    private JComboBox<String> queryExtensionDropdown;
    // Text field for the database to write to.
    private JTextField myDatabaseField;
    // Buttons for starting, stopping, and browsing for a directory to monitor.
    private JButton myWriteDbButton, myQueryButton, myClearButton, myBrowseButton, myStartButton, myStopButton;
    // Buttons for the image icons.
    private JButton myImgStartButton, myImgStopButton, myImgDBButton, myImgClearButton;
    // GridBagConstraint for the layout.
    private GridBagConstraints myGBC;
    // Main panel for the layout.
    private JPanel myMainPanel;

    /**
     * Constructor for the FWPanel. This will create the panel and set up the layout.
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
        setUpDatabaseBox();

        add(myMainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the buttons for the image icons.
     */
    private void createButtonBar() {
        ImageIcon startImageIcon = new ImageIcon("files/startWatching.png");
        ImageIcon stopImageIcon = new ImageIcon("files/stopWatching.png");
        ImageIcon dbImageIcon = new ImageIcon("files/startDB.png");
        ImageIcon clearImageIcon = new ImageIcon("files/clearData.png");

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
    private void setUpExtensionBox() {
        JLabel monitorLabel = new JLabel("Monitor by extension");
        adjustGridBagConstraints(0, 1, 1);
        myMainPanel.add(monitorLabel, myGBC);

        extensionDropdown = new JComboBox<>(
                new String[] { "Enter an extension", "All extensions", "DOCX", "PDF", "TXT", "PNG", "JPG", "JPEG",
                        "GIF", "MP3", "MP4", "WAV",
                        "AVI", "MOV", "CSV" });
        adjustGridBagConstraints(1, 1, GridBagConstraints.REMAINDER);
        myMainPanel.add(extensionDropdown, myGBC);
    }

    /**
     * Sets up the directory box for the panel.
     */
    private void setUpDirectoryBox() {
        JLabel directoryLabel = new JLabel("Directory to monitor");
        adjustGridBagConstraints(0, 2, 1);
        myMainPanel.add(directoryLabel, myGBC);

        directoryField = new JTextField(0); // Increase the size of the text field
        adjustGridBagConstraints(1, 2, GridBagConstraints.REMAINDER, 1.0);
        myGBC.fill = GridBagConstraints.HORIZONTAL; // Make the text field fill the available space
        myMainPanel.add(directoryField, myGBC);
    }

    /**
     * Sets up the directory buttons for the panel.
     */
    private void setUpDirectoryButtons() {
        myStartButton = createModernButton("Start");
        myStopButton = createModernButton("Stop");
        myBrowseButton = createModernButton("Browse");
        // Disabling both buttons for until the user has a directory and extension to
        // monitor.
        myStopButton.setEnabled(false);
        myStartButton.setEnabled(false);
        adjustGridBagConstraints(0, 3, 1, 1 / 3.0);
        myGBC.fill = GridBagConstraints.HORIZONTAL;
        myMainPanel.add(myBrowseButton, myGBC);
        adjustGridBagConstraints(1, 3, GridBagConstraints.RELATIVE, 1 / 3.0);
        myMainPanel.add(myStartButton, myGBC);
        adjustGridBagConstraints(3, 3, GridBagConstraints.REMAINDER, 1 / 3.0);
        myMainPanel.add(myStopButton, myGBC);
    }

    /**
     * Sets up the database box for the panel.
     */
    private void setUpDatabaseBox() {
        JLabel queryLabel = new JLabel("Query or Write by extension");
        adjustGridBagConstraints(0, 4, 1);
        myMainPanel.add(queryLabel, myGBC);

        queryExtensionDropdown = new JComboBox<>(new String[] { "txt", "log", "csv" });
        adjustGridBagConstraints(1, 4, GridBagConstraints.REMAINDER);
        myMainPanel.add(queryExtensionDropdown, myGBC);

        JLabel databaseLabel = new JLabel("Database");
        adjustGridBagConstraints(0, 5, 1);
        myMainPanel.add(databaseLabel, myGBC);

        myDatabaseField = new JTextField(30); // Increase the size of the text field
        adjustGridBagConstraints(1, 5, GridBagConstraints.REMAINDER, 1.0);
        myGBC.fill = GridBagConstraints.HORIZONTAL; // Make the text field fill the available space
        myMainPanel.add(myDatabaseField, myGBC);

        myWriteDbButton = createModernButton("Write to database");
        myQueryButton = createModernButton("Query");
        myClearButton = createModernButton("Clear");
        adjustGridBagConstraints(0, 6, 2, .5);
        myGBC.fill = GridBagConstraints.HORIZONTAL; // Reset fill
        myMainPanel.add(myWriteDbButton, myGBC);
        adjustGridBagConstraints(2, 6);
        myMainPanel.add(myQueryButton, myGBC);
        adjustGridBagConstraints(0, 7);
        myGBC.fill = GridBagConstraints.HORIZONTAL;
        myGBC.gridwidth = GridBagConstraints.REMAINDER;
        myMainPanel.add(myClearButton, myGBC);
    }

    /**
     * Creates a "modern design" button with the given text.
     * @param text The text being passed in that will appear on the button.
     * @return The button with the given text in the modern design.
     */
    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    /**
     * Gets the extension box.
     * @return The extension box.
     */
    public JComboBox<String> getExtensionBox() {
        return extensionDropdown;
    }

    /**
     * Gets the directory box.
     * @return The directory box.
     */
    public JTextField getDirectoryField() {
        return directoryField;
    }

    /**
     * Gets the start button on the panel.
     * @return The start button on the panel.
     */
    public JButton getStartButton() {
        return myStartButton;
    }

    /**
     * Gets the stop button.
     * @return The stop button.
     */
    public JButton getStopButton() {
        return myStopButton;
    }

    /**
     * Gets the directory browse button.
     * @return The directory browsing button.
     */
    public JButton getBrowseButton() {
        return myBrowseButton;
    }

    /**
     * Gets the clear button
     * @return The clear button.
     */
    public JButton getClearButton() {
        return myClearButton;
    }

    /**
     * Gets the button to start the database.
     * @return The button to start the database.
     */
    public JButton getMyWriteDBButton() {
        return myWriteDbButton;
    }

    /**
     * Gets the start button with an image
     * @return The start button with an image.
     */
    public JButton getMyImgStarButton() {
        return myImgStartButton;
    }

    /**
     * Gets the stop button with an image.
     * @return The stop button with an image.
     */
    public JButton getMyImgStopButton() {
        return myImgStopButton;
    }

    /**
     * Gets the button to query the database with an image.
     * @return The button to query the database with an image.
     */
    public JButton getMyImgDBButton() {
        return myImgDBButton;
    }

    /**
     * Gets the clear button with an image.
     * @return The clear button with an image.
     */
    public JButton getMyImgClearButton() {
        return myImgClearButton;
    }

    /**
     * Gets the database textfield.
     * @return The database textfield.
     */
    public JTextField getMyDatabaseField() {
        return myDatabaseField;
    }

    /**
     * Helper constructor to clean up code above, adjusting gridbag with X and Y values.
     * @param theX The X value to be adjusted for the gridbag.
     * @param theY The Y value to be adjusted for the gridbag.
     */
    private void adjustGridBagConstraints(int theX, int theY) {
        myGBC.gridx = theX;
        myGBC.gridy = theY;
    }

    /**
     * Helper constructor to clean up code above, adjusting gridbag with X, Y, and width values.
     * @param theX The X value to be adjusted for the gridbag.
     * @param theY The Y value to be adjusted for the gridbag.
     * @param theWidth The width value to be adjusted for the gridbag.
     */
    private void adjustGridBagConstraints(int theX, int theY, int theWidth) {
        myGBC.gridx = theX;
        myGBC.gridy = theY;
        myGBC.gridwidth = theWidth;
    }

    /**
     * Helper constructor to clean up code above, adjusting gridbag with X, Y, width, and weightx values.
     * @param theX The X value to be adjusted for the gridbag.
     * @param theY The Y value to be adjusted for the gridbag.
     * @param theWidth The width value to be adjusted for the gridbag.
     * @param theWeightx The weightx value to be adjusted for the gridbag.
     */
    private void adjustGridBagConstraints(int theX, int theY, int theWidth, double theWeightx) {
        myGBC.gridx = theX;
        myGBC.gridy = theY;
        myGBC.gridwidth = theWidth;
        myGBC.weightx = theWeightx;
    }
}
