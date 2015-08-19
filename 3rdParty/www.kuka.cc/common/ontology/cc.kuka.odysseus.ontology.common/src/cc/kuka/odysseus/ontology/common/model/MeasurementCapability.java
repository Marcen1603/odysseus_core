/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.common.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MeasurementCapability extends Property {
    private final String name;
    private final Property forProperty;
    private final List<Condition> inConditions = new ArrayList<>();
    private final List<MeasurementProperty> hasMeasurementProperties = new ArrayList<>();

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
        final List<MeasurementProperty> measurementProperties = new ArrayList<>();
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
