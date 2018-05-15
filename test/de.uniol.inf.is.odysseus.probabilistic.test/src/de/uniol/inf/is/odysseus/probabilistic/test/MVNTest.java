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

import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@RunWith(Parameterized.class)
public class MVNTest {

    @Parameters(name = "{index}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { //
                { new double[] { 0.0, 0.0 }, new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } }, new double[] { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY }, new double[] { 0.0, 0.0 }, 0.3333 }, //
        });
    }

    @Parameter(0)
    public double[] mu;
    @Parameter(1)
    public double[][] sigma;
    @Parameter(2)
    public double[] lower;
    @Parameter(3)
    public double[] upper;
    @Parameter(4)
    public double probability;

    private Matrix lowerBound;
    private Matrix upperBound;
    private double tmpResult;

    @Test
    public void test() {
        if (lower != null)
            givenLowerBound(lower);
        if (upper != null)
            givenUpperBound(upper);

        whenEstimateCumulativeProbabilityFor(mu, sigma);

        thenProbabilityIs(probability);
    }

    private void givenLowerBound(double[] lower) {
        this.lowerBound = new Matrix(new double[][] { lower });
    }

    private void givenUpperBound(double[] upper) {
        this.upperBound = new Matrix(new double[][] { upper });
    }

    private void whenEstimateCumulativeProbabilityFor(double[] mu, double[][] sigma) {

        Matrix muVector = new Matrix(new double[][] { mu });
        Matrix sigmaMatrix = new Matrix(sigma);
        Matrix tmpLowerBound;
        if (lowerBound == null) {
            final double[] lower = new double[mu.length];
            Arrays.fill(lower, Double.NEGATIVE_INFINITY);
            tmpLowerBound = new Matrix(new double[][] { lower });
        } else {
            tmpLowerBound = lowerBound;
        }
        Matrix tmpUpperBound;
        if (upperBound == null) {
            tmpUpperBound = new Matrix(muVector);
        } else {
            tmpUpperBound = upperBound.substract(new Matrix(muVector));
        }
        System.out.print(String.format("∫N(%s, %s)dx = ", muVector, sigmaMatrix));
        final Stopwatch timer = Stopwatch.createStarted();

        tmpResult = QSIMVN.cumulativeProbability(5000, sigmaMatrix, tmpLowerBound, tmpUpperBound).getProbability();
        timer.stop();

        System.out.println(String.format("%s", tmpResult));
        System.out.println("Test took: " + timer);
    }

    private void thenProbabilityIs(double result) {
        assertThat(String.format("Invalid result %s", tmpResult), tmpResult, is(result));
    }
}
