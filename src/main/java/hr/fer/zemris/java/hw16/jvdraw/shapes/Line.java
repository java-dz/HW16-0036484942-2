package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.AbstractShapePanel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.LinePanel;

/**
 * This class extends the {@link GeometricalObject} and represents a drawable,
 * parsable and serializable line. It contains some useful methods, for getting
 * and setting foreground and background colors of the line, getting the
 * bounding box of the line, setting its endpoint to a <tt>(x, y)</tt>
 * coordinate and serializing the line into a string that can later be parsed to
 * recreate the same line.
 *
 * @author Mario Bobic
 */
public class Line extends GeometricalObject {

	/** Counter of this class' instantiated objects. */
	private static int instanceCounter = 0;
	/** Instance index of this object. */
	private int instance;

	/** Horizontal position of start point. */
	private int x0;
	/** Vertical position of start point. */
	private int y0;
	/** Horizontal position of end point. */
	private int x1;
	/** Vertical position of end point. */
	private int y1;
	
	/** Line color. */
	private Color color;
	
	/**
	 * Constructs an instance of {@code Line} with the specified arguments.
	 *
	 * @param x0 horizontal position of start point
	 * @param y0 vertical position of start point
	 * @param x1 horizontal position of end point
	 * @param y1 vertical position of end point
	 */
	public Line(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		
		synchronized (Line.class) {
			instanceCounter++;
			instance = instanceCounter;
		}
	}
	
	/**
	 * Private constructor used for the shape creator lambda.
	 *
	 * @param x horizontal position of start and end point
	 * @param y vertical position of start and end point
	 */
	private Line(int x, int y) {
		this(x, y, x, y);
	}
	
	/**
	 * Resets the instance counter of this class.
	 */
	public static void resetCounter() {
		instanceCounter = 0;
	}
	
	/**
	 * Returns a {@link ShapeCreator} object for this class.
	 * 
	 * @return a {@link ShapeCreator} object for this class
	 */
	public static ShapeCreator getShapeCreator() {
		return Line::new;
	}
	
	/**
	 * Parses the specified string <tt>s</tt> as a {@code Line} object. The
	 * specified string must match serialization type of this object in order to
	 * be parsed.
	 * <p>
	 * Throws {@link IllegalArgumentException} if the string can not be parsed
	 * as a {@code Line} object.
	 * 
	 * @param s string to be parsed
	 * @return a {@code Line} object parsed from the specified string
	 * @throws NullPointerException if <tt>s</tt> is <tt>null</tt>
	 * @throws IllegalArgumentException if <tt>s</tt> can not be parsed
	 */
	public static Line parse(String s) {
		if (!s.startsWith("LINE")) {
			throw new IllegalArgumentException("Line must start with 'LINE'");
		}
		
		String[] arguments = s.split(" ");
		if (arguments.length != 8) {
			throw new IllegalArgumentException("Line must contain 7 elements.");
		}

		int x0 = Integer.parseInt(arguments[1]);
		int y0 = Integer.parseInt(arguments[2]);
		int x1 = Integer.parseInt(arguments[3]);
		int y1 = Integer.parseInt(arguments[4]);
		
		int r = Integer.parseInt(arguments[5]);
		int g = Integer.parseInt(arguments[6]);
		int b = Integer.parseInt(arguments[7]);
		
		Line line = new Line(x0, y0, x1, y1);
		line.setForeground(new Color(r, g, b));
		return line;
	}
	
	/**
	 * Serializes the specified line <tt>l</tt> into a string that can later
	 * be parsed to recreate the same line.
	 * 
	 * @param l line to be serialized
	 * @return a serialized string of the specified line
	 */
	public static String serialize(Line l) {
		return String.format("LINE %d %d %d %d %d %d %d",
				l.x0, l.y0, l.x1, l.y1,
				l.color.getRed(),
				l.color.getGreen(),
				l.color.getBlue());
	}

	@Override
	public void draw(Graphics2D area, int offsetX, int offsetY) {
		Color oldColor = area.getColor();
		
		area.setColor(color);
		area.drawLine(x0+offsetX, y0+offsetY, x1+offsetX, y1+offsetY);
		
		area.setColor(oldColor);
	}

	@Override
	public Color getForeground() {
		return color;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns <tt>null</tt> since this object does not own a background color.
	 */
	@Override
	public Color getBackground() {
		return null;
	}
	
	@Override
	public void setForeground(Color color) {
		this.color = color;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Has no effect since this object does not own a background color.
	 */
	@Override
	public void setBackground(Color color) {
	}

	@Override
	public Rectangle getBoundingBox() {
		int startX = Math.min(x0, x1);
		int endX = Math.max(x0, x1);
		
		int startY = Math.min(y0, y1);
		int endY = Math.max(y0, y1);

		return new Rectangle(startX, startY, endX-startX, endY-startY);
	}

	@Override
	public void setEndpoint(int x, int y) {
		x1 = x;
		y1 = y;
	}
	
	@Override
	public String serialize() {
		return serialize(this);
	}

	@Override
	public String getName() {
		return "Line " + instance;
	}

	@Override
	public AbstractShapePanel getModificationPanel() {
		return new LinePanel(this);
	}

	/**
	 * Returns the horizontal position of start point.
	 *
	 * @return the horizontal position of start point
	 */
	public int getX0() {
		return x0;
	}

	/**
	 * Sets the horizontal position of start point.
	 *
	 * @param x0 the new horizontal position of start point
	 */
	public void setX0(int x0) {
		this.x0 = x0;
	}

	/**
	 * Returns the vertical position of start point.
	 *
	 * @return the vertical position of start point
	 */
	public int getY0() {
		return y0;
	}

	/**
	 * Sets the vertical position of start point.
	 *
	 * @param y0 the new vertical position of start point
	 */
	public void setY0(int y0) {
		this.y0 = y0;
	}

	/**
	 * Returns the horizontal position of end point.
	 *
	 * @return the horizontal position of end point
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * Sets the horizontal position of end point.
	 *
	 * @param x1 the new horizontal position of end point
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}

	/**
	 * Returns the vertical position of end point.
	 *
	 * @return the vertical position of end point
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * Sets the vertical position of end point.
	 *
	 * @param y1 the new vertical position of end point
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}

}
