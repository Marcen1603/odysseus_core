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
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
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
public class SelectPOCompareContinuousProbabilityDistributionsWithDiscreteNumbersTest extends AbstractSelectPOTest {

    @Parameters(name = "{index}: Predicate: {0}, Input: [{1},{2}], Existence: {5}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { //
                // Test selection operation on a continuous distribution

                // Given the expression x < y, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "x < y", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x <= y, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "x <= y", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })///
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y > x, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "y > x", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) }, 2.0), //
                        0, 0.5 }, //
                // Given the expression y >= x, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "y >= x", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x > y, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "x > y", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x >= y, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "x >= y", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y < x, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "y < x", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y <= x, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should return the tuple with existence 0.5.
                { "y <= x", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x == y, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should discard the tuple.
                { "x == y", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        null, //
                        null, //
                        0.0 }, //
                // Given the expression x == y, with x being a continuous
                // distribution with x = N(0.0, 1.0) and y = 0,
                // the selection should discard the tuple.
                { "y == x", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        null, //
                        null, //
                        0.0 }, //

                // Test selection operation on a multivariate mixture
                // distribution containing two continuous distribution

                // Given the expression x < y, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "x < y", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x <= y, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "x <= y", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y > x, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "y > x", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y >= x, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "y >= x", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x > y, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "x > y", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x >= y, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "x >= y", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y < x, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "y < x", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression y <= x, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should return the tuple with existence 0.5.
                { "y <= x", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY) }, 2.0), //
                        0, //
                        0.5 }, //
                // Given the expression x == y, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should discard the tuple.
                { "x == y", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        null, //
                        null, //
                        0.0 }, //
                // Given the expression y == x, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 0,
                // the selection should discard the tuple.
                { "y == x", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        0, //
                        null, //
                        null, //
                        0.0 }, //
                // Given the expression x == y, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 1,
                // the selection should discard the tuple.
                { "x == y", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        1, //
                        null, //
                        null, //
                        0.0 }, //
                // Given the expression y == x, with x being two continuous
                // distribution with x = 0.5N(-1.0, 1.0) + 0.5N(1.0, 1.0) and y
                // = 1,
                // the selection should discard the tuple.
                { "y == x", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 1.0 }), //
                                new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                        }), //
                        1, //
                        null, //
                        null, //
                        0.0 }, //
        });

    }



    @Parameter(0)
    public String predicateString;
    @Parameter(1)
    public Object inputX;
    @Parameter(2)
    public Object inputY;
    @Parameter(3)
    public Object outputX;
    @Parameter(4)
    public Object outputY;
    @Parameter(5)
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
        givenInputTupleWithValues(new Object[] { this.inputX, this.inputY });

        whenProcessTuple();
        if ((this.outputX == null) && (this.outputY == null)) {
            thenOutputEquals(this.existence, (Object[]) null);
        } else {
            thenOutputEquals(this.existence, new Object[] { this.outputX, this.outputY });
        }
    }

}
