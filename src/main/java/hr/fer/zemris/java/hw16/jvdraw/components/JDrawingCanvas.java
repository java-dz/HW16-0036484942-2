package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

/**
 * The JDrawingCanvas class is in charge for handling concrete images. It
 * contains two references on {@code BufferedImage}s:
 * <ul>
 * <li>A reference to the cached image which is an image that contains the look
 * before last change.
 * <li>A reference to the current image which is an image with all changes
 * saved.
 * </ul>
 * <p>
 * This class implements a {@link DrawingModelListener} in order to draw images
 * when a drawing event occurs.
 * <p>
 * This class also contains several listeners, which are described below:
 * <ul>
 * <li>A mouse listener that listens for a {@code mousePressed} event and starts
 * the drawing process. When the mouse is finally released, the drawing is
 * finished and saved.
 * <li>A mouse motion listener which listens for mouse motion events and updates
 * endpoint of the shape that is currently being drawn.
 * <li>A component listener that redraws the current image upon component
 * resized event.
 * </ul>
 *
 * @author Mario Bobic
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /** Model used by this canvas. */
    private JDrawingCanvasModel model;
    /** An image that contains the look before last change. */
    private BufferedImage cachedImage;
    /** Current image with all changes saved. */
    private BufferedImage currentImage;

    /**
     * Constructs an instance of {@code JDrawingCanvas} with the specified
     * <tt>model</tt>.
     *
     * @param model a drawing canvas model for this canvas
     */
    public JDrawingCanvas(JDrawingCanvasModel model) {
        this.model = model;
        model.addDrawingModelListener(this);
        addListeners();
    }

    /**
     * Adds several listeners to this object, which are described below:
     * <ul>
     * <li>A mouse listener that listens for a {@code mousePressed} event and
     * starts the drawing process. When the mouse is finally released, the
     * drawing is finished and saved.
     * <li>A mouse motion listener which listens for mouse motion events and
     * updates endpoint of the shape that is currently being drawn.
     * <li>A component listener that redraws the current image upon component
     * resized event.
     * </ul>
     */
    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!model.isCurrentlyDrawing()) {
                    model.start(e.getX(), e.getY());
                } else {
                    model.finish();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                moved(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                moved(e);
            }
            private void moved(MouseEvent e) {
                if (model.isCurrentlyDrawing()) {
                    model.update(e.getX(), e.getY());
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = Math.max(1, getWidth());
                int height = Math.max(1, getHeight());

                currentImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                cachedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

                drawFrom(model);
            }
        });
    }

    /**
     * Draws the current image graphics from the specified <tt>source</tt>.
     * <p>
     * This method is used when it is not possible to use the cached image to
     * achieve better performance.
     *
     * @param source source containing geometric shapes
     */
    private void drawFrom(DrawingModel source) {
        Graphics2D g = getCurrentImageGraphics();
        g.setColor(Color.WHITE); // plane color is white
        g.fillRect(0, 0, currentImage.getWidth(), currentImage.getHeight());

        int sourceLastIndex = source.getSize() - 1;
        for (int i = 0; i < sourceLastIndex; i++) {
            GeometricalObject shape = source.getObject(i);
            shape.draw(g, 0, 0);
        }

        if (sourceLastIndex >= 0) {
            GeometricalObject shape = source.getObject(sourceLastIndex);

            if (model.isCurrentlyDrawing()) {
                flushImage();
                shape.draw(g, 0, 0);
            } else {
                shape.draw(g, 0, 0);
                flushImage();
            }
        } else {
            flushImage();
        }

        g.dispose();
    }

    /**
     * Restores current image from the cached version by copying raster data of
     * the <tt>cachedImage</tt> to the <tt>currentImage</tt>.
     * <p>
     * This method is used to revert the last change, which is often a preview
     * of a geometric shape.
     */
    private void restoreImage() {
        cachedImage.copyData(currentImage.getRaster());
    }

    /**
     * Flushes the cached image to the current image by copying raster data of
     * the <tt>currentImage</tt> to the <tt>cachedImage</tt>.
     * <p>
     * This method is used to flush the final image to current image after the
     * geometric shape has finally been drawn.
     */
    private void flushImage() {
        currentImage.copyData(cachedImage.getRaster());
    }

    @Override
    public String getName() {
        return model.getName();
    }

    /**
     * Removes all shapes from this canvas' model.
     */
    public void clear() {
        model.clear();
    }

    /**
     * Returns the current image graphics object with anti-aliasing hints to
     * produce better and clearer images.
     *
     * @return the current image graphics object with anti-aliasing hints
     */
    private Graphics2D getCurrentImageGraphics() {
        Graphics2D g = (Graphics2D) currentImage.createGraphics();

        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        return g;
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        if (index0 == index1 && index0 == source.getSize() - 1) {
            flushImage();
            Graphics2D g = getCurrentImageGraphics();
            source.getObject(index0).draw(g, 0, 0);
        } else {
            drawFrom(source);
        }

        repaint();
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        if (index0 == index1 && index0 == source.getSize() - 1 && model.isCurrentlyDrawing()) {
            restoreImage();
        } else {
            drawFrom(source);
        }

        repaint();
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        objectsRemoved(source, index0, index1);
        objectsAdded(source, index0, index1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(currentImage, 0, 0, null);
    }

}
