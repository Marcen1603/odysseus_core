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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class MultiWorldAvgPartialAggregate<T> extends AbstractPartialAggregate<T> {
    /** The sum of the aggregate. */
    private AbstractProbabilisticValue<?> sum;
    /** The count of the aggregate. */
    private double count;
    /** The result data type. */
    private final String datatype;

    /**
     * Default constructor.
     * 
     * @param datatype
     *            The result datatype
     */
    public MultiWorldAvgPartialAggregate(final String datatype) {
        this.sum = new ProbabilisticDouble(0.0, 1.0);
        this.count = 0.0;
        this.datatype = datatype;
    }

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param sum
     *            The sum
     * @param count
     *            The count
     * @param datatype
     *            The result datatype
     */
    public MultiWorldAvgPartialAggregate(final AbstractProbabilisticValue<?> sum, final double count, final String datatype) {
        this.datatype = datatype;
        this.count = count;
        this.sum = sum;
    }

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param aggregate
     *            The aggregate
     * @param datatype
     *            The result datatype
     */
    public MultiWorldAvgPartialAggregate(final AbstractProbabilisticValue<?> aggregate, final String datatype) {
        this.datatype = datatype;
        this.add(aggregate);
    }

    /**
     * Copy constructor.
     * 
     * @param avgPartialAggregate
     *            The object to copy from
     */
    public MultiWorldAvgPartialAggregate(final MultiWorldAvgPartialAggregate<T> avgPartialAggregate) {
        this.sum = avgPartialAggregate.sum;
        this.count = avgPartialAggregate.count;
        this.datatype = avgPartialAggregate.datatype;
    }

    /**
     * Gets the value of the average property.
     * 
     * @return the average
     */
    public final AbstractProbabilisticValue<?> getAggregate() {
        final Map<Double, Double> values = new HashMap<Double, Double>(this.sum.getValues().size());
        for (final Entry<?, Double> entry : this.sum.getValues().entrySet()) {
            final double value = ((Number) entry.getKey()).doubleValue() / this.count;
            if (values.containsKey(value)) {
                values.put(value, values.get(value) + entry.getValue());
            }
            else {
                values.put(value, entry.getValue());
            }
        }
        return new ProbabilisticDouble(values);
    }

    /**
     * Gets the current sum of the aggregate.
     * 
     * @return Th sum
     */
    public AbstractProbabilisticValue<?> getSum() {
        return this.sum;
    }

    /**
     * Gets the current count of the aggregate.
     * 
     * @return The count
     */
    public double getCount() {
        return this.count;
    }

    /**
     * Add the given value to the aggregate.
     * 
     * @param value
     *            The value
     */
    public final void add(final AbstractProbabilisticValue<?> value) {
        final Map<Double, Double> newValues = new HashMap<Double, Double>(this.sum.getValues().size() * value.getValues().size());
        double probability = 0.0;
        for (final Entry<?, Double> sumEntry : this.sum.getValues().entrySet()) {
            for (final Entry<?, Double> valueEntry : value.getValues().entrySet()) {
                final double newValue = ((Number) sumEntry.getKey()).doubleValue() + ((Number) valueEntry.getKey()).doubleValue();

                if (newValues.containsKey(newValue)) {
                    newValues.put(newValue, newValues.get(newValue) + (sumEntry.getValue() * valueEntry.getValue()));
                }
                else {
                    newValues.put(newValue, sumEntry.getValue() * valueEntry.getValue());
                }
                probability += sumEntry.getValue() * valueEntry.getValue();
            }
        }
        this.sum = new ProbabilisticDouble(newValues);
        this.count += probability;
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("MultiWorldAvgPartialAggregate (").append(this.hashCode()).append(")").append(this.sum);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final MultiWorldAvgPartialAggregate<T> clone() {
        return new MultiWorldAvgPartialAggregate<T>(this);
    }

}
