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
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Stopwatch;

import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@RunWith(Parameterized.class)
public class IntervalTest {

    @Parameters(name = "{index}: {0} {1} {2} {3}")
    public static Collection<Object[]> data() {
        
        
        return Arrays.asList(new Object[][] { //
                { true, true, true, true }, //
                { true, true, true, false }, //
                { true, true, false, true }, //
                { true, true, false, false }, //
                { true, false, true, true }, //
                // { true, false, true, false }, //
                // { true, false, false, true }, //
                // { true, false, false, false }, //
                // { false, true, true, true }, //
                // { false, true, true, false }, //
                // { false, true, false, true }, //
                // { false, true, false, false }, //
                // { false, false, true, true }, //
                // { false, false, true, false }, //
                // { false, false, false, true }, //
                // { false, false, false, false }, //

        });
    }

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

    @Parameter(0)
    public boolean infInclusive;
    @Parameter(1)
    public boolean supInclusive;
    @Parameter(2)
    public boolean otherInfInclusive;
    @Parameter(3)
    public boolean otherSupInclusive;

    private Interval interval;

    private Interval resultInterval;
    private boolean resultInfInclusive;
    private boolean resultSupInclusive;

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
    @Ignore
    @Test
    public void testIntersects() {
        givenInterval(1, 3);

        thenIntervalIntersects(2, 4);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#isEmpty()}.
     */
    @Ignore
    @Test
    public void testIsEmpty() {
        givenInterval(1, 1);

        if (this.infInclusive || this.supInclusive) {
            thenIntervalIsNotEmpty();
        } else {
            thenIntervalIsEmpty();
        }
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#isNaN()}.
     */
    @Ignore
    @Test
    public void testIsNaN() {
        givenInterval(Double.NaN, Double.NaN);

        thenResultIsNaN();
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#length()}.
     */
    @Ignore
    @Test
    public void testLength() {
        givenInterval(1, 1);

    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testAddInterval() {
        givenInterval(-4, 4);

        whenAdd(-5, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -9, 9, this.supInclusive & this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testAddPositiveInterval() {
        givenInterval(0, 4);

        whenAdd(1, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, 1, 9, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testAddNegativeInterval() {
        givenInterval(-4, 0);

        whenAdd(-5, -1);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -9, -1, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(double)}.
     */
    @Test
    public void testAddDouble() {
        givenInterval(-4, 4);

        whenAdd(5);

        thenResultIs(this.infInclusive, 1, 9, this.supInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSubtractInterval() {
        givenInterval(-4, 4);

        whenSubstract(-5, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -9, 9, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSubtractNegativeInterval() {
        givenInterval(-4, 0);

        whenSubstract(-5, -1);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -3, 5, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testSubtractPositiveInterval() {
        givenInterval(0, 4);

        whenSubstract(1, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -5, 3, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(double)}.
     */
    @Test
    public void testSubtractDouble() {
        givenInterval(-4, 4);

        whenSubstract(5);

        thenResultIs(this.infInclusive, -9, -1, this.supInclusive);

    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}
     * and
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     * . \f$ [a, b] - [c, d] + [c, d] = [a - d + c, b - c + d] \f$
     */
    @Test
    public void testSubtractAndAddInterval() {
        givenInterval(-4, 4);

        whenSubstract(-5, 5);
        whenUsingLastResult();
        whenAdd(-5, 5);

        //
        thenResultIs(this.infInclusive && this.otherInfInclusive, -14, 14, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#subtract(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}
     * and
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#add(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     * . \f$ [a, b] + [c, d] + [c, d] = [a + c - d, b + d - c] \f$
     */
    @Test
    public void testAddAndSubtractInterval() {
        givenInterval(-4, 4);

        whenAdd(-5, 5);
        whenUsingLastResult();
        whenSubstract(-5, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -14, 14, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#multiply(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */

    @Test
    public void testMultiplyInterval() {
        givenInterval(-4, 4);

        whenMultiply(-5, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -20, 20, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#multiply(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */

    @Test
    public void testMultiplyNegativeInterval() {
        givenInterval(-4, 0);

        whenMultiply(-5, -1);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -0.0, 20, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#multiply(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */

    @Test
    public void testMultiplyPositiveInterval() {
        givenInterval(0, 4);

        whenMultiply(1, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, 0, 20, this.supInclusive && this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#multiply(double)}.
     */

    @Test
    public void testMultiplyDouble() {
        givenInterval(-4, 4);

        whenMultiply(5);

        thenResultIs(this.infInclusive, -20, 20, this.supInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDivideInterval() {
        givenInterval(-4, 4);

        whenDivide(-5, 5);

        thenResultIs(false, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testDivideNegativeIntervalWithZero() {
        givenInterval(-4, 0);

        whenDivide(-5, 0);

        thenResultIs(this.infInclusive && this.otherInfInclusive, -0.0, 4, this.supInclusive && this.otherSupInclusive);
    }
    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDivideNegativeInterval() {
        givenInterval(-4, -1);

        whenDivide(-5, -1);

        thenResultIs(this.infInclusive && this.otherInfInclusive, 0.2, 4, this.supInclusive && this.otherSupInclusive);
    }
    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testDividePositiveIntervalWithZero() {
        givenInterval(0, 4);

        whenDivide(0, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, 0, 4, this.supInclusive && this.otherSupInclusive);
    }
    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDividePositiveInterval() {
        givenInterval(1, 4);

        whenDivide(1, 5);

        thenResultIs(this.infInclusive && this.otherInfInclusive, 0.2, 4, this.supInclusive && this.otherSupInclusive);
    }
    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#divide(double)}.
     */
    @Test
    public void testDivideDouble() {
        givenInterval(-4, 4);

        whenDivide(5);

        thenResultIs(this.infInclusive, -0.8, 0.8, this.supInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#union(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */

    @Test
    public void testUnion() {
        givenInterval(1, 3);

        whenUnion(2, 6);

        thenResultIs(this.infInclusive, 1, 6, this.otherSupInclusive);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#intersection(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testIntersection() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#difference(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testDifference() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#contains(double)}.
     */
    @Ignore
    @Test
    public void testContainsDouble() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#contains(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testContainsInterval() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(double)}.
     */
    @Ignore
    @Test
    public void testSplitDouble() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#split(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testSplitInterval() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#compareTo(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Ignore
    @Test
    public void testCompareTo() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#equals(java.lang.Object)}.
     */
    @Ignore
    @Test
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

    private void givenInterval(double inf, double sup) {
        this.interval = new Interval(inf, sup, infInclusive, supInclusive);
    }

    private void whenAdd(double inf, double sup) {
        Interval other = new Interval(inf, sup, this.otherInfInclusive, this.otherSupInclusive);

        System.out.print(String.format("%s + %s == ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.add(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenAdd(double value) {

        System.out.print(String.format("%s + %s == ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.add(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenSubstract(double inf, double sup) {
        Interval other = new Interval(inf, sup, this.otherInfInclusive, this.otherSupInclusive);

        System.out.print(String.format("%s - %s == ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.subtract(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenSubstract(double value) {
        System.out.print(String.format("%s - %s == ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.subtract(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenMultiply(double inf, double sup) {
        Interval other = new Interval(inf, sup, this.otherInfInclusive, this.otherSupInclusive);

        System.out.print(String.format("%s * %s == ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.multiply(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenMultiply(double value) {
        System.out.print(String.format("%s * %s == ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.multiply(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenUsingLastResult() {
        this.interval = this.resultInterval.clone();
    }

    private void whenDivide(double inf, double sup) {
        Interval other = new Interval(inf, sup, this.otherInfInclusive, this.otherSupInclusive);

        System.out.print(String.format("%s / %s == ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.divide(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenDivide(double value) {
        System.out.print(String.format("%s / %s == ", this.interval, value));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.divide(value);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void whenUnion(double inf, double sup) {
        Interval other = new Interval(inf, sup, this.otherInfInclusive, this.otherSupInclusive);

        System.out.print(String.format("%s u %s == ", this.interval, other));
        final Stopwatch timer = Stopwatch.createStarted();

        this.resultInterval = this.interval.union(other);

        timer.stop();

        System.out.println(String.format("%s", this.resultInterval));
        System.out.println("Test took: " + timer);
    }

    private void thenIntervalIntersects(double inf, double sup) {
        Interval other = new Interval(inf, sup, this.otherInfInclusive, this.otherSupInclusive);
        assertThat("Interval do not intersect", this.interval.intersects(other), is(true));
    }

    private void thenIntervalIsEmpty() {
        assertThat("Interval is not empty", this.interval.isEmpty(), is(true));
    }

    private void thenIntervalIsNotEmpty() {
        assertThat("Interval is  empty", this.interval.isEmpty(), is(false));
    }

    private void thenResultIs(double inf, double sup) {
        assertThat(String.format("Invalid result %s", this.resultInterval), this.resultInterval, is(new Interval(inf, sup, resultInfInclusive, resultSupInclusive)));
    }

    private void thenResultIs(boolean infInclusive, double inf, double sup, boolean supInclusive) {
        assertThat(String.format("Invalid result %s", this.resultInterval), this.resultInterval, is(new Interval(inf, sup, infInclusive, supInclusive)));
    }

    private void thenResultIsNaN() {
        assertThat(String.format("Invalid result %s", this.interval), this.interval.isNaN(), is(true));

    }
}
