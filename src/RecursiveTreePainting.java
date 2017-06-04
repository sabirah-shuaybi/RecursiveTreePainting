import java.awt.Color;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;


/**
 *@author: Sabirah Shuaybi
 */
public class RecursiveTreePainting extends JComponent implements MouseListener {

    /** Colors for trunk, branches and leaves **/
    private static final Color LIGHT_BROWN = new Color(105, 72, 33);
    private static final Color GREEN = new Color(28, 127, 25);
    private static final Color ORANGE = new Color(169, 79, 13);
    private static final Color RED = new Color(124, 26, 12);
    private static final Color YELLOW = new Color(158, 162, 19);

    /** Number of generations to create branches (for coarser/finer detail) **/
    private static final int NUM_GENERATIONS = 6;

    /** Number of children for each branch (for sparser/fluffier trees) **/
    private static final int NUM_CHILDREN = 3;

    /** Golden ratio (to aesthetically paint child branches) **/
    private static final double GOLDEN_RATIO = 1.618;

    /** Maximum branching angle of children from a parent branch **/
    private static final double MAX_BRANCHING_ANGLE = .5*Math.PI;

    /** Diameter of leaf **/
    private static final int LEAF_DIAM = 5;

    /** Number of copies of the 1st generation branch (for denser trees) **/
    private static final int TRUNK_TWINS = 5;

    /** Points to designate the start and end of trunk **/
    private Point2D.Double trunkStart;
    private Point2D.Double trunkEnd;

    private boolean mouseReleased;

    public RecursiveTreePainting() {

        addMouseListener(this);

        //For a dynamic window that accommodates changes in dimension
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {

                //Call repaint every time window size increases or decreases
                    //Repaint will in turn call paintBackground
                        //So, a new background will be painted with the new dimensions
                repaint();
            }
        });
    }

    /**
     * Method for drawing on this component.
     * Overrides the paint method specified in JComponent (parent).
     * Calls Paints background and if mouse released, paints tree.
     *
     * @param g: The graphics object to draw on
     */
    public void paintComponent(Graphics g) {
        paintBackground(g);

        //Only paint tree if user has released mouse AND
                //if the press point is different from release point because
                    //an angle cannot be computed if the points are the exact same
        if (mouseReleased && !trunkStart.equals(trunkEnd)) {
            paintTrunk(g);
        }
    }

    /**
     * Method for drawing the background of the tree painting.
     * Draws a black backdrop whose dimensions depend on the component's dimensions
     *
     * @param g: The graphics object to draw on
     */
    private void paintBackground(Graphics g) {
        //Black backdrop fills the entire panel
        g.setColor(Color.BLACK);

        //Create dynamic dimensions for backdrop that expand/shrink if window size expands/shrinks
        g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
    }

    /**
     * Draws the main trunk from user press point to release point.
     *
     * @param g: The graphics object to draw on
     */
    private void paintTrunk(Graphics g) {
        //Type cast g into a 2D Graphics object
        Graphics2D g2d = (Graphics2D) g;

        //Start the recursive branch drawing with trunk values passed in (since trunk = parent branch)
        drawBranch(g2d, trunkStart, trunkEnd, NUM_GENERATIONS);
    }

    /**
     * A recursive method that draws branches from a start point to an end point.
     * Then it makes the end point into the new start point, computes a new end point,
     * decrements generation calls itself again and again until generation is zero
     * at which point it calls paintLeaf and ends the branch drawings.
     *
     * @param g2d: The 2D graphics object to draw on
     * @param start: Start point of the branch to be drawn
     * @param end: End point of the branch to be drawn
     * @param generation;
     */
    private void drawBranch(Graphics2D g2d, Point2D.Double start, Point2D.Double end, int generation) {

        //Passing in generation for "width" parameter because width of branch depends
            //on its generation. The lower the generation, the smaller the width.
        paintLine(g2d, start, end, generation);

        //If generation is zero, paint leaf and exit method
        if (generation == 0) {
            paintLeaf(g2d, end);
            return;
        }

        //Each branch generation itself will have a several children branches (compounding recursion)
        int numberOfChildren = getNumChildren(generation);
        //Keep recursively drawing a branch with new parameters
        for(int i=0; i<numberOfChildren; i++) {
            drawBranch(g2d, end, getNewEndPoint(start, end), generation-1);
        }
    }

    /**
     * Paints a line of a given thickness and a given color
     * from a start point to an end point.
     *
     * @param g2d: The 2D graphics object to draw on
     * @param start: The start point of branch
     * @param end: The end point of branch
     * @param width; The thickness of the branch
     */
    private void paintLine(Graphics2D g2d, Point2D.Double start, Point2D.Double end, int width) {
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(LIGHT_BROWN);
        g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
    }

    /**
     * Returns the total number of children a branch will have.
     * This value depends on the generation of the parent branch.
     * The 1st generation (the branches directly after the trunk)
     * will have a certain number of trunk twins (constant)
     * and thus, the number of children depends on the generation.
     *
     * @param generation: the generation of the parent branch
     * @return the total number of branches to draw
     */
    private int getNumChildren(int generation) {
        if (generation == NUM_GENERATIONS)
            return NUM_CHILDREN * TRUNK_TWINS;
        else
            return NUM_CHILDREN;
    }

    /**
     * Computes a valid end point for a branch.
     * Only uses those randomly generated angle values that are within the defined interval.
     * If this angle is not found, uses recursion to call itself again in hopes of a better angle next time
     * This ensures a more realistic depiction of a tree, with branches at natural angles.
     *
     * @param parentStart:
     * @param parentEnd:
     * @return a branch end point that is angled within the defined interval
     */
    private Point2D.Double getNewEndPoint(Point2D.Double parentStart, Point2D.Double parentEnd) {
        double parentLength = parentStart.distance(parentEnd);
        double branchLength = parentLength * (1/GOLDEN_RATIO);

        //Compute a potential end point
        Point2D.Double branchEnd = computeEndpoint(parentEnd, branchLength, getRandomAngle());

        //Consolidate the start/end points of parent branch into a line object
        Line2D.Double parentLine = new Line2D.Double(parentStart, parentEnd);

        //Create a line for a branch from end of trunk point to end of branch point
        Line2D.Double branchLine = new Line2D.Double(parentEnd, branchEnd);

        //Potential angle
        double angle = getAbsoluteAngleBetween(parentLine, branchLine);

        //Return end point only if the angle is within the desired range
        if (angle <= MAX_BRANCHING_ANGLE) {  //Base case for recursion

            //This end point will now serve as the starting point for a child branch
            return branchEnd;
        }
        //Keep trying until an angle that is <= MAX_BRANCHING_ANGLE is found
        else return getNewEndPoint(parentStart, parentEnd);
    }

    /**
     * Method for painting leaf centered on top of branch.
     *
     * @param g: The graphics object to draw on
     * @param branchEndPt: Point at which to draw leaf (AKA branch end point)
     */
    protected void paintLeaf(Graphics g, Point2D.Double branchEndPt) {
        g.setColor(randomLeafColor());

        //To center leaf on top of the branch branch
        double leafCenterX = branchEndPt.getX() - LEAF_DIAM / 2;
        double leafCenterY = branchEndPt.getY() - LEAF_DIAM / 2;

        //Draw the leaf
        g.fillOval((int) leafCenterX, (int) leafCenterY, LEAF_DIAM, LEAF_DIAM);
    }

    /**
     * @return double: a random angle (radians) between 0 and 2 * PI.
     */
    private double getRandomAngle() {
        return new Random().nextDouble() * 2 * Math.PI;
    }

    /**
     * Compute the point that is length away from point p at the specified angle.
     * Uses cosine to get the new x coordinate, sine to get the new y coordinate.
     *
     * @param p:      a (starting) point
     * @param length: the distance away from point p
     * @param angle:  specified angle
     * @return point that is length away from p at given angle
     */
    private Point2D.Double computeEndpoint(Point2D p, double length, double angle) {
        return new Point2D.Double(p.getX() + length * Math.cos(angle), // x is cos
                p.getY() + length * Math.sin(angle)); // y is sin
    }

    /**
     * Positions/Shifts line to the origin (of a typical x,y coordinate graph),
     * thus rendering it a vector.
     *
     * @param line: the line to be converted to a vector
     * @return vector point
     */
    private Point2D.Double toVector(Line2D.Double line) {
        //Translating line to the origin (shifting while maintaining the slope of line)
        return new Point2D.Double(line.getX2() - line.getX1(), line.getY2() - line.getY1());
    }

    /**
     * Computes the absolute angle (no negatives)between two lines
     * via vector formula.
     *
     * @param line1: the first line
     * @param line2: the second line
     * @return the absolute value of the angle between line1 and line2
     */
    private double getAbsoluteAngleBetween(Line2D.Double line1, Line2D.Double line2) {
        //Converting two lines into two vectors
        //These vectors have been positioned at the origin
        //(to calculate the absolute angle between them)
        Point2D.Double v1 = toVector(line1);
        Point2D.Double v2 = toVector(line2);

        double y1 = v1.getY();
        double x1 = v1.getX();
        double x2 = v2.getX();
        double y2 = v2.getY();

        //Formula for calculating the angle between two vectors
        double numer = (x1 * x2 + y1 * y2);
        double denom = (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
        return Math.acos(numer / denom);
    }

    /**
     * Method for randomly selecting from the constant leaf colors
     *
     * @return a leaf color
     */
    private Color randomLeafColor() {
        Random r = new Random();

        //4 random integer values
        int n = r.nextInt(4);

        //Return a color based on the random int generated
        switch(n) {
            case 0: return ORANGE;
            case 1: return RED;
            case 2: return YELLOW;
        }
        return GREEN;
    }

    /**
     * Methods below are required by mouseListener interface
     *
     * @param e: The user mouse event
     */

    public void mousePressed(MouseEvent e) {
        //Capture the point at which mouse was pressed
        trunkStart = new Point2D.Double(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        mouseReleased = true;

        //Capture the point at which mouse was released
        trunkEnd = new Point2D.Double(e.getX(), e.getY());

        //Invoke the paintComponent method via repaint()
        //This will re-paint the screen with a new tree
        //each time mouse is released or
        //when window dimensions are changed
        repaint();
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }
}
