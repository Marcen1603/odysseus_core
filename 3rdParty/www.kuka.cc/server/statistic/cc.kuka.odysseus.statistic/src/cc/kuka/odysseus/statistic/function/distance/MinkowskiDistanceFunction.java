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
package cc.kuka.odysseus.statistic.function.distance;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MinkowskiDistanceFunction extends AbstractMinkowskiDistanceFunction {

    /**
     *
     */
    private static final long serialVersionUID = -5585099743044025756L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public MinkowskiDistanceFunction() {
        super(MinkowskiDistanceFunction.ACC_TYPES);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final Double getValue() {
        final RealMatrix a = MatrixUtils.createRowRealMatrix(new double[] { this.getNumericalInputValue(0).doubleValue() });
        final RealMatrix b = MatrixUtils.createRowRealMatrix(new double[] { this.getNumericalInputValue(1).doubleValue() });
        final double order = this.getNumericalInputValue(2).doubleValue();
        return AbstractMinkowskiDistanceFunction.getValueInternal(a, b, order);
    }
}
