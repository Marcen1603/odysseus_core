/**
 * Copyright 2017 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Stopwatch;

import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class IntervalTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private Interval interval;

    private Interval resultInterval;
    private Interval resultInterval2;
    private Interval resultInterval3;

    private double resultValue;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#intersects(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testIntersects() {
        givenInterval(1, 3);

        thenIntervalIntersects(2, 4);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        givenInterval(1, 1);

        thenIntervalIsNotEmpty();

    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#isNaN()}.
     */
    @Test
    public void testIsNaN() {
        givenInterval(Double.NaN, Double.NaN);

        thenResultIsNaN();
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#length()}.
     */
    @Test
    public void testLength() {
        givenInterval(1, 2);

        thenResultLengthIs(1);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(double)}.
     */
    @Test
    public void testAddDouble() {
        givenInterval(-4, 4);

        whenAdd(5);

        thenResultIs(1, 9);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(double)}.
     */
    @Test
    public void testSubtractDouble() {
        givenInterval(-4, 4);

        whenSubstract(5);

        thenResultIs(-9, -1);

    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#multiply(double)}.
     */

    @Test
    public void testMultiplyDouble() {
        givenInterval(-4, 4);

        whenMultiply(5);

        thenResultIs(-20, 20);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(double)}.
     */
    @Test
    public void testDivideDouble() {
        givenInterval(-4, 4);

        whenDivide(5);

        thenResultIs(-0.8, 0.8);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#union(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */

    @Test
    public void testUnion() {
        givenInterval(1, 3);

        whenUnion(2, 6);

        thenResultIs(1, 6);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#intersection(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testIntersection() {
        givenInterval(1, 5);

        whenIntersection(3, 6);

        thenResultIs(3, 5);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#difference(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDifferenceWithNonOverlappingIntervals() {
        givenInterval(1, 3);

        whenDifference(4, 6);

        thenResultIs(1, 3);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#difference(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDifferenceWithLeftOverlappingIntervals() {
        givenInterval(2, 6);

        whenDifference(1, 3);

        thenResultIs(3, 6);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#difference(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDifferenceWithRightOverlappingIntervals() {
        givenInterval(1, 3);

        whenDifference(2, 6);

        thenResultIs(1, 2);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#difference(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDifference() {
        givenInterval(1, 6);

        whenDifference(2, 3);

        thenResultIs(1, 2);
        thenSecondResultIs(3, 6);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#contains(double)}.
     */
    @Test
    public void testContainsDouble() {
        givenInterval(1, 6);

        thenResultContains(1);
        thenResultContains(2);
        thenResultContains(6);
        thenResultContainsNot(7);
        thenResultContainsNot(0);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#contains(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testContainsInterval() {
        givenInterval(1, 6);

        thenResultContains(1, 2);
        thenResultContains(2, 3);
        thenResultContains(5, 6);
        thenResultContainsNot(6, 7);
        thenResultContainsNot(0, 1);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(double)}.
     */

    @Test
    public void testSplitDouble() {
        givenInterval(1, 6);

        whenSplit(3);

        thenResultIs(1, 3);
        thenSecondResultIs(3, 6);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSplitNonOverlappingInterval() {
        givenInterval(2, 3);

        whenSplit(4, 6);

        thenResultIs(2, 3);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSplitLeftOverlappingInterval() {
        givenInterval(2, 6);

        whenSplit(1, 3);

        thenResultIs(2, 3);
        thenSecondResultIs(3, 6);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSplitRightOverlappingInterval() {
        givenInterval(1, 3);

        whenSplit(2, 6);

        thenResultIs(1, 2);
        thenSecondResultIs(2, 3);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSplitInterval() {
        givenInterval(2, 6);

        whenSplit(3, 4);

        thenResultIs(2, 3);
        thenSecondResultIs(3, 4);
        thenThirdResultIs(4, 6);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#compareTo(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */

    @Test
    public void testCompareTo() {
        givenInterval(2, 6);

        whenCompare(2, 6);

        thenIntervalIsComparable(2, 6);
        thenResultIs(0);

        whenCompare(0, 1);

        thenIntervalIsComparable(0, 1);
        thenResultIsPositive();

        whenCompare(7, 8);

        thenIntervalIsComparable(7, 8);
        thenResultIsNegative();

        whenCompare(0, 2);

        thenIntervalIsComparable(0, 2);
        thenResultIsPositive();

        whenCompare(6, 8);

        thenIntervalIsComparable(6, 8);
        thenResultIsNegative();

        whenCompare(0, 3);

        thenIntervalIsComparable(0, 3);
        thenResultIsPositive();

        whenCompare(5, 8);

        thenIntervalIsComparable(5, 8);
        thenResultIsNegative();

        whenCompare(0, 8);

        thenIntervalIsComparable(0, 8);
        thenResultIs(0);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#equals(java.lang.Object)}.
     */

    @Test
    public void testEqualsObject() {
        givenInterval(2, 3);

        thenIntervalIs(2, 3);
        thenIntervalIsNot(1, 3);
        thenIntervalIsNot(2, 4);
        thenIntervalIsNot(2.5, 2.5);
        thenIntervalIsNot(1.5, 3.5);

    }

    private void givenInterval(double inf, double sup) {
        this.interval = new Interval(inf, sup);
    }

    private void whenAdd(double value) {

        System.out.print(String.format("%s + %s = ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.add(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenSubstract(double value) {
        System.out.print(String.format("%s - %s = ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.subtract(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenMultiply(double value) {
        System.out.print(String.format("%s * %s = ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.multiply(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenUsingLastResult() {
        this.interval = this.resultInterval.clone();
    }

    private void whenDivide(double value) {
        System.out.print(String.format("%s / %s = ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.divide(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenUnion(double inf, double sup) {
        Interval other = new Interval(inf, sup);

        System.out.print(String.format("%s union %s = ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.union(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenIntersection(double inf, double sup) {
        Interval other = new Interval(inf, sup);

        System.out.print(String.format("%s intersect %s = ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.intersection(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenDifference(double inf, double sup) {
        Interval other = new Interval(inf, sup);

        System.out.print(String.format("%s diff %s = ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        Interval[] difference = this.interval.difference(other);
        this.resultInterval = difference[0];
        resultInterval2 = null;
        if (difference.length > 1) {
            this.resultInterval2 = difference[1];
        }

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenSplit(double value) {

        System.out.print(String.format("%s split %s = ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        Interval[] split = this.interval.split(value);
        this.resultInterval = split[0];
        resultInterval2 = null;
        if (split.length > 1) {
            this.resultInterval2 = split[1];
        }

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenSplit(double inf, double sup) {
        Interval other = new Interval(inf, sup);

        System.out.print(String.format("%s split %s = ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        Interval[] split = this.interval.split(other);
        this.resultInterval = split[0];
        resultInterval2 = null;
        resultInterval3 = null;

        if (split.length > 1) {
            this.resultInterval2 = split[1];
        }
        if (split.length > 2) {
            this.resultInterval3 = split[2];
        }
        timer.stop();

        System.out.println(String.format("%s %s %s", this.resultInterval, this.resultInterval2, this.resultInterval3));
        System.out.println("Test took: " + timer);
    }

    private void whenCompare(double inf, double sup) {
        Interval other = new Interval(inf, sup);

        System.out.print(String.format("%s == %s = ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultValue = this.interval.compareTo(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultValue));
        System.out.println("Test took: " + timer);
    }

    private void thenIntervalIsComparable(double inf, double sup) {
        Interval other = new Interval(inf, sup);

        assertThat("Interval violates sgn(x.compareTo(y)) == -sgn(y.compareTo(x))", Integer.signum(this.interval.compareTo(other)) == -Integer.signum(other.compareTo(this.interval)), is(true));

        Interval z = other.add(1);
        if ((this.interval.compareTo(other) > 0) && (other.compareTo(z) > 0)) {
            assertThat("Interval violates (x.compareTo(y)>0 && y.compareTo(z)>0) implies x.compareTo(z)>0", this.interval.compareTo(z) > 0, is(true));
        }
        if (this.interval.compareTo(other) == 0) {
            Interval positive = this.interval.add(1);
            Interval negative = this.interval.subtract(1);

            assertThat("Interval violates x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z))",
                    Integer.signum(this.interval.compareTo(positive)) == Integer.signum(other.compareTo(positive)), is(true));
            assertThat("Interval violates x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z))",
                    Integer.signum(this.interval.compareTo(negative)) == Integer.signum(other.compareTo(negative)), is(true));
        }
    }

    private void thenIntervalIntersects(double inf, double sup) {
        Interval other = new Interval(inf, sup);
        assertThat("Interval do not intersect", this.interval.intersects(other), is(true));
    }

    private void thenIntervalIsEmpty() {
        assertThat("Interval is not empty", this.interval.isEmpty(), is(true));
    }

    private void thenIntervalIsNotEmpty() {
        assertThat("Interval is  empty", this.interval.isEmpty(), is(false));
    }

    private void thenIntervalIs(double inf, double sup) {
        assertThat(String.format("Invalid interval %s", this.interval), this.interval, is(new Interval(inf, sup)));
    }

    private void thenIntervalIsNot(double inf, double sup) {
        assertThat(String.format("Invalid interval %s", this.interval), this.interval, is(not(new Interval(inf, sup))));
    }

    private void thenResultIs(double value) {
        assertThat(String.format("Invalid result %s", this.resultValue), this.resultValue, is(value));
    }

    private void thenResultIsPositive() {
        assertThat(String.format("Invalid result %s", this.resultValue), this.resultValue > 0, is(true));
    }

    private void thenResultIsNegative() {
        assertThat(String.format("Invalid result %s", this.resultValue), this.resultValue < 0, is(true));
    }

    private void thenResultIs(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.resultInterval), this.resultInterval, is(new Interval(inf, sup)));
    }

    private void thenResultIsNot(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.resultInterval), this.resultInterval, is(not(new Interval(inf, sup))));
    }

    private void thenSecondResultIs(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.resultInterval2), this.resultInterval2, is(new Interval(inf, sup)));
    }

    private void thenThirdResultIs(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.resultInterval3), this.resultInterval3, is(new Interval(inf, sup)));
    }

    private void thenResultLengthIs(double value) {
        assertThat(String.format("Invalid interval length %s", this.interval.length()), this.interval.length(), is(value));
    }

    private void thenResultContains(double value) {
        assertThat(String.format("Invalid result %s", this.interval), this.interval.contains(value), is(true));
    }

    private void thenResultContainsNot(double value) {
        assertThat(String.format("Invalid result %s", this.interval), this.interval.contains(value), is(false));
    }

    private void thenResultContains(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.interval), this.interval.contains(new Interval(inf, sup)), is(true));
    }

    private void thenResultContainsNot(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.interval), this.interval.contains(new Interval(inf, sup)), is(false));
    }

    private void thenResultIsNaN() {
        assertThat(String.format("Invalid result %s", this.interval), this.interval.isNaN(), is(true));

    }
}
