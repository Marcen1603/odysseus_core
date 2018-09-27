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
public class Yxy extends AbstractColorSpace {
    public final double Y;
    public final double x;
    public final double y;

    /**
     * Class constructor.
     *
     * @param Y
     * @param x
     * @param y
     */
    public Yxy(final double Y, final double x, final double y) {
        super();
        this.Y = Y;
        this.x = x;
        this.y = y;
    }

    public XYZ toXYZ() {
        final double X = this.x * (this.Y / this.y);
        final double Z = (1.0 - this.x - this.y) * (this.Y / this.y);
        return new XYZ(X, this.Y, Z);
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
        return new double[] { this.Y, this.x, this.y };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.Y);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.x);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
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
        final Yxy other = (Yxy) obj;
        if (Double.doubleToLongBits(this.Y) != Double.doubleToLongBits(other.Y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Yxy [Y=" + this.Y + ", x=" + this.x + ", y=" + this.y + "]";
    }

}
