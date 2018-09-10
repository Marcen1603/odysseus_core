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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class SumPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = 2965760779236451387L;
	/** The value of the aggregate. */
    private MultivariateMixtureDistribution sum;
    /** The result data type. */
    private final String datatype;

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param distribution
     *            The distribution
     * @param datatype
     *            The result datatype
     */
    public SumPartialAggregate(final MultivariateMixtureDistribution distribution, final String datatype) {
        this.sum = distribution;
        this.datatype = datatype;
    }

    /**
     * Copy constructor.
     * 
     * @param sumPartialAggregate
     *            The object to copy from
     */
    public SumPartialAggregate(final SumPartialAggregate<T> sumPartialAggregate) {
        this.sum = sumPartialAggregate.sum.clone();
        this.datatype = sumPartialAggregate.datatype;
    }

    /**
     * Gets the value of the sum property.
     * 
     * @return the sum
     */
    public final MultivariateMixtureDistribution getSum() {
        return this.sum;
    }

    /**
     * Add the given distribution to the aggregate.
     * 
     * @param value
     *            The value to add
     */
    public final void add(final MultivariateMixtureDistribution value) {
        this.sum = this.sum.add(value);
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("SumPartialAggregate (").append(this.hashCode()).append(")").append(this.sum);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final SumPartialAggregate<T> clone() {
        return new SumPartialAggregate<T>(this);
    }
}
