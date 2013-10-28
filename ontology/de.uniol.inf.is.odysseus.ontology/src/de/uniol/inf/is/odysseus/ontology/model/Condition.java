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
package de.uniol.inf.is.odysseus.ontology.model;

import java.net.URI;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Condition {
    private final URI uri;
    private final SDFAttribute attribute;
    private final Interval interval;
    private String unit;

    /**
     * Class constructor.
     * 
     * @param name
     * @param attribute
     * @param interval
     */
    public Condition(final URI uri, final SDFAttribute attribute, final Interval interval) {
        super();
        this.uri = uri;
        this.attribute = attribute;
        this.interval = interval;
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return this.uri;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.uri.getFragment();
    }

    /**
     * @return the attribute
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * @return the interval
     */
    public Interval getInterval() {
        return this.interval;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(final String unit) {
        this.unit = unit;
    }

}
