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
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.common.ProbabilisticContinuousUtils;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * Greater-Equals operator for continuous probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousGreaterEqualsOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9122605635777338549L;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IOperator#getPrecedence()
     */
    @Override
    public final int getPrecedence() {
        return 8;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
     */
    @Override
    public String getSymbol() {
        return ">=";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
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

        final double value = ProbabilisticContinuousUtils.cumulativeProbability(a, lowerBound, upperBound);
        a.setScale(a.getScale() / value);
        final Interval[] support = new Interval[a.getDimension()];
        for (int i = 0; i < a.getDimension(); i++) {
            final Interval interval = new Interval(lowerBound.getEntry(i), upperBound.getEntry(i));
            support[i] = a.getSupport(i).intersection(interval);
        }
        a.setSupport(support);
        return a;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
     */
    @Override
    public final SDFDatatype getReturnType() {
        return SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.mep.IBinaryOperator#isCommutative()
     */
    @Override
    public final boolean isCommutative() {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.mep.IBinaryOperator#isAssociative()
     */
    @Override
    public final boolean isAssociative() {
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#
     * isLeftDistributiveWith
     * (de.uniol.inf.is.odysseus.mep.IOperator)
     */
    @Override
    public final boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#
     * isRightDistributiveWith
     * (de.uniol.inf.is.odysseus.mep.IOperator)
     */
    @Override
    public final boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.mep.IOperator#getAssociativity()
     */
    @Override
    public final de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
            { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER,
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG },
            SDFDatatype.NUMBERS };

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
     */
    @Override
    public final SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > (this.getArity() - 1)) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return ProbabilisticContinuousGreaterEqualsOperator.ACC_TYPES[argPos];
    }

}
