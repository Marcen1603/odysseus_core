package de.uniol.inf.is.odysseus.probabilistic.math;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Distribution {
    private final double mean;
    private final double deviation;

    public Distribution(final double mean, final double deviation) {
        this.mean = mean;
        this.deviation = deviation;
    }

    public double getMean() {
        return this.mean;
    }

    public double getDeviation() {
        return this.deviation;
    }

    public double getVariance() {
        return Math.pow(this.deviation, 2);
    }

    public Distribution add(final Distribution other) {
        return new Distribution(this.mean + other.mean, Math.sqrt(Math.pow(this.deviation, 2)
                + Math.pow(other.deviation, 2)));
    }

    public Distribution sub(final Distribution other) {
        return new Distribution(this.mean - other.mean, Math.sqrt(Math.pow(this.deviation, 2)
                - Math.pow(other.deviation, 2)));
    }

    public double getValue(final double value) {
        return this.phi((value - this.mean) / this.getDeviation()) / this.getDeviation();
    }

    private double phi(final double value) {
        return Math.exp((-1 * Math.pow(value, 2)) / 2) / Math.sqrt(2 * Math.PI);
    }

    public double getArea(final double value) {
        final double x = (value - this.mean) / this.getDeviation();
        if (x < -8.0) {
            return 0.0;
        }
        if (x > 8.0) {
            return 1.0;
        }
        double sum = 0.0, term = x;
        for (int i = 3; (sum + term) != sum; i += 2) {
            sum = sum + term;
            term = (term * x * x) / i;
        }
        return 0.5 + (sum * this.phi(x));
    }

    public double getArea(final Interval interval) {
        return this.getArea(interval.sup()) - this.getArea(interval.inf());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("mu: ").append(this.getMean()).append(" sigma: ").append(this.getDeviation());
        return sb.toString();
    }

}
