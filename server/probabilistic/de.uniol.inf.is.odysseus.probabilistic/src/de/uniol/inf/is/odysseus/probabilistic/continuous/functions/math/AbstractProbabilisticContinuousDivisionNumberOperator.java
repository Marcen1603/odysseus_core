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
public abstract class AbstractProbabilisticContinuousDivisionNumberOperator extends AbstractProbabilisticBinaryOperator<IMultivariateDistribution> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7478030161128270461L;

    public AbstractProbabilisticContinuousDivisionNumberOperator(SDFDatatype[][] accTypes) {
    	super("/",accTypes,SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
    }
    
    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IOperator#getPrecedence()
     */
    @Override
    public final int getPrecedence() {
        return 5;
    }

  
    /**
     * Divide the given distribution by the given value.
     * 
     * @param a
     *            The distribution
     * @param b
     *            The value
     * @return The distribution of a/b
     */
    protected final IMultivariateDistribution getValueInternal(final MultivariateMixtureDistribution a, final Double b) {
        final List<Pair<Double, IMultivariateDistribution>> mvns = new ArrayList<Pair<Double, IMultivariateDistribution>>();
        for (final Pair<Double, IMultivariateDistribution> entry : a.getComponents()) {
            final IMultivariateDistribution normalDistribution = entry.getValue();
            final Double weight = entry.getKey();
            final double[] means = normalDistribution.getMean();
            for (int i = 0; i < means.length; i++) {
                means[i] /= b;
            }
            final RealMatrix covariances = new Array2DRowRealMatrix(normalDistribution.getVariance()).scalarMultiply(1.0 / (b * b));
            final IMultivariateDistribution component = new MultivariateNormalDistribution(means, covariances.getData());
            mvns.add(new Pair<Double, IMultivariateDistribution>(weight, component));
        }
        final MultivariateMixtureDistribution result = new MultivariateMixtureDistribution(mvns);
        final Interval[] support = new Interval[result.getSupport().length];
        for (int i = 0; i < result.getSupport().length; i++) {
            support[i] = result.getSupport(i).div(b);
        }
        result.setSupport(support);
        result.setScale(a.getScale());
        result.setAttributes(a.getAttributes());
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
    public boolean isCommutative() {
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

}
