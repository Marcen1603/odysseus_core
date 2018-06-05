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
public class CIELCH extends AbstractColorSpace {
    public final double L;
    public final double C;
    public final double H;

    /**
     * Class constructor.
     *
     * @param L
     * @param C
     * @param H
     */
    public CIELCH(final double L, final double C, final double H) {
        super();
        this.L = L;
        this.C = C;
        this.H = H;
    }

    public CIELab toCIELab() {
        final double a = Math.cos(Math.toRadians(this.H)) * this.C;
        final double b = Math.sin(Math.toRadians(this.H)) * this.C;
        return new CIELab(this.L, a, b);
    }

    public CIELCH[] getTriadic() {
        final CIELCH[] result = new CIELCH[2];
        result[0] = new CIELCH(this.L, this.C, this.H + 120.0);
        result[1] = new CIELCH(this.L, this.C, this.H - 120.0);
        return result;
    }

    public CIELCH[] getSplitComplements() {
        final CIELCH[] result = new CIELCH[2];
        result[0] = new CIELCH(this.L, this.C, this.H + 150.0);
        result[1] = new CIELCH(this.L, this.C, this.H - 150.0);
        return result;
    }

    public CIELCH[] getAnalogous() {
        final CIELCH[] result = new CIELCH[2];
        result[0] = new CIELCH(this.L, this.C, this.H + 30.0);
        result[1] = new CIELCH(this.L, this.C, this.H - 30.0);
        return result;
    }

    public CIELCH[] getMonochromatic(final double CPrime) {
        final CIELCH[] result = new CIELCH[2];
        result[0] = new CIELCH(this.L, this.C + CPrime, this.H);
        result[1] = new CIELCH(this.L, this.C - CPrime, this.H);
        return result;
    }

    public CIELCH getComplement() {
        return new CIELCH(this.L, this.C, this.H + 180.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        return this.toCIELab().toRGB();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return new double[] { this.L, this.C, this.H };
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
        temp = Double.doubleToLongBits(this.H);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.L);
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
        final CIELCH other = (CIELCH) obj;
        if (Double.doubleToLongBits(this.C) != Double.doubleToLongBits(other.C)) {
            return false;
        }
        if (Double.doubleToLongBits(this.H) != Double.doubleToLongBits(other.H)) {
            return false;
        }
        if (Double.doubleToLongBits(this.L) != Double.doubleToLongBits(other.L)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CIELCH [L=" + this.L + ", C=" + this.C + ", H=" + this.H + "]";
    }

}
