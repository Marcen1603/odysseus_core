/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.interval.datatype;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Interval<T extends Number> implements Serializable, IClone, Comparable<Interval<T>> {

    /**
     *
     */
    private static final long serialVersionUID = -5098617654305826274L;
    private final T inf;
    private final T sup;
    private final boolean leftOpen;
    private final boolean rightOpen;

    /**
     * 
     * Creates a new closed interval.
     *
     * @param inf
     * @param sup
     */
    public Interval(final T inf, final T sup) {
        this.inf = inf;
        this.sup = sup;
        this.rightOpen = false;
        this.leftOpen = false;
    }

    /**
     * 
     * Class constructor.
     *
     * @param inf
     * @param sup
     * @param open
     */
    public Interval(final T inf, final T sup, final boolean open) {
        this.inf = inf;
        this.sup = sup;
        this.rightOpen = open;
        this.leftOpen = open;
    }

    /**
     * 
     * Class constructor.
     *
     * @param inf
     * @param sup
     * @param left
     * @param right
     */
    public Interval(final T inf, final T sup, final boolean left, final boolean right) {
        this.inf = inf;
        this.sup = sup;
        this.rightOpen = right;
        this.leftOpen = left;
    }

    /**
     * 
     * Class constructor.
     *
     * @param clone
     */
    public Interval(Interval<T> clone) {
        this.inf = clone.inf;
        this.sup = clone.sup;
        this.rightOpen = clone.rightOpen;
        this.leftOpen = clone.leftOpen;
    }

    public T inf() {
        return this.inf;
    }

    public T sup() {
        return this.sup;
    }

    public boolean isEmpty() {
        if (isOpen() || isRightOpen() || isLeftOpen()) {
            return (this.inf.doubleValue() >= this.sup.doubleValue());
        }
        return (this.inf.doubleValue() > this.sup.doubleValue());
    }

    public boolean isOpen() {
        return isRightOpen() && isLeftOpen();
    }

    public boolean isClosed() {
        return isRightClosed() && isLeftClosed();
    }

    public boolean isRightOpen() {
        return this.rightOpen;
    }

    public boolean isRightClosed() {
        return !this.rightOpen;
    }

    public boolean isLeftOpen() {
        return this.leftOpen;
    }

    public boolean isLeftClosed() {
        return !this.leftOpen;
    }

    public boolean contains(final T value) {
        if (isOpen()) {
            return ((value.doubleValue() > this.inf.doubleValue()) && (value.doubleValue() < this.sup.doubleValue()));
        }
        if (isRightOpen()) {
            return ((value.doubleValue() >= this.inf.doubleValue()) && (value.doubleValue() < this.sup.doubleValue()));
        }
        if (isLeftOpen()) {
            return ((value.doubleValue() > this.inf.doubleValue()) && (value.doubleValue() <= this.sup.doubleValue()));
        }
        return ((value.doubleValue() >= this.inf.doubleValue()) && (value.doubleValue() <= this.sup.doubleValue()));

    }

    public boolean contains(final Interval<T> other) {
        if (other.isEmpty()) {
            return true;
        }
        if (this.isEmpty()) {
            return false;
        }
        if (this.equals(other)) {
            return true;
        }
        if (isOpen() && other.isRightOpen()) {
            return ((other.inf.doubleValue() > this.inf.doubleValue()) && (other.sup.doubleValue() <= this.sup.doubleValue()));
        }
        if (isOpen() && other.isLeftOpen()) {
            return ((other.inf.doubleValue() >= this.inf.doubleValue()) && (other.sup.doubleValue() < this.sup.doubleValue()));
        }
        if (isOpen() && other.isClosed()) {
            return ((other.inf.doubleValue() > this.inf.doubleValue()) && (other.sup.doubleValue() < this.sup.doubleValue()));
        }

        if (isRightOpen() && other.isLeftOpen()) {
            return ((other.inf.doubleValue() >= this.inf.doubleValue()) && (other.sup.doubleValue() < this.sup.doubleValue()));
        }
        if (isRightOpen() && other.isClosed()) {
            return ((other.inf.doubleValue() >= this.inf.doubleValue()) && (other.sup.doubleValue() < this.sup.doubleValue()));
        }

        if (isLeftOpen() && other.isRightOpen()) {
            return ((other.inf.doubleValue() > this.inf.doubleValue()) && (other.sup.doubleValue() <= this.sup.doubleValue()));
        }

        if (isLeftOpen() && other.isClosed()) {
            return ((other.inf.doubleValue() > this.inf.doubleValue()) && (other.sup.doubleValue() <= this.sup.doubleValue()));
        }

        return ((other.inf.doubleValue() >= this.inf.doubleValue()) && (other.sup.doubleValue() <= this.sup.doubleValue()));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Interval<T> other) {
        return Double.valueOf(this.inf.doubleValue()).compareTo(Double.valueOf(other.inf.doubleValue())) + Double.valueOf(this.sup.doubleValue()).compareTo(Double.valueOf(other.sup.doubleValue()));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.inf.doubleValue());
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.sup.doubleValue());
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     *
     * {@inheritDoc}
     */
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
        @SuppressWarnings("unchecked")
        final Interval<T> other = (Interval<T>) obj;
        if (Double.doubleToLongBits(this.inf.doubleValue()) != Double.doubleToLongBits(other.inf.doubleValue())) {
            return false;
        }
        if (Double.doubleToLongBits(this.sup.doubleValue()) != Double.doubleToLongBits(other.sup.doubleValue())) {
            return false;
        }
        if (this.leftOpen != other.leftOpen || this.rightOpen != other.rightOpen) {
            return false;
        }
        return true;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Interval<T> clone() {
        return new Interval<>(this);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return (isLeftOpen() ? "(" : "[") + this.inf + "," + this.sup + (isRightOpen() ? ")" : "]");
    }
}
