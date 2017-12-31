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
import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;

/**
 * Processing tests for {@link ProbabilisticSelectPO} operator covering
 * {@link MultivariateNormalDistribution} filtering. This test does not cover
 * the estimation of the the cumulative distribution itself.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@RunWith(Parameterized.class)
public class SelectPOCompareMultiDimensionalContinuousProbabilityDistributionsWithDiscreteNumbersTest extends AbstractSelectPOTest {
    private static final double[][] SIGMA = new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } };
    private static final double[] MU_ZERO = new double[] { 0.0, 0.0 };
    private static final double[] MU_POSITIVE = new double[] { 1.0, 1.0 };
    private static final double[] MU_NEGATIVE = new double[] { -1.0, -1.0 };
    private static final double[] NEGATIVE_INFINITY = new double[] { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY };
    private static final double[] POSITIVE_INFINITY = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
    private static final double[] ZERO = new double[] { 0.0, 0.0 };

    @Parameters(name = "{index}: Predicate: {0}, Input: [{1},{2}], Existence: {5}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { //
                // Test selection operation on a continuous distribution

                // 0
                // Given the expression as2DVector(x1,x2) < [y1,y2], with x
                // being a
                // continuous
                // distribution with x = N({0.0, 0.0}, {{1.0, 0.5}, {0.5, 1.0}})
                // and
                // y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, NEGATIVE_INFINITY, ZERO).
                { "as2DVector(x1,x2) < [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE), new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) },
                                1.0 / cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 1
                // Given the expression as2DVector(x1,x2) <=[y1,y2], with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, NEGATIVE_INFINITY, ZERO).
                { "as2DVector(x1,x2) <= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 1.0 / cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 2
                // Given the expression [y1,y2] > as2DVector(x1,x2), with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, NEGATIVE_INFINITY, ZERO).
                { "[y1,y2] > as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE), new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) },
                                1.0 / cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 3
                // Given the expression [y1,y2] >= as2DVector(x1,x2), with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, NEGATIVE_INFINITY, ZERO).
                { "[y1,y2] >= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) }, 1.0 / cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 4
                // Given the expression as2DVector(x1,x2) > [y1,y2], with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, ZERO, POSITIVE_INFINITY).
                { "as2DVector(x1,x2) > [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY), new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) },
                                1.0 / cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // 5
                // Given the expression as2DVector(x1,x2) >= [y1,y2], with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, ZERO, POSITIVE_INFINITY).
                { "as2DVector(x1,x2) >= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) }, 1.0 / cdf(MU_ZERO, SIGMA, NEGATIVE_INFINITY, ZERO)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // 6
                // Given the expression [y1,y2] < as2DVector(x1,x2), with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, ZERO, POSITIVE_INFINITY).
                { "[y1,y2] < as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY), new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) },
                                1.0 / cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // 7
                // Given the expression [y1,y2] <= as2DVector(x1,x2), with x
                // being a
                // continuous distribution with x = N({0.0, 0.0}, {{1.0, 0.5},
                // {0.5, 1.0}})
                // and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_ZERO,
                // SIGMA, ZERO, POSITIVE_INFINITY).
                { "[y1,y2] <= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_ZERO, SIGMA)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) }, 1.0 / cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY)), //
                        new Object[] { 0.0, 0.0 }, //
                        cdf(MU_ZERO, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // Test selection operation on a multivariate mixture
                // distribution containing two continuous distribution

                // 8
                // Given the expression as2DVector(x1,x2) < [y1,y2], with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2 + cdf(MU_POSITIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2.
                { "as2DVector(x1,x2) < [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE), new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 9
                // Given the expression as2DVector(x1,x2) <= [y1,y2], with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2 + cdf(MU_POSITIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2.
                { "as2DVector(x1,x2) <= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 10
                // Given the expression [y1,y2] > as2DVector(x1,x2), with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2 + cdf(MU_POSITIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2.
                { "[y1,y2] > as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE), new Interval(Double.NEGATIVE_INFINITY, -Double.MIN_VALUE) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 11
                // Given the expression [y1,y2] >= as2DVector(x1,x2), with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2 + cdf(MU_POSITIVE,
                // SIGMA, NEGATIVE_INFINITY, ZERO)/2.
                { "[y1,y2] >= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(Double.NEGATIVE_INFINITY, 0.0) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, NEGATIVE_INFINITY, ZERO) + 0.5 * cdf(MU_NEGATIVE, SIGMA, NEGATIVE_INFINITY, ZERO) }, //
                // 12
                // Given the expression as2DVector(x1,x2) > [y1,y2], with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2 + cdf(MU_POSITIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2.
                { "as2DVector(x1,x2) > [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY), new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // 13
                // Given the expression as2DVector(x1,x2) >= [y1,y2], with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2 + cdf(MU_POSITIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2.
                { "as2DVector(x1,x2) >= [y1,y2]", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // 14
                // Given the expression [y1,y2] < as2DVector(x1,x2), with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2 + cdf(MU_POSITIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2.
                { "[y1,y2] < as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY), new Interval(Double.MIN_VALUE, Double.POSITIVE_INFINITY) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY) }, //
                // 15
                // Given the expression [y1,y2] <= as2DVector(x1,x2), with x
                // being two
                // continuous distribution with x = 0.5N({-1.0, -1.0}, {{0.5,
                // 1.0}, {1.0, 0.5}}) + 0.5N({1.0, 1.0}, {{0.5, 1.0}, {1.0,
                // 0.5}}) and y = {0.0, 0.0},
                // the selection should reduce the existence to cdf(MU_NEGATIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2 + cdf(MU_POSITIVE,
                // SIGMA, ZERO, POSITIVE_INFINITY)/2.
                { "[y1,y2] <= as2DVector(x1,x2)", new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                        new IMultivariateDistribution[] { //
                                new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                        }), //
                        new Object[] { 0.0, 0.0 }, //
                        new MultivariateMixtureDistribution(new double[] { 0.5, 0.5 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(MU_NEGATIVE, SIGMA), //
                                        new MultivariateNormalDistribution(MU_POSITIVE, SIGMA)//
                                }, new Interval[] { new Interval(0.0, Double.POSITIVE_INFINITY), new Interval(0.0, Double.POSITIVE_INFINITY) },
                                1.0 / (0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY))), //
                        new Object[] { 0.0, 0.0 }, //
                        0.5 * cdf(MU_POSITIVE, SIGMA, ZERO, POSITIVE_INFINITY) + 0.5 * cdf(MU_NEGATIVE, SIGMA, ZERO, POSITIVE_INFINITY) }, //
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
     * Helper method to estimate the cumulative distribution function (CDF) of
     * the given multivariate normal distribution
     * 
     * @param mu
     *            The mean
     * @param sigma
     *            The correlation matrix
     * @param lower
     *            The lower limit for the integration
     * @param upper
     *            The upper limit for the integration
     * @return The cumulative distribution at each row
     */
    public static double cdf(double[] mu, double[][] sigma, double[] lower, double[] upper) {
        Matrix muVector = new Matrix(new double[][] { mu });
        Matrix sigmaMatrix = new Matrix(sigma);
        Matrix lowerBound = (new Matrix(new double[][] { lower })).substract(muVector);
        Matrix upperBound = (new Matrix(new double[][] { upper })).substract(muVector);
        return QSIMVN.cumulativeProbability(5000, sigmaMatrix, lowerBound, upperBound).getProbability();
    }

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
