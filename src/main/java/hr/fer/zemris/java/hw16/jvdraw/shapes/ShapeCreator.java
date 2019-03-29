package hr.fer.zemris.java.hw16.jvdraw.shapes;

/**
 * This interface should be implemented by classes whose objects know how to
 * create instances of {@linkplain GeometricalObject} classes.
 *
 * @author Mario Bobic
 */
public interface ShapeCreator {

    /**
     * Creates an instance of {@linkplain GeometricalObject} with the specified
     * initial <tt>x</tt> and <tt>y</tt> coordinates.
     *
     * @param x horizontal start coordinate
     * @param y vertical start coordinate
     * @return an instance of {@linkplain GeometricalObject}
     */
    public GeometricalObject create(int x, int y);

}
