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
package cc.kuka.odysseus.ontology.storage.manager.impl;

import java.net.URI;

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.storage.manager.SourceManager;
import cc.kuka.odysseus.ontology.storage.vocabulary.DUL;
import cc.kuka.odysseus.ontology.storage.vocabulary.SSN;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */

public class SourceManagerImpl implements SourceManager {
	private final OntModel aBox;

	/**
	 * Class constructor.
	 *
	 */
	public SourceManagerImpl(final OntModel aBox) {
		this.aBox = aBox;
	}

	@Override
	public void createFeatureOfInterest(final FeatureOfInterest featureOfInterest) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		final Individual thisFeatureOfInterest = this.createFeatureOfInterest(featureOfInterest.uri(),
				featureOfInterest.name());
		for (final Property property : featureOfInterest.hasProperties()) {
			final Individual thisProperty = this.createProperty(property.uri(), property.name());
			this.addPropertyToFeatureOfInterest(thisFeatureOfInterest, thisProperty);
		}

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	public void deleteFeatureOfInterest(final FeatureOfInterest featureOfInterest) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		this.deleteFeatureOfInterest(featureOfInterest.uri());

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	@Override
	public void createProperty(final FeatureOfInterest featureOfInterest, final Property property) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		final Individual thisFeatureOfInterest = this.createFeatureOfInterest(featureOfInterest.uri(),
				featureOfInterest.name());
		this.addPropertyToFeatureOfInterest(thisFeatureOfInterest, property);

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	public void deleteProperty(final Property property) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		this.deleteProperty(property.uri());

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	@Override
	public void createSensingDevice(final SensingDevice sensingDevice) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}
		try {
			final Individual thisSensingDevice = this.createSensingDevice(sensingDevice.uri(), sensingDevice.name());
			if (!sensingDevice.hasMeasurementCapabilities().isEmpty()) {
				for (final MeasurementCapability capability : sensingDevice.hasMeasurementCapabilities()) {
					this.addMeasurementCapabilityToSensingDevice(thisSensingDevice, capability);
					this.addPropertyToSensingDevice(thisSensingDevice, capability.forProperty());
				}
			}
			if (this.getABox().supportsTransactions()) {
				this.getABox().commit();
			}
		} catch (final Exception e) {
			if (this.getABox().supportsTransactions()) {
				this.getABox().abort();
			}
			throw e;
		}
	}

	public void deleteSensingDevice(final SensingDevice sensingDevice) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		this.deleteSensingDevice(sensingDevice.uri());

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	@Override
	public void createMeasurementCapability(final SensingDevice sensingDevice,
			final MeasurementCapability measurementCapability) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}
		try {
			final Individual thisSensingDevice = this.createSensingDevice(sensingDevice.uri(), sensingDevice.name());
			this.addMeasurementCapabilityToSensingDevice(thisSensingDevice, measurementCapability);
			if (this.getABox().supportsTransactions()) {
				this.getABox().commit();
			}
		} catch (final Exception e) {
			if (this.getABox().supportsTransactions()) {
				this.getABox().abort();
			}
			throw e;
		}
	}

	public void deleteMeasurementCapability(final MeasurementCapability measurementCapability) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		this.deleteMeasurementCapability(measurementCapability.uri());

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	@Override
	public void createCondition(final MeasurementCapability measurementCapability, final Condition condition) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}
		try {
			final Individual thisMeasurementCapability = this.createMeasurementCapability(measurementCapability.uri(),
					measurementCapability.name());
			this.addConditionToMeasurementCapability(thisMeasurementCapability, condition);
			if (this.getABox().supportsTransactions()) {
				this.getABox().commit();
			}
		} catch (final Exception e) {
			if (this.getABox().supportsTransactions()) {
				this.getABox().abort();
			}
			throw e;
		}
	}

	public void deleteCondition(final Condition condition) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		this.deleteCondition(condition.uri());

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	@Override
	public void createMeasurementProperty(final MeasurementCapability measurementCapability,
			final MeasurementProperty measurementProperty) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}
		try {
			final Individual thisMeasurementCapability = this.createMeasurementCapability(measurementCapability.uri(),
					measurementCapability.name());
			this.addMeasurementPropertyToMeasurementCapability(thisMeasurementCapability, measurementProperty);
			if (this.getABox().supportsTransactions()) {
				this.getABox().commit();
			}
		} catch (final Exception e) {
			if (this.getABox().supportsTransactions()) {
				this.getABox().abort();
			}
			throw e;
		}
	}

	public void deleteMeasurementProperty(final MeasurementProperty measurementProperty) {
		if (this.getABox().supportsTransactions()) {
			this.getABox().begin();
		}

		this.deleteMeasurementProperty(measurementProperty.uri());

		if (this.getABox().supportsTransactions()) {
			this.getABox().commit();
		}
	}

	// Private methods
	// -----------------------------------------------------------------------------------------------------------------

	private void addMeasurementCapabilityToSensingDevice(final Individual sensingDevice,
			final MeasurementCapability measurementCapability) {
		final Individual thisMeasurementCapability = this.createMeasurementCapability(measurementCapability.uri(),
				measurementCapability.name());
		final Individual thisProperty = this.createProperty(measurementCapability.forProperty().uri(),
				measurementCapability.forProperty().name());
		this.addPropertyToMeasurementCapability(thisMeasurementCapability, thisProperty);

		if (!measurementCapability.inConditions().isEmpty()) {
			for (final Condition condition : measurementCapability.inConditions()) {
				this.addConditionToMeasurementCapability(thisMeasurementCapability, condition);
			}
		}
		if (!measurementCapability.hasMeasurementProperties().isEmpty()) {
			for (final MeasurementProperty measurementProperty : measurementCapability.hasMeasurementProperties()) {
				this.addMeasurementPropertyToMeasurementCapability(thisMeasurementCapability, measurementProperty);
			}
		}
		this.addMeasurementCapabilityToSensingDevice(sensingDevice, thisMeasurementCapability);
	}

	private void addMeasurementPropertyToMeasurementCapability(final Individual measurementCapability,
			final MeasurementProperty measurementProperty) {
		final Individual thisMeasurementProperty = this.createMeasurementProperty(measurementProperty.uri(),
				measurementProperty.name(), measurementProperty.resource(), measurementProperty.expression());
		this.addMeasurementPropertyToMeasurementCapability(measurementCapability, thisMeasurementProperty);
	}

	private void addConditionToMeasurementCapability(final Individual measurementCapability,
			final Condition condition) {
		final Individual property = this.createProperty(condition.onProperty().uri(), condition.onProperty().name());
		final Individual thisCondition = this.createCondition(condition.uri(), condition.name(), property,
				condition.expression());
		this.addConditionToMeasurementCapability(measurementCapability, thisCondition);
	}

	private void addPropertyToSensingDevice(final Individual sensingDevice, final Property property) {
		final Individual thisProperty = this.createProperty(property.uri(), property.name());
		this.addPropertyToSensingDevice(sensingDevice, thisProperty);
	}

	private void addPropertyToFeatureOfInterest(final Individual featureOfInterest, final Property property) {
		final Individual thisProperty = this.createProperty(property.uri(), property.name());
		this.addPropertyToFeatureOfInterest(featureOfInterest, thisProperty);
	}

	// Basic add methods
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 *
	 * @param featureOfInterest
	 * @param property
	 */
	private void addPropertyToFeatureOfInterest(final Individual featureOfInterest, final Individual property) {
		this.getABox().createObjectProperty(SSN.isPropertyOf.getURI());
		this.getABox().createObjectProperty(SSN.hasProperty.getURI());

		this.getABox().add(featureOfInterest, SSN.hasProperty, property);
		this.getABox().add(property, SSN.isPropertyOf, featureOfInterest);
	}

	/**
	 *
	 * @param sensingDevice
	 * @param property
	 */
	private void addPropertyToSensingDevice(final Individual sensingDevice, final Individual property) {
		this.getABox().createObjectProperty(SSN.observes.getURI());
		this.getABox().add(sensingDevice, SSN.observes, property);
	}

	/**
	 *
	 * @param sensingDevice
	 * @param measurementCapability
	 */
	private void addMeasurementCapabilityToSensingDevice(final Individual sensingDevice,
			final Individual measurementCapability) {
		this.getABox().createObjectProperty(SSN.hasMeasurementCapability.getURI());
		this.getABox().add(sensingDevice, SSN.hasMeasurementCapability, measurementCapability);
	}

	/**
	 *
	 * @param measurementCapability
	 * @param measurementProperty
	 */
	private void addMeasurementPropertyToMeasurementCapability(final Individual measurementCapability,
			final Individual measurementProperty) {
		this.getABox().createObjectProperty(SSN.hasMeasurementProperty.getURI());
		this.getABox().add(measurementCapability, SSN.hasMeasurementProperty, measurementProperty);
	}

	/**
	 *
	 * @param measurementCapability
	 * @param property
	 */
	private void addPropertyToMeasurementCapability(final Individual measurementCapability, final Individual property) {
		this.getABox().createObjectProperty(SSN.forProperty.getURI());
		this.getABox().add(measurementCapability, SSN.forProperty, property);
	}

	/**
	 *
	 * @param measurementCapability
	 * @param condition
	 */
	private void addConditionToMeasurementCapability(final Individual measurementCapability,
			final Individual condition) {
		this.getABox().createObjectProperty(SSN.inCondition.getURI());
		this.getABox().add(measurementCapability, SSN.inCondition, condition);
	}

	// Basic create methods
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Create a new measurement property with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 * @param resource
	 * @return The measurement property individual.
	 */
	private Individual createMeasurementProperty(final URI uri, final String name, final String resource,
			final String expression) {
		this.getABox().createClass(resource);
		this.getABox().createObjectProperty(DUL.hasDataValue.getURI());
		final Individual thisResource = this.getABox().getOntClass(resource).asIndividual();
		final Individual measurementProperty = this.getABox().createIndividual(uri.toString(),
				this.getABox().getOntClass(resource.toString()));

		this.getABox().add(measurementProperty, DUL.hasDataValue, expression,
				TypeMapper.getInstance().getTypeByValue(expression));
		this.getABox().add(measurementProperty, RDFS.subClassOf, thisResource);
		this.getABox().add(measurementProperty, RDFS.label, name);

		return measurementProperty;
	}

	/**
	 * Delete a measurement property with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 */
	private void deleteMeasurementProperty(final URI uri) {
		// TODO Implement delete measurement property.
		throw new IllegalArgumentException("Not implemented yet");
	}

	/**
	 * Create a new measurement capability with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 * @return The measurement capability individual.
	 */
	private Individual createMeasurementCapability(final URI uri, final String name) {
		this.getABox().createClass(SSN.MeasurementCapability.getURI());
		final Individual measurementCapability = this.getABox().createIndividual(uri.toString(),
				SSN.MeasurementCapability);
		this.getABox().add(measurementCapability, RDFS.label, name);
		return measurementCapability;
	}

	/**
	 * Delete a measurement capability with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 */
	private void deleteMeasurementCapability(final URI uri) {
		// TODO Implement delete measurement capability.
		throw new IllegalArgumentException("Not implemented yet");
	}

	/**
	 * Create a new condition with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 * @return The condition individual.
	 */
	private Individual createCondition(final URI uri, final String name, final Individual property,
			final String expression) {
		this.getABox().createClass(SSN.Condition.getURI());
		this.getABox().createObjectProperty(DUL.hasDataValue.getURI());

		final Individual condition = this.getABox().createIndividual(uri.toString(), SSN.Condition);
		this.getABox().add(condition, DUL.hasDataValue, expression,
				TypeMapper.getInstance().getTypeByValue(expression));
		this.getABox().add(condition, RDFS.subClassOf, property);
		this.getABox().add(condition, RDFS.label, name);

		return condition;
	}

	/**
	 * Delete a condition with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 */
	private void deleteCondition(final URI uri) {
		// TODO Implement delete condition.
		throw new IllegalArgumentException("Not implemented yet");
	}

	/**
	 * Create a new feature of interest with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 * @return The feature of interest individual.
	 */
	private Individual createFeatureOfInterest(final URI uri, final String name) {
		this.getABox().createClass(SSN.FeatureOfInterest.getURI());
		final Individual featureOfInterest = this.getABox().createIndividual(uri.toString(), SSN.FeatureOfInterest);
		this.getABox().add(featureOfInterest, RDFS.label, name);
		return featureOfInterest;
	}

	/**
	 * Delete a feature of interest with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 */
	private void deleteFeatureOfInterest(final URI uri) {
		// TODO Implement delete feature of interest.
		throw new IllegalArgumentException("Not implemented yet");
	}

	/**
	 * Create a new sensing device with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 * @return The sensing device individual.
	 */
	private Individual createSensingDevice(final URI uri, final String name) {
		this.getABox().createClass(SSN.SensingDevice.getURI());
		final Individual sensingDevice = this.getABox().createIndividual(uri.toString(), SSN.SensingDevice);
		this.getABox().add(sensingDevice, RDFS.label, name);
		return sensingDevice;
	}

	/**
	 * Delete a sensing device with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 */
	private void deleteSensingDevice(final URI uri) {
		// TODO Implement delete sensing device.
		throw new IllegalArgumentException("Not implemented yet");
	}

	/**
	 * Create a new property with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 * @return The property individual.
	 */
	private Individual createProperty(final URI uri, final String name) {
		this.getABox().createClass(SSN.Property.getURI());
		final Individual property = this.getABox().createIndividual(uri.toString(), SSN.Property);
		this.getABox().add(property, RDFS.label, name);
		return property;
	}

	/**
	 * Delete a property with the given URI.
	 *
	 * @param uri
	 *            The URI.
	 */
	private void deleteProperty(final URI uri) {
		// TODO Implement delete property.
		throw new IllegalArgumentException("Not implemented yet");
	}

	/**
	 * Gets the A box.
	 *
	 * @return The A box.
	 */
	private OntModel getABox() {
		return this.aBox;
	}
}
