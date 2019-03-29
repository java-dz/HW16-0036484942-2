package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * This class represents a color area that shows a color chooser dialog upon
 * mouse press. It implements the {@link IColorProvider} interface and offers
 * methods to add and remove color change listeners.
 *
 * @author Mario Bobic
 */
public class JColorArea extends JComponent implements IColorProvider {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /** The preferred dimension of instances of this class. */
    private static final Dimension PREFERRED_DIMENSION = new Dimension(15, 15);

    /** The currently selected color. */
    private Color selectedColor;

    /** A list of listeners. */
    private List<ColorChangeListener> listeners = new ArrayList<>();

    /**
     * Constructs an instance of {@code JColorArea} with the specified
     * <tt>initialColor</tt>.
     *
     * @param initialColor initial color to be set
     */
    public JColorArea(Color initialColor) {
        selectedColor = initialColor;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Color newColor = JColorChooser.showDialog(
                    JColorArea.this,
                    "Set color",
                    selectedColor
                );

                if (newColor != null) {
                    setCurrentColor(newColor);
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return PREFERRED_DIMENSION;
    }

    @Override
    public Color getCurrentColor() {
        return selectedColor;
    }

    /**
     * Sets the currently selected color to the specified <tt>color</tt>.
     *
     * @param color color to be set
     */
    public void setCurrentColor(Color color) {
        Color oldColor = selectedColor;
        selectedColor = color;

        fire(oldColor);
        repaint();
    }

    /**
     * Fires a new color selected event, notifying every registered listener.
     *
     * @param oldColor previously selected color
     */
    private void fire(Color oldColor) {
        listeners.forEach((l) -> {
            l.newColorSelected(this, oldColor, selectedColor);
        });
    }

    /**
     * Adds a listener to the model that's notified each time a change to the
     * model data occurs.
     *
     * @param l the <tt>ColorChangeListener</tt> to be added
     */
    public void addColorChangeListener(ColorChangeListener l) {
        listeners = new ArrayList<>(listeners);
        listeners.add(l);
    }

    /**
     * Removes a listener from the list that's notified each time a change to
     * the model data occurs.
     *
     * @param l the <tt>ColorChangeListener</tt> to be removed
     */
    public void removeColorChangeListener(ColorChangeListener l) {
        listeners = new ArrayList<>(listeners);
        listeners.remove(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension size = getSize();

        g.setColor(selectedColor);
        g.fillRect(0, 0, size.width, size.height);
    }

}
