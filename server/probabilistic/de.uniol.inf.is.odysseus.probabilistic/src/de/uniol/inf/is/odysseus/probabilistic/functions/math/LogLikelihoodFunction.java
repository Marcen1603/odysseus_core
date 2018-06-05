/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class LogLikelihoodFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = -1353720563846245827L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS, { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE } };

    public LogLikelihoodFunction() {
        super("loglikelihood", 2, ACC_TYPES, SDFDatatype.DOUBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getValue() {
        final double[] a = ((double[]) this.getInputValue(0));
        final MultivariateMixtureDistribution b = (MultivariateMixtureDistribution) this.getInputValue(1);
        double sum = 0.0;
        for (Double point : a) {
            double density = b.density(new double[] { point });
            if (density > 0.0) {
                sum += FastMath.log(density);
            }
        }
        return sum / a.length;
    }
}
