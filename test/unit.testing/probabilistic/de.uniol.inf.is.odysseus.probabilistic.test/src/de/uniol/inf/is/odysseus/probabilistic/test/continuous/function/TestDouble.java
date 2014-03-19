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
package de.uniol.inf.is.odysseus.probabilistic.test.continuous.function;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticAddNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticAddOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticDivideNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticMultiplyNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticSubtractNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticSubtractOperator;
import de.uniol.inf.is.odysseus.probabilistic.test.TestConstants;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Test
public class TestDouble {

    /**
     * Test "leq"-operator for continuous values.
     * 
     * @param left
     *            The left value
     * @param mixtures
     *            The normal distribution mixture
     * @param right
     *            The right value
     * @param result
     *            The expected result
     */

    @Test(dataProvider = "continuousSmallerEqualsDouble")
    public final void testDoubleSmallerEquals(final ProbabilisticDouble left, final MultivariateMixtureDistribution mixtures, final Double right, final double result) {
        final IFunction<ProbabilisticBooleanResult> function = new ProbabilisticSmallerEqualsOperator();
        ((AbstractProbabilisticFunction<ProbabilisticBooleanResult>) function).getDistributions().add(mixtures);
        function.setArguments(new Constant<IMultivariateDistribution>(mixtures, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
        Assert.assertEquals(((MultivariateMixtureDistribution) function.getValue().getDistribution()).getScale(), result, TestConstants.EPSILON);
    }

    /**
     * Test "eq"-operator for continuous values.
     * 
     * @param left
     *            The left value
     * @param mixtures
     *            The normal distribution mixture
     * @param right
     *            The right value
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousEqualsDouble")
    public final void testDoubleEquals(final ProbabilisticDouble left, final MultivariateMixtureDistribution mixtures, final Double right, final double result) {
        final IFunction<ProbabilisticBooleanResult> function = new ProbabilisticEqualsOperator();
        ((AbstractProbabilisticFunction<ProbabilisticBooleanResult>) function).getDistributions().add(mixtures);
        function.setArguments(new Constant<IMultivariateDistribution>(mixtures, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
        Assert.assertEquals(((MultivariateMixtureDistribution) function.getValue().getDistribution()).getScale(), result, TestConstants.EPSILON);
    }

    /**
     * Test "geq"-operator for continuous values.
     * 
     * @param left
     *            The left value
     * @param mixtures
     *            The normal distribution mixture
     * @param right
     *            The right value
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousGreaterEqualsDouble")
    public final void testDoubleGreaterEquals(final ProbabilisticDouble left, final MultivariateMixtureDistribution mixtures, final Double right, final double result) {
        final IFunction<ProbabilisticBooleanResult> function = new ProbabilisticGreaterEqualsOperator();
        ((AbstractProbabilisticFunction<ProbabilisticBooleanResult>) function).getDistributions().add(mixtures);
        function.setArguments(new Constant<IMultivariateDistribution>(mixtures, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
        Assert.assertEquals(((MultivariateMixtureDistribution) function.getValue().getDistribution()).getScale(), result, TestConstants.EPSILON);
    }

    /**
     * Test "+"-operator for continuous values.
     * 
     * @param a
     *            The distribution mixture a
     * @param b
     *            The distribution mixture b
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousPlusDouble")
    public final void testDoublePlus(final IMultivariateDistribution a, final IMultivariateDistribution b, final MultivariateMixtureDistribution result) {
        final IFunction<IMultivariateDistribution> function = new ProbabilisticAddOperator();
        function.setArguments(new Constant<IMultivariateDistribution>(a, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<IMultivariateDistribution>(b,
                SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
        Assert.assertEquals(function.getValue(), result);
        Assert.assertNotEquals(function.getValue(), a);
    }

    /**
     * Test "-"-operator for continuous values.
     * 
     * @param a
     *            The distribution mixture a
     * @param b
     *            The distribution mixture b
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousMinusDouble")
    public final void testDoubleMinus(final MultivariateMixtureDistribution a, final MultivariateMixtureDistribution b, final MultivariateMixtureDistribution result) {
        final IFunction<IMultivariateDistribution> function = new ProbabilisticSubtractOperator();
        function.setArguments(new Constant<IMultivariateDistribution>(a, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<IMultivariateDistribution>(b,
                SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
        Assert.assertEquals(function.getValue(), result);
        Assert.assertNotEquals(function.getValue(), a);
    }

    /**
     * Test "+"-operator for continuous values.
     * 
     * @param mixture
     *            The distribution mixture
     * @param value
     *            The value
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousPlusDoubleNumber")
    public final void testDoubleNumberPlus(final MultivariateMixtureDistribution mixture, final double value, final MultivariateMixtureDistribution result) {
        final IFunction<IMultivariateDistribution> function = new ProbabilisticAddNumberRHSOperator();
        function.setArguments(new Constant<IMultivariateDistribution>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
        Assert.assertEquals(function.getValue(), result);
        Assert.assertNotEquals(function.getValue(), mixture);
    }

    /**
     * Test "-"-operator for continuous values.
     * 
     * @param mixture
     *            The distribution mixture
     * @param value
     *            The value
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousMinusDoubleNumber")
    public final void testDoubleNumberMinus(final MultivariateMixtureDistribution mixture, final double value, final MultivariateMixtureDistribution result) {
        final IFunction<IMultivariateDistribution> function = new ProbabilisticSubtractNumberRHSOperator();
        function.setArguments(new Constant<IMultivariateDistribution>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
        Assert.assertEquals(function.getValue(), result);
        Assert.assertNotEquals(function.getValue(), mixture);
    }

    /**
     * Test "*"-operator for continuous values.
     * 
     * @param mixture
     *            The distribution mixture
     * @param value
     *            The value
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousMultiplicationDoubleNumber")
    public final void testDoubleNumberMultiplication(final MultivariateMixtureDistribution mixture, final double value, final MultivariateMixtureDistribution result) {
        final IFunction<IMultivariateDistribution> function = new ProbabilisticMultiplyNumberRHSOperator();
        function.setArguments(new Constant<IMultivariateDistribution>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
        Assert.assertEquals(function.getValue(), result);
        Assert.assertNotEquals(function.getValue(), mixture);
    }

    /**
     * Test "/"-operator for continuous values.
     * 
     * @param mixture
     *            The distribution mixture
     * @param value
     *            The value
     * @param result
     *            The expected result
     */
    @Test(dataProvider = "continuousDivisionDoubleNumber")
    public final void testDoubleNumberDivision(final MultivariateMixtureDistribution mixture, final double value, final MultivariateMixtureDistribution result) {
        final IFunction<IMultivariateDistribution> function = new ProbabilisticDivideNumberRHSOperator();
        function.setArguments(new Constant<IMultivariateDistribution>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
        Assert.assertEquals(function.getValue(), result);
        Assert.assertNotEquals(function.getValue(), mixture);
    }

    /**
     * Data provider for "lt" tests.
     * 
     * @return Data for the "lt" tests
     */
    @DataProvider(name = "continuousSmallerThanDouble")
    public final Object[][] provideContinuousSmallerThanDoubleValues() {
        return new Object[][] {};
    }

    /**
     * Data provider for "leq" tests.
     * 
     * @return Data for the "leq" tests
     */
    @DataProvider(name = "continuousSmallerEqualsDouble")
    public final Object[][] provideContinuousSmallerEqualsDoubleValues() {
        final MultivariateMixtureDistribution univariateMixtures = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0, 2.0 },
                new double[] { 1.0, 0.0, 1.0 }));
        final MultivariateMixtureDistribution multivariateMixtures = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0,
                1.0 }));
        return new Object[][] { { new ProbabilisticDouble(0), univariateMixtures, 3.0, 1.0 / 0.9973020261614356 }, { new ProbabilisticDouble(0), multivariateMixtures, 3.0, 1.0 / 0.9973020261614356 } };
    }

    /**
     * Data provider for "eq" tests.
     * 
     * @return Data for the "eq" tests
     */
    @DataProvider(name = "continuousEqualsDouble")
    public final Object[][] provideContinuousEqualsDoubleValues() {
        final MultivariateMixtureDistribution univariateMixtures = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0, 2.0 },
                new double[] { 1.0, 0.0, 1.0 }));
        final MultivariateMixtureDistribution multivariateMixtures = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0,
                1.0 }));
        return new Object[][] { { new ProbabilisticDouble(0), univariateMixtures, 3.0, Double.POSITIVE_INFINITY }, { new ProbabilisticDouble(0), multivariateMixtures, 3.0, Double.POSITIVE_INFINITY } };
    }

    /**
     * Data provider for "geq" tests.
     * 
     * @return Data for the "geq" tests
     */
    @DataProvider(name = "continuousGreaterEqualsDouble")
    public final Object[][] provideContinuousGreaterEqualsDoubleValues() {
        final MultivariateMixtureDistribution univariateMixtures = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0, 2.0 },
                new double[] { 1.0, 0.0, 1.0 }));
        final MultivariateMixtureDistribution multivariateMixtures = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0,
                1.0 }));
        return new Object[][] { { new ProbabilisticDouble(0), univariateMixtures, 3.0, 1.0 / 1.8222246957988279E-6 },
                { new ProbabilisticDouble(0), multivariateMixtures, 3.0, 1.0 / 1.8222246957988279E-6 } };
    }

    /**
     * Data provider for "gt" tests.
     * 
     * @return Data for the "gt" tests
     */
    @DataProvider(name = "continuousGreaterThanDouble")
    public final Object[][] provideContinuousGreaterThanDoubleValues() {
        return new Object[][] {};
    }

    /**
     * Data provider for "+" operation.
     * 
     * @return An array of test and expected values
     */
    @DataProvider(name = "continuousPlusDouble")
    public final Object[][] provideContinuousPlusDoubleValues() {
        final MultivariateMixtureDistribution a = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));
        final MultivariateMixtureDistribution b1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));
        final MultivariateMixtureDistribution b2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { -3.0 }, new double[] { 3.0 }));
        final MultivariateMixtureDistribution b3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 1.0 }));

        final MultivariateMixtureDistribution resultMixture1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 6.0 }, new double[] { 6.0 }));

        final MultivariateMixtureDistribution resultMixture2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 6.0 }));

        final MultivariateMixtureDistribution resultMixture3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 6.0 }, new double[] { 4.0 }));

        return new Object[][] { { a, b1, resultMixture1 }, { a, b2, resultMixture2 }, { a, b3, resultMixture3 } };
    }

    /**
     * Data provider for "-" operation.
     * 
     * @return An array of test and expected values
     */
    @DataProvider(name = "continuousMinusDouble")
    public final Object[][] provideContinuousMinusDoubleValues() {
        final MultivariateMixtureDistribution a = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));
        final MultivariateMixtureDistribution b1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 2.0 }));
        final MultivariateMixtureDistribution b2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { -3.0 }, new double[] { 2.0 }));
        final MultivariateMixtureDistribution b3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 1.0 }));

        final MultivariateMixtureDistribution resultMixture1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 5.0 }));

        final MultivariateMixtureDistribution resultMixture2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 6.0 }, new double[] { 5.0 }));

        final MultivariateMixtureDistribution resultMixture3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 4.0 }));

        return new Object[][] { { a, b1, resultMixture1 }, { a, b2, resultMixture2 }, { a, b3, resultMixture3 } };
    }

    /**
     * Data provider for "+" operation.
     * 
     * @return An array of test and expected values
     */
    @DataProvider(name = "continuousPlusDoubleNumber")
    public final Object[][] provideContinuousPlusDoubleNumberValues() {
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 4.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 5.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 3.0 }));

        return new Object[][] { { mixture, 1.0, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
    }

    /**
     * Data provider for "-" operation.
     * 
     * @return An array of test and expected values
     */
    @DataProvider(name = "continuousMinusDoubleNumber")
    public final Object[][] provideContinuousMinusDoubleNumberValues() {
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 2.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 5.0 }, new double[] { 3.0 }));

        return new Object[][] { { mixture, 1.0, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
    }

    /**
     * Data provider for "*" operation.
     * 
     * @return An array of test and expected values
     */
    @DataProvider(name = "continuousMultiplicationDoubleNumber")
    public final Object[][] provideContinuousMultiplicationDoubleNumberValues() {
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 * 1.5 },
                new double[] { 3.0 * (1.5 * 1.5) }));

        final MultivariateMixtureDistribution resultMixture2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 6.0 }, new double[] { 12.0 }));

        final MultivariateMixtureDistribution resultMixture3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { -6.0 }, new double[] { 12.0 }));

        return new Object[][] { { mixture, 1.5, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
    }

    /**
     * Data provider for "/" operation.
     * 
     * @return An array of test and expected values
     */
    @DataProvider(name = "continuousDivisionDoubleNumber")
    public final Object[][] provideContinuousDivisionDoubleNumberValues() {
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 3.0 }));

        final MultivariateMixtureDistribution resultMixture1 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 3.0 / 1.5 },
                new double[] { 3.0 / (1.5 * 1.5) }));

        final MultivariateMixtureDistribution resultMixture2 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { 1.5 }, new double[] { 0.75 }));

        final MultivariateMixtureDistribution resultMixture3 = new MultivariateMixtureDistribution(1.0, new MultivariateNormalDistribution(new double[] { -1.5 }, new double[] { 0.75 }));

        return new Object[][] { { mixture, 1.5, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
    }
}
