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
public class CMY extends AbstractColorSpace {
    public final double C;
    public final double M;
    public final double Y;

    /**
     * Class constructor.
     *
     * @param C
     * @param M
     * @param Y
     */
    public CMY(final double C, final double M, final double Y) {
        super();
        this.C = C;
        this.M = M;
        this.Y = Y;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        final double R = (1.0 - this.C) * 255.0;
        final double G = (1.0 - this.M) * 255.0;
        final double B = (1.0 - this.Y) * 255.0;
        return new RGB(R, G, B);
    }

    public CMYK toCMYK() {
        double K = 1.0;

        if (this.C < K) {
            K = this.C;
        }
        if (this.M < K) {
            K = this.M;
        }
        if (this.Y < K) {
            K = this.Y;
        }
        if (K == 1) { // Black
            return new CMYK(0.0, 0.0, 0.0, K);
        }

        final double CPrime = (this.C - K) / (1.0 - K);
        final double MPrime = (this.M - K) / (1.0 - K);
        final double YPrime = (this.Y - K) / (1.0 - K);
        return new CMYK(CPrime, MPrime, YPrime, K);

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return new double[] { this.C, this.M, this.Y };
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
        final CMY other = (CMY) obj;
        if (Double.doubleToLongBits(this.C) != Double.doubleToLongBits(other.C)) {
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
        return "CMY [C=" + this.C + ", M=" + this.M + ", Y=" + this.Y + "]";
    }

}
