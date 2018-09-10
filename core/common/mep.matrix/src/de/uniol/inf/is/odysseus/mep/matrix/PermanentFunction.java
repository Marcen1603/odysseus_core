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
package de.uniol.inf.is.odysseus.mep.matrix;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.ArithmeticUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP Function to calculate the permanent of a matrix.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Permanent">Permanent</a>
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 */
public class PermanentFunction extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = 8610775865571282157L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public PermanentFunction() {
        super("perm", 1, PermanentFunction.accTypes, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return new Double(PermanentFunction.getValueInternal(a));
    }

    protected static double getValueInternal(final RealMatrix a) {
        if (a.isSquare()) {
            final int order = a.getRowDimension();

            final int[] indexes = new int[order];
            for (int i = 0; i < order; i++) {
                indexes[i] = i;
            }
            // factorial function is moved to CombinatoricsUtils in future
            // versions
            
            @SuppressWarnings("deprecation")
			final long factorial = ArithmeticUtils.factorial(order);
            final List<int[]> permutations = new ArrayList<>((int) factorial);
            PermanentFunction.calcHeapPermutation(permutations, indexes, order);

            double sum = 0.0;
            for (final int[] p : permutations) {
                double product = 1.0;
                for (int i = 0; i < order; i++) {
                    product *= a.getEntry(i, p[i]);
                }
                sum += product;
            }
            return sum;
        }
        throw new NonSquareMatrixException(a.getRowDimension(), a.getColumnDimension());
    }

    private static void calcHeapPermutation(final List<int[]> out, final int[] value, final int n) {
        if (n == 1) {
            out.add(value.clone());
        }
        else {
            for (int i = 0; i < n; i++) {
                PermanentFunction.calcHeapPermutation(out, value, n - 1);
                if ((n % 2) == 1) {
                    final int tmp = value[0];
                    value[0] = value[n - 1];
                    value[n - 1] = tmp;
                }
                else {
                    final int tmp = value[i];
                    value[i] = value[n - 1];
                    value[n - 1] = tmp;
                }
            }
        }
    }

}
