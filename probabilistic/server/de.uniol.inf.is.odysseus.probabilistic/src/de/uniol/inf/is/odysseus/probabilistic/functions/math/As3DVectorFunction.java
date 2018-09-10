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
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class As3DVectorFunction extends AbstractProbabilisticFunction<IMultivariateDistribution[]> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8985768484486661113L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE }, { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE },
            { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE } };

    public As3DVectorFunction() {
        super("as3DVector", 3, As3DVectorFunction.ACC_TYPES, SDFProbabilisticDatatype.VECTOR_PROBABILISTIC_DOUBLE);
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.
     * ProbabilisticIntegrateMultivariateFunction#getValue()
     */
    @Override
    public final IMultivariateDistribution[] getValue() {
        return new IMultivariateDistribution[] { (IMultivariateDistribution) this.getInputValue(0), (IMultivariateDistribution) this.getInputValue(1),
                (IMultivariateDistribution) this.getInputValue(2) };
    }

}
