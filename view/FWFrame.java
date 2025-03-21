
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class is used to create the frame for the File Watcher GUI.
 */
public final class FWFrame extends JFrame {
    /** A ToolKit. */
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();
    /** The Dimension of the screen. */
    private static final Dimension SCREEN_SIZE = KIT.getScreenSize();
    /** The width of the screen. */
    private static final int SCREEN_WIDTH = SCREEN_SIZE.width;
    /** The height of the screen. */
    private static final int SCREEN_HEIGHT = SCREEN_SIZE.height;
    /**
     * A Factor for scaling the size of the GUI relative to the current screen size.
     */
    private static final double VERTICAL_SCALE = 0.7;
    private static final double HORIZONTAL_SCALE = 0.3;

    /**
     * Sets up the frame outline for the main window.
     * @return The frame for the main window.
     */
    public final JFrame frameOutline() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Failed to set look and feel. Reverting to default swing look.");
        }

        // Setting up the frame and adjusting it to be 70% of the screens dimensions.
        final JFrame fileFrame = new JFrame("TCSS 360 - File Watcher");
        fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the icon for the frame.
        final ImageIcon icon = new ImageIcon(getClass().getResource("/appIcon.png"));
        fileFrame.setIconImage(icon.getImage());

        fileFrame.setSize((int) (SCREEN_WIDTH * HORIZONTAL_SCALE), (int) (SCREEN_HEIGHT * VERTICAL_SCALE));

        // Centering the frame on program start.
        fileFrame.setLocationRelativeTo(null);

        return fileFrame;
    }

    /** 
     * Helper method for the query window popup to be resized wider than tall.
     * @param theHorizontal The horizontal dimension.
     * @param theVertical The vertical dimension.
     */
    public final void queryFrameSize(final double theHorizontal, final double theVertical){
        final double localHeight = SCREEN_HEIGHT;
        final double localWidth = SCREEN_WIDTH;
        
        this.setSize((int) (localHeight * theHorizontal), (int) (localWidth * theVertical));
    }

}
