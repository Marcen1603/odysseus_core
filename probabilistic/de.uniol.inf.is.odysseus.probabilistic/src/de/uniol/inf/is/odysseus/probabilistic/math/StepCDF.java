package de.uniol.inf.is.odysseus.probabilistic.math;

public class StepCDF implements CumulativeDistributionFunction {
	private double[][] points;

	@Override
	public double evaluate(double x) {
		if (x < points[0][0]) {
			return 0.0;
		}
		if (x >= points[points.length - 1][0]) {
			return 1.0;
		}

		for (int i = 0; i < points.length - 1; i++) {
			if ((points[i][0] <= x) && (x < points[i + 1][0])) {
				return points[i][1];
			}
		}
		throw new IllegalArgumentException("No data points found");
	}

	@Override
	public CumulativeDistributionFunction add() {
		// TODO Auto-generated method stub
		return null;
	}

}
