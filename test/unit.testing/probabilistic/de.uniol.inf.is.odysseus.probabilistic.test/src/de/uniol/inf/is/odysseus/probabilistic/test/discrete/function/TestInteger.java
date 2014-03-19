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
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticInteger;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Test
public class TestInteger {
//
//    /**
//     * Test "+"-operator for discrete values.
//     * 
//     * @param left
//     *            The left value
//     * @param right
//     *            The right value
//     * @param result
//     *            The expected result
//     */
//    @Test(dataProvider = "discretePlusInteger")
//    public final void testIntegerPlus(final ProbabilisticInteger left, final ProbabilisticInteger right, final ProbabilisticDouble result) {
//        final IFunction<ProbabilisticDouble> function = new ProbabilisticPlusOperator();
//        function.setArguments(new Constant<ProbabilisticInteger>(left, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER), new Constant<ProbabilisticInteger>(right,
//                SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
//        Assert.assertEquals(function.getValue(), result);
//    }
//
//    /**
//     * Test "-"-operator for discrete values.
//     * 
//     * @param left
//     *            The left value
//     * @param right
//     *            The right value
//     * @param result
//     *            The expected result
//     */
//    @Test(dataProvider = "discreteMinusInteger")
//    public final void testIntegerMinus(final ProbabilisticInteger left, final ProbabilisticInteger right, final ProbabilisticDouble result) {
//        final IFunction<ProbabilisticDouble> function = new ProbabilisticMinusOperator();
//        function.setArguments(new Constant<ProbabilisticInteger>(left, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER), new Constant<ProbabilisticInteger>(right,
//                SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
//        Assert.assertEquals(function.getValue(), result);
//    }
//
//    /**
//     * Test "*"-operator for discrete values.
//     * 
//     * @param left
//     *            The left value
//     * @param right
//     *            The right value
//     * @param result
//     *            The expected result
//     */
//    @Test(dataProvider = "discreteMultiplicationInteger")
//    public final void testIntegerMultiplication(final ProbabilisticInteger left, final ProbabilisticInteger right, final ProbabilisticDouble result) {
//        final IFunction<ProbabilisticDouble> function = new ProbabilisticMultiplicationOperator();
//        function.setArguments(new Constant<ProbabilisticInteger>(left, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER), new Constant<ProbabilisticInteger>(right,
//                SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
//        Assert.assertEquals(function.getValue(), result);
//    }
//
//    /**
//     * Test "/"-operator for discrete values.
//     * 
//     * @param left
//     *            The left value
//     * @param right
//     *            The right value
//     * @param result
//     *            The expected result
//     */
//    @Test(dataProvider = "discreteDivisionInteger")
//    public final void testIntegerDivision(final ProbabilisticInteger left, final ProbabilisticInteger right, final ProbabilisticDouble result) {
//        final IFunction<ProbabilisticDouble> function = new ProbabilisticDivisionOperator();
//        function.setArguments(new Constant<ProbabilisticInteger>(left, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER), new Constant<ProbabilisticInteger>(right,
//                SDFProbabilisticDatatype.PROBABILISTIC_INTEGER));
//        Assert.assertEquals(function.getValue(), result);
//    }
//
//    /**
//     * Data provider for "+" operation.
//     * 
//     * @return An array of test and expected values
//     */
//    @DataProvider(name = "discretePlusInteger")
//    public final Object[][] provideDiscretePlusIntegerValues() {
//        return new Object[][] {
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0 }, new Double[] { 0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0 }, new Double[] { 0.0625, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }), new ProbabilisticInteger(new Integer[] { 1, 3 }, new Double[] { 0.5, 0.5 }),
//                        new ProbabilisticDouble(new Double[] { 2.0, 4.0, 6.0, 8.0, 10.0 }, new Double[] { 0.125, 0.25, 0.25, 0.25, 0.125 }) } };
//    }
//
//    /**
//     * Data provider for "-" operation.
//     * 
//     * @return An array of test and expected values
//     */
//    @DataProvider(name = "discreteMinusInteger")
//    public final Object[][] provideDiscreteMinusIntegerValues() {
//        return new Object[][] {
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { -6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] { 0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { -6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] { 0.0625, 0.125, 0.1875, 0.25, 0.1875, 0.125, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { -10.0, -8.0, -6.0, -4.0, -2.0, 0.0, 2.0, 4.0, 6.0, }, new Double[] { 0.0625, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }), new ProbabilisticInteger(new Integer[] { 1, 3 }, new Double[] { 0.5, 0.5 }),
//                        new ProbabilisticDouble(new Double[] { -2.0, 0.0, 2.0, 4.0, 6.0 }, new Double[] { 0.125, 0.25, 0.25, 0.25, 0.125 }) } };
//    }
//
//    /**
//     * Data provider for "*" operation.
//     * 
//     * @return An array of test and expected values
//     */
//    @DataProvider(name = "discreteMultiplicationInteger")
//    public final Object[][] provideDiscreteMultiplicationIntegerValues() {
//        return new Object[][] {
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0, 7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 }, new Double[] { 0.0625, 0.125, 0.125, 0.125, 0.0625, 0.125, 0.125, 0.0625,
//                                0.125, 0.0625 }) },
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0, 7.0, 9.0, 15.0, 21.0, 25.0, 35.0, 49.0 }, new Double[] { 0.0625, 0.125, 0.125, 0.125, 0.0625, 0.125, 0.125, 0.0625,
//                                0.125, 0.0625 }) },
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0, 7.0, 9.0, 11.0, 15.0, 21.0, 27.0, 33.0, 45.0, 55.0, 63.0, 77.0 }, new Double[] { 0.0625, 0.125, 0.0625, 0.0625, 0.125,
//                                0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625 }) },
//                { new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }), new ProbabilisticInteger(new Integer[] { 1, 3 }, new Double[] { 0.5, 0.5 }),
//                        new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0, 7.0, 9.0, 15.0, 21.0 }, new Double[] { 0.125, 0.25, 0.125, 0.125, 0.125, 0.125, 0.125 }) } };
//    }
//
//    /**
//     * Data provider for "/" operation.
//     * 
//     * @return An array of test and expected values
//     */
//    @DataProvider(name = "discreteDivisionInteger")
//    public final Object[][] provideDiscreteDivisionIntegerValues() {
//        return new Object[][] {
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 0.14285714285714285, 0.2, 0.3333333333333333, 0.42857142857142855, 0.6, 0.7142857142857143, 1.0, 1.4, 1.6666666666666667,
//                                2.3333333333333335, 3.0, 5.0, 7.0, }, new Double[] { 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.25, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625 }) },
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 7, 5, 3, 1 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 0.14285714285714285, 0.2, 0.3333333333333333, 0.42857142857142855, 0.6, 0.7142857142857143, 1.0, 1.4, 1.6666666666666667,
//                                2.3333333333333335, 3.0, 5.0, 7.0, }, new Double[] { 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.25, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625 }) },
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 9, 11 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticDouble(new Double[] { 0.09090909090909091, 0.1111111111111111, 0.2727272727272727, 0.3333333333333333, 0.45454545454545453, 0.5555555555555556,
//                                0.6363636363636364, 0.7777777777777778, 1.0, 1.6666666666666667, 2.3333333333333335, 3.0, 5.0, 7.0, }, new Double[] { 0.0625, 0.0625, 0.0625, 0.125, 0.0625, 0.0625,
//                                0.0625, 0.0625, 0.125, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625 }) },
//                {
//                        new ProbabilisticInteger(new Integer[] { 1, 3, 5, 7 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
//                        new ProbabilisticInteger(new Integer[] { 1, 3 }, new Double[] { 0.5, 0.5 }),
//                        new ProbabilisticDouble(new Double[] { 0.3333333333333333, 1.0, 1.6666666666666667, 2.3333333333333335, 3.0, 5.0, 7.0, }, new Double[] { 0.125, 0.25, 0.125, 0.125, 0.125,
//                                0.125, 0.125 }) } };
//    }
}
