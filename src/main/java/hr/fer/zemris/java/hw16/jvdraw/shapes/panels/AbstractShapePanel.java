package hr.fer.zemris.java.hw16.jvdraw.shapes.panels;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is an abstract representation of the shape panel. It extends
 * {@code JPanel} and it serves as a container for shape's fields in order to
 * alter the shape.
 *
 * @author Mario Bobic
 */
public abstract class AbstractShapePanel extends JPanel {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of {@code AbstractShapePanel} with
     * {@code GridLayout} manager.
     */
    public AbstractShapePanel() {
        setLayout(new GridLayout(0, 2, 5, 5));
    }

    /**
     * Adds both <tt>text</tt> as a {@code JLabel} with right alignment and
     * <tt>comp</tt> to the panel.
     * <p>
     * Since a {@code GridLayout} manager manages this panel, these components
     * will be set side-by-side.
     *
     * @param text text describing the component
     * @param comp a component
     */
    protected void addComponent(String text, JComponent comp) {
        add(new JLabel(text, JLabel.RIGHT));
        add(comp);
    }

    /**
     * Updates the shape with data from the panel.
     *
     * @throws IllegalArgumentException if an illegal argument is specified
     */
    public abstract void updateShape();
}
