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
package cc.kuka.odysseus.statistic.function.distribution;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ExponentialPDFFunction extends AbstractFunction<Double> {
    /**
     *
     */
    private static final long serialVersionUID = 3977348841534829270L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ExponentialPDFFunction() {
        super("exppdf", 2, ExponentialPDFFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final double x = this.getNumericalInputValue(0).doubleValue();
        final double mean = this.getNumericalInputValue(1).doubleValue();
        final ExponentialDistribution distribution = new ExponentialDistribution(mean);
        return new Double(distribution.density(x));
    }
}
