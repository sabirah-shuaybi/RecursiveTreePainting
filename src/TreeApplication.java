import javax.swing.JFrame;

/**
 * Main application for starting up the Tree Painting GUI.
 *
 * Command line usage:
 *
 * To create a recursive tree painting:
 *     java -cp bin TreeApplication
 *
 * @author Sabirah Shuaybi (Modified)
 **/
public class TreeApplication {
    public static final String INSTRUCTIONS_TEXT = "Click, drag, and release to paint a tree!";

    //Large frame dimensions for a larger window at start of program
    public static final int FRAME_WIDTH = 700;
    public static final int FRAME_HEIGHT = 900;

    /**
     * Create a JFrame that holds the TreePaintings.
     **/
    public static void main(String[] args)
    {
        JFrame guiFrame;

        // create a new JFrame to hold a single TreePainting
        guiFrame = new JFrame("A Single Tree Painting");

        // set size
        guiFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // create a TreePanel and add it
        guiFrame.add(new SingleTreePanel(INSTRUCTIONS_TEXT));

        // exit normally on closing the window
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // show frame
        guiFrame.setVisible(true);
    }
}

