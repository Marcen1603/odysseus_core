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
package de.uniol.inf.is.odysseus.probabilistic.sensor.model;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapability {
    private final String name;
    private final SDFAttribute attribute;
    private final List<Condition> conditions = new ArrayList<Condition>();
    private final List<MeasurementProperty> measurementProperties = new ArrayList<MeasurementProperty>();

    /**
     * Class constructor.
     * 
     * @param name
     * @param attribute
     */
    public MeasurementCapability(String name, SDFAttribute attribute) {
        super();
        this.name = name;
        this.attribute = attribute;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the attribute
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * @return the conditions
     */
    public List<Condition> getConditions() {
        return this.conditions;
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public void removeCondition(Condition condition) {
        this.conditions.remove(condition);
    }

    /**
     * @return the measurementProperties
     */
    public List<MeasurementProperty> getMeasurementProperties() {
        return this.measurementProperties;
    }

    public void addMeasurementProperty(MeasurementProperty measurementProperty) {
        this.measurementProperties.add(measurementProperty);
    }

    public void removeMeasurementProperty(MeasurementProperty measurementProperty) {
        this.measurementProperties.remove(measurementProperty);
    }
}
