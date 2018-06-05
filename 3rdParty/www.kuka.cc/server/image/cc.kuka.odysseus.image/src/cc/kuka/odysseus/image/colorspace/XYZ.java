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
public class XYZ extends AbstractColorSpace {
    public final double X;
    public final double Y;
    public final double Z;

    /**
     * Class constructor.
     *
     * @param X
     * @param Y
     * @param Z
     */
    public XYZ(final double X, final double Y, final double Z) {
        super();
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        final double xPrime = this.X / 100.0;
        final double yPrime = this.Y / 100.0;
        final double zPrime = this.Z / 100.0;

        double R = (xPrime * 3.2406) + (yPrime * -1.5372) + (zPrime * -0.4986);
        double G = (xPrime * -0.9689) + (yPrime * 1.8758) + (zPrime * 0.0415);
        double B = (xPrime * 0.0557) + (yPrime * -0.2040) + (zPrime * 1.0570);

        if (R > 0.0031308) {
            R = (1.055 * (Math.pow(R, (1.0 / 2.4)))) - 0.055;
        }
        else {
            R = 12.92 * R;
        }
        if (G > 0.0031308) {
            G = (1.055 * (Math.pow(G, (1.0 / 2.4)))) - 0.055;
        }
        else {
            G = 12.92 * G;
        }
        if (B > 0.0031308) {
            B = (1.055 * (Math.pow(B, (1.0 / 2.4)))) - 0.055;
        }
        else {
            B = 12.92 * B;
        }

        R = R * 255.0;
        G = G * 255.0;
        B = B * 255.0;
        return new RGB(R, G, B);
    }

    public CIELab toCIELab() {
        // Observer= 2°, Illuminant= D65
        final double xRef = 95.047;
        final double yRef = 100.000;
        final double zRef = 108.883;

        double xPrime = this.X / xRef;
        double yPrime = this.Y / yRef;
        double zPrime = this.Z / zRef;

        if (xPrime > 0.008856) {
            xPrime = Math.pow(xPrime, (1.0 / 3.0));
        }
        else {
            xPrime = (7.787 * xPrime) + (16.0 / 116.0);
        }
        if (yPrime > 0.008856) {
            yPrime = Math.pow(yPrime, (1.0 / 3.0));
        }
        else {
            yPrime = (7.787 * yPrime) + (16.0 / 116.0);
        }
        if (zPrime > 0.008856) {
            zPrime = Math.pow(zPrime, (1.0 / 3.0));
        }
        else {
            zPrime = (7.787 * zPrime) + (16.0 / 116.0);
        }

        final double L = (116.0 * xPrime) - 16.0;
        final double a = 500.0 * (xPrime - yPrime);
        final double b = 200.0 * (yPrime - zPrime);
        return new CIELab(L, a, b);
    }

    public Yxy toYxy() {
        final double x = this.X / (this.X + this.Y + this.Z);
        final double y = this.Y / (this.X + this.Y + this.Z);
        return new Yxy(this.Y, x, y);
    }

    public HunterLab toHunterLab() {
        final double L = 10.0 * Math.sqrt(this.Y);
        final double a = 17.5 * (((1.02 * this.X) - this.Y) / Math.sqrt(this.Y));
        final double b = 7.0 * ((this.Y - (0.847 * this.Z)) / Math.sqrt(this.Y));
        return new HunterLab(L, a, b);
    }

    public CIELuv toCieLuv() {
        // Observer= 2°, Illuminant= D65
        final double xRef = 95.047;
        final double yRef = 100.000;
        final double zRef = 108.883;

        double u = (4 * this.X) / (this.X + (15 * this.Y) + (3 * this.Z));
        double v = (9 * this.Y) / (this.X + (15 * this.Y) + (3 * this.Z));

        double yPrime = this.Y / 100.0;
        if (yPrime > 0.008856) {
            yPrime = Math.pow(yPrime, (1.0 / 3.0));
        }
        else {
            yPrime = (7.787 * yPrime) + (16.0 / 116.0);
        }

        final double uRef = (4.0 * xRef) / (xRef + (15.0 * yRef) + (3.0 * zRef));
        final double vRef = (9.0 * yRef) / (xRef + (15.0 * yRef) + (3.0 * zRef));

        final double L = (116.0 * yPrime) - 16.0;
        u = 13.0 * L * (u - uRef);
        v = 13.0 * L * (v - vRef);
        return new CIELuv(L, u, v);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return new double[] { this.X, this.Y, this.Z };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.X);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.Y);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.Z);
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
        final XYZ other = (XYZ) obj;
        if (Double.doubleToLongBits(this.X) != Double.doubleToLongBits(other.X)) {
            return false;
        }
        if (Double.doubleToLongBits(this.Y) != Double.doubleToLongBits(other.Y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.Z) != Double.doubleToLongBits(other.Z)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "XYZ [X=" + this.X + ", Y=" + this.Y + ", Z=" + this.Z + "]";
    }

}
