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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class MatrixFunctionProvider implements IFunctionProvider {

    public MatrixFunctionProvider() {
    }

    @Override
    public List<IFunction<?>> getFunctions() {

        List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
        functions.add(new MatrixGetEntry());
        functions.add(new MatrixGetVectorOperator());
        functions.add(new MatrixGetValueOperator());
        functions.add(new VectorGetOperator());
        
        functions.add(new MatrixEqualsOperator());
        functions.add(new MatrixMinusOperator());
        functions.add(new MatrixMultiplicationOperator());
        functions.add(new MatrixPlusOperator());
        functions.add(new MatrixPowerOperator());
        functions.add(new MatrixDeterminantFunction());
        functions.add(new MatrixTraceFunction());
        functions.add(new MatrixTransposeFunction());
        functions.add(new MatrixInverseFunction());
        functions.add(new MatrixIdentityMatrixFunction());

        functions.add(new MatrixSubMatrixFunction());
        
        // Aggregation functions
        functions.add(new MatrixSMinFunction());
        functions.add(new MatrixSMaxFunction());
        functions.add(new MatrixSCountFunction());
        functions.add(new MatrixSSumFunction());
        functions.add(new MatrixSAVGFunction());
        functions.add(new MatrixSMedianFunction());
        functions.add(new VectorSMinFunction());
        functions.add(new VectorSMaxFunction());
        functions.add(new VectorSCountFunction());
        functions.add(new VectorSSumFunction());
        functions.add(new VectorSAVGFunction());
        
        functions.add(new MatrixToVectorFunction());
        functions.add(new VectorToMatrixFunction());       
        return functions;
    }

}
