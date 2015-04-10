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

    @Override
    public double[] toArray() {
        return new double[] { this.C, this.M, this.Y, this.K };
    }

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
