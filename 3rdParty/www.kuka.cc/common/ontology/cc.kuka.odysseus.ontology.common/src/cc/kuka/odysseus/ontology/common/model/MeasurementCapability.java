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
import java.util.Objects;

import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MeasurementCapability extends Property {
	private final String name;
	private final Property property;
	private final List<Condition> conditions = new ArrayList<>();
	private final List<MeasurementProperty> measurementProperties = new ArrayList<>();

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
	public MeasurementCapability(final URI uri, final String name, final Property property) {
		super(uri);
		Objects.requireNonNull(name, "Name can't be null");
		Objects.requireNonNull(property, "Property can't be null");
		this.name = name;
		this.property = property;
	}

	/**
	 * @return the name
	 */
	@Override
	public String name() {
		return this.name;
	}

	/**
	 * @return the forProperty
	 */
	public Property forProperty() {
		return this.property;
	}

	/**
	 * @return the inConditions
	 */
	public List<Condition> inConditions() {
		return Collections.unmodifiableList(this.conditions);
	}

	/**
	 *
	 * @param condition
	 */
	public MeasurementCapability add(final Condition condition) {
		this.conditions.add(condition);
		return this;
	}

	/**
	 *
	 * @param condition
	 */
	public MeasurementCapability remove(final Condition condition) {
		this.conditions.remove(condition);
		return this;
	}

	/**
	 * @return the hasMeasurementProperties
	 */
	public List<MeasurementProperty> hasMeasurementProperties() {
		return Collections.unmodifiableList(this.measurementProperties);
	}

	public List<MeasurementProperty> hasMeasurementProperty(final String property) {
		// FIXME Check enum for possible values
		final SSNMeasurementProperty ssnMeasurementProperty = SSNMeasurementProperty.valueOf(property);
		final List<MeasurementProperty> measurementProperties = new ArrayList<>();
		for (final MeasurementProperty measurementProperty : this.measurementProperties) {
			if (measurementProperty.resource().equals(ssnMeasurementProperty.resource())) {
				measurementProperties.add(measurementProperty);
			}
		}
		return measurementProperties;
	}

	/**
	 *
	 * @param measurementProperty
	 */
	public MeasurementCapability add(final MeasurementProperty measurementProperty) {
		this.measurementProperties.add(measurementProperty);
		return this;
	}

	/**
	 *
	 * @param measurementProperty
	 */
	public MeasurementCapability remove(final MeasurementProperty measurementProperty) {
		this.measurementProperties.remove(measurementProperty);
		return this;
	}
}
