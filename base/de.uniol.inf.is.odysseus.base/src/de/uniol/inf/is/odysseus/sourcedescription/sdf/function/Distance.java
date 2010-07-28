/**
 * 
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class Distance extends CustomFunction {
	private static Double RADIUS = 6367000.0;

	/**
	 * 
	 */
	public Distance() {
		numberOfParameters = 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sourcedescription.sdf.function.CustomFunction
	 * #getName()
	 */
	@Override
	public String getName() {
		return "distance";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nfunk.jep.function.PostfixMathCommand#run(java.util.Stack)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run(Stack inStack) throws ParseException {
		// check the stack
		checkStack(inStack);

		// get the parameters from the stack
		Object fromLat = inStack.pop();
		Object fromLng = inStack.pop();
		Object toLat = inStack.pop();
		Object toLng = inStack.pop();
		// check whether the argument is of the right type
		if ((fromLat instanceof Double) && (fromLng instanceof Double)
				&& (toLat instanceof Double) && (toLng instanceof Double)) {

			double deltaLatitude = Math.sin(Math
					.toRadians((((Double) fromLat) - ((Double) toLat))) / 2);
			double deltaLongitude = Math.sin(Math
					.toRadians((((Double) fromLng) - ((Double) toLng))) / 2);
			double circleDistance = 2 * Math.asin(Math.min(1, Math
					.sqrt(deltaLatitude * deltaLatitude
							+ Math.cos(Math.toRadians((Double) fromLat))
							* Math.cos(Math.toRadians((Double) toLat))
							* deltaLongitude * deltaLongitude)));
			Double distance = Math.abs(RADIUS * circleDistance);
			// push the result on the inStack
			inStack.push(new Double(distance));
		} else {
			throw new ParseException("Invalid parameter type");
		}
	}

	public static void setRadius(double radius) {
		RADIUS = radius;
	}

}
