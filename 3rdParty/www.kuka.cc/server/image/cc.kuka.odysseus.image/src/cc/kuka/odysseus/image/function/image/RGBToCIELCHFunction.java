/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.image.function.image;

import cc.kuka.odysseus.image.colorspace.CIELCH;
import cc.kuka.odysseus.image.colorspace.RGB;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RGBToCIELCHFunction extends AbstractFunction<double[]> {
    /**
     *
     */
    private static final long serialVersionUID = 8900518236739760663L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public RGBToCIELCHFunction() {
        super("RGBToCIELCH", 3, RGBToCIELCHFunction.ACC_TYPES, SDFDatatype.VECTOR_DOUBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getValue() {
        final double R = this.getNumericalInputValue(0).doubleValue();
        final double G = this.getNumericalInputValue(1).doubleValue();
        final double B = this.getNumericalInputValue(2).doubleValue();
        final CIELCH colorSpace = (new RGB(R, G, B)).toCIELCH();
        return colorSpace.toArray();
    }
}
