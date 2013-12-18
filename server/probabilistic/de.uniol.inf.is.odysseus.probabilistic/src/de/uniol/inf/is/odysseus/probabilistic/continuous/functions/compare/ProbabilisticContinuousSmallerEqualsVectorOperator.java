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
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Smaller-Equals operator for continuous probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousSmallerEqualsVectorOperator extends AbstractProbabilisticContinuousCompareOperator {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9122605635777338549L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSymbol() {
        return "<=";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int getPrecedence() {
        return 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final NormalDistributionMixture getValue() {
        final Object[] aVector = this.getInputValue(0);
        final NormalDistributionMixture a = ((NormalDistributionMixture) aVector[0]).clone();

        final double[][] b = (double[][]) this.getInputValue(1);
        final double[] lowerBoundData = new double[a.getDimension()];
        Arrays.fill(lowerBoundData, Double.NEGATIVE_INFINITY);
        final double[] upperBoundData = new double[a.getDimension()];
        Arrays.fill(upperBoundData, Double.POSITIVE_INFINITY);
        System.arraycopy(b[0], 0, upperBoundData, 0, b[0].length);

        final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
        final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

        return this.getValueInternal(a, lowerBound, upperBound);
    }

    /*
     * 
     * 
     * 
     * /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.VECTOR_PROBABILISTIC_CONTINUOUS_DOUBLE },
            { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE } };

    /**
     * {@inheritDoc}
     */
    @Override
    public final SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > (this.getArity() - 1)) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return ProbabilisticContinuousSmallerEqualsVectorOperator.ACC_TYPES[argPos];
    }

}
