package hr.fer.zemris.java.hw16.jvdraw.components;

/**
 * The listener interface for receiving "interesting" drawing events (objects
 * added, objects removed, objects changed) on a component.
 * <p>
 * The class that is interested in processing a drawing event usually implements
 * this interface.
 * <p>
 * The listener object created from this class is then registered with a
 * component using the component's <tt>addDrawingModelListener</tt> method. A
 * color change event is generated when the color is changed upon the source.
 *
 * @author Mario Bobic
 */
public interface DrawingModelListener {

    /**
     * Sent after the indices in the index0, index1 interval have been inserted
     * in the data model. The new interval includes both index0 and index1.
     *
     * @param source the <tt>DrawingModel</tt> that triggered the event
     * @param index0 starting index of interval added
     * @param index1 ending index of interval added
     */
    public void objectsAdded(DrawingModel source, int index0, int index1);

    /**
     * Sent after the indices in the index0, index1 interval have been removed
     * from the data model. The interval includes both index0 and index1.
     *
     * @param source the <tt>DrawingModel</tt> that triggered the event
     * @param index0 starting index of interval removed
     * @param index1 ending index of interval removed
     */
    public void objectsRemoved(DrawingModel source, int index0, int index1);

    /**
     * Sent when the contents of the list has changed in a way that's too
     * complex to characterize with the previous methods. For example, this is
     * sent when an item has been replaced. Index0 and index1 bracket the
     * change.
     *
     * @param source the <tt>DrawingModel</tt> that triggered the event
     * @param index0 starting index of interval changed
     * @param index1 ending index of interval changed
     */
    public void objectsChanged(DrawingModel source, int index0, int index1);

}
