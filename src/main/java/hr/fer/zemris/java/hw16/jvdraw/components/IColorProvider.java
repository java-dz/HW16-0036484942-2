package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

/**
 * Interface that enables fetching of current color of an object implementing
 * this interface.
 *
 * @author Mario Bobic
 */
public interface IColorProvider {
	
	/**
	 * Returns the current color of the object.
	 * 
	 * @return the current color of the object
	 */
	public Color getCurrentColor();

}