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
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticContinuousDivisionNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticContinuousMinusNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticContinuousMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticContinuousMultiplicationNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticContinuousPlusNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticContinuousPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
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
	public final void testDoubleSmallerEquals(final ProbabilisticContinuousDouble left, final NormalDistributionMixture mixtures, final Double right, final double result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousSmallerEqualsOperator();
		((AbstractProbabilisticFunction<NormalDistributionMixture>) function).getDistributions().add(mixtures);
		function.setArguments(new Constant<NormalDistributionMixture>(mixtures, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue().getScale(), result, TestConstants.EPSILON);
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
	public final void testDoubleEquals(final ProbabilisticContinuousDouble left, final NormalDistributionMixture mixtures, final Double right, final double result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousEqualsOperator();
		((AbstractProbabilisticFunction<NormalDistributionMixture>) function).getDistributions().add(mixtures);
		function.setArguments(new Constant<NormalDistributionMixture>(mixtures, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue().getScale(), result, TestConstants.EPSILON);
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
	public final void testDoubleGreaterEquals(final ProbabilisticContinuousDouble left, final NormalDistributionMixture mixtures, final Double right, final double result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousGreaterEqualsOperator();
		((AbstractProbabilisticFunction<NormalDistributionMixture>) function).getDistributions().add(mixtures);
		function.setArguments(new Constant<NormalDistributionMixture>(mixtures, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue().getScale(), result, TestConstants.EPSILON);
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
	public final void testDoublePlus(final NormalDistributionMixture a, final NormalDistributionMixture b, final NormalDistributionMixture result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousPlusOperator();
		function.setArguments(new Constant<NormalDistributionMixture>(a, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<NormalDistributionMixture>(b, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
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
	public final void testDoubleMinus(final NormalDistributionMixture a, final NormalDistributionMixture b, final NormalDistributionMixture result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousMinusOperator();
		function.setArguments(new Constant<NormalDistributionMixture>(a, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<NormalDistributionMixture>(b, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
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
	public final void testDoubleNumberPlus(final NormalDistributionMixture mixture, final double value, final NormalDistributionMixture result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousPlusNumberRHSOperator();
		function.setArguments(new Constant<NormalDistributionMixture>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
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
	public final void testDoubleNumberMinus(final NormalDistributionMixture mixture, final double value, final NormalDistributionMixture result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousMinusNumberRHSOperator();
		function.setArguments(new Constant<NormalDistributionMixture>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
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
	public final void testDoubleNumberMultiplication(final NormalDistributionMixture mixture, final double value, final NormalDistributionMixture result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousMultiplicationNumberRHSOperator();
		function.setArguments(new Constant<NormalDistributionMixture>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
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
	public final void testDoubleNumberDivision(final NormalDistributionMixture mixture, final double value, final NormalDistributionMixture result) {
		final IFunction<NormalDistributionMixture> function = new ProbabilisticContinuousDivisionNumberRHSOperator();
		function.setArguments(new Constant<NormalDistributionMixture>(mixture, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(value, SDFDatatype.DOUBLE));
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
		final NormalDistributionMixture univariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		final NormalDistributionMixture multivariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		return new Object[][] { { new ProbabilisticContinuousDouble(0), univariateMixtures, 3.0, 1.0 / 0.9973020261614356 }, { new ProbabilisticContinuousDouble(0), multivariateMixtures, 3.0, 1.0 / 0.9973020261614356 } };
	}

	/**
	 * Data provider for "eq" tests.
	 * 
	 * @return Data for the "eq" tests
	 */
	@DataProvider(name = "continuousEqualsDouble")
	public final Object[][] provideContinuousEqualsDoubleValues() {
		final NormalDistributionMixture univariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		final NormalDistributionMixture multivariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		return new Object[][] { { new ProbabilisticContinuousDouble(0), univariateMixtures, 3.0, Double.POSITIVE_INFINITY }, { new ProbabilisticContinuousDouble(0), multivariateMixtures, 3.0, Double.POSITIVE_INFINITY } };
	}

	/**
	 * Data provider for "geq" tests.
	 * 
	 * @return Data for the "geq" tests
	 */
	@DataProvider(name = "continuousGreaterEqualsDouble")
	public final Object[][] provideContinuousGreaterEqualsDoubleValues() {
		final NormalDistributionMixture univariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		final NormalDistributionMixture multivariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		return new Object[][] { { new ProbabilisticContinuousDouble(0), univariateMixtures, 3.0, 1.0 / 1.8222246957988279E-6 }, { new ProbabilisticContinuousDouble(0), multivariateMixtures, 3.0, 1.0 / 1.8222246957988279E-6 } };
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
		final NormalDistributionMixture a = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });
		final NormalDistributionMixture b1 = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });
		final NormalDistributionMixture b2 = new NormalDistributionMixture(new double[] { -3.0 }, new double[] { 3.0 });
		final NormalDistributionMixture b3 = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 1.0 });

		final NormalDistributionMixture resultMixture1 = new NormalDistributionMixture(new double[] { 6.0 }, new double[] { 6.0 });

		final NormalDistributionMixture resultMixture2 = new NormalDistributionMixture(new double[] { 0.0 }, new double[] { 6.0 });

		final NormalDistributionMixture resultMixture3 = new NormalDistributionMixture(new double[] { 6.0 }, new double[] { 4.0 });

		return new Object[][] { { a, b1, resultMixture1 }, { a, b2, resultMixture2 }, { a, b3, resultMixture3 } };
	}

	/**
	 * Data provider for "-" operation.
	 * 
	 * @return An array of test and expected values
	 */
	@DataProvider(name = "continuousMinusDouble")
	public final Object[][] provideContinuousMinusDoubleValues() {
		final NormalDistributionMixture a = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });
		final NormalDistributionMixture b1 = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 2.0 });
		final NormalDistributionMixture b2 = new NormalDistributionMixture(new double[] { -3.0 }, new double[] { 2.0 });
		final NormalDistributionMixture b3 = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 1.0 });

		final NormalDistributionMixture resultMixture1 = new NormalDistributionMixture(new double[] { 0.0 }, new double[] { 5.0 });

		final NormalDistributionMixture resultMixture2 = new NormalDistributionMixture(new double[] { 6.0 }, new double[] { 5.0 });

		final NormalDistributionMixture resultMixture3 = new NormalDistributionMixture(new double[] { 0.0 }, new double[] { 4.0 });

		return new Object[][] { { a, b1, resultMixture1 }, { a, b2, resultMixture2 }, { a, b3, resultMixture3 } };
	}

	/**
	 * Data provider for "+" operation.
	 * 
	 * @return An array of test and expected values
	 */
	@DataProvider(name = "continuousPlusDoubleNumber")
	public final Object[][] provideContinuousPlusDoubleNumberValues() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture1 = new NormalDistributionMixture(new double[] { 4.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture2 = new NormalDistributionMixture(new double[] { 5.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture3 = new NormalDistributionMixture(new double[] { 1.0 }, new double[] { 3.0 });

		return new Object[][] { { mixture, 1.0, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
	}

	/**
	 * Data provider for "-" operation.
	 * 
	 * @return An array of test and expected values
	 */
	@DataProvider(name = "continuousMinusDoubleNumber")
	public final Object[][] provideContinuousMinusDoubleNumberValues() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture1 = new NormalDistributionMixture(new double[] { 2.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture2 = new NormalDistributionMixture(new double[] { 1.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture3 = new NormalDistributionMixture(new double[] { 5.0 }, new double[] { 3.0 });

		return new Object[][] { { mixture, 1.0, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
	}

	/**
	 * Data provider for "*" operation.
	 * 
	 * @return An array of test and expected values
	 */
	@DataProvider(name = "continuousMultiplicationDoubleNumber")
	public final Object[][] provideContinuousMultiplicationDoubleNumberValues() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture1 = new NormalDistributionMixture(new double[] { 3.0 * 1.5 }, new double[] { 3.0 * (1.5 * 1.5) });

		final NormalDistributionMixture resultMixture2 = new NormalDistributionMixture(new double[] { 6.0 }, new double[] { 12.0 });

		final NormalDistributionMixture resultMixture3 = new NormalDistributionMixture(new double[] { -6.0 }, new double[] { 12.0 });

		return new Object[][] { { mixture, 1.5, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
	}

	/**
	 * Data provider for "/" operation.
	 * 
	 * @return An array of test and expected values
	 */
	@DataProvider(name = "continuousDivisionDoubleNumber")
	public final Object[][] provideContinuousDivisionDoubleNumberValues() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(new double[] { 3.0 }, new double[] { 3.0 });

		final NormalDistributionMixture resultMixture1 = new NormalDistributionMixture(new double[] { 3.0 / 1.5 }, new double[] { 3.0 / (1.5 * 1.5) });

		final NormalDistributionMixture resultMixture2 = new NormalDistributionMixture(new double[] { 1.5 }, new double[] { 0.75 });

		final NormalDistributionMixture resultMixture3 = new NormalDistributionMixture(new double[] { -1.5 }, new double[] { 0.75 });

		return new Object[][] { { mixture, 1.5, resultMixture1 }, { mixture, 2.0, resultMixture2 }, { mixture, -2.0, resultMixture3 } };
	}
}
