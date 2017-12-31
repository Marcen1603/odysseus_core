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
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateEnumeratedDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;

/**
 * Processing tests for {@link ProbabilisticSelectPO} operator covering
 * {@link MultivariateEnumeratedDistribution} filtering.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@RunWith(Parameterized.class)
public class SelectPOCompareMultiDimensionalDiscreteProbabilityDistributionsWithDiscreteNumbersTest extends AbstractSelectPOTest {

    @Parameters(name = "{index}: Predicate: {0}, Input: [{1},{2}], Existence: {5}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { //
                // Test selection operation on a enumerated distribution

                // 0
                // Given the expression as2DVector(x1,x2) < [y1,y2], with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should discard the tuple.
                { "as2DVector(x1,x2) < [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        null, //
                        null, //
                        0.0 }, //
                // 1
                // Given the expression as2DVector(x1,x2) <=[y1,y2], with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should return the tuple.
                { "as2DVector(x1,x2) <= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) }), //
                        new Object[] { 0.0, 0.0 }, //
                        1.0 }, //
                // 2
                // Given the expression [y1,y2] > as2DVector(x1,x2), with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should discard the tuple.
                { "[y1,y2] > as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        null, //
                        null, //
                        0.0 }, //
                // 3
                // Given the expression [y1,y2] >= as2DVector(x1,x2), with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should return the tuple.
                { "[y1,y2] >= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) }), //
                        new Object[] { 0.0, 0.0 }, //
                        1.0 }, //
                // 4
                // Given the expression as2DVector(x1,x2) > [y1,y2], with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should discard the tuple.
                { "as2DVector(x1,x2) > [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        null, //
                        null, //
                        0.0 }, //
                // 5
                // Given the expression as2DVector(x1,x2) >= [y1,y2], with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should return the tuple.
                { "as2DVector(x1,x2) >= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) }), //
                        new Object[] { 0.0, 0.0 }, //
                        1.0 }, //
                // 6
                // Given the expression [y1,y2] < as2DVector(x1,x2), with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should discard the tuple.
                { "[y1,y2] < as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        null, //
                        null, //
                        0.0 }, //
                // 7
                // Given the expression [y1,y2] <= as2DVector(x1,x2), with x
                // being a
                // discrete
                // distribution with a singleton at {0.0,0.0} with p(x)=1.0 and
                // y = {0,0},
                // the selection should return the tuple.
                { "[y1,y2] <= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { 0.0, 0.0 }, 1.0)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) }), //
                        new Object[] { 0.0, 0.0 }, //
                        1.0 }, //
                // Test selection operation on a multivariate mixture
                // distribution containing two enumerated distribution

                // 8
                // Given the expression as2DVector(x1,x2) < [y1,y2], with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "as2DVector(x1,x2) < [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE), new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 9
                // Given the expression as2DVector(x1,x2) <= [y1,y2], with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "as2DVector(x1,x2) <= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 10
                // Given the expression [y1,y2] > as2DVector(x1,x2), with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "[y1,y2] > as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE), new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 11
                // Given the expression [y1,y2] >= as2DVector(x1,x2), with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "[y1,y2] >= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 12
                // Given the expression as2DVector(x1,x2) > [y1,y2], with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "as2DVector(x1,x2) > [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY), new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 13
                // Given the expression as2DVector(x1,x2) >= [y1,y2], with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "as2DVector(x1,x2) >= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) },2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 14
                // Given the expression [y1,y2] < as2DVector(x1,x2), with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "[y1,y2] < as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY), new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
                // 15
                // Given the expression [y1,y2] <= as2DVector(x1,x2), with x
                // being two discrete
                // distribution with singletons at {-1.0,-1.0} with p(x)=1.0 and
                // {1.0,1.0} with p(1.0)=1.0 both weighted by 0.5, and y =
                // {0.0,0.0},
                // the selection should return the tuple with existence 0.5.
                { "[y1,y2] <= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateEnumeratedDistribution(new double[] { -1.0, -1.0 }, 1.0), //
                                        new MultivariateEnumeratedDistribution(new double[] { 1.0, 1.0 }, 1.0)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) }, 2.0), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 }, //
        });

    }

    @Parameter(0)
    public String predicateString;
    @Parameter(1)
    public Object inputX;
    @Parameter(2)
    public Object[] inputY;
    @Parameter(3)
    public Object outputX;
    @Parameter(4)
    public Object[] outputY;
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
                attribute("x1").as(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), //
                attribute("x2").as(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), //
                attribute("y1").as(SDFDatatype.DOUBLE), //
                attribute("y2").as(SDFDatatype.DOUBLE) //

        );
        givenPredicate(this.predicateString);
        givenProbabilisticSelectPO();
        givenInputTupleWithValues(new Object[] { this.inputX, this.inputX, this.inputY[0], this.inputY[1] });

        whenProcessTuple();
        if ((this.outputX == null) && (this.outputY == null)) {
            thenOutputEquals(this.existence, (Object[]) null);
        } else {
            thenOutputEquals(this.existence, new Object[] { this.outputX, this.outputX, this.outputY[0], this.outputY[1] });
        }
    }

}
