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

package de.uniol.inf.is.odysseus.probabilistic.common;

import java.io.Serializable;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Interval implements Serializable, Cloneable, Comparable<Interval> {
    /** Infinity interval. */
    public static final Interval MAX = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, false);
    /** Empty interval. */
    public static final Interval EMPTY = new Interval(Double.NaN, Double.NaN, false, false);

    /**
     * 
     */
    private static final long serialVersionUID = -6115929417128254867L;
    /** The infimum of the interval. */
    private final double inf;
    /** Infimum inclusive. */
    private final boolean infInclusive;
    /** The supremum of the interval. */
    private final double sup;
    /** Supremum inclusive. */
    private final boolean supInclusive;

    /**
     * Creates a new interval with the given infimum and supremum.
     * 
     * @param inf
     *            The infimum
     * @param sup
     *            The supremum
     * @return The interval
     */
    public static Interval of(final double inf, final double sup) {
        return new Interval(inf, sup, true, true);
    }

    /**
     * Creates a new interval with the given infimum and supremum.
     * 
     * @param inf
     *            The infimum
     * @param sup
     *            The supremum
     * @param infInclusive
     *            Infimum inclusive/exclusive
     * @param supInclusive
     *            Supremum inclusive/exclusive
     * @return The interval
     */
    public static Interval of(final double inf, final double sup, final boolean infInclusive, final boolean supInclusive) {
        return new Interval(inf, sup, infInclusive, supInclusive);
    }

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
        this.infInclusive = true;
        this.supInclusive = true;
    }

    /**
     * Creates a new interval with the given infimum and supremum.
     * 
     * @param inf
     *            The infimum
     * @param sup
     *            The supremum
     * @param infInclusive
     *            Infimum inclusive/exclusive
     * @param supInclusive
     *            Supremum inclusive/exclusive
     */
    public Interval(final double inf, final double sup, final boolean infInclusive, final boolean supInclusive) {
        this.inf = inf;
        this.sup = sup;
        this.infInclusive = infInclusive;
        this.supInclusive = supInclusive;
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
        return ((!this.isEmpty()) && (!other.isEmpty()) && //
                (//
                 // [a,b] [c,d]
                (this.infInclusive && this.supInclusive && other.infInclusive && other.supInclusive && (other.inf <= this.sup) && (this.inf <= other.sup)) || //
                // (a,b] [c,d]
                        (!this.infInclusive && this.supInclusive && other.infInclusive && other.supInclusive && (other.inf <= this.sup) && (this.inf < other.sup)) || //
                        // [a,b) [c,d]
                        (this.infInclusive && !this.supInclusive && other.infInclusive && other.supInclusive && (other.inf < this.sup) && (this.inf <= other.sup)) || //
                        // (a,b) [c,d]
                        (!this.infInclusive && !this.supInclusive && other.infInclusive && other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //

                        // [a,b] (c,d]
                        (this.infInclusive && this.supInclusive && !other.infInclusive && other.supInclusive && (other.inf < this.sup) && (this.inf <= other.sup)) || //
                        // (a,b] (c,d]
                        (!this.infInclusive && this.supInclusive && !other.infInclusive && other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //
                        // [a,b) (c,d]
                        (this.infInclusive && !this.supInclusive && !other.infInclusive && other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //
                        // (a,b) (c,d]
                        (!this.infInclusive && !this.supInclusive && !other.infInclusive && other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //

                        // [a,b] [c,d)
                        (this.infInclusive && this.supInclusive && other.infInclusive && !other.supInclusive && (other.inf <= this.sup) && (this.inf < other.sup)) || //
                        // (a,b] [c,d)
                        (!this.infInclusive && this.supInclusive && other.infInclusive && !other.supInclusive && (other.inf <= this.sup) && (this.inf < other.sup)) || //
                        // [a,b) [c,d)
                        (this.infInclusive && !this.supInclusive && other.infInclusive && !other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //
                        // (a,b) [c,d)
                        (!this.infInclusive && !this.supInclusive && other.infInclusive && !other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //

                        // [a,b] (c,d)
                        (this.infInclusive && this.supInclusive && !other.infInclusive && !other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //
                        // (a,b] (c,d)
                        (!this.infInclusive && this.supInclusive && !other.infInclusive && !other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //
                        // [a,b) (c,d)
                        (this.infInclusive && !this.supInclusive && !other.infInclusive && !other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) || //
                        // (a,b) (c,d)
                        (!this.infInclusive && !this.supInclusive && !other.infInclusive && !other.supInclusive && (other.inf < this.sup) && (this.inf < other.sup)) //
                ));
    }

    /**
     * Checks whether this interval is empty.
     * 
     * @return <code>true</code> if this interval is empty
     */
    public final boolean isEmpty() {
        // [a,b]
        if (infInclusive && supInclusive) {
            return this.inf > this.sup;
        }
        // (a,b] || [a,b) || (a,b)
        return this.inf >= this.sup;
    }

    /**
     * Checks whether this interval is NaN.
     * 
     * @return <code>true</code> if this interval is NaN
     */
    public final boolean isNaN() {
        return ((Double.isNaN(this.inf)) || (Double.isNaN(this.sup)));
    }

    /**
     * Gets the length of this interval.
     * 
     * @return The length of the interval
     */
    public final double length() {
        if (!this.isEmpty()) {
            return this.sup - this.inf;
        } else {
            return 0.0;
        }
    }

    /**
     * Adds the other to this interval and returns the new interval.
     * 
     * \f$ [a, b] + [c, d] = [a + c, b + d] \f$
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval add(final Interval other) {
        double a = this.inf;
        double b = this.sup;
        double c = other.inf;
        double d = other.sup;
        boolean l = this.infInclusive && other.infInclusive;
        boolean r = this.supInclusive && other.supInclusive;
        return Interval.of(a + c, b + d, l, r);
    }

    /**
     * Adds the given value to this interval and returns the new interval.
     * 
     * @param v
     *            The value
     * @return The result of the operation
     */
    public final Interval add(final double v) {
        double a = this.inf;
        double b = this.sup;
        boolean l = this.infInclusive;
        boolean r = this.supInclusive;
        return Interval.of(a + v, b + v, l, r);
    }

    /**
     * Subtracts the other from this interval and returns the new interval.
     * 
     * \f$ [a, b] - [c, d] = [a - d, b - c] \f$
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval subtract(final Interval other) {
        double a = this.inf;
        double b = this.sup;
        double c = other.inf;
        double d = other.sup;
        boolean l = this.infInclusive && other.infInclusive;
        boolean r = this.supInclusive && other.supInclusive;
        return Interval.of(a - d, b - c, l, r);
    }

    /**
     * Subtracts the given value from this interval and returns the new
     * interval.
     * 
     * @param value
     *            The value
     * @return The result of the operation
     */
    public final Interval subtract(final double v) {
        double a = this.inf;
        double b = this.sup;
        boolean l = this.infInclusive;
        boolean r = this.supInclusive;
        return Interval.of(a - v, b - v, l, r);
    }

    /**
     * Multiplies the other to this interval and returns the new interval.
     * 
     * \f$ [a, b] × [c, d] = [\min(a × c, a × d, b × c, b × d), \max(a × c, a ×
     * d, b × c, b × d)] \f$
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval multiply(final Interval other) {
        double a = this.inf;
        double b = this.sup;
        double c = other.inf;
        double d = other.sup;

        boolean l = this.infInclusive && other.infInclusive;
        boolean r = this.supInclusive && other.supInclusive;

        if ((a >= 0) && (b >= 0)) {
            return Interval.of(a * c, b * d, l, r);

        }
        double ac = a * c;
        double ad = a * d;
        double bc = b * c;
        double bd = b * d;

        return Interval.of(Math.min(Math.min(ac, ad), Math.min(bc, bd)), Math.max(Math.max(ac, ad), Math.max(bc, bd)), l, r);
    }

    /**
     * Multiplies the given value from this interval and returns the new
     * interval.
     * 
     * @param v
     *            The value
     * @return The result of the operation
     */
    public final Interval multiply(final double v) {
        double a = this.inf;
        double b = this.sup;

        boolean l = this.infInclusive;
        boolean r = this.supInclusive;
        return Interval.of(a * v, b * v, l, r);
    }

    /**
     * Divides this interval by the other and returns the new interval.
     * 
     * The estimation of [a,b]/[c,d] is based of Ratz relational division
     * operator: D. Ratz. On extended interval arithmetic and inclusion
     * isotonicity. Institut für Angewandte Mathematik, Universität Karlsruhe,
     * 1996.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval divide(final Interval other) {
        double a = this.inf;
        double b = this.sup;
        double c = other.inf;
        double d = other.sup;

        boolean l = this.infInclusive && other.infInclusive;
        boolean r = this.supInclusive && other.supInclusive;

        if (!other.contains(0.0)) {
            return this.multiply(Interval.of(1.0 / d, 1.0 / c, other.infInclusive, other.supInclusive));
        }
        if ((this.contains(0.0)) && (other.contains(0.0))) {
            return Interval.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, false);
        }
        if (b < 0.0 && c < d && d < 0.0) {
            return Interval.of(b / c, Double.POSITIVE_INFINITY, l, false);
        }
        if (b < 0.0 && c < 0.0 && 0.0 < d) {
            return Interval.of(Double.NEGATIVE_INFINITY, b / d, false, r).union(Interval.of(b / c, Double.POSITIVE_INFINITY, l, false));
        }
        if (b < 0.0 && c == 0.0 && c < d) {
            return Interval.of(Double.NEGATIVE_INFINITY, b / d, false, r);
        }
        if (0.0 < a && c < d && d == 0.0) {
            return Interval.of(Double.NEGATIVE_INFINITY, a / c, false, r);
        }
        if (0.0 < a && c < 0.0 && 0.0 < d) {
            return Interval.of(Double.NEGATIVE_INFINITY, a / c, false, r).union(Interval.of(a / d, Double.POSITIVE_INFINITY, l, false));
        }
        if (0.0 < a && c == 0.0 && c < d) {
            return Interval.of(a / d, Double.POSITIVE_INFINITY, l, false);
        }
        if ((!this.contains(0.0)) && (c == 0.0) && (d == 0.0)) {
            return Interval.EMPTY;
        }
        return Interval.EMPTY;
    }

    /**
     * Divides this interval by the given value and returns the new interval.
     * 
     * @param v
     *            The value
     * @return The result of the operation
     */
    public final Interval divide(final double v) {
        double a = this.inf;
        double b = this.sup;

        boolean l = this.infInclusive;
        boolean r = this.supInclusive;

        if (v != 0.0) {
            return Interval.of(a / v, b / v, l, r);
        }

        return Interval.EMPTY;
    }

    /**
     * Returns the union of this interval and the other.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval union(final Interval other) {
        final boolean infInclusive;
        if (this.inf < other.inf) {
            infInclusive = this.infInclusive;
        } else if (other.inf < this.inf) {
            infInclusive = other.infInclusive;
        } else {
            infInclusive = this.infInclusive || other.infInclusive;
        }
        final boolean supInclusive;
        if (this.sup > other.sup) {
            supInclusive = this.supInclusive;
        } else if (other.sup > this.sup) {
            supInclusive = other.supInclusive;
        } else {
            supInclusive = this.supInclusive || other.supInclusive;
        }
        return new Interval(Math.min(this.inf, other.inf), Math.max(this.sup, other.sup), infInclusive, supInclusive);
    }

    /**
     * Returns the intersection of this interval and the other.
     * 
     * @param other
     *            The other interval
     * @return The result of the operation
     */
    public final Interval intersection(final Interval other) {
        if (!intersects(other)) {
            return new Interval(Double.NaN, Double.NaN);
        }
        boolean infInclusive = this.inf > other.inf ? this.infInclusive : other.infInclusive;
        boolean supInclusive = this.sup < other.sup ? this.supInclusive : other.supInclusive;
        return new Interval(Math.max(this.inf, other.inf), Math.min(this.sup, other.sup), infInclusive, supInclusive);
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
            return new Interval[] { new Interval(Double.NaN, Double.NaN) };
        }
        if ((other.inf >= this.inf) && (other.sup <= this.sup)) {
            return new Interval[] { new Interval(this.inf, other.inf, this.infInclusive, other.infInclusive), new Interval(other.sup, this.sup, other.supInclusive, this.supInclusive) };
        }
        if ((other.inf <= this.inf) && (other.sup <= this.sup)) {
            return new Interval[] { new Interval(other.sup, this.sup, other.supInclusive, this.supInclusive) };
        }
        if (other.inf >= this.inf) {
            return new Interval[] { new Interval(this.inf, other.inf, this.infInclusive, other.infInclusive) };
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
        if (this.infInclusive && this.supInclusive) {
            return ((value >= this.inf) && (value <= this.sup));
        }
        if (!this.infInclusive && this.supInclusive) {
            return ((value > this.inf) && (value <= this.sup));
        }
        if (this.infInclusive && !this.supInclusive) {
            return ((value >= this.inf) && (value < this.sup));
        }
        if (!this.infInclusive && !this.supInclusive) {
            return ((value > this.inf) && (value < this.sup));
        }
        return false;
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
        return ((contains(other.inf)) && (contains(other.sup)));
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
        } else if (value < this.inf) {
            intervals[0] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
            intervals[1] = new Interval(value, value);
        } else if (value > this.sup) {
            intervals[0] = new Interval(value, value);
            intervals[1] = new Interval(Double.MAX_VALUE, Double.MIN_VALUE);
        } else {
            intervals[0] = new Interval(this.inf, value, this.infInclusive, true);
            intervals[1] = new Interval(value, this.sup, true, this.supInclusive);
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
            return new Interval[] { new Interval(this.inf, other.inf, this.infInclusive, this.supInclusive), new Interval(other.inf, other.sup, other.infInclusive, other.supInclusive),
                    new Interval(other.sup, this.sup, other.supInclusive, this.supInclusive) };
        } else {
            if ((this.contains(other.inf)) && (this.sup != other.inf)) {
                return this.split(other.inf);
            } else if ((this.contains(other.sup)) && (this.inf != other.sup)) {
                return this.split(other.sup);
            } else {
                return new Interval[] { new Interval(this.inf, this.sup, this.infInclusive, this.supInclusive) };
            }
        }
    }

    /**
     * Estimate the midpoint of the interval
     * @return The midpoint
     */
    public final double midpoint(){
        return (this.sup-this.inf)/2.0;
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
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.inf);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (this.infInclusive ? 1231 : 1237);
        temp = Double.doubleToLongBits(this.sup);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (this.supInclusive ? 1231 : 1237);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        if (Double.doubleToLongBits(this.inf) != Double.doubleToLongBits(other.inf)) {
            return false;
        }
        if (this.infInclusive != other.infInclusive) {
            return false;
        }
        if (Double.doubleToLongBits(this.sup) != Double.doubleToLongBits(other.sup)) {
            return false;
        }
        if (this.supInclusive != other.supInclusive) {
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
        return new Interval(this.inf, this.sup, this.infInclusive, this.supInclusive);
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.infInclusive) {
            sb.append("[");
        } else {
            sb.append("(");

        }
        if (this.inf == Double.NEGATIVE_INFINITY) {
            sb.append("-oo");
        } else {
            sb.append(this.inf);
        }
        sb.append(",");
        if (this.sup == Double.POSITIVE_INFINITY) {
            sb.append("oo");
        } else {
            sb.append(this.sup);
        }
        if (this.supInclusive) {
            sb.append("]");
        } else {
            sb.append(")");
        }
        return sb.toString();
    }

}
