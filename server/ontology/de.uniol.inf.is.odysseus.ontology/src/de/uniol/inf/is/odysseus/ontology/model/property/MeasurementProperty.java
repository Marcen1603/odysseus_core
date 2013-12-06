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
package de.uniol.inf.is.odysseus.ontology.model.property;

import java.net.URI;
import java.util.Objects;

import com.hp.hpl.jena.rdf.model.Resource;

import de.uniol.inf.is.odysseus.ontology.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementProperty extends Property implements IMeasurementProperty {
    /** The unit. */
    private String unit;
    /** The expression of the proeprty. */
    private final String expression;
    /** The SSN resource. */
    private final Resource resource;

    /**
     * Class constructor.
     * 
     * @param uri
     *            The URI
     * @param resource
     *            the SSN resource
     * @param expression
     *            The expression
     * 
     */
    public MeasurementProperty(final URI uri, final Resource resource, final String expression) {
        super(uri);
        Objects.requireNonNull(resource);
        Objects.requireNonNull(expression);
        this.resource = resource;
        this.expression = expression;
    }

    /**
     * @param unit
     *            the unit to set
     */
    @Override
    public void setUnit(final String unit) {
        this.unit = unit;
    }

    /**
     * @return the unit
     */
    @Override
    public String getUnit() {
        return this.unit;
    }

    /**
     * @return the resource
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        return this.getExpression();
    }
}
