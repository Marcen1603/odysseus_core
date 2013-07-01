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
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticLong;
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
public class TestLong {

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
	@Test(dataProvider = "discreteSmallerThanLong")
	public final void testLongSmallerThan(final ProbabilisticLong left,
			final ProbabilisticLong right, final double result) {
		final IFunction<Double> function = new ProbabilisticSmallerThanOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

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
	@Test(dataProvider = "discreteSmallerEqualsLong")
	public final void testLongSmallerEquals(final ProbabilisticLong left,
			final ProbabilisticLong right, final double result) {
		final IFunction<Double> function = new ProbabilisticSmallerEqualsOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discreteEqualsLong")
	public final void testLongEquals(final ProbabilisticLong left,
			final ProbabilisticLong right, final double result) {
		final IFunction<Double> function = new ProbabilisticEqualsOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discreteEqualsLong")
	public final void testLongNot(final ProbabilisticLong left,
			final ProbabilisticLong right, final double result) {
		final IFunction<Double> equalsFunction = new ProbabilisticEqualsOperator();
		equalsFunction.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
		final IFunction<Double> notFunction = new ProbabilisticNotOperator();
		notFunction.setArguments(new Constant<Double>(
				equalsFunction.getValue(), SDFDatatype.DOUBLE));
		Assert.assertEquals(notFunction.getValue(), 1.0 - result, 10E-9);
	}

	/**
	 * Test "&&"-operator for discrete values.
	 */
	@Test
	public final void testLongAnd() {
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
	public final void testLongOr() {
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
	@Test(dataProvider = "discreteGreaterEqualsLong")
	public final void testLongGreaterEquals(final ProbabilisticLong left,
			final ProbabilisticLong right, final double result) {
		final IFunction<Double> function = new ProbabilisticGreaterEqualsOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discreteGreaterThanLong")
	public final void testLongGreaterThan(final ProbabilisticLong left,
			final ProbabilisticLong right, final double result) {
		final IFunction<Double> function = new ProbabilisticGreaterThanOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discretePlusLong")
	public final void testLongPlus(final ProbabilisticLong left,
			final ProbabilisticLong right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discreteMinusLong")
	public final void testLongMinus(final ProbabilisticLong left,
			final ProbabilisticLong right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discreteMultiplicationLong")
	public final void testLongMultiplication(final ProbabilisticLong left,
			final ProbabilisticLong right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
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
	@Test(dataProvider = "discreteDivisionLong")
	public final void testLongDivision(final ProbabilisticLong left,
			final ProbabilisticLong right, final ProbabilisticDouble result) {
		final IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
		function.setArguments(new Constant<ProbabilisticLong>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_LONG),
				new Constant<ProbabilisticLong>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_LONG));
		Assert.assertEquals(function.getValue(), result);
	}

	@DataProvider(name = "discreteSmallerThanLong")
	public final Object[][] provideDiscreteSmallerThanLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.5625 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }), 0.125 } };
	}

	@DataProvider(name = "discreteSmallerEqualsLong")
	public final Object[][] provideDiscreteSmallerEqualsLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.6875 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }), 0.375 } };
	}

	@DataProvider(name = "discreteEqualsLong")
	public final Object[][] provideDiscreteEqualsLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.25 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.25 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.125 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }), 0.25 } };
	}

	@DataProvider(name = "discreteGreaterEqualsLong")
	public final Object[][] provideDiscreteGreaterEqualsLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.4375 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }), 0.875 } };
	}

	@DataProvider(name = "discreteGreaterThanLong")
	public final Object[][] provideDiscreteGreaterThanLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.3125 },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }), 0.625 } };
	}

	@DataProvider(name = "discretePlusLong")
	public final Object[][] provideDiscretePlusLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0, 16.0, 18.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMinusLong")
	public final Object[][] provideDiscreteMinusLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -10.0, -8.0,
								-6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0, },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { -2.0, 0.0, 2.0,
								4.0, 6.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMultiplicationLong")
	public final Object[][] provideDiscreteMultiplicationLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 11.0, 15.0, 21.0, 27.0, 33.0, 45.0,
								55.0, 63.0, 77.0 }, new Double[] { 0.0625,
								0.125, 0.0625, 0.0625, 0.125, 0.0625, 0.0625,
								0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625,
								0.0625 }) },
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0 }, new Double[] { 0.125,
								0.25, 0.125, 0.125, 0.125, 0.125, 0.125 }) } };
	}

	@DataProvider(name = "discreteDivisionLong")
	public final Object[][] provideDiscreteDivisionLongValues() {
		return new Object[][] {
				{
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
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
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 7L, 5L, 3L, 1L },
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
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L, 9L, 11L },
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
						new ProbabilisticLong(new Long[] { 1L, 3L, 5L, 7L },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticLong(new Long[] { 1L, 3L },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] {
								0.3333333333333333, 1.0, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.125, 0.25, 0.125, 0.125,
										0.125, 0.125, 0.125 }) } };
	}
}
