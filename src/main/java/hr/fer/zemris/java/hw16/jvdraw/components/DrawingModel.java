package hr.fer.zemris.java.hw16.jvdraw.components;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

/**
 * This interface defines basic drawing manipulation methods and can be used to
 * get the value of each cell in a list and the length of the list. Logically
 * the model is a vector, indices vary from {@code 0} to
 * {@code DrawingModel.getSize() - 1}. Any change to the contents or length of
 * the data model must be reported to all of the {@code DrawingModelListener}s.
 *
 * @author Mario Bobic
 */
public interface DrawingModel {

	/**
	 * Returns the length of the component list.
	 * 
	 * @return the length of the component list
	 */
	public int getSize();

	/**
	 * Returns the shape at the specified <tt>index</tt>.
	 * 
	 * @param index the requested index
	 * @return the shape at <tt>index</tt>
	 */
	public GeometricalObject getObject(int index);
	
	/**
	 * Removes the shape at the specified <tt>index</tt>.
	 * 
	 * @param index index
	 */
	public void removeObject(int index);
	
	/**
	 * Removes all shapes from this model.
	 */
	public void clear();

	/**
	 * Adds the specified <tt>shape</tt> to the model.
	 * 
	 * @param shape shape to be added
	 */
	public void add(GeometricalObject shape);

	/**
	 * Adds a listener to the model that's notified each time a change to the
	 * model data occurs.
	 * 
	 * @param l the <tt>DrawingModelListener</tt> to be added
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes a listener from the list that's notified each time a change to
	 * the model data occurs.
	 * 
	 * @param l the <tt>DrawingModelListener</tt> to be removed
	 */
	public void removeDrawingModelListener(DrawingModelListener l);

}