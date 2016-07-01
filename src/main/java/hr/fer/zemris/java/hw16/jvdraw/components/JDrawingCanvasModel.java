package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.ShapeFactory;

/**
 * A drawing canvas model that implements both {@link DrawingModel} and
 * {@link ColorChangeListener}. Contains some additional methods for easier
 * geometric shape and list manipulation.
 * <p>
 * This model contains a boolean flag that indicates if a document change has
 * been made and the path of the file that is currently opened.
 *
 * @author Mario Bobic
 */
public class JDrawingCanvasModel implements DrawingModel, ColorChangeListener {

	/** Flag that indicates if a document change has been made. */
	private boolean changed;
	
	/** The path of the file that is currently opened. */
	private Path filePath;
	
	/** List of geometric shapes. */
	private List<GeometricalObject> shapes = new ArrayList<>();

	/** List of listeners. */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	/** Foreground color picker. */
	private JColorArea foreground;
	/** Background color picker. */
	private JColorArea background;
	
	/**
	 * A geometric shape that is currently being drawn.
	 * Is <tt>null</tt> if model is not currently being drawn on.
	 */
	private GeometricalObject drawingShape;
	
	/**
	 * Constructs an instance of {@code JDrawingCanvasModel} with the specified
	 * arguments.
	 *
	 * @param foreground foreground color picker
	 * @param background background color picker
	 */
	public JDrawingCanvasModel(JColorArea foreground, JColorArea background) {
		this.foreground = foreground;
		this.background = background;
		foreground.addColorChangeListener(this);
		background.addColorChangeListener(this);
	}
	
	/**
	 * Returns the name of the file that is currently opened. Since the file is
	 * represented by a {@code Path} object, the file name is the
	 * <em>farthest</em> element from the root in the directory hierarchy.
	 * <p>
	 * Returns <tt>Untitled</tt> if file path is <tt>null</tt>.
	 * 
	 * @return the name of the file that is currently opened, or Untitled
	 */
	public String getName() {
		if (filePath == null) {
			return "Untitled";
		} else {
			return filePath.getFileName().toString();
		}
	}
	
	/**
	 * Returns true if the image has had a change since it was last saved.
	 * False otherwise.
	 * 
	 * @return the changed state of the image
	 */
	public boolean isChanged() {
		return changed;
	}
	
	/**
	 * Sets the changed state of the image to the specified value.
	 * 
	 * @param changed the changed state to be set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * Returns the path of the file that is currently opened.
	 * <p>
	 * Returns <tt>null</tt> if image is not associated with file on disk.
	 * 
	 * @return the path of the file that is currently opened
	 */
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Sets the path of the file that is currently opened to the specified
	 * <tt>filePath</tt>.
	 * 
	 * @param filePath path of the file to be set
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Starts the drawing process by creating a shape from the
	 * {@link ShapeFactory} and setting its foreground and background
	 * colors to appropriate values.
	 * <p>
	 * This method notifies all active listeners of the change.
	 * 
	 * @param x start x position of the area
	 * @param y start y position of the area
	 */
	public void start(int x, int y) {
		drawingShape = ShapeFactory.create(x, y);
		
		drawingShape.setForeground(foreground.getCurrentColor());
		drawingShape.setBackground(background.getCurrentColor());
		add(drawingShape);
	}

	/**
	 * Updates the shape that is currently being drawn by setting its
	 * {@link GeometricalObject#setEndpoint endpoint} to the specified
	 * <tt>x</tt> and <tt>y</tt> values.
	 * <p>
	 * This method notifies all active listeners of the change.
	 * 
	 * @param x x position of the endpoint
	 * @param y y position of the endpoint
	 */
	public void update(int x, int y) {
		drawingShape.setEndpoint(x, y);

		int index = shapes.size() - 1;
		for (DrawingModelListener listener : listeners) {
			listener.objectsChanged(this, index, index);
		}
	}

	/**
	 * Finished the drawing process by setting the shape that is currently being
	 * drawn to <tt>null<tt>.
	 * <p>
	 * This method notifies all active listeners of the change.
	 */
	public void finish() {
		drawingShape = null;
		int index = shapes.size() - 1;

		for (DrawingModelListener listener : listeners) {
			listener.objectsAdded(this, index, index);
		}
	}
	
	/**
	 * Returns <tt>true</tt> if a drawing is currently being made on the model.
	 * <tt>False</tt> otherwise.
	 * 
	 * @return true if a drawing is currently being made on the model
	 */
	public boolean isCurrentlyDrawing() {
		return drawingShape != null;
	}
	
	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if (isCurrentlyDrawing()) {
			if (source == foreground) {
				drawingShape.setForeground(newColor);
			} else if (source == background) {
				drawingShape.setBackground(newColor);
			}
		}
	}

	@Override
	public int getSize() {
		return shapes.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return shapes.get(index);
	}

	@Override
	public void removeObject(int index) {
		if (index >= shapes.size() || index < 0) {
			return;
		}

		shapes.remove(index);
		for (DrawingModelListener listener : listeners) {
			listener.objectsRemoved(this, index, index);
		}

		changed = true;
	}
	
	/**
	 * Removes the specified <tt>shape</tt> from this model.
	 * 
	 * @param shape shape to be removed from this model
	 */
	public void remove(GeometricalObject shape) {
		removeObject(shapes.indexOf(shape));
	}
	
	@Override
	public void clear() {
		int endIndex = Math.max(shapes.size() - 1, 0);
		shapes.clear();

		for (DrawingModelListener listener : listeners) {
			listener.objectsRemoved(this, 0, endIndex);
		}

		changed = false;
	}

	@Override
	public void add(GeometricalObject shape) {
		shapes.add(shape);
		int index = shapes.size() - 1;
		for (DrawingModelListener listener : listeners) {
			listener.objectsAdded(this, index, index);
		}

		changed = true;
	}
	
	/**
	 * Fires an objects changed event to all registered listeners.
	 * 
	 * @param index index of the object that was changed
	 */
	public void changeObject(int index) {
		for (DrawingModelListener listener : listeners) {
			listener.objectsChanged(this, index, index);
		}

		changed = true;
	}
	
	/**
	 * Serializes all geometric shapes that are on the image and returns a list
	 * of serialized shapes ready to be parsed and re-serialized again.
	 * 
	 * @return a list of serialized shapes
	 */
	public List<String> serialize() {
		List<String> list = new ArrayList<>();
		
		shapes.forEach((shape) -> {
			list.add(shape.serialize());
		});
		
		return list;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}
	
}
