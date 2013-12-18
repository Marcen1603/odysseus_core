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
 * Greater-Equals operator for continuous probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousGreaterEqualsOperator extends AbstractProbabilisticContinuousCompareOperator {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9122605635777338549L;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getSymbol() {
        return ">=";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final NormalDistributionMixture getValue() {
        final NormalDistributionMixture a = ((NormalDistributionMixture) this.getInputValue(0)).clone();

        final Double b = this.getNumericalInputValue(1);
        final double[] lowerBoundData = new double[a.getDimension()];
        Arrays.fill(lowerBoundData, b);
        final double[] upperBoundData = new double[a.getDimension()];
        Arrays.fill(upperBoundData, Double.POSITIVE_INFINITY);

        final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
        final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

        return this.getValueInternal(a, lowerBound, upperBound);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return 8;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_NUMBERS, SDFDatatype.NUMBERS };

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return ProbabilisticContinuousGreaterEqualsOperator.ACC_TYPES[argPos];
    }

}
