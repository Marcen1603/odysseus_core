/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class InverseMatrixFunction extends AbstractFunction<double[][]> {

    private static final long serialVersionUID = -1627768697759701027L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public InverseMatrixFunction() {
        super("inv", 1, InverseMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return InverseMatrixFunction.getValueInternal(a);
    }

    protected static double[][] getValueInternal(final RealMatrix a) {
        try {
            return new CholeskyDecomposition(a).getSolver().getInverse().getData();
        }
        catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
            return new LUDecomposition(a).getSolver().getInverse().getData();
        }
    }

}
