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
package cc.kuka.odysseus.financial.function.financial;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class AnnualPercentageRateFunction extends AbstractFunction<Double> {
    /**
     *
     */
    private static final long serialVersionUID = -988984406194130693L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public AnnualPercentageRateFunction() {
        super("APR", 2, AnnualPercentageRateFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getValue() {
        // stated annual interest yield
        final double y = this.getNumericalInputValue(0).doubleValue();
        // number of compounding periods per year
        final double n = this.getNumericalInputValue(1).doubleValue();
        return new Double((FastMath.pow(1.0 + y, 1.0 / n) - 1.0) * n);
    }
}
