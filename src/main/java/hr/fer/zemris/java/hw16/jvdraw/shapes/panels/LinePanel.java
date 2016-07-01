package hr.fer.zemris.java.hw16.jvdraw.shapes.panels;

import java.util.Objects;

import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;

/**
 * This class extends the {@code AbstractShapePanel} and serves as a container
 * for line's fields in order to alter the line.
 *
 * @author Mario Bobic
 */
public class LinePanel extends AbstractShapePanel {
	/** Serialization UID. */
	private static final long serialVersionUID = 1L;

	/** Line to be altered. */
	private Line line;

	/** Text field for the x0 component. */
	private JTextField tfX0;
	/** Text field for the y0 component. */
	private JTextField tfY0;
	/** Text field for the x1 component. */
	private JTextField tfX1;
	/** Text field for the y1 component. */
	private JTextField tfY1;

	/** A color picked for specifying the line color. */
	private JColorArea colorPicker;
	
	/**
	 * Constructs an instance of {@code LinePanel} with the specified
	 * <tt>line</tt>.
	 *
	 * @param line line to be altered
	 * @throws NullPointerException if <tt>line</tt> is <tt>null</tt>
	 */
	public LinePanel(Line line) {
		this.line = Objects.requireNonNull(line);
		
		tfX0 = new JTextField(String.valueOf(line.getX0()));
		tfY0 = new JTextField(String.valueOf(line.getY0()));
		tfX1 = new JTextField(String.valueOf(line.getX1()));
		tfY1 = new JTextField(String.valueOf(line.getY1()));
		colorPicker = new JColorArea(line.getForeground());

		addComponent("Start x coordinate:", tfX0);
		addComponent("Start y coordinate:", tfY0);
		addComponent("End x coordinate:", tfX1);
		addComponent("End y coordinate:", tfY1);
		addComponent("Color:", colorPicker);
	}

	@Override
	public void updateShape() {
		line.setX0(Integer.parseInt(tfX0.getText()));
		line.setY0(Integer.parseInt(tfY0.getText()));
		line.setX1(Integer.parseInt(tfX1.getText()));
		line.setY1(Integer.parseInt(tfY1.getText()));
		line.setForeground(colorPicker.getCurrentColor());
	}
}
