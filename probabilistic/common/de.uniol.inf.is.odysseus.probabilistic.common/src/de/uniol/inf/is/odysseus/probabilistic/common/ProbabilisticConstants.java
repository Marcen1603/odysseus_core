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
package de.uniol.inf.is.odysseus.probabilistic.common;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class ProbabilisticConstants {
    /** Acceptable epsilon for test on equality. */
    public static final double EPSILON = 10E-9;
    /** Number of samples for the Genz algorithm. */
    public static final int SAMPLES = 100;

    /** Maximum number of iterations for distribution fitting in MEP functions. */
    public static final int MEP_MAX_ITERATIONS = 1000;
    /** Threshold for distribution fitting in MEP functions. */
    public static final double MEP_THRESHOLD = 10E-4;
    /** Samples for distribution fitting in MEP functions. */
    public static final int MEP_SAMPLES = 100;

    /**
     * Private constructor.
     */
    private ProbabilisticConstants() {
        throw new UnsupportedOperationException();
    }
}
