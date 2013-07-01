package de.uniol.inf.is.odysseus.probabilistic.test.discrete.function;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticShort;
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

@Test(singleThreaded = true)
public class TestShort {
	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	/**
 * 
 */
	@BeforeTest
	public void setUpAll() {

	}

	/**
 * 
 */
	@AfterTest
	public void tearDownAll() {

	}

	/**
 * 
 */
	@AfterMethod
	public void tearDown() {

	}

	@Test(dataProvider = "discreteSmallerThanShort")
	public void testShortSmallerThan(ProbabilisticShort left,
			ProbabilisticShort right, double result) {
		IFunction<Double> function = new ProbabilisticSmallerThanOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteSmallerEqualsShort")
	public void testShortSmallerEquals(ProbabilisticShort left,
			ProbabilisticShort right, double result) {
		IFunction<Double> function = new ProbabilisticSmallerEqualsOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteEqualsShort")
	public void testShortEquals(ProbabilisticShort left,
			ProbabilisticShort right, double result) {
		IFunction<Double> function = new ProbabilisticEqualsOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteEqualsShort")
	public void testShortNot(ProbabilisticShort left, ProbabilisticShort right,
			double result) {
		IFunction<Double> equalsFunction = new ProbabilisticEqualsOperator();
		equalsFunction.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		IFunction<Double> notFunction = new ProbabilisticNotOperator();
		notFunction.setArguments(new Constant<Double>(
				equalsFunction.getValue(), SDFDatatype.DOUBLE));
		Assert.assertEquals(notFunction.getValue(), 1.0 - result, 10E-9);
	}

	@Test
	public void testShortAnd() {
		IFunction<Double> function = new ProbabilisticAndOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.min(0.25, 0.35), 10E-9);
	}

	@Test
	public void testShortOr() {
		IFunction<Double> function = new ProbabilisticOrOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.max(0.25, 0.35), 10E-9);
	}

	@Test(dataProvider = "discreteGreaterEqualsShort")
	public void testShortGreaterEquals(ProbabilisticShort left,
			ProbabilisticShort right, double result) {
		IFunction<Double> function = new ProbabilisticGreaterEqualsOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteGreaterThanShort")
	public void testShortGreaterThan(ProbabilisticShort left,
			ProbabilisticShort right, double result) {
		IFunction<Double> function = new ProbabilisticGreaterThanOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discretePlusShort")
	public void testShortPlus(ProbabilisticShort left,
			ProbabilisticShort right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result);
	}

	@Test(dataProvider = "discreteMinusShort")
	public void testShortMinus(ProbabilisticShort left,
			ProbabilisticShort right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result);
	}

	@Test(dataProvider = "discreteMultiplicationShort")
	public void testShortMultiplication(ProbabilisticShort left,
			ProbabilisticShort right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result);
	}

	@Test(dataProvider = "discreteDivisionShort")
	public void testShortDivision(ProbabilisticShort left,
			ProbabilisticShort right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
		function.setArguments(new Constant<ProbabilisticShort>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_SHORT),
				new Constant<ProbabilisticShort>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT));
		Assert.assertEquals(function.getValue(), result);
	}

	@DataProvider(name = "discreteSmallerThanShort")
	public Object[][] provideDiscreteSmallerThanShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.5625 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.125 } };
	}

	@DataProvider(name = "discreteSmallerEqualsShort")
	public Object[][] provideDiscreteSmallerEqualsShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.6875 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.375 } };
	}

	@DataProvider(name = "discreteEqualsShort")
	public Object[][] provideDiscreteEqualsShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.25 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.25 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.125 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.25 } };
	}

	@DataProvider(name = "discreteGreaterEqualsShort")
	public Object[][] provideDiscreteGreaterEqualsShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.625 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.4375 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.875 } };
	}

	@DataProvider(name = "discreteGreaterThanShort")
	public Object[][] provideDiscreteGreaterThanShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }), 0.375 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.3125 },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }), 0.625 } };
	}

	@DataProvider(name = "discretePlusShort")
	public Object[][] provideDiscretePlusShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625,
								0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0, 12.0, 14.0, 16.0, 18.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0,
								8.0, 10.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMinusShort")
	public Object[][] provideDiscreteMinusShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -6.0, -4.0,
								-2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] {
								0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125,
								0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { -10.0, -8.0,
								-6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0, },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.125, 0.125, 0.125, 0.125, 0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { -2.0, 0.0, 2.0,
								4.0, 6.0 }, new Double[] { 0.125, 0.25, 0.25,
								0.25, 0.125 }) } };
	}

	@DataProvider(name = "discreteMultiplicationShort")
	public Object[][] provideDiscreteMultiplicationShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 },
								new Double[] { 0.0625, 0.125, 0.125, 0.125,
										0.0625, 0.125, 0.125, 0.0625, 0.125,
										0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 11.0, 15.0, 21.0, 27.0, 33.0, 45.0,
								55.0, 63.0, 77.0 }, new Double[] { 0.0625,
								0.125, 0.0625, 0.0625, 0.125, 0.0625, 0.0625,
								0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625,
								0.0625 }) },
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0, 9.0, 15.0, 21.0 }, new Double[] { 0.125,
								0.25, 0.125, 0.125, 0.125, 0.125, 0.125 }) } };
	}

	@DataProvider(name = "discreteDivisionShort")
	public Object[][] provideDiscreteDivisionShortValues() {
		return new Object[][] {
				{
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
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
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 7, 5, 3, 1 },
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
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3, 9, 11 },
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
						new ProbabilisticShort(new Short[] { 1, 3, 5, 7 },
								new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticShort(new Short[] { 1, 3 },
								new Double[] { 0.5, 0.5 }),
						new ProbabilisticDouble(new Double[] {
								0.3333333333333333, 1.0, 1.6666666666666667,
								2.3333333333333335, 3.0, 5.0, 7.0, },
								new Double[] { 0.125, 0.25, 0.125, 0.125,
										0.125, 0.125, 0.125 }) } };
	}
}
