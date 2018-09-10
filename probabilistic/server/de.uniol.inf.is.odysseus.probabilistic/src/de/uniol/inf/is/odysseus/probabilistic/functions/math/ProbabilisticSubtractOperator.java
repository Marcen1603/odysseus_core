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
package de.uniol.inf.is.odysseus.probabilistic.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticSubtractOperator extends AbstractProbabilisticBinaryOperator<IMultivariateDistribution> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3516509026688087365L;

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

    public ProbabilisticSubtractOperator() {
        super("-", ProbabilisticSubtractOperator.ACC_TYPES, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
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
    protected final IMultivariateDistribution getValueInternal(final MultivariateMixtureDistribution a, final MultivariateMixtureDistribution b) {
        return a.subtract(b);
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

}
