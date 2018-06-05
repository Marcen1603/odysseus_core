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
package cc.kuka.odysseus.misc.function.miscellaneous;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Pythagorean expectation formular
 * https://en.wikipedia.org/wiki/Pythagorean_expectation
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PythagoreanExpectationWithExponentFunction extends AbstractFunction<Double> {
    /**
     * 
     */
    private static final long serialVersionUID = 175974312537758386L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public PythagoreanExpectationWithExponentFunction() {
        super("PythagoreanExpectation", 3, PythagoreanExpectationWithExponentFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final double scored = this.getNumericalInputValue(0).doubleValue();
        final double allowed = this.getNumericalInputValue(1).doubleValue();
        final double exponent = this.getNumericalInputValue(2).doubleValue();

        return new Double(Math.pow(scored, exponent) / (Math.pow(scored, exponent) + Math.pow(allowed, exponent)));
    }

}
