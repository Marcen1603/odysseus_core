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

import com.hp.hpl.jena.rdf.model.Resource;

import de.uniol.inf.is.odysseus.ontology.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementProperty extends Property implements IMeasurementProperty {
    private String unit;
    private String expression;
    private final Resource resource;

    /**
     * Class constructor.
     * 
     */
    public MeasurementProperty(URI uri, Resource resource) {
        super(uri);
        this.resource = resource;
    }

    /**
     * @param unit
     *            the unit to set
     */
    @Override
    public void setUnit(String unit) {
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
}
