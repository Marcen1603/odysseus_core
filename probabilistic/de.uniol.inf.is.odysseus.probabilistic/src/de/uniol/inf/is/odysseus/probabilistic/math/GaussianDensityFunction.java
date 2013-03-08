/*
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

package de.uniol.inf.is.odysseus.probabilistic.math;

import org.apache.commons.math3.distribution.NormalDistribution;

public class GaussianDensityFunction implements ProbabilityDensityFunction {

    private final NormalDistribution distribution;

    public GaussianDensityFunction(final double mean, final double standardDeviation) {
        this.distribution = new NormalDistribution(mean, standardDeviation);
    }

    @Override
    public double density(final double x) {
        return this.distribution.density(x);
    }

    @Override
    public double cumulativeProbability(final double x1, final double x2) {
        return this.distribution.cumulativeProbability(x1, x2);
    }

    public GaussianDensityFunction add(final GaussianDensityFunction probabilityDensityFunction) {
        return new GaussianDensityFunction(this.distribution.getMean()
                + probabilityDensityFunction.distribution.getMean(), this.distribution.getStandardDeviation()
                + probabilityDensityFunction.distribution.getStandardDeviation());
    }

    public GaussianDensityFunction substract(final GaussianDensityFunction probabilityDensityFunction) {
        return new GaussianDensityFunction(this.distribution.getMean()
                - probabilityDensityFunction.distribution.getMean(), this.distribution.getStandardDeviation()
                - probabilityDensityFunction.distribution.getStandardDeviation());
    }

    @Override
    public GaussianDensityFunction clone() {
        return new GaussianDensityFunction(this.distribution.getMean(), this.distribution.getStandardDeviation());
    }
}
