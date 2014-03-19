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
package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Test(singleThreaded = true)
public class TestProbabilisticTuple {
    /**
     * Test restrict method of probabilistic tuple.
     * 
     * @param input
     *            The input tuple
     * @param restrict
     *            The restriction matrix
     * @param output
     *            The expected result
     */
    @Test(dataProvider = "tuple")
    public final void testRestrict(final ProbabilisticTuple<IMetaAttribute> input, final int[] restrict, final ProbabilisticTuple<IMetaAttribute> output) {
        for (int i = 0; i < input.getAttributes().length; i++) {
            if (input.getAttribute(i) instanceof ProbabilisticDouble) {
                final int distribution = ((ProbabilisticDouble) input.getAttribute(i)).getDistribution();
                boolean contains = false;
                for (int d = 0; d < input.getDistribution(distribution).getAttributes().length; d++) {
                    if (input.getDistribution(distribution).getAttribute(d) == i) {
                        contains = true;
                    }
                }

                Assert.assertTrue(contains);
            }
        }
        final ProbabilisticTuple<IMetaAttribute> restrictedNew = input.restrict(restrict, true);
        System.out.println("Input: " + input + " -" + Arrays.toString(restrict) + "> " + restrictedNew);
        final ProbabilisticTuple<IMetaAttribute> restricted = input.restrict(restrict, false);

        Assert.assertEquals(restricted, restrictedNew);
        Assert.assertEquals(restricted, input);
        for (int i = 0; i < restricted.getAttributes().length; i++) {
            if (restricted.getAttribute(i) instanceof ProbabilisticDouble) {
                final int distribution = ((ProbabilisticDouble) restricted.getAttribute(i)).getDistribution();
                boolean contains = false;
                for (int d = 0; d < restricted.getDistribution(distribution).getAttributes().length; d++) {
                    if (restricted.getDistribution(distribution).getAttribute(d) == i) {
                        contains = true;
                    }
                }

                Assert.assertTrue(contains);
            }
        }
    }

    /**
     * Probabilistic tuple provider.
     * 
     * @return A array of probabilistic tuples
     */
    @DataProvider(name = "tuple")
    public final Object[][] provideTuple() {
        MultivariateNormalDistribution distribution3D1 = new MultivariateNormalDistribution(new double[] { 3.0, 6.0, 9.0 }, new double[][] { { 1.0, 0.3, 0.3 }, { 0.3, 1.0, 0.3 },
                { 0.3, 0.3, 1.0 } });
        MultivariateNormalDistribution distribution3D2 = new MultivariateNormalDistribution(new double[] { -3.0, -6.0, -9.0 }, new double[][] { { 1.0, 0.3, 0.3 }, { 0.3, 1.0, 0.3 },
                { 0.3, 0.3, 1.0 } });

        MultivariateNormalDistribution distribution2D1 = new MultivariateNormalDistribution(new double[] { 2.0, 4.0 }, new double[][] { { 1.0, 0.2 }, { 0.2, 1.0 } });
        MultivariateNormalDistribution distribution2D2 = new MultivariateNormalDistribution(new double[] { -2.0, -4.0 }, new double[][] { { 1.0, 0.2 }, { 0.2, 1.0 } });

        MultivariateNormalDistribution distribution1D1 = new MultivariateNormalDistribution(new double[] { 1.0 }, new double[][] { { 1.0 } });
        MultivariateNormalDistribution distribution1D2 = new MultivariateNormalDistribution(new double[] { -1.0 }, new double[][] { { 1.0 } });

        MultivariateMixtureDistribution mixture3D = new MultivariateMixtureDistribution(new double[] { 0.75, 0.25 }, new IMultivariateDistribution[] { distribution3D1,
                distribution3D2 });
        mixture3D.setAttributes(new int[] { 1, 3, 5 });
        MultivariateMixtureDistribution mixture2D = new MultivariateMixtureDistribution(new double[] { 0.75, 0.25 }, new IMultivariateDistribution[] { distribution2D1,
                distribution2D2 });
        mixture2D.setAttributes(new int[] { 2, 4 });
        MultivariateMixtureDistribution mixture1D = new MultivariateMixtureDistribution(new double[] { 0.75, 0.25 }, new IMultivariateDistribution[] { distribution1D1,
                distribution1D2 });
        mixture1D.setAttributes(new int[] { 0 });

        ProbabilisticTuple<?> tuple3D = new ProbabilisticTuple<>(new Object[] { "zero", new ProbabilisticDouble(0), "two", new ProbabilisticDouble(0), "four",
                new ProbabilisticDouble(0), "six" }, new MultivariateMixtureDistribution[] { mixture3D }, true);
        ProbabilisticTuple<?> tuple2D = new ProbabilisticTuple<>(new Object[] { "zero", "one", new ProbabilisticDouble(0), "three", new ProbabilisticDouble(0), "five", "six" },
                new MultivariateMixtureDistribution[] { mixture2D }, true);
        ProbabilisticTuple<?> tuple1D = new ProbabilisticTuple<>(new Object[] { new ProbabilisticDouble(0), "one", "two", "three", "four", "five", "six" },
                new MultivariateMixtureDistribution[] { mixture1D }, true);

        ProbabilisticTuple<?> tuple3D2D = new ProbabilisticTuple<>(new Object[] { "zero", new ProbabilisticDouble(0), new ProbabilisticDouble(1),
                new ProbabilisticDouble(0), new ProbabilisticDouble(1), new ProbabilisticDouble(0), "six" }, new MultivariateMixtureDistribution[] {
                mixture3D, mixture2D }, true);

        return new Object[][] { { tuple3D.clone(), new int[] { 0, 2, 3 }, tuple3D.clone() }, { tuple2D.clone(), new int[] { 1, 2, 3 }, tuple2D.clone() },
                { tuple1D.clone(), new int[] { 0, 2, 3 }, tuple1D.clone() }, { tuple3D2D.clone(), new int[] { 0, 2, 3 }, tuple3D2D.clone() } };
    }

}
