package de.uniol.inf.odysseus.spatiotemporal.types.point;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

/**
 * The solution here is based on this answer:
 * https://stackoverflow.com/questions/32076041/extrapolation-in-java
 * 
 * It uses two spline function, one for each dimension of the moving points (lat
 * / lng or x / y). For interpolation, the normal functions from Apache Math are
 * used. For extrapolation (before and after the first / last known point), the
 * first or last spline is used and calculated for the time which is desired (in
 * the past or in the future).
 * 
 * @author Tobias Brandt
 *
 */
public class SplineMovingPointFunction implements TemporalFunction<GeometryWrapper> {

	private PolynomialSplineFunction functionX;
	private PolynomialSplineFunction functionY;
	private GeometryFactory factory;

	public SplineMovingPointFunction(PolynomialSplineFunction functionX, PolynomialSplineFunction functionY) {
		this.functionX = functionX;
		this.functionY = functionY;
		this.factory = new GeometryFactory();
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {

		final PolynomialFunction[] splinesX = functionX.getPolynomials();
		final PolynomialFunction firstFunctionX = splinesX[0];
		final PolynomialFunction lastFunctionX = splinesX[splinesX.length - 1];

		final PolynomialFunction[] splinesY = functionY.getPolynomials();
		final PolynomialFunction firstFunctionY = splinesY[0];
		final PolynomialFunction lastFunctionY = splinesY[splinesY.length - 1];

		// Knots (t) should be equal on both dimensions
		final double[] knotsX = functionX.getKnots();
		final double firstKnotX = knotsX[0];
		final double lastKnotX = knotsX[knotsX.length - 1];

		final double[] knotsY = functionY.getKnots();

		double x = 0;
		double y = 0;

		if (time.getMainPoint() > lastKnotX) {
			x = lastFunctionX.value(time.getMainPoint() - knotsX[knotsX.length - 2]);
			y = lastFunctionY.value(time.getMainPoint() - knotsY[knotsY.length - 2]);
		} else if (time.getMainPoint() < firstKnotX) {
			x = firstFunctionX.value(time.getMainPoint() - knotsX[knotsX.length - 2]);
			y = firstFunctionY.value(time.getMainPoint() - knotsY[knotsY.length - 2]);
		} else {
			x = functionX.value(time.getMainPoint());
			y = functionY.value(time.getMainPoint());
		}
		Point geomtryPoint = factory.createPoint(new Coordinate(x, y));
		GeometryWrapper wrapper = new GeometryWrapper(geomtryPoint);
		return wrapper;
	}

	@Override
	public String toString() {
		return "Spline - x[0] = " + this.functionX.getPolynomials()[0] + "; y[0] = "
				+ this.functionY.getPolynomials()[0];
	}

}
