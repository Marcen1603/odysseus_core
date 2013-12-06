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

package de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype;

import java.util.Map;

/**
 * Data type representing a probabilistic discrete Double value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticDouble extends AbstractProbabilisticValue<Double> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3098205199340767119L;

    /**
     * Creates a new {@link ProbabilisticDouble} with the given value and
     * probability.
     * 
     * @param value
     *            The value
     * @param probability
     *            The associated probability
     */
    public ProbabilisticDouble(final Double value, final Double probability) {
        super(value, probability);
    }

    /**
     * Creates a new {@link ProbabilisticDouble} with the given values.
     * 
     * @param values
     *            The values
     */
    public ProbabilisticDouble(final Map<Double, Double> values) {
        super(values);
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            The object to copy from
     */
    public ProbabilisticDouble(final ProbabilisticDouble other) {
        super(other);
    }

    /**
     * Creates a new {@link ProbabilisticDouble} with the given values and
     * probabilities.
     * 
     * @param values
     *            The values
     * @param probabilities
     *            The associated probabilities
     */
    public ProbabilisticDouble(final Double[] values, final Double[] probabilities) {
        super(values, probabilities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticDouble clone() {
        return new ProbabilisticDouble(this);
    }
}
