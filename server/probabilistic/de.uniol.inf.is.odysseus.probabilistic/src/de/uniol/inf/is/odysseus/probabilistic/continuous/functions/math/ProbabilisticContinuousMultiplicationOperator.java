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

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.UnivariateNormalMixtureExpectationMaximization;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.ProbabilisticConstants;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousMultiplicationOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2233338064657887517L;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IOperator#getPrecedence()
     */
    @Override
    public final int getPrecedence() {
        return 5;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
     */
    @Override
    public final String getSymbol() {
        return "*";
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
     * Multiplies the given distribution to the other distribution. The result
     * is estimated using the EM algorithm.
     * 
     * 
     * 
     * @param a
     *            The distribution
     * @param b
     *            The distribution to multiply
     * @return The approximated distribution of a*b
     */
    protected final NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
        final double[][] aSamples = a.getMixtures().sample(ProbabilisticConstants.MEP_SAMPLES);
        final double[][] bSamples = b.getMixtures().sample(ProbabilisticConstants.MEP_SAMPLES);
        final double[][] samples = new double[ProbabilisticConstants.MEP_SAMPLES][a.getDimension()];

        for (int i = 0; i < ProbabilisticConstants.MEP_SAMPLES; i++) {
            for (int j = 0; j < samples[i].length; j++) {
                samples[i][j] = aSamples[i][j] * bSamples[i][j];
            }
        }
        final NormalDistributionMixture result;
        if (samples[0].length < 2) {
            final MixtureMultivariateNormalDistribution model = UnivariateNormalMixtureExpectationMaximization.estimate(samples, a.getMixtures().getComponents().size()
                    + b.getMixtures().getComponents().size());
            final UnivariateNormalMixtureExpectationMaximization em = new UnivariateNormalMixtureExpectationMaximization(samples);
            em.fit(model, ProbabilisticConstants.MEP_MAX_ITERATIONS, ProbabilisticConstants.MEP_THRESHOLD);
            result = new NormalDistributionMixture(em.getFittedModel().getComponents());
        }
        else {
            final MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(samples, a.getMixtures().getComponents().size()
                    + b.getMixtures().getComponents().size());
            final MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(samples);
            em.fit(model, ProbabilisticConstants.MEP_MAX_ITERATIONS, ProbabilisticConstants.MEP_THRESHOLD);
            result = new NormalDistributionMixture(em.getFittedModel().getComponents());
        }

        final Interval[] support = new Interval[a.getSupport().length];
        for (int i = 0; i < a.getSupport().length; i++) {
            support[i] = a.getSupport(i).mul(b.getSupport(i));
        }
        result.setSupport(support);
        result.setScale(a.getScale() * b.getScale());
        return result;
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
        return true;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isAssociative()
     */
    @Override
    public final boolean isAssociative() {
        return true;
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
        return ProbabilisticContinuousPlusOperator.ACC_TYPES;
    }

}
