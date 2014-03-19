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
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousMinusOperator extends AbstractProbabilisticBinaryOperator<IMultivariateDistribution> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3516509026688087365L;

    public ProbabilisticContinuousMinusOperator() {
    	super("-",ACC_TYPES,SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
    }
    
    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IOperator#getPrecedence()
     */
    @Override
    public final int getPrecedence() {
        return 6;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final IMultivariateDistribution getValue() {
        final MultivariateMixtureDistribution a = (MultivariateMixtureDistribution) this.getInputValue(0);
        final MultivariateMixtureDistribution b = (MultivariateMixtureDistribution) this.getInputValue(1);
        return this.getValueInternal(a, b);
    }

    /**
     * Subtracts the given distribution from the other distribution.
     * 
     * The result is another normal distribution mixture with:
     * 
     * \f$ \mu = \mu_{1} - \mu_{2} \f$
     * 
     * and
     * 
     * \f$ \sigma = \sigma_{1} + \sigma_{2} \f$
     * 
     * @param a
     *            The distribution
     * @param b
     *            The distribution to subtract
     * @return The distribution of a-b
     */
    protected final IMultivariateDistribution getValueInternal(final MultivariateMixtureDistribution a,
            final MultivariateMixtureDistribution b) {
        final List<Pair<Double, IMultivariateDistribution>> mixtures = new ArrayList<Pair<Double, IMultivariateDistribution>>();
        for (final Pair<Double, IMultivariateDistribution> aEntry : a.getComponents()) {
            final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMean());
            final RealMatrix aCovarianceMatrix = new Array2DRowRealMatrix(aEntry.getValue().getVariance());
            for (final Pair<Double, IMultivariateDistribution> bEntry : b.getComponents()) {
                final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getValue().getMean());
                final RealMatrix bCovarianceMatrix = new Array2DRowRealMatrix(bEntry.getValue().getVariance());

                final IMultivariateDistribution distribution = new MultivariateNormalDistribution(aMean.subtract(bMean).getColumn(0), aCovarianceMatrix.add(bCovarianceMatrix).getData());
                mixtures.add(new Pair<Double, IMultivariateDistribution>(aEntry.getKey() * bEntry.getKey(), distribution));
            }
        }
        final MultivariateMixtureDistribution result = new MultivariateMixtureDistribution(mixtures);
        final Interval[] support = new Interval[a.getSupport().length];
        for (int i = 0; i < a.getSupport().length; i++) {
            support[i] = a.getSupport(i).sub(b.getSupport(i));
        }
        result.setSupport(support);
        result.setScale(a.getScale() * b.getScale());
        return result;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IOperator#getAssociativity()
     */
    @Override
    public final de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isCommutative()
     */
    @Override
    public final boolean isCommutative() {
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isAssociative()
     */
    @Override
    public final boolean isAssociative() {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.mep.IBinaryOperator#isLeftDistributiveWith(de
     * .uniol.inf.is.odysseus.mep.IOperator)
     */
    @Override
    public final boolean isLeftDistributiveWith(final IOperator<IMultivariateDistribution> operator) {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.mep.IBinaryOperator#isRightDistributiveWith(
     * de.uniol.inf.is.odysseus.mep.IOperator)
     */
    @Override
    public final boolean isRightDistributiveWith(final IOperator<IMultivariateDistribution> operator) {
        return false;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT,
            SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE,
            SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG };


}
