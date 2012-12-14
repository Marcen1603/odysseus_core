package de.uniol.inf.is.odysseus.probabilistic.math;

import java.io.Serializable;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Interval implements Serializable, Cloneable, Comparable<Interval> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -6115929417128254867L;
    private final double      inf;
    private final double      sup;

    public Interval(final double inf, final double sup) {
        this.inf = inf;
        this.sup = sup;
    }

    public double inf() {
        return this.inf;
    }

    public double sup() {
        return this.sup;
    }

    public boolean intersects(final Interval other) {
        return ((!this.isEmpty()) && (!other.isEmpty()) && (other.inf <= this.sup) && (this.inf <= other.sup));
    }

    public boolean isEmpty() {
        return !(this.inf <= this.sup);
    }

    public double length() {
        if (!this.isEmpty()) {
            return this.sup - this.inf;
        }
        else {
            return 0.0;
        }
    }

    public Interval add(final Interval other) {
        return new Interval(this.inf + other.inf, this.sup + other.sup);
    }

    public Interval sub(final Interval other) {
        return new Interval(this.inf - other.sup, this.sup - other.inf);
    }

    public Interval mul(final Interval other) {
        final double inf = Math.min(Math.min(this.inf * other.inf, this.inf * other.sup),
                Math.min(this.sup * other.inf, this.sup * other.sup));
        final double sup = Math.max(Math.max(this.inf * other.inf, this.inf * other.sup),
                Math.max(this.sup * other.inf, this.sup * other.sup));

        return new Interval(inf, sup);
    }

    public Interval div(final Interval other) throws IntervalArithmeticException {
        if (!other.contains(0.0)) {
            final double inf = Math.min(Math.min(this.inf / other.inf, this.inf / other.sup),
                    Math.min(this.sup / other.inf, this.sup / other.sup));
            final double sup = Math.max(Math.max(this.inf / other.inf, this.inf / other.sup),
                    Math.max(this.sup / other.inf, this.sup / other.sup));

            return new Interval(inf, sup);
        }
        else {
            throw new IntervalArithmeticException("Division by interval containing zero");
        }
    }

    public Interval union(final Interval other) {
        return new Interval(Math.min(this.inf, other.inf), Math.max(this.sup, other.sup));
    }

    public Interval intersection(final Interval other) {
        if ((this.isEmpty()) || (other.isEmpty()) || (!(other.inf <= this.sup)) || (!(this.inf <= other.sup))) {
            return new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
        }
        return new Interval(Math.max(this.inf, other.inf), Math.min(this.sup, other.sup));
    }

    public Interval[] difference(final Interval other) {
        if (!this.intersects(other)) {
            return new Interval[] { new Interval(Double.MAX_VALUE, Double.MIN_VALUE) };
        }
        if ((other.inf >= this.inf) && (other.sup <= this.sup)) {
            return new Interval[] { new Interval(this.inf, other.inf), new Interval(other.sup, this.sup) };
        }
        if ((other.inf <= this.inf) && (other.sup <= this.sup)) {
            return new Interval[] { new Interval(other.sup, this.sup) };
        }
        if (other.inf >= this.inf) {
            return new Interval[] { new Interval(this.inf, other.inf) };
        }
        return new Interval[] { new Interval(Double.MAX_VALUE, Double.MIN_VALUE) };
    }

    public boolean contains(final double value) {
        return ((value >= this.inf) && (value <= this.sup));
    }

    public boolean contains(final Interval other) {
        if (other.isEmpty()) {
            return true;
        }
        if (this.isEmpty()) {
            return false;
        }
        return ((other.inf >= this.inf) && (other.sup <= this.sup));
    }

    public Interval[] split(final double value) {
        final Interval[] intervals = new Interval[2];
        if (this.isEmpty()) {
            intervals[0] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
            intervals[1] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
        }
        else if (value < this.inf) {
            intervals[0] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
            intervals[1] = new Interval(value, value);
        }
        else if (value > this.sup) {
            intervals[0] = new Interval(value, value);
            intervals[1] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
        }
        else {
            intervals[0] = new Interval(this.inf, value);
            intervals[1] = new Interval(value, this.sup);
        }
        return intervals;
    }

    public Interval[] split(final Interval other) {
        if (!this.intersects(other)) {
            return new Interval[] { new Interval(Double.MAX_VALUE, Double.MIN_VALUE) };
        }
        if (this.contains(other)) {
            return new Interval[] { new Interval(this.inf, other.inf), new Interval(other.inf, other.sup),
                    new Interval(other.sup, this.sup) };
        }
        else {
            if ((this.contains(other.inf)) && (this.sup != other.inf)) {
                return this.split(other.inf);
            }
            else if ((this.contains(other.sup)) && (this.inf != other.sup)) {
                return this.split(other.sup);
            }
            else {
                return new Interval[] { new Interval(this.inf, this.sup) };
            }
        }
    }

    @Override
    public int compareTo(final Interval other) {
        return Double.valueOf(this.inf).compareTo(Double.valueOf(other.inf))
                + Double.valueOf(this.sup).compareTo(Double.valueOf(other.sup));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.inf);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.sup);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Interval other = (Interval) obj;
        if (Double.doubleToLongBits(this.inf) != Double.doubleToLongBits(other.inf)) {
            return false;
        }
        if (Double.doubleToLongBits(this.sup) != Double.doubleToLongBits(other.sup)) {
            return false;
        }
        return true;
    }

    @Override
    public Interval clone() {
        return new Interval(this.inf, this.sup);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[" + this.inf + "," + this.sup + "]";
    }
}
