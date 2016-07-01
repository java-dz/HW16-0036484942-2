package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Factory class for vending standard {@link GeometricalObject} objects.
 *
 * @author Mario Bobic
 */
public class ShapeFactory {

	/** A singleton shape creator object. */
	private static ShapeCreator shapeCreator;
	
	/**
	 * Disables instantiation.
	 */
	private ShapeFactory() {
	}
	
	/**
	 * Creates an instance of {@linkplain GeometricalObject} with the specified
	 * initial <tt>x</tt> and <tt>y</tt> coordinates.
	 * 
	 * @param x horizontal start coordinate
	 * @param y vertical start coordinate
	 * @return an instance of {@linkplain GeometricalObject}
	 */
	public static GeometricalObject create(int x, int y) {
		return shapeCreator.create(x, y);
	}
	
	/**
	 * Returns actions of all available shapes, ready to be used in button
	 * models.
	 * 
	 * @return actions of all available shapes
	 */
	public static List<Action> getShapeActions() {
		List<Action> list = new ArrayList<>();
		
		list.add(new AbstractAction("Line") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeCreator = Line.getShapeCreator();
			}
		});
		
		list.add(new AbstractAction("Circle") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeCreator = Circle.getShapeCreator();
			}
		});
		
		list.add(new AbstractAction("Filled circle") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeCreator = FilledCircle.getShapeCreator();
			}
		});
		
		list.add(new AbstractAction("Rectangle") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeCreator = Rectangle.getShapeCreator();
			}
		});
		
		return list;
	}
	
	/**
	 * Resets the instance counter of all shape classes.
	 * <p>
	 * This method is used when a workspace reset is required.
	 */
	public static void resetInstanceCounters() {
		Line.resetCounter();
		Circle.resetCounter();
		Rectangle.resetCounter();
	}
	
	/**
	 * Parses the specified string <tt>s</tt> as an instance of
	 * {@code GeometricalObject} object. The specified string must match
	 * serialization type of some geometric object in order to be parsed.
	 * <p>
	 * Throws {@link IllegalArgumentException} if the string can not be parsed
	 * as either of the {@code GeometricalObject} object.
	 * 
	 * @param s string to be parsed
	 * @return a {@code GeometricalObject} object parsed from the specified string
	 * @throws NullPointerException if <tt>s</tt> is <tt>null</tt>
	 * @throws IllegalArgumentException if <tt>s</tt> can not be parsed
	 */
	public static GeometricalObject parse(String s) {
		s = s.replaceAll("\\s+", " ").trim();
		
		if (s.startsWith("LINE")) {
			return Line.parse(s);
		} else if (s.startsWith("CIRCLE")) {
			return Circle.parse(s);
		} else if (s.startsWith("FCIRCLE")) {
			return FilledCircle.parse(s);
		} else if (s.startsWith("RECTANGLE")) {
			return Rectangle.parse(s);
		} else {
			throw new IllegalArgumentException("Can not determine object type.");
		}
	}
	
	/**
	 * Parses the specified list of <tt>lines</tt> as instances of
	 * {@code GeometricalObject} objects. All strings specified in the list must
	 * match serialization type of some geometric object in order to be parsed.
	 * <p>
	 * Throws {@link IllegalArgumentException} if the string can not be parsed
	 * as either of the {@code GeometricalObject} object.
	 * 
	 * @param lines list of lines to be parsed
	 * @return a list of {@code GeometricalObject} objects parsed from the
	 *         specified list of lines
	 * @throws NullPointerException if <tt>lines</tt> or any string in the list
	 *         is <tt>null</tt>
	 * @throws IllegalArgumentException if any <tt>line</tt> can not be parsed
	 */
	public static List<GeometricalObject> parse(List<String> lines) {
		List<GeometricalObject> list = new ArrayList<>();
		
		lines.forEach((line) -> {
			list.add(parse(line));
		});
		
		return list;
	}
	
}
