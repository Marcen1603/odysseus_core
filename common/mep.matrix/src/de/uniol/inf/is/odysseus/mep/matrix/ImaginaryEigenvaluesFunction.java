/**********************************************************************************
 * Copyright 2015 The Odysseus Team
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

import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ImaginaryEigenvaluesFunction extends AbstractFunction<double[]> {

    /**
     * 
     */
    private static final long serialVersionUID = -2443986378325415726L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public ImaginaryEigenvaluesFunction() {
        super("ieig", 1, ImaginaryEigenvaluesFunction.ACC_TYPES, SDFDatatype.VECTOR_DOUBLE);
    }

    @Override
    public double[] getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return ImaginaryEigenvaluesFunction.getValueInternal(a);
    }

    protected static double[] getValueInternal(final RealMatrix a) {
        final EigenDecomposition decomposition = new EigenDecomposition(a);
        return decomposition.getImagEigenvalues();
    }

    public static void main(String[] args) {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {

        { 1.0000, 0.5000, 0.3333, 0.2500 },

        { 0.5000, 1.0000, 0.6667, 0.5000 },

        { 0.3333, 0.6667, 1.0000, 0.7500 },

        { 0.2500, 0.5000, 0.7500, 1.0000 } });

        System.out.println(Arrays.toString(getValueInternal(A)));
    }
}
