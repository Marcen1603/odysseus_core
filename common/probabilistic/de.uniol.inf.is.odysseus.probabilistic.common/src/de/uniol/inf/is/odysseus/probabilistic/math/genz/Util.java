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
import java.util.List;

import org.apache.commons.math3.primes.Primes;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.util.FastMath;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class Util {
    /** The function to use for max. */
    private static Max maxFunction = new Max();

    /**
     * 
     * @param ds
     * @return
     */
    public static double max(final double[] ds) {
        return Util.maxFunction.evaluate(ds);
    }

    /**
     * 
     * @param z
     * @return
     */
    public static double phi(final double z) {
        return Erf.erfc(-z / FastMath.sqrt(2)) / 2;
    }

    /**
     * Standard statistical normal distribution functions
     * 
     * @param p
     * @return
     */
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

    /**
     * Return primes lower than limit.
     * 
     * @param limit
     *            The limit
     * @return The list of primes lower than limit
     */
    public static List<Integer> generatePrimes(final int limit) {
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

    /**
     * Hidden utility constructor.
     */
    private Util() {
        throw new UnsupportedOperationException();
    }
}
