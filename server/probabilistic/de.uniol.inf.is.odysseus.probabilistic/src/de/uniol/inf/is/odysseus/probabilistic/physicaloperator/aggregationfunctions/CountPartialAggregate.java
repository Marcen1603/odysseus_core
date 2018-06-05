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

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class CountPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = -8566525376093084571L;
	/** The value of the aggregate. */
    private double count = 0.0;
    /** The result data type. */
    private final String datatype;

    /**
     * Default constructor.
     * 
     * @param datatype
     *            The result datatype
     */
    public CountPartialAggregate(final String datatype) {
        this.count = 0.0;
        this.datatype = datatype;
    }

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param count
     *            The value of count
     * @param datatype
     *            The result datatype
     */
    public CountPartialAggregate(final double count, final String datatype) {
        this.count = count;
        this.datatype = datatype;
    }

    /**
     * Copy constructor.
     * 
     * @param countPartialAggregate
     *            The object to copy from
     */
    public CountPartialAggregate(final CountPartialAggregate<T> countPartialAggregate) {
        this.count = countPartialAggregate.count;
        this.datatype = countPartialAggregate.datatype;
    }

    /**
     * Add the given value to the aggregate.
     * 
     * @param value
     *            The value to add
     */
    public final void add(final double probability) {
        this.count += probability;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return The count value
     */
    public final double getCount() {
        return this.count;
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("CountPartialAggregate (").append(this.hashCode()).append(")").append(this.count);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final CountPartialAggregate<T> clone() {
        return new CountPartialAggregate<T>(this);
    }

}
