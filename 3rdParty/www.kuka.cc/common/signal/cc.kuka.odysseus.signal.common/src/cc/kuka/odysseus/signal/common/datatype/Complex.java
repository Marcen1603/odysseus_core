/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.signal.common.datatype;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Complex implements IClone {

    /** The imaginary part. */
    private final double imaginary;
    /** The real part. */
    private final double real;

    /**
     * Class constructor.
     *
     */
    public Complex(final double real, final double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Class constructor.
     *
     * @param clone
     */
    public Complex(final Complex clone) {
        this.real = clone.real;
        this.imaginary = clone.imaginary;
    }

    /**
     * @return the imaginary
     */
    public double getImaginary() {
        return this.imaginary;
    }

    /**
     * @return the real
     */
    public double getReal() {
        return this.real;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Complex clone() {
        return new Complex(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(imaginary);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(real);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Complex other = (Complex) obj;
        if (Double.doubleToLongBits(imaginary) != Double.doubleToLongBits(other.imaginary))
            return false;
        if (Double.doubleToLongBits(real) != Double.doubleToLongBits(other.real))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(getReal());
        sb.append(", ");
        sb.append(getImaginary());
        sb.append(")");
        return sb.toString();
    }
}
