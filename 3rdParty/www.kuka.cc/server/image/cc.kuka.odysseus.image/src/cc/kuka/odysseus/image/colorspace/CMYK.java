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
package cc.kuka.odysseus.image.colorspace;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class CMYK extends AbstractColorSpace {

    // The cyan color (C)
    public final double C;
    // The magenta color (M)
    public final double M;
    // The yellow color (Y)
    public final double Y;
    // The black key (K)
    public final double K;

    /**
     * Class constructor.
     *
     * @param C
     * @param M
     * @param Y
     * @param K
     */
    public CMYK(final double C, final double M, final double Y, final double K) {
        super();
        this.C = C;
        this.M = M;
        this.Y = Y;
        this.K = K;
    }

    public CMY toCMY() {
        final double CPrime = ((this.C * (1.0 - this.K)) + this.K);
        final double MPrime = ((this.M * (1.0 - this.K)) + this.K);
        final double YPrime = ((this.Y * (1.0 - this.K)) + this.K);
        return new CMY(CPrime, MPrime, YPrime);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return new double[] { this.C, this.M, this.Y, this.K };
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        return this.toCMY().toRGB();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.C);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.K);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.M);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.Y);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
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
        final CMYK other = (CMYK) obj;
        if (Double.doubleToLongBits(this.C) != Double.doubleToLongBits(other.C)) {
            return false;
        }
        if (Double.doubleToLongBits(this.K) != Double.doubleToLongBits(other.K)) {
            return false;
        }
        if (Double.doubleToLongBits(this.M) != Double.doubleToLongBits(other.M)) {
            return false;
        }
        if (Double.doubleToLongBits(this.Y) != Double.doubleToLongBits(other.Y)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CMYK [C=" + this.C + ", M=" + this.M + ", Y=" + this.Y + ", K=" + this.K + "]";
    }

}
