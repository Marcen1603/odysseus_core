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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousPlusNumberRHSOperator extends AbstractProbabilisticContinuousPlusNumberOperator {
    /**
	 * 
	 */
    private static final long serialVersionUID = -7844361382196518561L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE }, SDFDatatype.NUMBERS };

    public ProbabilisticContinuousPlusNumberRHSOperator() {
    	super(ACC_TYPES);
    }
  

    /*
     * 
     * @see de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.
     * AbstractProbabilisticContinuousPlusNumberOperator#isCommutative()
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
    public final IMultivariateRealDistribution getValue() {
        final ExtendedMixtureMultivariateRealDistribution a = (ExtendedMixtureMultivariateRealDistribution) this.getInputValue(0);
        final Double b = this.getNumericalInputValue(1);
        return this.getValueInternal(a, b);
    }
}
