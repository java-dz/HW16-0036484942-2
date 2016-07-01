package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.AbstractShapePanel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.RectanglePanel;

/**
 * This class extends the {@link GeometricalObject} and represents a drawable,
 * parsable and serializable rectangle. It contains some useful methods, for
 * getting and setting foreground and background colors of the rectangle,
 * getting the bounding box of the rectangle, setting its endpoint to a
 * <tt>(x, y)</tt> coordinate and serializing the rectangle into a string that
 * can later be parsed to recreate the same rectangle.
 *
 * @author Mario Bobic
 */
public class Rectangle extends GeometricalObject {

	/** Counter of this class' instantiated objects. */
	private static int instanceCounter = 0;
	/** Instance index of this object. */
	private int instance;

	/** The x coordinate of the starting point. */
	private int x;
	/** The y coordinate of the starting point. */
	private int y;
	/** Width of the rectangle. */
	private int w;
	/** Height of the rectangle. */
	private int h;
	
	/** Outline color. */
	private Color lineColor;
	
	/**
	 * Constructs an instance of {@code Rectangle} with the specified arguments.
	 *
	 * @param x the x coordinate of the starting point
	 * @param y the y coordinate of the starting point
	 * @param w width of the rectangle
	 * @param h height of the rectangle
	 */
	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		synchronized (Rectangle.class) {
			instanceCounter++;
			instance = instanceCounter;
		}
	}
	
	/**
	 * Private constructor used for the shape creator lambda.
	 *
	 * @param x horizontal position of start point
	 * @param y vertical position of start point
	 */
	private Rectangle(int x, int y) {
		this(x, y, 0, 0);
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
		return Rectangle::new;
	}
	
	/**
	 * Parses the specified string <tt>s</tt> as a {@code Rectangle} object. The
	 * specified string must match serialization type of this object in order to
	 * be parsed.
	 * <p>
	 * Throws {@link IllegalArgumentException} if the string can not be parsed
	 * as a {@code Rectangle} object.
	 * 
	 * @param s string to be parsed
	 * @return a {@code Rectangle} object parsed from the specified string
	 * @throws NullPointerException if <tt>s</tt> is <tt>null</tt>
	 * @throws IllegalArgumentException if <tt>s</tt> can not be parsed
	 */
	public static Rectangle parse(String s) {
		if (!s.startsWith("RECTANGLE")) {
			throw new IllegalArgumentException("Rectangle must start with 'RECTANGLE'");
		}
		
		String[] arguments = s.split(" ");
		if (arguments.length != 8) {
			throw new IllegalArgumentException("Rectangle must contain 7 elements.");
		}

		int x = Integer.parseInt(arguments[1]);
		int y = Integer.parseInt(arguments[2]);
		int w = Integer.parseInt(arguments[3]);
		int h = Integer.parseInt(arguments[4]);
		
		int r = Integer.parseInt(arguments[5]);
		int g = Integer.parseInt(arguments[6]);
		int b = Integer.parseInt(arguments[7]);
		
		Rectangle rectangle = new Rectangle(x, y, w, h);
		rectangle.setForeground(new Color(r, g, b));
		return rectangle;
	}
	
	/**
	 * Serializes the specified rectangle <tt>r</tt> into a string that can later
	 * be parsed to recreate the same rectangle.
	 * 
	 * @param r rectangle to be serialized
	 * @return a serialized string of the specified rectangle
	 */
	public static String serialize(Rectangle r) {
		return String.format("RECTANGLE %d %d %d %d %d %d %d",
				r.x, r.y, r.w, r.h,
				r.lineColor.getRed(),
				r.lineColor.getGreen(),
				r.lineColor.getBlue());
	}

	@Override
	public void draw(Graphics2D area, int offsetX, int offsetY) {
		Color oldColor = area.getColor();
		
		int x = this.x;
		int y = this.y;
		int w = this.w;
		int h = this.h;
		
		if (w < 0) {
			x += w;
			w = -w;
		}
		
		if (h < 0) {
			y += h;
			h = -h;
		}
		
		area.setColor(lineColor);
		area.drawRect(x+offsetX, y+offsetY, w, h);
		
		area.setColor(oldColor);
	}

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
		this.lineColor = color;
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
	public java.awt.Rectangle getBoundingBox() {
		return new java.awt.Rectangle(x, y, w, h);
	}

	@Override
	public void setEndpoint(int x, int y) {
		w = x - this.x;
		h = y - this.y;
	}
	
	@Override
	public String serialize() {
		return serialize(this);
	}

	@Override
	public String getName() {
		return "Rectangle " + instance;
	}

	@Override
	public AbstractShapePanel getModificationPanel() {
		return new RectanglePanel(this);
	}

	/**
	 * Returns the x coordinate of the starting point.
	 *
	 * @return the x coordinate of the starting point
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of the starting point.
	 *
	 * @param x the new x coordinate of the starting point
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate of the starting point.
	 *
	 * @return the y coordinate of the starting point
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y coordinate of the starting point.
	 *
	 * @param y the new y coordinate of the starting point
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns the width of the rectangle.
	 *
	 * @return the width of the rectangle
	 */
	public int getW() {
		return w;
	}

	/**
	 * Sets the width of the rectangle.
	 *
	 * @param w the new width of the rectangle
	 */
	public void setW(int w) {
		this.w = w;
	}

	/**
	 * Returns the height of the rectangle.
	 *
	 * @return the height of the rectangle
	 */
	public int getH() {
		return h;
	}

	/**
	 * Sets the height of the rectangle.
	 *
	 * @param h the new height of the rectangle
	 */
	public void setH(int h) {
		this.h = h;
	}

}
