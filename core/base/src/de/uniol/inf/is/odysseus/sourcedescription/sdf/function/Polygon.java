/**
 * Christian Kuka [christian@kuka.cc]
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.Vector;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka [christian@kuka.cc]
 * @version $Revision$
 */
public class Polygon extends AbstractFunction<Double> {

	private static Double RADIUS = 6367000.0;

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "polygon";
	}

	@Override
	public Double getValue() {

		// get the parameters from the stack
		@SuppressWarnings("unchecked")
		Vector<Double> polygon = (Vector<Double>) getInputValue(0);
		double longitude = (Double) getInputValue(1);
		double latitude = (Double) getInputValue(2);

		int j = polygon.size() - 2;
		boolean isIn = false;
		double[] boundingBox = getBoundingBox(polygon);
		if ((latitude > boundingBox[2]) || (latitude < boundingBox[0])
				|| (longitude > boundingBox[3]) || (longitude < boundingBox[1])) {
			return 0.0;
		} else {
			for (int i = 0; i < polygon.size(); i = i + 2) {
				if (((polygon.get(i + 1) < longitude) && (polygon.get(j + 1) >= longitude))
						|| ((polygon.get(j + 1) < longitude) && (polygon
								.get(i + 1) >= longitude))) {
					if (polygon.get(i) + (longitude - polygon.get(i + 1))
							/ (polygon.get(j + 1) - polygon.get(i + 1))
							* (polygon.get(j) - polygon.get(i)) < latitude) {
						isIn = !isIn;
					}
				}
				j = i;
			}
			return ((isIn) ? 1.0 : 0.0);
		}
	}

	public static void setRadius(double radius) {
		RADIUS = radius;
	}

	private double[] getBoundingBox(Vector<Double> polygon) {
		double[] boundingBox = new double[4];
		if (polygon.size() % 2 == 0) {
			for (int i = 0; i < polygon.size(); i = i + 2) {
				double latitude = polygon.get(i);
				double longitude = polygon.get(i + 1);
				if (latitude < boundingBox[0]) {
					boundingBox[0] = latitude;
				}
				if (longitude < boundingBox[1]) {
					boundingBox[1] = longitude;
				}
				if (latitude > boundingBox[2]) {
					boundingBox[2] = latitude;
				}
				if (longitude > boundingBox[3]) {
					boundingBox[3] = longitude;
				}
			}
		}
		return boundingBox;
	}

	@Override
	public Class<? extends Double> getType() {
		return Double.class;
	}
}
