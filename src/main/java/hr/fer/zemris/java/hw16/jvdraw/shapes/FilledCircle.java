package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.AbstractShapePanel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.FilledCirclePanel;

/**
 * This class extends the {@link Circle} and represents a drawable,
 * parsable and serializable filled circle. It contains some useful methods,
 * for getting and setting foreground and background colors of the filled circle,
 * getting the bounding box of the filled circle, setting its endpoint to a
 * <tt>(x, y)</tt> coordinate and serializing the filled circle into a string that can
 * later be parsed to recreate the same filled circle.
 *
 * @author Mario Bobic
 */
public class FilledCircle extends Circle {

	/** Color of the filling. */
	private Color fillColor;
	
	/**
	 * Constructs an instance of {@code FilledCircle} with the specified arguments.
	 *
	 * @param x horizontal coordinate of the circle center
	 * @param y vertical coordinate of the circle center
	 * @param radius circle radius
	 */
	public FilledCircle(int x, int y, int radius) {
		super(x, y, radius);
	}
	
	/**
	 * Constructs an instance of {@code FilledCircle} from the specified <tt>circle</tt>.
	 *
	 * @param circle circle to be &quot;transformed&quot; into the filled circle
	 */
	public FilledCircle(Circle circle) {
		this(circle.x, circle.y, circle.radius);
	}
	
	/**
	 * Private constructor used for the shape creator lambda.
	 *
	 * @param x horizontal coordinate of the circle center
	 * @param y vertical coordinate of the circle center
	 */
	private FilledCircle(int x, int y) {
		super(x, y, 0);
	}
	
	/**
	 * Returns a {@link ShapeCreator} object for this class.
	 * 
	 * @return a {@link ShapeCreator} object for this class
	 */
	public static ShapeCreator getShapeCreator() {
		return FilledCircle::new;
	}
	
	/**
	 * Parses the specified string <tt>s</tt> as a {@code FilledCircle} object. The
	 * specified string must match serialization type of this object in order to
	 * be parsed.
	 * <p>
	 * Throws {@link IllegalArgumentException} if the string can not be parsed
	 * as a {@code FilledCircle} object.
	 * 
	 * @param s string to be parsed
	 * @return a {@code FilledCircle} object parsed from the specified string
	 * @throws NullPointerException if <tt>s</tt> is <tt>null</tt>
	 * @throws IllegalArgumentException if <tt>s</tt> can not be parsed
	 */
	public static FilledCircle parse(String s) {
		if (!s.startsWith("FCIRCLE")) {
			throw new IllegalArgumentException("Filled circle must start with 'FCIRCLE'");
		}
		
		String[] arguments = s.split(" ");
		if (arguments.length != 10) {
			throw new IllegalArgumentException("Filled circle must contain 9 elements.");
		}

		instanceCounter--; // this is a "transformation"
		FilledCircle filledCircle = new FilledCircle(parseWithoutCheck(s));
		
		int r = Integer.parseInt(arguments[7]);
		int g = Integer.parseInt(arguments[8]);
		int b = Integer.parseInt(arguments[9]);
		
		filledCircle.setBackground(new Color(r, g, b));
		return filledCircle;
	}
	
	/**
	 * Serializes the specified filled circle <tt>fc</tt> into a string that can
	 * later be parsed to recreate the same filled circle.
	 * 
	 * @param fc filled circle to be serialized
	 * @return a serialized string of the specified filled circle
	 */
	public static String serialize(FilledCircle fc) {
		return "F" + Circle.serialize(fc) +
			String.format(" %d %d %d",
				fc.fillColor.getRed(),
				fc.fillColor.getGreen(),
				fc.fillColor.getBlue());
	}
	
	@Override
	public void draw(Graphics2D area, int offsetX, int offsetY) {
		Color oldColor = area.getColor();
		
		area.setColor(fillColor);
		area.fillOval(x-radius+offsetX, y-radius+offsetY, 2*radius, 2*radius);
		
		area.setColor(oldColor);
		super.draw(area, offsetX, offsetY);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns the fill color of this circle.
	 */
	@Override
	public Color getBackground() {
		return fillColor;
	}
	
	@Override
	public void setBackground(Color color) {
		fillColor = Objects.requireNonNull(color);
	}
	
	@Override
	public String serialize() {
		return serialize(this);
	}
	
	@Override
	public AbstractShapePanel getModificationPanel() {
		return new FilledCirclePanel(this);
	}

}
