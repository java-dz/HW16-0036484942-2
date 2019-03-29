package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

/**
 * The listener interface for receiving "interesting" color change events (new
 * color selected) on a component.
 * <p>
 * The class that is interested in processing a color change event usually
 * implements this interface.
 * <p>
 * The listener object created from this class is then registered with a
 * component using the component's <tt>addColorChangeListener</tt> method. A
 * color change event is generated when the color is changed upon the source.
 *
 * @author Mario Bobic
 */
public interface ColorChangeListener {

    /**
     * Invoked when a new color is selected upon the <tt>source</tt>
     *
     * @param source source that triggered color change event
     * @param oldColor previous color of the <tt>source</tt>
     * @param newColor new color of the <tt>source</tt>
     */
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);

}
