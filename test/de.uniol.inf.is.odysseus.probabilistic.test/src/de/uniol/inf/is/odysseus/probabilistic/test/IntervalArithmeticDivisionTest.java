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

import java.util.Arrays;
import java.util.Collection;

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
public class IntervalArithmeticDivisionTest {
    @Parameters(name = "{index}: {0} / {1} = {2}")
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][] { //
                // 0 !E [c, d]
                { Interval.of(1.0, 3.0), Interval.of(3.0, 5.0), new Interval[] { Interval.of(0.2, 1.0) } }, //

                // b < 0.0 && c < d && d == 0.0
                { Interval.of(-3.0, -1.0), Interval.of(-5.0, 0.0), new Interval[] { Interval.of(0.2, Double.POSITIVE_INFINITY) } }, //

                // b < 0.0 && c < 0.0 && 0.0 < d
                { Interval.of(-3.0, -1.0), Interval.of(-5.0, 3.0), new Interval[] { Interval.of(Double.NEGATIVE_INFINITY, -1.0 / 3.0), Interval.of(-1.0 / -5.0, Double.POSITIVE_INFINITY) } }, //

                // b < 0.0 && c == 0.0 && c < d
                { Interval.of(-3.0, -1.0), Interval.of(0.0, 3.0), new Interval[] { Interval.of(Double.NEGATIVE_INFINITY, -1.0 / 3.0) } }, //

                // 0.0 < a && c < d && d == 0.0
                { Interval.of(1.0, 3.0), Interval.of(-3.0, 0.0), new Interval[] { Interval.of(Double.NEGATIVE_INFINITY, 1.0 / -3.0) } }, //

                // 0.0 < a && c < 0.0 && 0.0 < d
                { Interval.of(1.0, 3.0), Interval.of(-3.0, 1.0), new Interval[] { Interval.of(Double.NEGATIVE_INFINITY, 1.0 / -3.0), Interval.of(1.0 / 1.0, Double.POSITIVE_INFINITY) } }, //

                // 0.0 < a && c == 0.0 && c < d
                { Interval.of(1.0, 3.0), Interval.of(0.0, 01.0), new Interval[] { Interval.of(1.0, Double.POSITIVE_INFINITY) } }, //

                // 0 !E [a, b] && c == 0.0 && d == 0.0
                { Interval.of(-3.0, -1.0), Interval.of(0.0, 0.0), new Interval[] { Interval.EMPTY } }, //

                // 0 E [a, b] && 0 E [c, d]
                { Interval.of(-3.0, 1.0), Interval.of(-3.0, 1.0), new Interval[] { Interval.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) } },//

        });
    }

    @Parameter(0)
    public Interval left;
    @Parameter(1)
    public Interval right;
    @Parameter(2)
    public Interval[] result;

    private Interval[] tmpResult;

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.common.Interval#multiply(de.uniol.inf.is.odysseus.probabilistic.common.Interval)}.
     */
    @Test
    public void testDivideInterval() {
        whenDivide(left, right);

        thenResultIs(result);
    }

    private void whenDivide(Interval left, Interval right) {

        System.out.print(String.format("%s / %s == ", left, right));
        final Stopwatch timer = Stopwatch.createStarted();

        tmpResult = left.divide(right);

        timer.stop();

        System.out.println(String.format("%s", Arrays.toString(tmpResult)));
        System.out.println("Test took: " + timer);
    }

    private void thenResultIs(Interval[] result) {
        assertThat(String.format("Invalid result %s", Arrays.toString(tmpResult)), tmpResult, is(result));
    }

}
