/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace;

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
