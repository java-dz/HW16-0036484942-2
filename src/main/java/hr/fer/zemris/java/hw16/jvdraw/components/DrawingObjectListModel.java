package hr.fer.zemris.java.hw16.jvdraw.components;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

/**
 * A drawing list model usually representing drawing history. Extends the
 * {@link AbstractListModel} and implements {@link DrawingModelListener}.
 *
 * @author Mario Bobic
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject>
		implements DrawingModelListener {

	/** Serialization UID. */
	private static final long serialVersionUID = 1L;

	/** Instance of {@code DrawingModel} to fetch list size and elements. */
	private DrawingModel model;

	/**
	 * Constructs an instance of {@code DrawingObjectListModel} with the
	 * specified drawing <tt>model</tt>.
	 *
	 * @param model model for fetching list size and list elements
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		super.fireIntervalAdded(source, index0, index1);

	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		super.fireIntervalRemoved(source, index0, index1);

	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		super.fireContentsChanged(source, index0, index1);
	}

}
