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
public class TestDouble {

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
	@Test(dataProvider = "discreteSmallerThanDouble")
	public final void testDoubleSmallerThan(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticSmallerThanOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));

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
	@Test(dataProvider = "discreteSmallerEqualsDouble")
	public final void testDoubleSmallerEquals(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticSmallerEqualsOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));

	
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
	@Test(dataProvider = "discreteEqualsDouble")
	public final void testDoubleEquals(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticEqualsOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		
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
	@Test(dataProvider = "discreteEqualsDouble")
	public final void testDoubleNot(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final double result) {
		final IFunction<ProbabilisticResult> equalsFunction = new ProbabilisticEqualsOperator();
		equalsFunction.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
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
	public final void testDoubleAnd() {
		final IFunction<Double> function = new ProbabilisticAndOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.min(0.25, 0.35), 10E-9);
	}

	/**
	 * Test "||"-operator for discrete values.
	 */
	@Test
	public final void testDoubleOr() {
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
	@Test(dataProvider = "discreteGreaterEqualsDouble")
	public final void testDoubleGreaterEquals(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticGreaterEqualsOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));

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
	@Test(dataProvider = "discreteGreaterThanDouble")
	public final void testDoubleGreaterThan(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final double result) {
		final IFunction<ProbabilisticResult> function = new ProbabilisticGreaterThanOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));

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
	@Test(dataProvider = "discretePlusDouble")
	public final void testDoublePlus(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
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
	@Test(dataProvider = "discreteMinusDouble")
	public final void testDoubleMinus(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
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
	@Test(dataProvider = "discreteMultiplicationDouble")
	public final void testDoubleMultiplication(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
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
	@Test(dataProvider = "discreteDivisionDouble")
	public final void testDoubleDivision(final ProbabilisticDouble left,
			final ProbabilisticDouble right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result);
	}

	@DataProvider(name = "discreteSmallerThanDouble")
	public final Object[][] provideDiscreteSmallerThanDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.5625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.125 } };
	}

	@DataProvider(name = "discreteSmallerEqualsDouble")
	public final Object[][] provideDiscreteSmallerEqualsDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.6875 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.375 } };
	}

	@DataProvider(name = "discreteEqualsDouble")
	public final Object[][] provideDiscreteEqualsDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.25 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.25 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.125 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.25 } };
	}

	@DataProvider(name = "discreteGreaterEqualsDouble")
	public final Object[][] provideDiscreteGreaterEqualsDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.4375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.875 } };
	}

	@DataProvider(name = "discreteGreaterThanDouble")
	public final Object[][] provideDiscreteGreaterThanDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.3125 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.625 } };
	}

	@DataProvider(name = "discretePlusDouble")
	public final Object[][] provideDiscretePlusDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0, 16.0, 18.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMinusDouble")
	public final Object[][] provideDiscreteMinusDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -10.0, -8.0,
								-6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0, },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { -2.0, 0.0, 2.0,
								4.0, 6.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMultiplicationDouble")
	public final Object[][] provideDiscreteMultiplicationDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 11.0, 15.0, 21.0, 27.0, 33.0, 45.0,
								55.0, 63.0, 77.0 }, new Double[] { 0.0625,
								0.125, 0.0625, 0.0625, 0.125, 0.0625, 0.0625,
								0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625,
								0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0 }, new Double[] { 0.125,
								0.25, 0.125, 0.125, 0.125, 0.125, 0.125 }) } };
	}

	@DataProvider(name = "discreteDivisionDouble")
	public final Object[][] provideDiscreteDivisionDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.14285714285714285, 0.2, 0.3333333333333333,
								0.42857142857142855, 0.6, 0.7142857142857143,
								1.0, 1.4, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.0625, 0.0625, 0.0625, 0.0625,
										0.0625, 0.0625, 0.25, 0.0625, 0.0625,
										0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.14285714285714285, 0.2, 0.3333333333333333,
								0.42857142857142855, 0.6, 0.7142857142857143,
								1.0, 1.4, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.0625, 0.0625, 0.0625, 0.0625,
										0.0625, 0.0625, 0.25, 0.0625, 0.0625,
										0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
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
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] {
								0.3333333333333333, 1.0, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.125, 0.25, 0.125, 0.125,
										0.125, 0.125, 0.125 }) } };
	}
}
