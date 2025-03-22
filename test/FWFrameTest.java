import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class FWFrameTest {

    private FWFrame fwFrame;
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();
    private static final Dimension SCREEN_SIZE = KIT.getScreenSize();
    private static final int SCREEN_WIDTH = SCREEN_SIZE.width;
    private static final int SCREEN_HEIGHT = SCREEN_SIZE.height;

    @Before
    public void setUp() {
        fwFrame = new FWFrame();
    }

    @Test
    public void testFrameOutline() {
        JFrame frame = fwFrame.frameOutline();
        assertNotNull(frame);
        assertEquals("TCSS 360 - File Watcher", frame.getTitle());
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
    
        // Check if the frame size is set correctly
        int expectedWidth = (int) (SCREEN_WIDTH * 0.3);
        int expectedHeight = (int) (SCREEN_HEIGHT * 0.7);
        assertEquals(expectedWidth, frame.getWidth());
        assertEquals(expectedHeight, frame.getHeight());
    
    }
    

    @Test
    public void testQueryFrameSize() {
        double testHorizontalScale = 0.5;
        double testVerticalScale = 0.5;

        fwFrame.queryFrameSize(testHorizontalScale, testVerticalScale);

        int expectedWidth = (int) (SCREEN_HEIGHT * testHorizontalScale);
        int expectedHeight = (int) (SCREEN_WIDTH * testVerticalScale);

        assertEquals(expectedWidth, fwFrame.getWidth());
        assertEquals(expectedHeight, fwFrame.getHeight());
    }
}
