package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.AbstractShapePanel;

/**
 * This class represents a drawable, parsable and serializable geometric shape.
 * It contains some useful methods, for getting and setting foreground and
 * background colors of the shape, getting the bounding box of the shape,
 * setting its endpoint to a <tt>(x, y)</tt> coordinate and serializing the
 * shape into a string that can later be parsed to recreate the same shape.
 * <p>
 * All implementing classes must also provide implementation for getting an
 * instance of class derived from {@link AbstractShapePanel}, a panel which
 * contains shape's fields in order to alter the shape.
 *
 * @author Mario Bobic
 */
public abstract class GeometricalObject {

    /**
     * Draws the current shape to the specified <tt>area</tt> considering the
     * specified offset <tt>offsetX</tt> and <tt>offsetY</tt>.
     *
     * @param area for drawing of object
     * @param offsetX horizontal offset
     * @param offsetY vertical offset
     */
    public abstract void draw(Graphics2D area, int offsetX, int offsetY);

    /**
     * Returns the foreground color of this shape.
     * <p>
     * A foreground color is most commonly an outline color.
     *
     * @return the foreground color of this shape
     */
    public abstract Color getForeground();

    /**
     * Returns the background color of this shape.
     * <p>
     * A background color is most commonly the filling color.
     *
     * @return the background color of this shape
     */
    public abstract Color getBackground();

    /**
     * Sets the foreground color of this shape to the specified <tt>color</tt>.
     *
     * @param color color to be set
     */
    public abstract void setForeground(Color color);

    /**
     * Sets the background color of this shape to the specified <tt>color</tt>.
     *
     * @param color color to be set
     */
    public abstract void setBackground(Color color);

    /**
     * Calculates the bounding box of the shape (the minimal box that
     * encapsulates the whole shape).
     * <p>
     * Lets assume that the shape is just a line: (10, 100) to (100, 20). The
     * bounding box is a box whose top-left corner is in (10, 20); it has width
     * 100-10=90 and height 100-20=80. In this case the following rectangle is
     * returned:
     *
     * <pre>
     * new Rectangle(10, 20, 90, 80);
     * </pre>
     *
     * @return the bounding box of the shape
     */
    public abstract Rectangle getBoundingBox();

    /**
     * Sets the endpoint of this shape. This method is called while the shape is
     * being drawn across the canvas and is used for preview purposes.
     *
     * @param x x coordinate of the endpoint
     * @param y y coordinate of the endpoint
     */
    public abstract void setEndpoint(int x, int y);

    /**
     * Serializes this shape into a String that is ready to be parsed and
     * re-serialized again to construct the same shape.
     *
     * @return a serialized string of this shape
     */
    public abstract String serialize();

    /**
     * Returns the name of this shape, containing shape's identifier. For
     * example, if this shape is the second instance of its own class, it's name
     * would be:
     *
     * <pre>
     * ShapeName 2
     * </pre>
     *
     * @return the name of this shape containing its identifier
     */
    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns an instance of class derived from {@link AbstractShapePanel}, a
     * panel which contains shape's fields in order to alter the shape.
     *
     * @return a modification panel for altering the shape
     */
    public abstract AbstractShapePanel getModificationPanel();

}
