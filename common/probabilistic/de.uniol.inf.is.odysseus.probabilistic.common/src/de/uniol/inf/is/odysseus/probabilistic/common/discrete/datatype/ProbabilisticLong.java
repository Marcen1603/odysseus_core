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
 * Data type representing a probabilistic discrete Long value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticLong extends AbstractProbabilisticValue<Long> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8340232282600212118L;

    /**
     * Creates a new {@link ProbabilisticLong} with the given value and
     * probability.
     * 
     * @param value
     *            The value
     * @param probability
     *            The associated probability
     */
    public ProbabilisticLong(final Long value, final Double probability) {
        super(value, probability);
    }

    /**
     * Creates a new {@link ProbabilisticLong} with the given values.
     * 
     * @param values
     *            The values
     */
    public ProbabilisticLong(final Map<Long, Double> values) {
        super(values);
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            The object to copy from
     */
    public ProbabilisticLong(final ProbabilisticLong other) {
        super(other);
    }

    /**
     * Creates a new {@link ProbabilisticLong} with the given values and
     * probabilities.
     * 
     * @param values
     *            The values
     * @param probabilities
     *            The associated probabilities
     */
    public ProbabilisticLong(final Long[] values, final Double[] probabilities) {
        super(values, probabilities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticLong clone() {
        return new ProbabilisticLong(this);
    }
}
