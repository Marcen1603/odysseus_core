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

	// @Test(dataProvider = "continuousSmallerThanDouble")
	// public void testDoubleSmallerThan(ProbabilisticContinuousDouble left,
	// NormalDistributionMixture mixtures,
	// Double right, double result) {
	// IFunction<Double> function = new
	// ProbabilisticContinuousSmallerThanOperator();
	// function.setArguments(new Constant<ProbabilisticContinuousDouble>(left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<Double>(right,
	// SDFDatatype.DOUBLE));
	// Assert.assertEquals(function.getValue(), result, 10E-9);
	// }

	/**
	 * Test "<="-operator for discrete values.
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
		function.setArguments(new Constant<ProbabilisticContinuousDouble>(left, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue().getScale(), result, TestConstants.EPSILON);
	}

	/**
	 * Test "=="-operator for discrete values.
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
		function.setArguments(new Constant<ProbabilisticContinuousDouble>(left, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE), new Constant<Double>(right, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue().getScale(), result, TestConstants.EPSILON);
	}

	/**
	 * Test "!"-operator for discrete values.
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
	// @Test(dataProvider = "continuousEqualsDouble")
	// public final void testDoubleNot(final ProbabilisticContinuousDouble left,
	// final NormalDistributionMixture mixtures, final Double right,
	// final double result) {
	// ProbabilisticContinuousEqualsOperator equalsFunction = new
	// ProbabilisticContinuousEqualsOperator();
	// equalsFunction.getDistributions().add(mixtures);
	// equalsFunction
	// .setArguments(
	// new Constant<ProbabilisticContinuousDouble>(
	// left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<Double>(right, SDFDatatype.DOUBLE));
	//
	// }

	// @Test
	// public final void testDoubleAnd() {
	// final IFunction<Double> function = new ProbabilisticAndOperator();
	// // ((AbstractProbabilisticFunction<Double>) function).getDistributions()
	// // .add(mixtures);
	// function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
	// new Constant<Double>(0.35, SDFDatatype.DOUBLE));
	// Assert.assertEquals(function.getValue(), Math.min(0.25, 0.35), TestConstants.EPSILON);
	// }
	//
	// @Test
	// public final void testDoubleOr() {
	// final IFunction<Double> function = new ProbabilisticOrOperator();
	// // ((AbstractProbabilisticFunction<Double>) function).getDistributions()
	// // .add(mixtures);
	// function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
	// new Constant<Double>(0.35, SDFDatatype.DOUBLE));
	// Assert.assertEquals(function.getValue(), Math.max(0.25, 0.35), TestConstants.EPSILON);
	// }

	/**
	 * Test ">="-operator for discrete values.
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

	// @Test(dataProvider = "continuousGreaterThanDouble")
	// public void testDoubleGreaterThan(ProbabilisticContinuousDouble left,
	// NormalDistributionMixture mixtures,
	// Double right, double result) {
	// IFunction<Double> function = new
	// ProbabilisticContinuousGreaterThanOperator();
	// ((AbstractProbabilisticFunction<Double>) function).getDistributions()
	// .add(mixtures);
	// function.setArguments(new Constant<ProbabilisticContinuousDouble>(left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<Double>(right, SDFDatatype.DOUBLE));
	// Assert.assertEquals(function.getValue(), result, TestConstants.EPSILON);
	// }

	// @Test(dataProvider = "continuousPlusDouble")
	// public void testDoublePlus(ProbabilisticContinuousDouble left,
	// ProbabilisticContinuousDouble right,
	// ProbabilisticContinuousDouble result) {
	// IFunction<ProbabilisticDouble> function = new
	// ProbabilisticContinuousPlusOperator();
	// function.setArguments(
	// new Constant<ProbabilisticContinuousDouble>(
	// left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<ProbabilisticContinuousDouble>(
	// right,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
	// Assert.assertEquals(function.getValue(), result);
	// }
	//
	// @Test(dataProvider = "continuousMinusDouble")
	// public void testDoubleMinus(ProbabilisticContinuousDouble left,
	// ProbabilisticContinuousDouble right,
	// ProbabilisticContinuousDouble result) {
	// IFunction<ProbabilisticDouble> function = new
	// ProbabilisticContinuousMinusOperator();
	// function.setArguments(
	// new Constant<ProbabilisticContinuousDouble>(
	// left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<ProbabilisticContinuousDouble>(
	// right,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
	// Assert.assertEquals(function.getValue(), result);
	// }
	//
	// @Test(dataProvider = "continuousMultiplicationDouble")
	// public void testDoubleMultiplication(ProbabilisticContinuousDouble left,
	// ProbabilisticContinuousDouble right,
	// ProbabilisticContinuousDouble result) {
	// IFunction<ProbabilisticDouble> function = new
	// ProbabilisticContinuousMultiplicationOperator();
	// function.setArguments(
	// new Constant<ProbabilisticContinuousDouble>(
	// left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<ProbabilisticContinuousDouble>(
	// right,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
	// Assert.assertEquals(function.getValue(), result);
	// }
	//
	// @Test(dataProvider = "continuousDivisionDouble")
	// public void testDoubleDivision(ProbabilisticContinuousDouble left,
	// ProbabilisticContinuousDouble right,
	// ProbabilisticContinuousDouble result) {
	// IFunction<ProbabilisticDouble> function = new
	// ProbabilisticContinuousDivisionOperator();
	// function.setArguments(
	// new Constant<ProbabilisticContinuousDouble>(
	// left,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
	// new Constant<ProbabilisticContinuousDouble>(
	// right,
	// SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
	// Assert.assertEquals(function.getValue(), result);
	// }
/**
 * Data provider for "<" tests.
 * @return Data for the "<" tests
 */
	@DataProvider(name = "continuousSmallerThanDouble")
	public final Object[][] provideContinuousSmallerThanDoubleValues() {
		return new Object[][] {};
	}

	/**
	 * Data provider for "<=" tests.
	 * 
	 * @return Data for the "<=" tests
	 */
	@DataProvider(name = "continuousSmallerEqualsDouble")
	public final Object[][] provideContinuousSmallerEqualsDoubleValues() {
		final NormalDistributionMixture univariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		final NormalDistributionMixture multivariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		return new Object[][] { { new ProbabilisticContinuousDouble(0), univariateMixtures, 3.0, 0.9973020261614356 }, { new ProbabilisticContinuousDouble(0), multivariateMixtures, 3.0, 0.9973020261614356 } };
	}

	/**
	 * Data provider for "==" tests.
	 * 
	 * @return Data for the "==" tests
	 */
	@DataProvider(name = "continuousEqualsDouble")
	public final Object[][] provideContinuousEqualsDoubleValues() {
		final NormalDistributionMixture univariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		final NormalDistributionMixture multivariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		return new Object[][] { { new ProbabilisticContinuousDouble(0), univariateMixtures, 3.0, 0.0 }, { new ProbabilisticContinuousDouble(0), multivariateMixtures, 3.0, 0.0 } };
	}

	/**
	 * Data provider for ">=" tests.
	 * 
	 * @return Data for the ">=" tests
	 */
	@DataProvider(name = "continuousGreaterEqualsDouble")
	public final Object[][] provideContinuousGreaterEqualsDoubleValues() {
		final NormalDistributionMixture univariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		final NormalDistributionMixture multivariateMixtures = new NormalDistributionMixture(new double[] { 1.0, 2.0 }, new double[] { 1.0, 0.0, 1.0 });
		return new Object[][] { { new ProbabilisticContinuousDouble(0), univariateMixtures, 3.0, 1.8222246957988279E-6 }, { new ProbabilisticContinuousDouble(0), multivariateMixtures, 3.0, 1.8222246957988279E-6 } };
	}

	/**
	 * Data provider for ">" tests.
	 * 
	 * @return Data for the ">" tests
	 */
	@DataProvider(name = "continuousGreaterThanDouble")
	public final Object[][] provideContinuousGreaterThanDoubleValues() {
		return new Object[][] {};
	}

}
