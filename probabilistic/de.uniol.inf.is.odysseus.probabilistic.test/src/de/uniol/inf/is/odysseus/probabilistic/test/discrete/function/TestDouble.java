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
public class TestDouble {

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

	@Test(dataProvider = "discreteSmallerThanDouble")
	public void testDoubleSmallerThan(ProbabilisticDouble left,
			ProbabilisticDouble right, double result) {
		IFunction<Double> function = new ProbabilisticSmallerThanOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteSmallerEqualsDouble")
	public void testDoubleSmallerEquals(ProbabilisticDouble left,
			ProbabilisticDouble right, double result) {
		IFunction<Double> function = new ProbabilisticSmallerEqualsOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteEqualsDouble")
	public void testDoubleEquals(ProbabilisticDouble left,
			ProbabilisticDouble right, double result) {
		IFunction<Double> function = new ProbabilisticEqualsOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteEqualsDouble")
	public void testDoubleNot(ProbabilisticDouble left,
			ProbabilisticDouble right, double result) {
		IFunction<Double> equalsFunction = new ProbabilisticEqualsOperator();
		equalsFunction.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		IFunction<Double> notFunction = new ProbabilisticNotOperator();
		notFunction.setArguments(new Constant<Double>(
				equalsFunction.getValue(), SDFDatatype.DOUBLE));
		Assert.assertEquals(notFunction.getValue(), 1.0 - result, 10E-9);
	}

	@Test
	public void testDoubleAnd() {
		IFunction<Double> function = new ProbabilisticAndOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.min(0.25, 0.35), 10E-9);
	}

	@Test
	public void testDoubleOr() {
		IFunction<Double> function = new ProbabilisticOrOperator();
		function.setArguments(new Constant<Double>(0.25, SDFDatatype.DOUBLE),
				new Constant<Double>(0.35, SDFDatatype.DOUBLE));
		Assert.assertEquals(function.getValue(), Math.max(0.25, 0.35), 10E-9);
	}

	@Test(dataProvider = "discreteGreaterEqualsDouble")
	public void testDoubleGreaterEquals(ProbabilisticDouble left,
			ProbabilisticDouble right, double result) {
		IFunction<Double> function = new ProbabilisticGreaterEqualsOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discreteGreaterThanDouble")
	public void testDoubleGreaterThan(ProbabilisticDouble left,
			ProbabilisticDouble right, double result) {
		IFunction<Double> function = new ProbabilisticGreaterThanOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result, 10E-9);
	}

	@Test(dataProvider = "discretePlusDouble")
	public void testDoublePlus(ProbabilisticDouble left,
			ProbabilisticDouble right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result);
	}

	@Test(dataProvider = "discreteMinusDouble")
	public void testDoubleMinus(ProbabilisticDouble left,
			ProbabilisticDouble right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result);
	}

	@Test(dataProvider = "discreteMultiplicationDouble")
	public void testDoubleMultiplication(ProbabilisticDouble left,
			ProbabilisticDouble right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result);
	}

	@Test(dataProvider = "discreteDivisionDouble")
	public void testDoubleDivision(ProbabilisticDouble left,
			ProbabilisticDouble right, ProbabilisticDouble result) {
		IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
		function.setArguments(new Constant<ProbabilisticDouble>(left,
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE),
				new Constant<ProbabilisticDouble>(right,
						SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		Assert.assertEquals(function.getValue(), result);
	}

	@DataProvider(name = "discreteSmallerThanDouble")
	public Object[][] provideDiscreteSmallerThanDoubleValues() {
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
	public Object[][] provideDiscreteSmallerEqualsDoubleValues() {
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
	public Object[][] provideDiscreteEqualsDoubleValues() {
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
	public Object[][] provideDiscreteGreaterEqualsDoubleValues() {
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
	public Object[][] provideDiscreteGreaterThanDoubleValues() {
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
	public Object[][] provideDiscretePlusDoubleValues() {
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
	public Object[][] provideDiscreteMinusDoubleValues() {
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
	public Object[][] provideDiscreteMultiplicationDoubleValues() {
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
	public Object[][] provideDiscreteDivisionDoubleValues() {
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
