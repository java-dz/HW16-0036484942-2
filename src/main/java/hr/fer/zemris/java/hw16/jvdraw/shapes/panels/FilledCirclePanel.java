package hr.fer.zemris.java.hw16.jvdraw.shapes.panels;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;

/**
 * This class extends the {@code CirclePanel} and serves as a container
 * for filled circle's fields in order to alter the filled circle.
 *
 * @author Mario Bobic
 */
public class FilledCirclePanel extends CirclePanel {
	/** Serialization UID. */
	private static final long serialVersionUID = 1L;

	/** Filled circle to be altered. */
	private FilledCircle circle;
	
	/** A color picked for specifying the fill color. */
	private JColorArea colorPicker;
	
	/**
	 * Constructs an instance of {@code FilledCirclePanel} with the specified
	 * <tt>circle</tt>.
	 *
	 * @param circle filled circle to be altered
	 * @throws NullPointerException if <tt>circle</tt> is <tt>null</tt>
	 */
	public FilledCirclePanel(FilledCircle circle) {
		super(circle);
		this.circle = circle;
		colorPicker = new JColorArea(circle.getBackground());
		addComponent("Fill color:", colorPicker);
	}

	@Override
	public void updateShape() {
		super.updateShape();
		circle.setBackground(colorPicker.getCurrentColor());
	}

}
