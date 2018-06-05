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
package cc.kuka.odysseus.signal.function.transform;

import cc.kuka.odysseus.signal.common.datatype.Complex;
import cc.kuka.odysseus.signal.common.sdf.schema.SDFSignalDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ToComplexFunction extends AbstractFunction<Complex> {

    /**
     *
     */
    private static final long serialVersionUID = -7161083367574085024L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public ToComplexFunction() {
        super("toComplex", 2, ToComplexFunction.accTypes, SDFSignalDatatype.COMPLEX);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Complex getValue() {
        final double real = this.getNumericalInputValue(0).doubleValue();
        final double imaginary = this.getNumericalInputValue(1).doubleValue();
        return new Complex(real, imaginary);
    }

}
