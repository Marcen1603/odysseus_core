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
package de.uniol.inf.is.odysseus.ontology.model.condition;

import java.net.URI;

import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class IntervalCondition extends Condition implements ICondition {
    /** The interval. */
    private final Interval interval;

    /**
     * Class constructor.
     * 
     * @param uri
     *            The URI
     * @param onProperty
     *            The property
     * @param interval
     *            The interval
     */
    public IntervalCondition(final URI uri, final Property onProperty, final Interval interval) {
        super(uri, onProperty);
        this.interval = interval;
    }

    /**
     * @return the interval
     */
    public Interval getInterval() {
        return this.interval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append("%s");
        sb.append(" >= ");
        sb.append(this.getInterval().inf());
        sb.append(") AND (");
        sb.append("%s");
        sb.append(" <= ");
        sb.append(this.getInterval().sup());
        sb.append(")");
        return sb.toString();
    }
}
