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
package de.uniol.inf.is.odysseus.mep.impl;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Internal function for building arrays.
 * Expects Double[] as arguments and builds a matrix out of them.
 * No check for rectangularity is applied. This is checked in the
 * ExpressionBuilderVisitor!
 */
public class MatrixFunction extends AbstractFunction<double[][]> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -742218734048620320L;

    public MatrixFunction() {
    	super("__matrix", 1,getAllTypes(1), SDFDatatype.MATRIX_DOUBLE);
    }
    
    public MatrixFunction(IMepExpression<?>[] lines) {
        super("__matrix", lines.length, getAccTypes(lines.length), SDFDatatype.MATRIX_DOUBLE);
        setArguments(lines);
    }

    public MatrixFunction(MatrixFunction other){
    	super(other);
    }
    
    @Override
    public double[][] getValue() {
        int arity = getArity();
        double[][] value = new double[arity][];
        for (int i = 0; i < arity; ++i) {
            value[i] = (double[]) getArgument(i).getValue();
        }

        return value;
    }

    private static SDFDatatype[][] getAccTypes(int length) {
        SDFDatatype[][] accTypes = new SDFDatatype[length][];
        Arrays.fill(accTypes, new SDFDatatype[] { SDFDatatype.VECTOR_DOUBLE });
        return accTypes;
    }
}
