package de.uniol.inf.is.odysseus.temporaltypes.types;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * A linear integer function that calculates the value for an integer for a
 * certain point in time based on a linear function.
 * 
 * @author Tobias Brandt
 *
 */
public class LinearIntegerFunction implements IntegerFunction {

	// Using the function y = mx + b with x being the time
	private double m;
	private double b;

	/**
	 * Using the function y = mx + b with x being the time.
	 * 
	 * @param m
	 *            slope / gradient
	 * @param b
	 *            b part of the equation
	 */
	public LinearIntegerFunction(double m, double b) {
		this.m = m;
		this.b = b;
	}

	@Override
	public int getValue(PointInTime time) {
		return (int) (m * time.getMainPoint() + b);
	}

	@Override
	public String toString() {
		return "y = " + m + "x + " + b;
	}

}
