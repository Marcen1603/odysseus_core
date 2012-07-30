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
	private final double inf;
	private final double sup;

	public Interval(double inf, double sup) {
		this.inf = inf;
		this.sup = sup;
	}

	public double inf() {
		return inf;
	}

	public double sup() {
		return sup;
	}

	public boolean intersects(Interval other) {
		return ((!this.isEmpty()) && (!other.isEmpty())
				&& (other.inf <= this.sup) && (this.inf <= other.sup));
	}

	public boolean isEmpty() {
		return !(inf <= sup);
	}

	public double length() {
		if (!isEmpty()) {
			return sup - inf;
		} else {
			return 0.0;
		}
	}

	public Interval add(Interval other) {
		return new Interval(this.inf + other.inf, this.sup + other.sup);
	}

	public Interval sub(Interval other) {
		return new Interval(this.inf - other.sup, this.sup - other.inf);
	}

	public Interval mul(Interval other) {
		double inf = Math.min(
				Math.min(this.inf * other.inf, this.inf * other.sup),
				Math.min(this.sup * other.inf, this.sup * other.sup));
		double sup = Math.max(
				Math.max(this.inf * other.inf, this.inf * other.sup),
				Math.max(this.sup * other.inf, this.sup * other.sup));

		return new Interval(inf, sup);
	}

	public Interval div(Interval other) throws IntervalArithmeticException {
		if (!other.contains(0.0)) {
			double inf = Math.min(
					Math.min(this.inf / other.inf, this.inf / other.sup),
					Math.min(this.sup / other.inf, this.sup / other.sup));
			double sup = Math.max(
					Math.max(this.inf / other.inf, this.inf / other.sup),
					Math.max(this.sup / other.inf, this.sup / other.sup));

			return new Interval(inf, sup);
		} else {
			throw new IntervalArithmeticException(
					"Division by interval containing zero");
		}
	}

	public Interval union(Interval other) {
		return new Interval(Math.min(this.inf, other.inf), Math.max(this.sup,
				other.sup));
	}

	public Interval intersection(Interval other) {
		if ((this.isEmpty()) || (other.isEmpty()) || (!(other.inf <= this.sup))
				|| (!(this.inf <= other.sup))) {
			return new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
		}
		return new Interval(Math.max(this.inf, other.inf), Math.min(this.sup,
				other.sup));
	}

	public Interval[] difference(Interval other) {
		if (!intersects(other)) {
			return new Interval[] { new Interval(Double.MAX_VALUE,
					Double.MIN_VALUE) };
		}
		if ((other.inf >= inf) && (other.sup <= sup)) {
			return new Interval[] { new Interval(inf, other.inf),
					new Interval(other.sup, sup) };
		}
		if ((other.inf <= inf) && (other.sup <= sup)) {
			return new Interval[] { new Interval(other.sup, sup) };
		}
		if (other.inf >= inf) {
			return new Interval[] { new Interval(inf, other.inf) };
		}
		return new Interval[] { new Interval(Double.MAX_VALUE, Double.MIN_VALUE) };
	}

	public boolean contains(double value) {
		return ((value >= this.inf) && (value <= this.sup));
	}

	public boolean contains(Interval other) {
		if (other.isEmpty()) {
			return true;
		}
		if (this.isEmpty()) {
			return false;
		}
		return ((other.inf >= this.inf) && (other.sup <= this.sup));
	}

	public Interval[] split(double value) {
		Interval[] intervals = new Interval[2];
		if (this.isEmpty()) {
			intervals[0] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
			intervals[1] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
		} else if (value < inf) {
			intervals[0] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
			intervals[1] = new Interval(value, value);
		} else if (value > sup) {
			intervals[0] = new Interval(value, value);
			intervals[1] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
		} else {
			intervals[0] = new Interval(inf, value);
			intervals[1] = new Interval(value, sup);
		}
		return intervals;
	}

	public Interval[] split(Interval other) {
		if (!intersects(other)) {
			return new Interval[] { new Interval(Double.MAX_VALUE,
					Double.MIN_VALUE) };
		}
		if (contains(other)) {
			return new Interval[] { new Interval(inf, other.inf),
					new Interval(other.inf, other.sup),
					new Interval(other.sup, sup) };
		} else {
			if ((contains(other.inf)) && (this.sup != other.inf)) {
				return split(other.inf);
			} else if ((contains(other.sup)) && (this.inf != other.sup)) {
				return split(other.sup);
			} else {
				return new Interval[] { new Interval(inf, sup) };
			}
		}
	}

	@Override
	public int compareTo(Interval other) {
		return Double.valueOf(inf).compareTo(Double.valueOf(other.inf))
				+ Double.valueOf(sup).compareTo(Double.valueOf(other.sup));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(inf);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sup);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Interval other = (Interval) obj;
		if (Double.doubleToLongBits(inf) != Double.doubleToLongBits(other.inf)) {
			return false;
		}
		if (Double.doubleToLongBits(sup) != Double.doubleToLongBits(other.sup)) {
			return false;
		}
		return true;
	}

	@Override
	public Interval clone() {
		return new Interval(inf, sup);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[" + inf + "," + sup + "]";
	}
}
