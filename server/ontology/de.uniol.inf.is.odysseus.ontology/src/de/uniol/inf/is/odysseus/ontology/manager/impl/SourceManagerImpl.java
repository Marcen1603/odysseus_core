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
package de.uniol.inf.is.odysseus.ontology.manager.impl;

import java.net.URI;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.uniol.inf.is.odysseus.ontology.manager.SourceManager;
import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.model.condition.IntervalCondition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
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

        final Individual thisFeatureOfInterest = this.createFeatureOfInterest(featureOfInterest.getUri(), featureOfInterest.getName());
        for (final Property property : featureOfInterest.getHasProperties()) {
            final Individual thisProperty = this.createProperty(property.getUri(), property.getName());
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

        this.deleteFeatureOfInterest(featureOfInterest.getUri());

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
            final Individual thisSensingDevice = this.createSensingDevice(sensingDevice.getUri(), sensingDevice.getName());
            if (!sensingDevice.getHasMeasurementCapabilities().isEmpty()) {
                for (final MeasurementCapability capability : sensingDevice.getHasMeasurementCapabilities()) {
                    this.addMeasurementCapabilityToSensingDevice(thisSensingDevice, capability);
                    this.addPropertyToSensingDevice(thisSensingDevice, capability.getForProperty());
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

        this.deleteSensingDevice(sensingDevice.getUri());

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
    }

    private void addMeasurementCapabilityToSensingDevice(final Individual sensingDevice, final MeasurementCapability measurementCapability) {
        final Individual thisMeasurementCapability = this.createMeasurementCapability(measurementCapability);

        if (!measurementCapability.getInConditions().isEmpty()) {
            for (final Condition condition : measurementCapability.getInConditions()) {
                this.addConditionToMeasurementCapability(thisMeasurementCapability, condition);
            }
        }
        if (!measurementCapability.getHasMeasurementProperties().isEmpty()) {
            for (final MeasurementProperty measurementProperty : measurementCapability.getHasMeasurementProperties()) {
                this.addMeasurementPropertyToMeasurementCapability(thisMeasurementCapability, measurementProperty);
            }
        }
        this.getABox().add(sensingDevice, SSN.hasMeasurementCapability, thisMeasurementCapability);
    }

    private void addMeasurementPropertyToMeasurementCapability(final Individual measurementCapability, final MeasurementProperty property) {
        final Individual thisMeasurementProperty = this.createMeasurementProperty(property);
        this.addMeasurementPropertyToMeasurementCapability(measurementCapability, thisMeasurementProperty);
    }

    private void addConditionToMeasurementCapability(final Individual measurementCapability, final Condition condition) {
        final Individual thisCondition = this.createCondition(condition);
        this.addConditionToMeasurementCapability(measurementCapability, thisCondition);
    }

    private void addPropertyToSensingDevice(final Individual sensingDevice, final Property property) {
        this.getABox().createObjectProperty(SSN.observes.getURI());
        final Individual thisProperty = this.createProperty(property.getUri(), property.getName());
        this.getABox().add(sensingDevice, SSN.observes, thisProperty);
    }

    /**
     * @param featureOfInterest
     * @param property
     */
    private void addPropertyToFeatureOfInterest(final Individual featureOfInterest, final Individual property) {
        this.getABox().createObjectProperty(SSN.isPropertyOf.getURI());
        this.getABox().createObjectProperty(SSN.hasProperty.getURI());

        this.getABox().add(featureOfInterest, SSN.hasProperty, property);
        this.getABox().add(property, SSN.isPropertyOf, featureOfInterest);
    }

    private Individual createMeasurementCapability(final MeasurementCapability measurementCapability) {
        this.getABox().createObjectProperty(SSN.forProperty.getURI());
        final Individual thisMeasurementCapability = this.createMeasurementCapability(measurementCapability.getUri(), measurementCapability.getName());

        final Individual property = this.createProperty(measurementCapability.getForProperty().getUri(), measurementCapability.getForProperty().getName());
        this.getABox().add(thisMeasurementCapability, SSN.forProperty, property);
        return thisMeasurementCapability;
    }

    private Individual createMeasurementProperty(final MeasurementProperty measurementProperty) {
        final Individual thisMeasurementProperty = this.createMeasurementProperty(measurementProperty.getUri(), measurementProperty.getResource());

        // FIXME Implement expression handling and storing
        this.getABox().add(thisMeasurementProperty, DUL.hasDataValue, measurementProperty.toString(), TypeMapper.getInstance().getTypeByValue(measurementProperty.toString()));

        this.getABox().add(thisMeasurementProperty, RDFS.subClassOf, measurementProperty.getResource());
        return thisMeasurementProperty;
    }

    private void addMeasurementPropertyToMeasurementCapability(final Individual measurementCapability, final Individual measurementProperty) {
        this.getABox().createObjectProperty(SSN.hasMeasurementProperty.getURI());
        this.getABox().add(measurementCapability, SSN.hasMeasurementProperty, measurementProperty);
    }

    private Individual createCondition(final Condition condition) {
        Individual thisCondition;
        if (condition instanceof ExpressionCondition) {
            thisCondition = this.createExpressionCondition((ExpressionCondition) condition);
        } else if (condition instanceof IntervalCondition) {
            thisCondition = this.createIntervalCondition((IntervalCondition) condition);
        } else {
            thisCondition = this.createCondition(condition.getUri());
        }
        final Individual property = this.createProperty(condition.getOnProperty().getUri(), condition.getOnProperty().getName());
        this.getABox().add(thisCondition, RDFS.subClassOf, property);
        return thisCondition;
    }

    private Individual createIntervalCondition(final IntervalCondition condition) {
        this.getABox().createObjectProperty(SSN.hasValue.getURI());

        final Individual thisCondition = this.createCondition(condition.getUri());
        final Individual interval = this.createInterval(URI.create(condition.getUri() + "/interval"), condition.getInterval(), condition.getUnit());

        this.getABox().add(thisCondition, SSN.hasValue, interval);
        return thisCondition;
    }

    private Individual createExpressionCondition(final ExpressionCondition condition) {
        this.getABox().createObjectProperty(SSN.hasValue.getURI());

        final Individual thisCondition = this.createCondition(condition.getUri());
        final Individual expression = this.createExpression(URI.create(condition.getUri() + "/interval"), condition.getExpression(), condition.getUnit());

        this.getABox().add(thisCondition, SSN.hasValue, expression);

        return thisCondition;
    }

    private Individual createExpression(final URI uri, final String expression, final String unit) {
        this.getABox().createClass(DUL.Region.getURI());
        this.getABox().createClass(DUL.Amount.getURI());
        this.getABox().createObjectProperty(DUL.hasDataValue.getURI());
        this.getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        this.getABox().createObjectProperty(ODYSSEUS.hasMinValue.getURI());
        this.getABox().createObjectProperty(ODYSSEUS.hasMaxValue.getURI());

        final Individual thisExpression = this.getABox().createIndividual(uri.toString(), DUL.Region);

        final Individual expressionValue = this.getABox().createIndividual(uri.toString() + "/expression", DUL.Amount);
        this.getABox().add(expressionValue, DUL.hasDataValue, expression, TypeMapper.getInstance().getTypeByValue(expression));

        if (unit != null) {
            this.getABox().add(thisExpression, DUL.isClassifiedBy, unit);
            this.getABox().add(thisExpression, DUL.isClassifiedBy, unit);
        }
        this.getABox().add(thisExpression, ODYSSEUS.hasExpression, expressionValue);

        return thisExpression;
    }

    private Individual createInterval(final URI uri, final Interval interval, final String unit) {
        this.getABox().createClass(DUL.Region.getURI());
        this.getABox().createClass(DUL.Amount.getURI());
        this.getABox().createObjectProperty(DUL.hasDataValue.getURI());
        this.getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        this.getABox().createObjectProperty(ODYSSEUS.hasMinValue.getURI());
        this.getABox().createObjectProperty(ODYSSEUS.hasMaxValue.getURI());

        final Individual thisInterval = this.getABox().createIndividual(uri.toString(), DUL.Region);

        final Individual minValue = this.getABox().createIndividual(uri.toString() + "/inf", DUL.Amount);
        this.getABox().add(minValue, DUL.hasDataValue, new Double(interval.inf()).toString(), TypeMapper.getInstance().getTypeByValue(interval.inf()));

        final Individual maxValue = this.getABox().createIndividual(uri.toString() + "/sup", DUL.Amount);
        this.getABox().add(maxValue, DUL.hasDataValue, new Double(interval.sup()).toString(), TypeMapper.getInstance().getTypeByValue(interval.sup()));

        if (unit != null) {
            this.getABox().add(minValue, DUL.isClassifiedBy, unit);
            this.getABox().add(maxValue, DUL.isClassifiedBy, unit);
        }

        this.getABox().add(thisInterval, ODYSSEUS.hasMinValue, minValue);
        this.getABox().add(thisInterval, ODYSSEUS.hasMaxValue, maxValue);

        return thisInterval;
    }

    private void addConditionToMeasurementCapability(final Individual measurementCapability, final Individual condition) {
        this.getABox().createObjectProperty(SSN.inCondition.getURI());
        this.getABox().add(measurementCapability, SSN.inCondition, condition);
    }

    /**
     * Create a new measurement property with the given URI.
     * 
     * @param uri
     *            The URI.
     * @param resource
     * @return The measurement property individual.
     */
    private Individual createMeasurementProperty(final URI uri, final Resource resource) {
        this.getABox().createClass(resource.getURI());
        final Individual measurementProperty = this.getABox().createIndividual(uri.toString(), this.getABox().getOntClass(resource.toString()));
        return measurementProperty;
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
        final Individual measurementCapability = this.getABox().createIndividual(uri.toString(), SSN.MeasurementCapability);
        this.getABox().add(measurementCapability, RDFS.label, name);
        return measurementCapability;
    }

    /**
     * Create a new condition with the given URI.
     * 
     * @param uri
     *            The URI.
     * @return The condition individual.
     */
    private Individual createCondition(final URI uri) {
        this.getABox().createClass(SSN.Condition.getURI());
        final Individual condition = this.getABox().createIndividual(uri.toString(), SSN.Condition);
        return condition;
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
        // TODO Implement delete sensing device
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
     * Gets the A box.
     * 
     * @return The A box.
     */
    private OntModel getABox() {
        return this.aBox;
    }
}
