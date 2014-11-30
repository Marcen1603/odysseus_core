/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class VectorToMatrixFunction extends AbstractFunction<double[][]> {
    /**
     *
     */
    private static final long serialVersionUID = -5197270672337781196L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS };

    public VectorToMatrixFunction() {
        super("toMatrix", 1, VectorToMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        return (new Array2DRowRealMatrix(new double[][] { (double[]) this.getInputValue(0) }, false)).getData();
    }
}
