/*
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

package de.uniol.inf.is.odysseus.probabilistic.math.genz;

import java.util.ArrayList;

import org.apache.commons.math3.primes.Primes;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.util.FastMath;

public class Util {
    private static Max maxFunction = new Max();

    // public static double sqrt(final double d) {
    // return FastMath.sqrt(d);
    // }

    public static double max(final double[] ds) {
        // C = max(A) returns the largest elements along different dimensions of
        // an array.
        //
        // If A is a vector, max(A) returns the largest element in A.
        //
        // If A is a matrix, max(A) treats the columns of A as vectors,
        // returning a row vector containing the maximum element from each
        // column.
        //
        // If A is a multidimensional array, max(A) treats the values along
        // the first non-singleton dimension as vectors, returning the
        // maximum value of each vector.
        //
        // C = max(A,B) returns an array the same size as A and B with the
        // largest elements taken from A or B. The dimensions of A and B must
        // match, or they may be scalar.
        //
        // C = max(A,[],dim) returns the largest elements along the dimension
        // of A specified by scalar dim. For example, max(A,[],1) produces
        // the maximum values along the first dimension of A.
        //
        // [C,I] = max(...) finds the indices of the maximum values of A,
        // and returns them in output vector I. If there are several
        // identical maximum values, the index of the first one found is
        // returned.

        // double max = 0;
        // for (final double d : ds) {
        // if (d > max) {
        // max = d;
        // }
        // }

        return maxFunction.evaluate(ds);
    }

    // public static double log(final double i) {
    // return FastMath.log(i);
    // }

    public static double phi(final double z) {
        return Erf.erfc(-z / FastMath.sqrt(2)) / 2;
    }

    // public static double sign(final double ai) {
    // return FastMath.signum(ai);
    // }

    public static double phinv(final double p) {
        // %
        // % Standard statistical normal distribution functions
        // %
        // function p = phi(z), p = erfc( -z/sqrt(2) )/2;
        // function z = phinv(p), z = norminv( p );
        // % function z = phinv(p), z = -sqrt(2)*erfcinv( 2*p ); % use if no
        // norminv
        // %
        //

        return -FastMath.sqrt(2) * Erf.erfcInv(2 * p);
    }

    // public static double exp(final double v) {
    // // Y = exp(X) returns the exponential for each element
    // // of X. exp operates element-wise on arrays. For
    // // complex x + i * y, exp returns the complex
    // // exponential ez = ex(cos y + i sin y). Use expm for matrix
    // // exponentials.
    // return FastMath.exp(v);
    // // return 0;
    // }

    // Return primes less than limit
    public static ArrayList<Integer> generatePrimes(final int limit) {
        final int numPrimes = Util.countPrimesUpperBound(limit);
        final ArrayList<Integer> primes = new ArrayList<Integer>(numPrimes);
        // final boolean[] isComposite = new boolean[limit]; // all false
        // final int sqrtLimit = (int) FastMath.sqrt(limit); // floor
        // for (int i = 2; i <= sqrtLimit; i++) {
        // if (!isComposite[i]) {
        // primes.add(i);
        // for (int j = i * i; j < limit; j += i) {
        // isComposite[j] = true;
        // }
        // }
        // }
        // for (int i = sqrtLimit + 1; i < limit; i++) {
        // if (!isComposite[i]) {
        // primes.add(i);
        // }
        // }
        //
        // // check limit for prime
        // if (Primes.isPrime(limit)) {
        // primes.add(limit);
        // }

        // TODO Check if this is maybe even faster. CKu-20131113
        for (int i = 2; i <= limit; i++) {
            if (Primes.isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    // private static boolean isPrime(final long n) {
    // if (n < 2) {
    // return false;
    // }
    // if ((n == 2) || (n == 3)) {
    // return true;
    // }
    // if (((n % 2) == 0) || ((n % 3) == 0)) {
    // return false;
    // }
    // final long sqrtN = (long) FastMath.sqrt(n) + 1;
    // for (long i = 6L; i <= sqrtN; i += 6) {
    // if (((n % (i - 1)) == 0) || ((n % (i + 1)) == 0)) {
    // return false;
    // }
    // }
    // return true;
    // }

    /**
     * 
     * @param max
     * @return
     */
    private static int countPrimesUpperBound(final int max) {
        if (max > 1) {
            return (int) ((1.25506 * max) / FastMath.log(max));
        }
        return 0;
    }
}
