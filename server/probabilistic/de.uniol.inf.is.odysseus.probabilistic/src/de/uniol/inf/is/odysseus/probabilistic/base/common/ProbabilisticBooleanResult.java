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
package de.uniol.inf.is.odysseus.probabilistic.base.common;

import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticBooleanResult {
    private final IMultivariateDistribution[] distributions;
    private final double probability;

    /**
     * @param distribution
     * @param probability
     */
    public ProbabilisticBooleanResult(IMultivariateDistribution distribution, double probability) {
        this(new IMultivariateDistribution[] { distribution }, probability);
    }

    /**
     * @param iMultivariateDistributions
     * @param probability2
     */
    public ProbabilisticBooleanResult(IMultivariateDistribution[] distributions, double probability) {
        super();
        this.distributions = distributions;
        this.probability = probability;
    }

    /**
     * @return the distribution
     */
    public IMultivariateDistribution getDistribution() {
        return this.distributions[0];
    }

    /**
     * @return the distribution
     */
    public IMultivariateDistribution[] getDistributions() {
        return this.distributions;
    }

    /**
     * @return the probability
     */
    public double getProbability() {
        return this.probability;
    }

}
