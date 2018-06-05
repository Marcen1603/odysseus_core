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
 * @version $Id: RGB.java | Fri Apr 10 23:28:57 2015 +0000 | ckuka $
 *
 */
public class RGB extends AbstractColorSpace {
    public static final RGB BLACK = new RGB(0, 0, 0);
    public static final RGB RED = new RGB(255, 0, 0);
    public static final RGB GREEN = new RGB(0, 255, 0);
    public static final RGB BLUE = new RGB(0, 0, 255);

    public static final RGB WHITE = new RGB(255, 255, 255);

    // The red color (R)
    public final double R;
    // The green color (G)
    public final double G;
    // The blue color (B)
    public final double B;

    /**
     * Class constructor.
     *
     * @param R
     * @param G
     * @param B
     */
    public RGB(final double R, final double G, final double B) {
        super();
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public RGB[] getTriadic() {
        final RGB[] result = new RGB[2];
        final CIELCH colorspace = this.toCIELCH();
        final CIELCH[] triadic = colorspace.getTriadic();
        result[0] = triadic[0].toCIELab().toXYZ().toRGB();
        result[1] = triadic[1].toCIELab().toXYZ().toRGB();
        return result;
    }

    public RGB[] getSplitComplements() {
        final RGB[] result = new RGB[2];
        final CIELCH colorspace = this.toCIELCH();
        final CIELCH[] splitComplements = colorspace.getSplitComplements();
        result[0] = splitComplements[0].toCIELab().toXYZ().toRGB();
        result[1] = splitComplements[1].toCIELab().toXYZ().toRGB();
        return result;
    }

    public RGB[] getAnalogous() {
        final RGB[] result = new RGB[2];
        final CIELCH colorspace = this.toCIELCH();
        final CIELCH[] analogous = colorspace.getAnalogous();
        result[0] = analogous[0].toCIELab().toXYZ().toRGB();
        result[1] = analogous[1].toCIELab().toXYZ().toRGB();
        return result;
    }

    public RGB[] getMonochromatic(final double CPrime) {
        final RGB[] result = new RGB[2];
        final CIELCH colorspace = this.toCIELCH();
        final CIELCH[] monochromatic = colorspace.getMonochromatic(CPrime);
        result[0] = monochromatic[0].toCIELab().toXYZ().toRGB();
        result[1] = monochromatic[1].toCIELab().toXYZ().toRGB();
        return result;
    }

    public RGB getComplement() {
        final CIELCH colorspace = this.toCIELCH();
        return colorspace.getComplement().toCIELab().toXYZ().toRGB();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RGB toRGB() {
        return this;
    }

    @Override
    public double[] toArray() {
        return new double[] { this.R, this.G, this.B };
    }

    public CIELab toCIELab() {
        return this.toXYZ().toCIELab();
    }

    public CIELCH toCIELCH() {
        return this.toXYZ().toCIELab().toCIELCH();

    }

    public CIELuv toCIELuv() {
        return this.toXYZ().toCieLuv();
    }

    public CMY toCMY() {
        final double C = 1.0 - (this.R / 255.0);
        final double M = 1.0 - (this.G / 255.0);
        final double Y = 1.0 - (this.B / 255.0);
        return new CMY(C, M, Y);
    }

    public CMYK toCMYK() {
        return this.toCMY().toCMYK();
    }

    public HSL toHSL() {
        final double rPrime = this.R / 255.0;
        final double gPrime = this.G / 255.0;
        final double bPrime = this.B / 255.0;

        final double min = Math.min(rPrime, Math.min(gPrime, bPrime));
        final double max = Math.max(rPrime, Math.max(gPrime, bPrime));
        final double delta = max - min;

        double H = 0.0;
        double S;
        final double L = (max + min) / 2.0;

        if (delta == 0.0) {
            H = 0.0;
            S = 0.0;
        }
        else {
            if (L < 0.5) {
                S = delta / (max + min);
            }
            else {
                S = delta / (2.0 - max - min);
            }

            final double detlaR = (((max - rPrime) / 6.0) + (delta / 2.0)) / delta;
            final double deltaG = (((max - gPrime) / 6.0) + (delta / 2.0)) / delta;
            final double deltaB = (((max - bPrime) / 6.0) + (delta / 2.0)) / delta;

            if (rPrime == max) {
                H = deltaB - deltaG;
            }
            else if (gPrime == max) {
                H = ((1.0 / 3.0) + detlaR) - deltaB;
            }
            else if (bPrime == max) {
                H = ((2.0 / 3.0) + deltaG) - detlaR;
            }

            if (H < 0.0) {
                H += 1.0;
            }
            if (H > 1.0) {
                H -= 1.0;
            }
        }
        return new HSL(H, S, L);
    }

    public HSV toHSV() {
        final double rPrime = this.R / 255.0;
        final double gPrime = this.G / 255.0;
        final double bPrime = this.B / 255.0;

        final double min = Math.min(rPrime, Math.min(gPrime, bPrime));
        final double max = Math.max(rPrime, Math.max(gPrime, bPrime));
        final double delta = max - min;

        double H = 0.0;
        double S;
        final double V = max;

        if (delta == 0.0) {
            H = 0.0;
            S = 0.0;
        }
        else {
            S = delta / max;

            final double deltaR = (((max - rPrime) / 6.0) + (delta / 2.0)) / delta;
            final double deltaG = (((max - gPrime) / 6.0) + (delta / 2.0)) / delta;
            final double deltaB = (((max - bPrime) / 6.0) + (delta / 2.0)) / delta;

            if (rPrime == max) {
                H = deltaB - deltaG;
            }
            else if (gPrime == max) {
                H = ((1.0 / 3.0) + deltaR) - deltaB;
            }
            else if (bPrime == max) {
                H = ((2.0 / 3.0) + deltaG) - deltaR;
            }

            if (H < 0.0) {
                H += 1.0;
            }
            if (H > 1.0) {
                H -= 1.0;
            }
        }
        return new HSV(H, S, V);
    }

    public XYZ toXYZ() {
        double rPrime = this.R / 255.0;
        double gPrime = this.G / 255.0;
        double bPrime = this.B / 255.0;

        if (rPrime > 0.04045) {
            rPrime = Math.pow(((rPrime + 0.055) / 1.055), 2.4);
        }
        else {
            rPrime = rPrime / 12.92;
        }
        if (gPrime > 0.04045) {
            gPrime = Math.pow(((gPrime + 0.055) / 1.055), 2.4);
        }
        else {
            gPrime = gPrime / 12.92;
        }
        if (bPrime > 0.04045) {
            bPrime = Math.pow(((bPrime + 0.055) / 1.055), 2.4);
        }
        else {
            bPrime = bPrime / 12.92;
        }

        rPrime = rPrime * 100.0;
        gPrime = gPrime * 100.0;
        bPrime = bPrime * 100.0;

        // Observer. = 2°, Illuminant = D65
        final double X = (rPrime * 0.4124) + (gPrime * 0.3576) + (bPrime * 0.1805);
        final double Y = (rPrime * 0.2126) + (gPrime * 0.7152) + (bPrime * 0.0722);
        final double Z = (rPrime * 0.0193) + (gPrime * 0.1192) + (bPrime * 0.9505);
        return new XYZ(X, Y, Z);
    }

    public HunterLab toHunterLab() {
        return this.toXYZ().toHunterLab();
    }

    public Yxy toYxy() {
        return this.toXYZ().toYxy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.B);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.G);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.R);
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
        final RGB other = (RGB) obj;
        if (Double.doubleToLongBits(this.B) != Double.doubleToLongBits(other.B)) {
            return false;
        }
        if (Double.doubleToLongBits(this.G) != Double.doubleToLongBits(other.G)) {
            return false;
        }
        if (Double.doubleToLongBits(this.R) != Double.doubleToLongBits(other.R)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.R + "," + this.G + "," + this.B;
    }

    /**
     * @param string
     * @return
     */
    public static RGB valueOf(String string) {
        String[] values = string.split(",");
        if (values.length == 3) {
            return new RGB(Double.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]));
        }
        return null;
    }
}
