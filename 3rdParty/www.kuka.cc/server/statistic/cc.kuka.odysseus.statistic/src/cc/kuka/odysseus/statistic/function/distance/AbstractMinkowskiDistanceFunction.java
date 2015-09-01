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

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public abstract class AbstractMinkowskiDistanceFunction extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = -4349207616969582261L;

    public AbstractMinkowskiDistanceFunction(final SDFDatatype[][] accTypes) {
        super("MinkowskiDistance", 3, accTypes, SDFDatatype.DOUBLE);
    }

    /**
     *
     * @param a
     *            The point
     * @param b
     *            The other point
     * @throws DimensionMismatchException
     * @return The distance measure
     */
    protected final static Double getValueInternal(final RealMatrix a, final RealMatrix b, final double order) {
        return Double.valueOf(a.walkInOptimizedOrder(new RealMatrixPreservingVisitor() {
            private double distance;

            @Override
            public void start(final int rows, final int columns, final int startRow, final int endRow, final int startColumn, final int endColumn) {
                this.distance = 0.0;
            }

            @Override
            public void visit(final int row, final int column, final double value) {
                this.distance += FastMath.pow(Math.abs(value - b.getEntry(row, column)), order);
            }

            @Override
            public double end() {
                return FastMath.pow(this.distance, 1.0 / order);
            }
        }));
    }

}