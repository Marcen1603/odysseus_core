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
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticSubtractNumberRHSOperator extends AbstractProbabilisticSubtractNumberOperator {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6021986662578189660L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE }, SDFDatatype.NUMBERS };

    public ProbabilisticSubtractNumberRHSOperator() {
        super(ProbabilisticSubtractNumberRHSOperator.ACC_TYPES);
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.
     * AbstractProbabilisticContinuousMinusNumberOperator#isCommutative()
     */
    @Override
    public final boolean isCommutative() {
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final IMultivariateDistribution getValue() {
        final MultivariateMixtureDistribution a = (MultivariateMixtureDistribution) this.getInputValue(0);
        final Double b = this.getNumericalInputValue(1);
        return this.getValueInternal(a, b);
    }
}
