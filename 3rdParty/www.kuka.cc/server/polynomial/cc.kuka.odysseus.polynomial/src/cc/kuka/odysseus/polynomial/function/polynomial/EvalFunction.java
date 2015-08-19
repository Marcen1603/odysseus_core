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
package cc.kuka.odysseus.polynomial.function.polynomial;

import cc.kuka.odysseus.polynomial.datatype.Polynomial;
import cc.kuka.odysseus.polynomial.sdf.schema.SDFPolynomialDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class EvalFunction extends AbstractFunction<Double> {
    /**
     *
     */
    private static final long serialVersionUID = -5982347249676487514L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFPolynomialDatatype.POLYNOMIAL }, SDFDatatype.NUMBERS };

    public EvalFunction() {
        super("eval", 2, EvalFunction.accTypes, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final Polynomial a = this.getInputValue(0);
        final double b = this.getNumericalInputValue(1).doubleValue();
        return new Double(a.evaluate(b));
    }
}
