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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ZScoreFunction extends AbstractZScoreFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = 700343179505368741L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ZScoreFunction() {
        super(ZScoreFunction.ACC_TYPES);
    }

    @Override
    public Double getValue() {
        final RealVector value = new ArrayRealVector(new double[] { this.getNumericalInputValue(0).doubleValue() }, false);
        final RealVector mean = new ArrayRealVector(new double[] { this.getNumericalInputValue(1).doubleValue() }, false);
        final double sigma = this.getNumericalInputValue(2).doubleValue();

        return new Double(AbstractZScoreFunction.getValueInternal(value, mean, sigma).getEntry(0));
    }
}
