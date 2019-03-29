package hr.fer.zemris.java.hw16.jvdraw.shapes.panels;

import java.util.Objects;

import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Rectangle;

/**
 * This class extends the {@code AbstractShapePanel} and serves as a container
 * for rectangle's fields in order to alter the rectangle.
 *
 * @author Mario Bobic
 */
public class RectanglePanel extends AbstractShapePanel {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /** Rectangle to be altered. */
    private Rectangle rectangle;

    /** Text field for the x component. */
    private JTextField tfX;
    /** Text field for the y component. */
    private JTextField tfY;
    /** Text field for the width component. */
    private JTextField tfW;
    /** Text field for the height component. */
    private JTextField tfH;

    /** A color picked for specifying the outline color. */
    private JColorArea colorPicker;

    /**
     * Constructs an instance of {@code RectanglePanel} with the specified
     * <tt>rectangle</tt>.
     *
     * @param rectangle rectangle to be altered
     * @throws NullPointerException if <tt>rectangle</tt> is <tt>null</tt>
     */
    public RectanglePanel(Rectangle rectangle) {
        this.rectangle = Objects.requireNonNull(rectangle);

        tfX = new JTextField(String.valueOf(rectangle.getX()));
        tfY = new JTextField(String.valueOf(rectangle.getY()));
        tfW = new JTextField(String.valueOf(rectangle.getW()));
        tfH = new JTextField(String.valueOf(rectangle.getH()));
        colorPicker = new JColorArea(rectangle.getForeground());

        addComponent("Start x coordinate:", tfX);
        addComponent("Start y coordinate:", tfY);
        addComponent("Width:", tfW);
        addComponent("Height:", tfH);
        addComponent("Outline color:", colorPicker);
    }

    @Override
    public void updateShape() {
        rectangle.setX(Integer.parseInt(tfX.getText()));
        rectangle.setY(Integer.parseInt(tfY.getText()));
        rectangle.setW(Integer.parseInt(tfW.getText()));
        rectangle.setH(Integer.parseInt(tfH.getText()));
        rectangle.setForeground(colorPicker.getCurrentColor());
    }
}
