/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.functions.compare;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Equals operator for continuous probabilistic values.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticEqualsVectorOperator extends AbstractProbabilisticCompareOperator {

    /**
     *
     */
    private static final long serialVersionUID = 3016679134461973157L;

    public ProbabilisticEqualsVectorOperator() {
        super("==", ProbabilisticEqualsVectorOperator.ACC_TYPES);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticBooleanResult getValue() {
        final Object[] aVector = this.getInputValue(0);
        final MultivariateMixtureDistribution a = ((MultivariateMixtureDistribution) aVector[0]).clone();

        final double[][] b = (double[][]) this.getInputValue(1);
        final double[] lowerBound = new double[a.getDimension()];
        Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
        System.arraycopy(b[0], 0, lowerBound, 0, b[0].length);
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, Double.POSITIVE_INFINITY);
        System.arraycopy(b[0], 0, lowerBound, 0, b[0].length);

        return this.getValueInternal(a, lowerBound, upperBound);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return 9;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.VECTOR_PROBABILISTIC_DOUBLE },
            { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE } };

}
