package hr.fer.zemris.java.hw16.jvdraw.shapes.panels;

import java.util.Objects;

import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;

/**
 * This class extends the {@code AbstractShapePanel} and serves as a container
 * for circle's fields in order to alter the circle.
 *
 * @author Mario Bobic
 */
public class CirclePanel extends AbstractShapePanel {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /** Circle to be altered. */
    private Circle circle;

    /** Text field for the x component. */
    private JTextField tfX;
    /** Text field for the y component. */
    private JTextField tfY;
    /** Text field for the radius component. */
    private JTextField tfRadius;

    /** A color picked for specifying the outline color. */
    private JColorArea colorPicker;

    /**
     * Constructs an instance of {@code CirclePanel} with the specified
     * <tt>circle</tt>.
     *
     * @param circle circle to be altered
     * @throws NullPointerException if <tt>circle</tt> is <tt>null</tt>
     */
    public CirclePanel(Circle circle) {
        this.circle = Objects.requireNonNull(circle);

        tfX = new JTextField(String.valueOf(circle.getX()));
        tfY = new JTextField(String.valueOf(circle.getY()));
        tfRadius = new JTextField(String.valueOf(circle.getRadius()));
        colorPicker = new JColorArea(circle.getForeground());

        addComponent("Center x coordinate:", tfX);
        addComponent("Center y coordinate:", tfY);
        addComponent("Radius:", tfRadius);
        addComponent("Outline color:", colorPicker);
    }

    @Override
    public void updateShape() {
        circle.setX(Integer.parseInt(tfX.getText()));
        circle.setY(Integer.parseInt(tfY.getText()));
        circle.setRadius(Integer.parseInt(tfRadius.getText()));
        circle.setForeground(colorPicker.getCurrentColor());
    }

}
