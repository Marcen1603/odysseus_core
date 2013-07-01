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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticInteger;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticAndOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticDivisionOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticGreaterThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticMultiplicationOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticNotOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticOrOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticSmallerThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Test
public class TestInteger {

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
	@Test(dataProvider = "discreteSmallerThanInteger")
	public final void testIntegerSmallerThan(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final double result) {
		final IFunction<Double> function = new ProbabilisticSmallerThanOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		Assert.assertEquals(function.getValue(), result, 10E-9);
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
	@Test(dataProvider = "discreteSmallerEqualsInteger")
	public final void testIntegerSmallerEquals(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final double result) {
		final IFunction<Double> function = new ProbabilisticSmallerEqualsOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		Assert.assertEquals(function.getValue(), result, 10E-9);
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
	@Test(dataProvider = "discreteEqualsInteger")
	public final void testIntegerEquals(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final double result) {
		final IFunction<Double> function = new ProbabilisticEqualsOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		Assert.assertEquals(function.getValue(), result, 10E-9);
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
	@Test(dataProvider = "discreteEqualsInteger")
	public final void testIntegerNot(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final double result) {
		final IFunction<Double> equalsFunction = new ProbabilisticEqualsOperator();
		equalsFunction.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		final IFunction<Double> notFunction = new ProbabilisticNotOperator();
		notFunction.setArguments(new Constant<Double>(
				equalsFunction.getValue(), SDFDatatype.DOUBLE));
		Assert.assertEquals(notFunction.getValue(), 1.0 - result, 10E-9);
	}

	/**
	 * Test "&&"-operator for discrete values.
	 */
	@Test
	public final void testIntegerAnd() {
		final IFunction<Double> function = new ProbabilisticAndOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.min(0.25, 0.35), 10E-9);
	}

	/**
	 * Test "||"-operator for discrete values.
	 */
	@Test
	public final void testIntegerOr() {
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
	@Test(dataProvider = "discreteGreaterEqualsInteger")
	public final void testIntegerGreaterEquals(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final double result) {
		final IFunction<Double> function = new ProbabilisticGreaterEqualsOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		Assert.assertEquals(function.getValue(), result, 10E-9);
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
	@Test(dataProvider = "discreteGreaterThanInteger")
	public final void testIntegerGreaterThan(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final double result) {
		final IFunction<Double> function = new ProbabilisticGreaterThanOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		Assert.assertEquals(function.getValue(), result, 10E-9);
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
	@Test(dataProvider = "discretePlusInteger")
	public final void testIntegerPlus(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
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
	@Test(dataProvider = "discreteMinusInteger")
	public final void testIntegerMinus(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
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
	@Test(dataProvider = "discreteMultiplicationInteger")
	public final void testIntegerMultiplication(
			final ProbabilisticInteger left, final ProbabilisticInteger right,
			final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
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
	@Test(dataProvider = "discreteDivisionInteger")
	public final void testIntegerDivision(final ProbabilisticInteger left,
			final ProbabilisticInteger right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
		function.setArguments(new Constant<ProbabilisticInteger>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_INTEGER),
				new Constant<ProbabilisticInteger>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
		Assert.assertEquals(function.getValue(), result);
	}

	@DataProvider(name = "discreteSmallerThanInteger")
	public final Object[][] provideDiscreteSmallerThanIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.5625 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.125 } };
	}

	@DataProvider(name = "discreteSmallerEqualsInteger")
	public final Object[][] provideDiscreteSmallerEqualsIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.6875 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.375 } };
	}

	@DataProvider(name = "discreteEqualsInteger")
	public final Object[][] provideDiscreteEqualsIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.25 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.25 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.125 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.25 } };
	}

	@DataProvider(name = "discreteGreaterEqualsInteger")
	public final Object[][] provideDiscreteGreaterEqualsIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.4375 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.875 } };
	}

	@DataProvider(name = "discreteGreaterThanInteger")
	public final Object[][] provideDiscreteGreaterThanIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.3125 },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.625 } };
	}

	@DataProvider(name = "discretePlusInteger")
	public final Object[][] provideDiscretePlusIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0, 16.0, 18.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMinusInteger")
	public final Object[][] provideDiscreteMinusIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -10.0, -8.0,
								-6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0, },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { -2.0, 0.0, 2.0,
								4.0, 6.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMultiplicationInteger")
	public final Object[][] provideDiscreteMultiplicationIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 11.0, 15.0, 21.0, 27.0, 33.0, 45.0,
								55.0, 63.0, 77.0 }, new Double[] { 0.0625,
								0.125, 0.0625, 0.0625, 0.125, 0.0625, 0.0625,
								0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625,
								0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0 }, new Double[] { 0.125,
								0.25, 0.125, 0.125, 0.125, 0.125, 0.125 }) } };
	}

	@DataProvider(name = "discreteDivisionInteger")
	public final Object[][] provideDiscreteDivisionIntegerValues() {
		return new Object[][] {
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.14285714285714285, 0.2, 0.3333333333333333,
								0.42857142857142855, 0.6, 0.7142857142857143,
								1.0, 1.4, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.0625, 0.0625, 0.0625, 0.0625,
										0.0625, 0.0625, 0.25, 0.0625, 0.0625,
										0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] {
								0.14285714285714285, 0.2, 0.3333333333333333,
								0.42857142857142855, 0.6, 0.7142857142857143,
								1.0, 1.4, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.0625, 0.0625, 0.0625, 0.0625,
										0.0625, 0.0625, 0.25, 0.0625, 0.0625,
										0.0625, 0.0625, 0.0625, 0.0625 }) },
				{
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 },
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
						new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticInteger(new Integer[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] {
								0.3333333333333333, 1.0, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.125, 0.25, 0.125, 0.125,
										0.125, 0.125, 0.125 }) } };
	}
}
