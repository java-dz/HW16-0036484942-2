package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.AbstractShapePanel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.CirclePanel;

/**
 * This class extends the {@link GeometricalObject} and represents a drawable,
 * parsable and serializable circle. It contains some useful methods,
 * for getting and setting foreground and background colors of the circle,
 * getting the bounding box of the circle, setting its endpoint to a
 * <tt>(x, y)</tt> coordinate and serializing the circle into a string that can
 * later be parsed to recreate the same circle.
 *
 * @author Mario Bobic
 */
public class Circle extends GeometricalObject {
	
	/** Counter of this class' instantiated objects. */
	protected static int instanceCounter = 0;
	/** Instance index of this object. */
	private int instance;
	
	/** Horizontal coordinate of the circle center. */
	protected int x;
	/** Vertical coordinate of the circle center. */
	protected int y;
	/** Circle radius. */
	protected int radius;

	/** Circle outline color. */
	private Color lineColor;
	
	/**
	 * Constructs an instance of {@code Circle} with the specified arguments.
	 *
	 * @param x horizontal coordinate of the circle center
	 * @param y vertical coordinate of the circle center
	 * @param radius circle radius
	 */
	public Circle(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		
		synchronized (Circle.class) {
			instanceCounter++;
			instance = instanceCounter;
		}
	}
	
	/**
	 * Private constructor used for the shape creator lambda.
	 *
	 * @param x horizontal coordinate of the circle center
	 * @param y vertical coordinate of the circle center
	 */
	private Circle(int x, int y) {
		this(x, y, 0);
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
		return Circle::new;
	}
	
	/**
	 * Parses the specified string <tt>s</tt> as a {@code Circle} object. The
	 * specified string must match serialization type of this object in order to
	 * be parsed.
	 * <p>
	 * Throws {@link IllegalArgumentException} if the string can not be parsed
	 * as a {@code Circle} object.
	 * 
	 * @param s string to be parsed
	 * @return a {@code Circle} object parsed from the specified string
	 * @throws NullPointerException if <tt>s</tt> is <tt>null</tt>
	 * @throws IllegalArgumentException if <tt>s</tt> can not be parsed
	 */
	public static Circle parse(String s) {
		if (!s.startsWith("CIRCLE")) {
			throw new IllegalArgumentException("Circle must start with 'CIRCLE'");
		}
		
		String[] arguments = s.split(" ");
		if (arguments.length != 7) {
			throw new IllegalArgumentException("Circle must contain 6 elements.");
		}

		return parseWithoutCheck(s);
	}
	
	/**
	 * Parses the specified string <tt>s</tt> without making a validation that
	 * the string contains a valid keyword and the valid number of elements.
	 * 
	 * @param s string to be parsed
	 * @return a {@code Circle} object parsed from the specified string
	 */
	protected static Circle parseWithoutCheck(String s) {
		String[] arguments = s.split(" ");
		
		int x = Integer.parseInt(arguments[1]);
		int y = Integer.parseInt(arguments[2]);
		int radius = Integer.parseInt(arguments[3]);
		
		int r = Integer.parseInt(arguments[4]);
		int g = Integer.parseInt(arguments[5]);
		int b = Integer.parseInt(arguments[6]);
		
		Circle circle = new Circle(x, y, radius);
		circle.setForeground(new Color(r, g, b));
		return circle;
	}
	
	/**
	 * Serializes the specified circle <tt>c</tt> into a string that can later
	 * be parsed to recreate the same circle.
	 * 
	 * @param c circle to be serialized
	 * @return a serialized string of the specified circle
	 */
	public static String serialize(Circle c) {
		return String.format("CIRCLE %d %d %d %d %d %d",
				c.x, c.y, c.radius,
				c.lineColor.getRed(),
				c.lineColor.getGreen(),
				c.lineColor.getBlue());
	}

	@Override
	public void draw(Graphics2D area, int offsetX, int offsetY) {
		Color savedColor = area.getColor();
		
		area.setColor(lineColor);
		area.drawOval(x-radius+offsetX, y-radius+offsetY, 2*radius, 2*radius);
		
		area.setColor(savedColor);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns the line color of this circle.
	 */
	@Override
	public Color getForeground() {
		return lineColor;
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
		lineColor = Objects.requireNonNull(color);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Has no effect since this object does not own a background color.
	 */
	@Override
	public void setBackground(Color color) {
	}

	/**
	 * Gets the bounding box.
	 *
	 * @return the bounding box
	 */
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x-radius, y-radius, 2*radius, 2*radius);
	}

	@Override
	public void setEndpoint(int x, int y) {
		radius = (int) Math.sqrt((this.x - x)*(this.x - x) + (this.y - y)*(this.y - y));
	}
	
	@Override
	public String serialize() {
		return serialize(this);
	}

	@Override
	public String getName() {
		return "Circle " + instance;
	}

	@Override
	public AbstractShapePanel getModificationPanel() {
		return new CirclePanel(this);
	}

	/**
	 * Returns the horizontal coordinate of the circle center.
	 *
	 * @return the horizontal coordinate of the circle center
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the horizontal coordinate of the circle center.
	 *
	 * @param x the new horizontal coordinate of the circle center
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the vertical coordinate of the circle center.
	 *
	 * @return the vertical coordinate of the circle center
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the vertical coordinate of the circle center.
	 *
	 * @param y the new vertical coordinate of the circle center
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns the circle radius.
	 *
	 * @return the circle radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the circle radius.
	 *
	 * @param radius the new circle radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

}
