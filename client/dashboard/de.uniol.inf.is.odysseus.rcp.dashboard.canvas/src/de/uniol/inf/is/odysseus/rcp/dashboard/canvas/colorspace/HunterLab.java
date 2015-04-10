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
public class HunterLab extends AbstractColorSpace {
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
    public HunterLab(final double L, final double a, final double b) {
        super();
        this.L = L;
        this.a = a;
        this.b = b;
    }

    public XYZ toXYZ() {
        double Y = this.L / 10.0;
        double X = ((this.a / 17.5) * this.L) / 10.0;
        double Z = ((this.b / 7.0) * this.L) / 10.0;

        Y = Math.pow(Y, 2.0);
        X = (X + Y) / 1.02;
        Z = -(Z - Y) / 0.847;
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
        final HunterLab other = (HunterLab) obj;
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
        return "HunterLab [L=" + this.L + ", a=" + this.a + ", b=" + this.b + "]";
    }

}
