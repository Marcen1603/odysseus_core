package de.uniol.inf.is.odysseus.temporaltypes.types.real;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

public class SplineDoubleFunction implements TemporalFunction<Double> {

	private PolynomialSplineFunction function;
	
	public SplineDoubleFunction(double[] tempDimension, double[] yDimension) {
		function = new SplineInterpolator().interpolate(tempDimension, yDimension);
	}
	
	@Override
	public Double getValue(PointInTime time) {

		final PolynomialFunction[] splines = function.getPolynomials();
		final PolynomialFunction firstFunction = splines[0];
		final PolynomialFunction lastFunction = splines[splines.length - 1];

		final double[] knots = function.getKnots();
		final double firstKnot = knots[0];
		final double lastKnot = knots[knots.length - 1];

		double value = 0;

		if (time.getMainPoint() > lastKnot) {
			value = lastFunction.value(time.getMainPoint() - knots[knots.length - 2]);
		} else if (time.getMainPoint() < firstKnot) {
			value = firstFunction.value(time.getMainPoint() - knots[knots.length - 2]);
		} else {
			value = function.value(time.getMainPoint());
		}
		
		return value;
		
	}

}
