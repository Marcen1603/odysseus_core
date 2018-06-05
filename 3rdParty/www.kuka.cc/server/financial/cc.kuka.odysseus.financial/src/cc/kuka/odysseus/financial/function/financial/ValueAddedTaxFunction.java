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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ValueAddedTaxFunction extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = -5058729395767496394L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ValueAddedTaxFunction() {
        super("VAT", 2, ValueAddedTaxFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getValue() {
        final double value = this.getNumericalInputValue(0).doubleValue();
        final double tax = this.getNumericalInputValue(1).doubleValue();
        return new Double((value * tax) / 100);
    }

}
