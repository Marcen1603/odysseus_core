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
package de.uniol.inf.is.odysseus.probabilistic.test.discrete.function;

import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticFloat;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticResult;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.bool.ProbabilisticAndOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.bool.ProbabilisticNotOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.bool.ProbabilisticOrOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticGreaterThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticSmallerThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticDivisionOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMultiplicationOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Test
public class TestFloat {

/**
	 * Test "<"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteSmallerThanFloat")
	public final void testFloatSmallerThan(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticSmallerThanOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));

		ProbabilisticResult value = function.getValue();
		Assert.assertEquals(value.getProbability(), result, 10E-9);
		double sum = 0.0;
		for (Entry<Double, Double> entry : ((ProbabilisticDouble) value
				.getValue()).getValues().entrySet()) {
			sum += entry.getValue();
		}
		Assert.assertEquals(sum, result, 10E-9);
	}

	/**
	 * Test "<="-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteSmallerEqualsFloat")
	public final void testFloatSmallerEquals(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticSmallerEqualsOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));

		ProbabilisticResult value = function.getValue();
		Assert.assertEquals(value.getProbability(), result, 10E-9);
		double sum = 0.0;
		for (Entry<Double, Double> entry : ((ProbabilisticDouble) value
				.getValue()).getValues().entrySet()) {
			sum += entry.getValue();
		}
		Assert.assertEquals(sum, result, 10E-9);
	}

	/**
	 * Test "=="-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteEqualsFloat")
	public final void testFloatEquals(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticEqualsOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));

		ProbabilisticResult value = function.getValue();
		Assert.assertEquals(value.getProbability(), result, 10E-9);
		double sum = 0.0;
		for (Entry<Double, Double> entry : ((ProbabilisticDouble) value
				.getValue()).getValues().entrySet()) {
			sum += entry.getValue();
		}
		Assert.assertEquals(sum, result, 10E-9);
	}

	/**
	 * Test "!"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteEqualsFloat")
	public final void testFloatNot(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final double result) {
		final IFunction<ProbabilisticResult> equalsFunction = new ProbabilisticEqualsOperator();
		equalsFunction.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));
		final IFunction<ProbabilisticResult> notFunction = new ProbabilisticNotOperator();
		notFunction.setArguments(new Constant<Double>(
				equalsFunction.getValue().getProbability(), SDFDatatype.DOUBLE));
		Assert.assertEquals(notFunction.getValue().getProbability(), 1.0 - result, 10E-9);
	}

	/**
	 * Test "&&"-operator for discrete values.
	 * 
	 */
	@Test
	public final void testFloatAnd() {
		final IFunction<Double> function = new ProbabilisticAndOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.min(0.25, 0.35), 10E-9);
	}

	/**
	 * Test "||"-operator for discrete values.
	 * 
	 */
	@Test
	public final void testFloatOr() {
		final IFunction<Double> function = new ProbabilisticOrOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.max(0.25, 0.35), 10E-9);
	}

	/**
	 * Test ">="-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteGreaterEqualsFloat")
	public final void testFloatGreaterEquals(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticGreaterEqualsOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));

		ProbabilisticResult value = function.getValue();
		Assert.assertEquals(value.getProbability(), result, 10E-9);
		double sum = 0.0;
		for (Entry<Double, Double> entry : ((ProbabilisticDouble) value
				.getValue()).getValues().entrySet()) {
			sum += entry.getValue();
		}
		Assert.assertEquals(sum, result, 10E-9);
	}

	/**
	 * Test ">"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteGreaterThanFloat")
	public final void testFloatGreaterThan(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticGreaterThanOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));
		
		ProbabilisticResult value = function.getValue();
		Assert.assertEquals(value.getProbability(), result, 10E-9);
		double sum = 0.0;
		for (Entry<Double, Double> entry : ((ProbabilisticDouble) value
				.getValue()).getValues().entrySet()) {
			sum += entry.getValue();
		}
		Assert.assertEquals(sum, result, 10E-9);
	}

	/**
	 * Test "+"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discretePlusFloat")
	public final void testFloatPlus(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));
		Assert.assertEquals(function.getValue(), result);
	}

	/**
	 * Test "-"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteMinusFloat")
	public final void testFloatMinus(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));
		Assert.assertEquals(function.getValue(), result);
	}

	/**
	 * Test "*"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteMultiplicationFloat")
	public final void testFloatMultiplication(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));
		Assert.assertEquals(function.getValue(), result);
	}

	/**
	 * Test "/"-operator for discrete values.
	 * 
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 * @param result
	 *            The expected result
	 */
	@Test(dataProvider = "discreteDivisionFloat")
	public final void testFloatDivision(final ProbabilisticFloat left,
			final ProbabilisticFloat right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
		function.setArguments(new Constant<ProbabilisticFloat>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT),
				new Constant<ProbabilisticFloat>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT));
		Assert.assertEquals(function.getValue(), result);
	}

	@DataProvider(name = "discreteSmallerThanFloat")
	public final Object[][] provideDiscreteSmallerThanFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.5625 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }), 0.125 } };
	}

	@DataProvider(name = "discreteSmallerEqualsFloat")
	public final Object[][] provideDiscreteSmallerEqualsFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.6875 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }), 0.375 } };
	}

	@DataProvider(name = "discreteEqualsFloat")
	public final Object[][] provideDiscreteEqualsFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.25 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.25 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.125 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }), 0.25 } };
	}

	@DataProvider(name = "discreteGreaterEqualsFloat")
	public final Object[][] provideDiscreteGreaterEqualsFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.4375 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }), 0.875 } };
	}

	@DataProvider(name = "discreteGreaterThanFloat")
	public final Object[][] provideDiscreteGreaterThanFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.3125 },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }), 0.625 } };
	}

	@DataProvider(name = "discretePlusFloat")
	public final Object[][] provideDiscretePlusFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0, 16.0, 18.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMinusFloat")
	public final Object[][] provideDiscreteMinusFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -10.0, -8.0,
								-6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0, },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { -2.0, 0.0, 2.0,
								4.0, 6.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMultiplicationFloat")
	public final Object[][] provideDiscreteMultiplicationFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 11.0, 15.0, 21.0, 27.0, 33.0, 45.0,
								55.0, 63.0, 77.0 }, new Double[] { 0.0625,
								0.125, 0.0625, 0.0625, 0.125, 0.0625, 0.0625,
								0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625,
								0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0 }, new Double[] { 0.125,
								0.25, 0.125, 0.125, 0.125, 0.125, 0.125 }) } };
	}

	@DataProvider(name = "discreteDivisionFloat")
	public final Object[][] provideDiscreteDivisionFloatValues() {
		return new Object[][] {
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.14285714285714285, 0.2, 0.3333333333333333,
								0.42857142857142855, 0.6, 0.7142857142857143,
								1.0, 1.4, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.0625, 0.0625, 0.0625, 0.0625,
										0.0625, 0.0625, 0.25, 0.0625, 0.0625,
										0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 7.0f, 5.0f, 3.0f,
								1.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.14285714285714285, 0.2, 0.3333333333333333,
								0.42857142857142855, 0.6, 0.7142857142857143,
								1.0, 1.4, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.0625, 0.0625, 0.0625, 0.0625,
										0.0625, 0.0625, 0.25, 0.0625, 0.0625,
										0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 9.0f,
								11.0f },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.09090909090909091, 0.1111111111111111,
								0.2727272727272727, 0.3333333333333333,
								0.45454545454545453, 0.5555555555555556,
								0.6363636363636364, 0.7777777777777778, 1.0,
								1.6666666666666667, 2.3333333333333335, 3.0,
								5.0, 7.0, }, new Double[] { 0.0625, 0.0625,
								0.0625, 0.125, 0.0625, 0.0625, 0.0625, 0.0625,
								0.125, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f, 5.0f,
								7.0f }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticFloat(new Float[] { 1.0f, 3.0f },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] {
								0.3333333333333333, 1.0, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.125, 0.25, 0.125, 0.125,
										0.125, 0.125, 0.125 }) } };
	}
}
