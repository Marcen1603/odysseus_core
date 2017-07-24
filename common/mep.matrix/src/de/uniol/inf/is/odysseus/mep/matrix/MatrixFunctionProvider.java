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

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MatrixFunctionProvider implements IFunctionProvider {

    public MatrixFunctionProvider() {
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();

        // Getter functions
        functions.add(new MatrixGetEntry());
        functions.add(new VectorGetEntry());

        functions.add(new MatrixGetVectorOperator());
        functions.add(new MatrixGetValueOperator());
        functions.add(new VectorGetValueOperator());

        functions.add(new SubMatrixFunction());
        functions.add(new SubVectorFunction());

        // Compare operator
        functions.add(new MatrixEqualsOperator());
        functions.add(new VectorEqualsOperator());

        // Arithmetic operators
        functions.add(new MatrixPlusOperator());
        functions.add(new MatrixPlusScalarRHSOperator());
        functions.add(new MatrixPlusScalarLHSOperator());
        functions.add(new VectorPlusOperator());
        functions.add(new VectorPlusScalarRHSOperator());
        functions.add(new VectorPlusScalarLHSOperator());

        functions.add(new MatrixMinusOperator());
        functions.add(new MatrixMinusScalarRHSOperator());
        functions.add(new VectorMinusOperator());
        functions.add(new VectorMinusScalarRHSOperator());

        functions.add(new MatrixMultiplicationOperator());
        functions.add(new MatrixMultiplicationScalarRHSOperator());
        functions.add(new MatrixMultiplicationScalarLHSOperator());
        functions.add(new VectorMultiplicationOperator());
        functions.add(new VectorMultiplicationScalarRHSOperator());
        functions.add(new VectorMultiplicationScalarLHSOperator());

        functions.add(new MatrixDivisionScalarRHSOperator());
        functions.add(new VectorDivisionScalarRHSOperator());

        functions.add(new MatrixPowerOperator());

        functions.add(new InnerProductMatrixFunction());
        functions.add(new InnerProductVectorFunction());

        // Aggregation functions
        functions.add(new MinMatrixFunction());
        functions.add(new MinVectorFunction());

        functions.add(new MaxMatrixFunction());
        functions.add(new MaxVectorFunction());

        functions.add(new CountMatrixFunction());
        functions.add(new CountVectorFunction());

        functions.add(new SumMatrixFunction());
        functions.add(new SumVectorFunction());

        functions.add(new AvgMatrixFunction());
        functions.add(new AvgVectorFunction());

        functions.add(new MedianMatrixFunction());
        functions.add(new MedianVectorFunction());

        functions.add(new StandardDeviationMatrixFunction());
        functions.add(new StandardDeviationVectorFunction());

        functions.add(new VarianceMatrixFunction());
        functions.add(new VarianceVectorFunction());

        // Transformation functions
        functions.add(new MatrixToVectorFunction());
        functions.add(new VectorToMatrixFunction());
        functions.add(new ToStringMatrixFunction());
        functions.add(new ToStringVectorFunction());
        functions.add(new VectorFromStringFunction());
        functions.add(new VectorFromStringRangeFunction());

        // Read functions
        functions.add(new ReadMatrixFunction());
        functions.add(new ReadVectorFunction());
        functions.add(new ReadVectorRowFunction());

        functions.add(new PermsFunction());
        functions.add(new PermanentFunction());
        functions.add(new DeterminantFunction());
        functions.add(new TraceFunction());
        functions.add(new TransposeFunction());
        functions.add(new InverseMatrixFunction());
        functions.add(new IdentityMatrixFunction());
        functions.add(new ZerosMatrixFunction());
        functions.add(new OnesMatrixFunction());

        // Eigenvalues
        functions.add(new EigenvaluesFunction());
        functions.add(new ImaginaryEigenvaluesFunction());

        // Singular Value Decomposition
        functions.add(new SingularValueDecompositionFunction());

        return functions;
    }

}
