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
public class ResidualValueFunction extends AbstractFunction<Double> {
    /**
     *
     */
    private static final long serialVersionUID = -8302193650326471045L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ResidualValueFunction() {
        super("ResidualValue", 3, ResidualValueFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getValue() {
        final double cost = this.getNumericalInputValue(0).doubleValue();
        final double scrapRate = this.getNumericalInputValue(1).doubleValue();
        final double lifespan = this.getNumericalInputValue(2).doubleValue();
        return new Double((cost - scrapRate) / lifespan);
    }

}
