import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * A SingleTreePanel displays instructions (top) and a RecursiveTreePainting.
 *
 *@author Sabirah Shuaybi (modified)
 **/
public class SingleTreePanel extends JPanel {

    private JLabel instructions;

    /**
     * Constructor uses a border layout. The GUI is set up by
     * putting a JLabel with instructions in the NORTH region,
     * and a new TreePainting object in the CENTER region.
     *
     * @param instructionsText: String of instructions to display
     **/
    public SingleTreePanel(String instructionsText) {
        // use a BorderLayout
        super(new BorderLayout());

        // create instructions and put at the top
        instructions = new JLabel(instructionsText);
        instructions.setHorizontalAlignment(JLabel.CENTER);
        add(instructions, BorderLayout.NORTH);

        //Use the rest of the panel for a recursive TreePainting object
        add(new RecursiveTreePainting(), BorderLayout.CENTER);
    }
}