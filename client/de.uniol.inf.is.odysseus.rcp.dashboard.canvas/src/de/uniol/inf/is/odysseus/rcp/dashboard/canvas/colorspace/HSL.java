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
public class HSL extends AbstractColorSpace {
    public final double H;
    public final double S;
    public final double L;

    /**
     * Class constructor.
     *
     * @param H
     * @param S
     * @param L
     */
    public HSL(final double H, final double S, final double L) {
        super();
        this.H = H;
        this.S = S;
        this.L = L;
    }

    @Override
    public RGB toRGB() {
        double R;
        double G;
        double B;
        if (this.S == 0.0) {
            R = this.L * 255.0;
            G = this.L * 255.0;
            B = this.L * 255.0;
        }
        else {
            double m;

            if (this.L < 0.5) {
                m = this.L * (1 + this.S);
            }
            else {
                m = (this.L + this.S) - (this.S * this.L);
            }

            final double x = (2.0 * this.L) - m;

            R = 255.0 * HSL.HueToRGB(x, m, this.H + (1.0 / 3.0));
            G = 255.0 * HSL.HueToRGB(x, m, this.H);
            B = 255.0 * HSL.HueToRGB(x, m, this.H - (1.0 / 3.0));
        }
        return new RGB(R, G, B);
    }

    private static double HueToRGB(final double x, final double m, final double H) {
        double HPrime = H;
        if (HPrime < 0.0) {
            HPrime += 1.0;
        }
        if (HPrime > 1.0) {
            HPrime -= 1.0;
        }
        if ((6.0 * HPrime) < 1.0) {
            return x + ((m - x) * 6.0 * HPrime);
        }
        if ((2.0 * HPrime) < 1.0) {
            return m;
        }
        if ((3.0 * HPrime) < 2.0) {
            return x + ((m - x) * ((2.0 / 3.0) - HPrime) * 6.0);
        }
        return x;
    }

    @Override
    public double[] toArray() {
        return new double[] { this.H, this.S, this.L };
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
        temp = Double.doubleToLongBits(this.L);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.S);
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
        final HSL other = (HSL) obj;
        if (Double.doubleToLongBits(this.H) != Double.doubleToLongBits(other.H)) {
            return false;
        }
        if (Double.doubleToLongBits(this.L) != Double.doubleToLongBits(other.L)) {
            return false;
        }
        if (Double.doubleToLongBits(this.S) != Double.doubleToLongBits(other.S)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HSL [H=" + this.H + ", S=" + this.S + ", L=" + this.L + "]";
    }

}
