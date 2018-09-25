/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.mep.commons.math;

import java.io.Serializable;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Interval implements Serializable, Cloneable, Comparable<Interval> {
    /** Infinity interval. */
    public static final Interval INFINITY = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    /**
	 * 
	 */
    private static final long serialVersionUID = -6115929417128254867L;
    /** The infimum of the interval. */
    private final double inf;
    /** The supremum of the interval. */
    private final double sup;

    /**
     * Creates a new interval with the given infimum and supremum.
     * 
     * @param inf
     *            The infimum
     * @param sup
     *            The supremum
     */
    public Interval(final double inf, final double sup) {
        this.inf = inf;
        this.sup = sup;
    }

    /**
     * Gets the infimum of the interval.
     * 
     * @return The infimum
     */
    public final double inf() {
        return this.inf;
    }

    /**
     * Gets the supremum of the interval.
     * 
     * @return The supremum
     */
    public final double sup() {
        return this.sup;
    }

    /**
     * Checks whether this interval and the other intersects.
     * 
     * @param other
     *            The other interval
     * @return <code>true</code> if this interval and the other intersects
     */
    public final boolean intersects(final Interval other) {
        return ((!this.isEmpty()) && (!other.isEmpty()) && (other.inf <= this.sup) && (this.inf <= other.sup));
    }

    /**
     * Checks whether this interval is empty.
     * 
     * @return <code>true</code> if this interval is empty
     */
    public final boolean isEmpty() {
        return !(this.inf <= this.sup);
    }

    /**
     * Gets the length of this interval.
     * 
     * @return The length of the interval
     */
    public final double length() {
        if (!this.isEmpty()) {
            return this.sup - this.inf;
        }
        else {
            return 0.0;
        }
    }

    /**
     * Adds the other to this interval and returns the new interval.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval add(final Interval other) {
        return new Interval(this.inf + other.inf, this.sup + other.sup);
    }

    /**
     * Adds the given value to this interval and returns the new interval.
     * 
     * @param value
     *            The value
     * @return The result of the operation
     */
    public final Interval add(final double value) {
        return new Interval(this.inf + value, this.sup + value);
    }

    /**
     * Subtracts the other from this interval and returns the new interval.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval subtract(final Interval other) {
        return new Interval(this.inf - other.sup, this.sup - other.inf);
    }

    /**
     * Subtracts the given value from this interval and returns the new
     * interval.
     * 
     * @param value
     *            The value
     * @return The result of the operation
     */
    public final Interval subtract(final double value) {
        return new Interval(this.inf - value, this.sup - value);
    }

    /**
     * Multiplies the other to this interval and returns the new interval.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval multiply(final Interval other) {
        final double newInf = Math.min(Math.min(this.inf * other.inf, this.inf * other.sup), Math.min(this.sup * other.inf, this.sup * other.sup));
        final double newSup = Math.max(Math.max(this.inf * other.inf, this.inf * other.sup), Math.max(this.sup * other.inf, this.sup * other.sup));

        return new Interval(newInf, newSup);
    }

    /**
     * Multiplies the given value from this interval and returns the new
     * interval.
     * 
     * @param value
     *            The value
     * @return The result of the operation
     */
    public final Interval multiply(final double value) {
        return new Interval(Math.min(this.sup * value, this.inf * value), Math.max(this.sup * value, this.inf * value));
    }

    /**
     * Divides this interval by the other and returns the new interval.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     * @throws IntervalArithmeticException
     *             if the other interval contains zero
     */
    public final Interval divide(final Interval other) throws IntervalArithmeticException {
        if ((other.inf() == 0.0) && (other.sup() == 0.0)) {
            return new Interval(Double.NaN, Double.NaN);
        }
        else if (0.0 <= other.inf()) {
            final double inf = Math.min(Math.min(divide(this.inf(), other.inf()), divide(this.inf(), other.sup())), Math.min(divide(this.sup(), other.inf()), divide(this.sup(), other.sup())));
            final double sup = Math.max(Math.max(divide(this.inf(), other.inf()), divide(this.inf(), other.sup())), Math.max(divide(this.sup(), other.inf()), divide(this.sup(), other.sup())));
            return new Interval(inf, sup);
        }
        else if (other.sup() <= 0.0) {
            return (new Interval(-this.sup(), -this.inf())).divide(new Interval(-other.sup(), -other.inf()));
        }
        else {
            Interval left = this.divide(new Interval(other.inf(), 0.0));
            Interval right = this.divide(new Interval(0.0, other.sup()));
            return left.union(right);
        }
    }

    /**
     * Divides this interval by the given value and returns the new interval.
     * 
     * @param value
     *            The value
     * @return The result of the operation
     */
    public final Interval divide(final double value) {
        return new Interval(Math.min(this.inf / value, this.sup / value), Math.max(this.inf / value, this.sup / value));
    }

    /**
     * Returns the union of this interval and the other.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval union(final Interval other) {
        return new Interval(Math.min(this.inf, other.inf), Math.max(this.sup, other.sup));
    }

    /**
     * Returns the intersection of this interval and the other.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval intersection(final Interval other) {
        if ((this.isEmpty()) || (other.isEmpty()) || (!(other.inf <= this.sup)) || (!(this.inf <= other.sup))) {
            return new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
        }
        return new Interval(Math.max(this.inf, other.inf), Math.min(this.sup, other.sup));
    }

    /**
     * Returns the difference between this interval and the other.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval[] difference(final Interval other) {
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

    /**
     * Checks whether the given value is in this interval.
     * 
     * @param value
     *            The value
     * @return <code>true</code> if the value is in this interval
     */
    public final boolean contains(final double value) {
        return ((value >= this.inf) && (value <= this.sup));
    }

    /**
     * Checks whether the given interval is in this interval.
     * 
     * @param other
     *            The other interval
     * @return <code>true</code> if the interval is in this interval
     */
    public final boolean contains(final Interval other) {
        if (other.isEmpty()) {
            return true;
        }
        if (this.isEmpty()) {
            return false;
        }
        return ((other.inf >= this.inf) && (other.sup <= this.sup));
    }

    /**
     * Split this interval at the given value.
     * 
     * @param value
     *            The value
     * @return The resulting intervals
     */
    public final Interval[] split(final double value) {
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

    /**
     * Split this interval at the given interval.
     * 
     * @param other
     *            The other interval
     * @return The resulting intervals
     */
    public final Interval[] split(final Interval other) {
        if (!this.intersects(other)) {
            return new Interval[] { new Interval(Double.MAX_VALUE, Double.MIN_VALUE) };
        }
        if (this.contains(other)) {
            return new Interval[] { new Interval(this.inf, other.inf), new Interval(other.inf, other.sup), new Interval(other.sup, this.sup) };
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

    private final double divide(final double a, final double b) {
        if ((Double.isInfinite(a)) && (b == 0.0)) {
            return 0.0;
        }
        else if ((a == 0.0) && (b == 0.0)) {
            return 0.0;
        }
        else if (Double.isInfinite(b)) {
            return 0.0;
        }
        else if ((a > 0.0) && (b == 0.0)) {
            return Double.POSITIVE_INFINITY;
        }
        else if ((a < 0.0) && (b == 0.0)) {
            return Double.NEGATIVE_INFINITY;
        }
        else {
            return a / b;
        }
    }

    /*
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo(final Interval other) {
        return Double.valueOf(this.inf).compareTo(Double.valueOf(other.inf)) + Double.valueOf(this.sup).compareTo(Double.valueOf(other.sup));
    }

    /*
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.inf);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.sup);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /*
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
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

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final Interval clone() {
        return new Interval(this.inf, this.sup);
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (this.inf == Double.NEGATIVE_INFINITY) {
            sb.append("-oo");
        }
        else {
            sb.append(this.inf);
        }
        sb.append(",");
        if (this.sup == Double.POSITIVE_INFINITY) {
            sb.append("oo");
        }
        else {
            sb.append(this.sup);
        }
        sb.append("]");
        return sb.toString();
    }

}
