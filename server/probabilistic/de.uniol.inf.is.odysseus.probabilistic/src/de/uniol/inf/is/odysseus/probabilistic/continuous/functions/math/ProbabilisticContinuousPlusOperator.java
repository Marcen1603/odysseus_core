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

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousPlusOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2533914833718506956L;

    public ProbabilisticContinuousPlusOperator() {
    	super("+", ACC_TYPES,SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
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
    public final NormalDistributionMixture getValue() {
        final NormalDistributionMixture a = (NormalDistributionMixture) this.getInputValue(0);
        final NormalDistributionMixture b = (NormalDistributionMixture) this.getInputValue(1);
        return this.getValueInternal(a, b);
    }

    /**
     * Adds the given distribution to the other distribution.
     * 
     * The result is another normal distribution mixture with:
     * 
     * \f$ \mu = \mu_{1} + \mu_{2} \f$
     * 
     * and
     * 
     * \f$ \sigma = \sigma_{1} + \sigma_{2} \f$
     * 
     * @param a
     *            The distribution
     * @param b
     *            The distribution to add
     * @return The distribution of a+b
     */
    protected final NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
        final List<Pair<Double, MultivariateNormalDistribution>> mixtures = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
        for (final Pair<Double, MultivariateNormalDistribution> aEntry : a.getMixtures().getComponents()) {
            final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMeans());
            final RealMatrix aCovarianceMatrix = aEntry.getValue().getCovariances();

            for (final Pair<Double, MultivariateNormalDistribution> bEntry : b.getMixtures().getComponents()) {
                final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getValue().getMeans());
                final RealMatrix bCovarianceMatrix = bEntry.getValue().getCovariances();

                final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(aMean.add(bMean).getColumn(0), aCovarianceMatrix.add(bCovarianceMatrix).getData());
                mixtures.add(new Pair<Double, MultivariateNormalDistribution>(aEntry.getKey() * bEntry.getKey(), distribution));
            }
        }

        final NormalDistributionMixture result = new NormalDistributionMixture(mixtures);
        final Interval[] support = new Interval[a.getSupport().length];
        for (int i = 0; i < a.getSupport().length; i++) {
            support[i] = a.getSupport(i).add(b.getSupport(i));
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
    public final boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.mep.IBinaryOperator#isRightDistributiveWith(
     * de.uniol.inf.is.odysseus.mep.IOperator)
     */
    @Override
    public final boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
        return false;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT,
            SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE,
            SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG };



}
