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
package de.uniol.inf.is.odysseus.ontology.common.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.ontology.common.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.common.model.property.MeasurementProperty;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapability extends Property {
    private final String name;
    private final Property forProperty;
    private final List<Condition> inConditions = new ArrayList<Condition>();
    private final List<MeasurementProperty> hasMeasurementProperties = new ArrayList<MeasurementProperty>();

    /**
     * Class constructor.
     * 
     * @param uri
     */
    public MeasurementCapability(final URI uri, final Property forProperty) {
        this(uri, uri.getFragment(), forProperty);
    }

    /**
     * Class constructor.
     * 
     * @param uri
     */
    public MeasurementCapability(final URI uri, final String name, final Property forProperty) {
        super(uri);
        this.name = name;
        this.forProperty = forProperty;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return the forProperty
     */
    public Property getForProperty() {
        return this.forProperty;
    }

    /**
     * @return the inConditions
     */
    public List<Condition> getInConditions() {
        return Collections.unmodifiableList(this.inConditions);
    }

    /**
     * 
     * @param inCondition
     */
    public void addCondition(final Condition inCondition) {
        this.inConditions.add(inCondition);
    }

    /**
     * 
     * @param inCondition
     */
    public void removeCondition(final Condition inCondition) {
        this.inConditions.remove(inCondition);
    }

    /**
     * @return the hasMeasurementProperties
     */
    public List<MeasurementProperty> getHasMeasurementProperties() {
        return Collections.unmodifiableList(this.hasMeasurementProperties);
    }

    public List<MeasurementProperty> getHasMeasurementProperty(final String property) {
        final SSNMeasurementProperty ssnMeasurementProperty = SSNMeasurementProperty.valueOf(property);
        final List<MeasurementProperty> measurementProperties = new ArrayList<MeasurementProperty>();
        for (final MeasurementProperty measurementProperty : this.hasMeasurementProperties) {
            if (measurementProperty.getResource().equals(ssnMeasurementProperty.getResource())) {
                measurementProperties.add(measurementProperty);
            }
        }
        return measurementProperties;
    }

    /**
     * 
     * @param hasMeasurementProperty
     */
    public void addMeasurementProperty(final MeasurementProperty hasMeasurementProperty) {
        this.hasMeasurementProperties.add(hasMeasurementProperty);
    }

    /**
     * 
     * @param hasMeasurementProperty
     */
    public void removeMeasurementProperty(final MeasurementProperty hasMeasurementProperty) {
        this.hasMeasurementProperties.remove(hasMeasurementProperty);
    }
}
