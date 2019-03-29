package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The StatusBar class contains a single {@link JLabel} object used for telling
 * foreground and background <tt>RGB</tt> components.
 * <p>
 * It implements the {@link ColorChangeListener} interface to be notified of a
 * color change event.
 *
 * @author Mario Bobic
 */
public class StatusBar extends JPanel implements ColorChangeListener {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /** JLabel containing color information */
    private JLabel colors = new JLabel();

    /** Foreground color picker. */
    private JColorArea foreground;
    /** Background color picker. */
    private JColorArea background;

    /**
     * Constructs an instance of {@code StatusBar} with the specified arguments.
     *
     * @param foreground foreground color picker
     * @param background background color picker
     * @throws NullPointerException if any argument is <tt>null</tt>
     */
    public StatusBar(JColorArea foreground, JColorArea background) {
        this.foreground = Objects.requireNonNull(foreground);
        this.background = Objects.requireNonNull(background);

        foreground.addColorChangeListener(this);
        background.addColorChangeListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(colors);

        newColorSelected(null, null, null);
    }

    @Override
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
        Color f = foreground.getCurrentColor();
        Color b = background.getCurrentColor();

        colors.setText("Foreground color: " + colorToString(f) + ", background color: " + colorToString(b));
    }

    /**
     * Returns a string representation of the specified color object
     * <tt>col</tt>, containing red, green and blue components in parentheses
     * separated by commas.
     * <p>
     * For example, returned String for {@link Color#RED} will be (255, 0, 0).
     *
     * @param col color to be returned as String
     * @return a string representation of the specified color
     */
    private static String colorToString(Color col) {
        return String.format("(%d, %d, %d)", col.getRed(), col.getGreen(), col.getBlue());
    }

}
