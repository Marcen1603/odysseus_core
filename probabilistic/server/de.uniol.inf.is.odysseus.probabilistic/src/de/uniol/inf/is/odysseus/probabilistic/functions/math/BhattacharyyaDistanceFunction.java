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
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class BhattacharyyaDistanceFunction extends AbstractBhattacharyyaDistanceFunction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3498463869497505997L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE }, { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE } };

    public BhattacharyyaDistanceFunction() {
        super(BhattacharyyaDistanceFunction.ACC_TYPES);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Double getValue() {
        final MultivariateMixtureDistribution a = (MultivariateMixtureDistribution) this.getInputValue(0);
        final MultivariateMixtureDistribution b = (MultivariateMixtureDistribution) this.getInputValue(1);
        return this.getValueInternal(a, b);
    }
}
