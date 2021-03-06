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
public class CIELab extends AbstractColorSpace {
    public final double L;
    public final double a;
    public final double b;

    /**
     * Class constructor.
     *
     * @param L
     * @param a
     * @param b
     */
    public CIELab(final double L, final double a, final double b) {
        super();
        this.L = L;
        this.a = a;
        this.b = b;
    }

    public XYZ toXYZ() {
        // Observer= 2°, Illuminant= D65
        final double xRef = 95.047;
        final double yRef = 100.000;
        final double zRef = 108.883;

        double Y = (this.L + 16.0) / 116.0;
        double X = (this.a / 500.0) + Y;
        double Z = Y - (this.b / 200.0);

        if (Math.pow(Y, 3) > 0.008856) {
            Y = Math.pow(Y, 3.0);
        }
        else {
            Y = (Y - (16.0 / 116.0)) / 7.787;
        }
        if (Math.pow(X, 3.0) > 0.008856) {
            X = Math.pow(X, 3.0);
        }
        else {
            X = (X - (16.0 / 116.0)) / 7.787;
        }
        if (Math.pow(Z, 3.0) > 0.008856) {
            Z = Math.pow(Z, 3.0);
        }
        else {
            Z = (Z - (16.0 / 116.0)) / 7.787;
        }

        X = xRef * X;
        Y = yRef * Y;
        Z = zRef * Z;
        return new XYZ(X, Y, Z);
    }

    public CIELCH toCIELCH() {
        double H = Math.atan2(this.b, this.a);
        if (H > 0.0) {
            H = (H / Math.PI) * 180.0;
        }
        else {
            H = 360.0 - ((Math.abs(H) / Math.PI) * 180.0);
        }

        final double C = Math.sqrt(Math.pow(this.a, 2.0) + Math.pow(this.b, 2.0));

        return new CIELCH(this.L, C, H);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        return this.toXYZ().toRGB();
    }

    @Override
    public double[] toArray() {
        return new double[] { this.L, this.a, this.b };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.L);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.a);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.b);
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
        final CIELab other = (CIELab) obj;
        if (Double.doubleToLongBits(this.L) != Double.doubleToLongBits(other.L)) {
            return false;
        }
        if (Double.doubleToLongBits(this.a) != Double.doubleToLongBits(other.a)) {
            return false;
        }
        if (Double.doubleToLongBits(this.b) != Double.doubleToLongBits(other.b)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CIELab [L=" + this.L + ", a=" + this.a + ", b=" + this.b + "]";
    }

}
