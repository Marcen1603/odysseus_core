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
public class ProbabilisticSubtractNumberLHSOperator extends AbstractProbabilisticSubtractNumberOperator {

    /**
     * 
     */
    private static final long serialVersionUID = 1627049434742292196L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE } };

    public ProbabilisticSubtractNumberLHSOperator() {
        super(ProbabilisticSubtractNumberLHSOperator.ACC_TYPES);
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
        final Double a = this.getNumericalInputValue(0);
        final MultivariateMixtureDistribution b = (MultivariateMixtureDistribution) this.getInputValue(1);

        return this.getValueInternal(b, -a);
    }
}
