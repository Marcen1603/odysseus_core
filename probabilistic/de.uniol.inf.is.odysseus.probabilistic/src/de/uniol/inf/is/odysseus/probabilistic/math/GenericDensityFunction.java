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

public class GenericDensityFunction implements ProbabilityDensityFunction {
    private CumulativeDistributionFunction cdf;

    @Override
    public double density(final double x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double cumulativeProbability(final double x1, final double x2) {
        return this.cdf.evaluate(x2) - this.cdf.evaluate(x1);
    }

    public void setCumulativeDistributionFunction(final CumulativeDistributionFunction cdf) {
        this.cdf = cdf;
    }

}
