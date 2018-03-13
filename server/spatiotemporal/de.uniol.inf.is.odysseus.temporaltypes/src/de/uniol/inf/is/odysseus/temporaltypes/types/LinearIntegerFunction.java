package de.uniol.inf.is.odysseus.temporaltypes.types;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

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

	public int getValue(PointInTime time) {
		return (int) (m * time.getMainPoint() + b);
	}
	
	@Override
	public String toString() {
		return "y = " + m + "x + " + b;
	}

}
