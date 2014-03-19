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
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixInverseFunction extends AbstractFunction<double[][]> {

    private static final long serialVersionUID = -1627768697759701027L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public MatrixInverseFunction() {
        super("inv", 1, accTypes, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return getValueInternal(a);
    }

    protected double[][] getValueInternal(RealMatrix a) {
        try {
            return new CholeskyDecomposition(a).getSolver().getInverse().getData();
        }
        catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
            return new LUDecomposition(a).getSolver().getInverse().getData();
        }
    }

}
