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
public class CIELuv extends AbstractColorSpace {
    public final double L;
    public final double u;
    public final double v;

    /**
     * Class constructor.
     *
     * @param L
     * @param u
     * @param v
     */
    public CIELuv(final double L, final double u, final double v) {
        super();
        this.L = L;
        this.u = u;
        this.v = v;
    }

    public XYZ toXYZ() {
        final double xRef = 95.047;
        final double yRef = 100.000;
        final double zRef = 108.883;

        double Y = (this.L + 16.0) / 116.0;
        if (Math.pow(Y, 3.0) > 0.008856) {
            Y = Math.pow(Y, 3.0);
        }
        else {
            Y = (Y - (16.0 / 116.0)) / 7.787;
        }

        final double uRef = (4.0 * xRef) / (xRef + (15.0 * yRef) + (3.0 * zRef));
        final double vRef = (9.0 * yRef) / (xRef + (15.0 * yRef) + (3.0 * zRef));

        final double uPrime = (this.u / (13.0 * this.L)) + uRef;
        final double vPrime = (this.v / (13.0 * this.L)) + vRef;

        Y = Y * 100;
        final double X = -(9.0 * Y * uPrime) / (((uPrime - 4.0) * vPrime) - (uPrime * vPrime));
        final double Z = ((9.0 * Y) - (15.0 * vPrime * Y) - (vPrime * X)) / (3.0 * vPrime);
        return new XYZ(X, Y, Z);
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
        return new double[] { this.L, this.u, this.v };
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
        temp = Double.doubleToLongBits(this.u);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.v);
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
        final CIELuv other = (CIELuv) obj;
        if (Double.doubleToLongBits(this.L) != Double.doubleToLongBits(other.L)) {
            return false;
        }
        if (Double.doubleToLongBits(this.u) != Double.doubleToLongBits(other.u)) {
            return false;
        }
        if (Double.doubleToLongBits(this.v) != Double.doubleToLongBits(other.v)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CIELuv [L=" + this.L + ", u=" + this.u + ", v=" + this.v + "]";
    }

}
