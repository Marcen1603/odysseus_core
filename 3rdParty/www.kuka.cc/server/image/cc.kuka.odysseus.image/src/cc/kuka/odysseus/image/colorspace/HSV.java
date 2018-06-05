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
public class HSV extends AbstractColorSpace {
    public final double H;
    public final double S;
    public final double V;

    /**
     * Class constructor.
     *
     * @param H
     * @param S
     * @param V
     */
    public HSV(final double H, final double S, final double V) {
        super();
        this.H = H;
        this.S = S;
        this.V = V;
    }

    /**
     * Class constructor.
     *
     * @param H
     * @param S
     * @param V
     */
    public HSV(final int H, final int S, final int V) {
        super();
        this.H = H / 255.0;
        this.S = S / 255.0;
        this.V = V / 255.0;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        double R;
        double G;
        double B;
        if (this.S == 0.0) {
            R = this.V * 255.0;
            G = this.V * 255.0;
            B = this.V * 255.0;
        }
        else {
            double hPrime = this.H * 6.0;
            if (hPrime == 6.0) {
                hPrime = 0.0;
            }
            final double i = Math.floor(hPrime);
            final double x = this.V * (1.0 - this.S);
            final double y = this.V * (1.0 - (this.S * (hPrime - i)));
            final double z = this.V * (1.0 - (this.S * (1.0 - (hPrime - i))));

            if (i == 0.0) {
                R = this.V;
                G = z;
                B = x;
            }
            else if (i == 1.0) {
                R = y;
                G = this.V;
                B = x;
            }
            else if (i == 2.0) {
                R = x;
                G = this.V;
                B = z;
            }
            else if (i == 3.0) {
                R = x;
                G = y;
                B = this.V;
            }
            else if (i == 4.0) {
                R = z;
                G = x;
                B = this.V;
            }
            else {
                R = this.V;
                G = x;
                B = y;
            }

            R = R * 255.0;
            G = G * 255.0;
            B = B * 255.0;
        }
        return new RGB(R, G, B);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return new double[] { this.H, this.S, this.V };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.H);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.S);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.V);
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
        final HSV other = (HSV) obj;
        if (Double.doubleToLongBits(this.H) != Double.doubleToLongBits(other.H)) {
            return false;
        }
        if (Double.doubleToLongBits(this.S) != Double.doubleToLongBits(other.S)) {
            return false;
        }
        if (Double.doubleToLongBits(this.V) != Double.doubleToLongBits(other.V)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HSV [H=" + this.H + ", S=" + this.S + ", V=" + this.V + "]";
    }

}
