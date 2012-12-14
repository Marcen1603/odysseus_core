package de.uniol.inf.is.odysseus.probabilistic.math;

public class LinCDF implements CumulativeDistributionFunction {
    private double[][] points;

    @Override
    public double evaluate(final double x) {
        if (x < this.points[0][0]) {
            return 0.0;
        }
        if (x >= this.points[this.points.length - 1][0]) {
            return 1.0;
        }

        for (int i = 0; i < (this.points.length - 1); i++) {
            if ((this.points[i][0] <= x) && (x < this.points[i + 1][0])) {
                return this.points[i][1]
                        + (((x - this.points[i][0]) / (this.points[i + 1][0] - this.points[i][0])) * (this.points[i + 1][1] - this.points[i][1]));
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
