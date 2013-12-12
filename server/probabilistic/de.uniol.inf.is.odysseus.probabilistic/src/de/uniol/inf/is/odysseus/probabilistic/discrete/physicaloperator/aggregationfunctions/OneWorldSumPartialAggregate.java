/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions;

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;

/**
 * Sum Aggregation on probabilistic data stream according to T.S.Jayram et al.
 * "Estimating statistical aggregates on probabilistic data streams"
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class OneWorldSumPartialAggregate<T> implements IPartialAggregate<T> {
    /** The value of the aggregate. */
    private double aggregate = 0;
    /** The result data type. */
    private final String datatype;

    /**
     * Default constructor.
     * 
     * @param datatype
     *            The result datatype
     */
    public OneWorldSumPartialAggregate(final String datatype) {
        this.aggregate = 0.0;
        this.datatype = datatype;
    }

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param value
     *            The value
     * @param datatype
     *            The result datatype
     */
    public OneWorldSumPartialAggregate(final AbstractProbabilisticValue<?> value, final String datatype) {
        this.datatype = datatype;
        this.add(value);
    }

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param aggregate
     *            The value of the sum
     * @param datatype
     *            The result datatype
     */
    public OneWorldSumPartialAggregate(final double aggregate, final String datatype) {
        this.aggregate = aggregate;
        this.datatype = datatype;
    }

    /**
     * Copy constructor.
     * 
     * @param sumPartialAggregate
     *            The object to copy from
     */
    public OneWorldSumPartialAggregate(final OneWorldSumPartialAggregate<T> sumPartialAggregate) {
        this.aggregate = sumPartialAggregate.aggregate;
        this.datatype = sumPartialAggregate.datatype;
    }

    /**
     * Gets the value of the sum property.
     * 
     * @return the sum
     */
    public final double getAggregate() {
        return this.aggregate;
    }

    /**
     * Add the given value to the aggregate.
     * 
     * @param value
     *            The value
     */
    public final void add(final AbstractProbabilisticValue<?> value) {
        for (final Entry<?, Double> entry : value.getValues().entrySet()) {
            this.aggregate += ((Number) entry.getKey()).doubleValue() * entry.getValue();
        }
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("OneWorldSumPartialAggregate (").append(this.hashCode()).append(")").append(this.aggregate);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final OneWorldSumPartialAggregate<T> clone() {
        return new OneWorldSumPartialAggregate<T>(this);
    }
}
