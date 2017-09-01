/**
 * Copyright 2017 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;

/**
 * Processing tests for {@link ProbabilisticSelectPO} operator covering
 * {@link MultivariateNormalDistribution} filtering.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@RunWith(Parameterized.class)
public class SelectPOWithContinuousProbabilityDistributionsTest extends AbstractSelectPOTest {

    @Parameters(name = "{index}: Predicate: {0}, Input: {1}, Output: {2}, Existence: {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { //
            { "x < y", new Object[] { //
                    new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 },
                            new IMultivariateDistribution[] {
                                    new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 }), //
                                    new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                    }), //
                    2 }, //
                new Object[] { //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 },
                                new IMultivariateDistribution[] {
                                        new MultivariateNormalDistribution(new double[] { 1.0 },
                                                new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 },
                                                new double[] { 1.0 })//
                        }), //
                        2 }, //
                0.84 }, //
        });

    }

    @Parameter(0)
    public String predicateString;
    @Parameter(1)
    public Object[] input;
    @Parameter(2)
    public Object[] output;
    @Parameter(3)
    public double existence;

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#process_next(de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple, int)}.
     *
     */
    @Test
    public void testProcess_next() {
        givenSchema(//
                attribute("x").as(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
                );
        givenPredicate(this.predicateString);
        givenProbabilisticSelectPO();
        givenInputTupleWithValues(this.input);

        whenProcessTuple();

        thenOutputEquals(this.existence, this.output);
    }

}
