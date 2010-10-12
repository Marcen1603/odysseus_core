/**
 * 
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class Distance extends AbstractFunction<Double> {
	private static Double RADIUS = 6367000.0;

	public static void setRadius(double radius) {
		RADIUS = radius;
	}

	@Override
	public int getArity() {
		return 4;
	}

	@Override
	public String getSymbol() {
		return "distance";
	}

	@Override
	public Double getValue() {
		Object fromLat = getInputValue(0);
		Object fromLng = getInputValue(1);
		Object toLat = getInputValue(2);
		Object toLng = getInputValue(3);

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
		return distance;
	}

	@Override
	public Class<? extends Double> getType() {
		return Double.class;
	}

}
