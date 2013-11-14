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
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SSNMeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.model.condition.IntervalCondition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings("unused")
public class SourceManagerImpl {
    private final OntModel aBox;

    /**
     * Class constructor.
     * 
     */
    public SourceManagerImpl(final OntModel aBox) {
        this.aBox = aBox;
    }

    public void createFeatureOfInterest(FeatureOfInterest featureOfInterest) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        final Individual thisFeatureOfInterest = this.createFeatureOfInterest(featureOfInterest.getUri());
        for (final Property property : featureOfInterest.getHasProperties()) {
            final Individual thisProperty = this.createProperty(property.getUri());
            this.addPropertyToFeatureOfInterest(thisFeatureOfInterest, thisProperty);
        }

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
    }

    public void deleteFeatureOfInterest(FeatureOfInterest featureOfInterest) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        this.deleteFeatureOfInterest(featureOfInterest.getUri());

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
    }

    public void createSensingDevice(final SensingDevice sensingDevice) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        final Individual thisSensingDevice = this.createSensingDevice(sensingDevice.getUri());
        final List<Individual> properties = new ArrayList<Individual>();
        if (!sensingDevice.getHasMeasurementCapabilities().isEmpty()) {
            for (final MeasurementCapability capability : sensingDevice.getHasMeasurementCapabilities()) {
                this.addMeasurementCapabilityToSensingDevice(thisSensingDevice, capability);
            }
        }

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
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
            for (MeasurementProperty measurementProperty : measurementCapability.getHasMeasurementProperties()) {
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

    private void addPropertyToSensingDevice(final Individual sensingDevice, final Individual property) {
        this.getABox().createObjectProperty(SSN.observes.getURI());
        this.getABox().add(sensingDevice, SSN.observes, property);
    }

    /**
     * @param featureOfInterest
     * @param property
     */
    private void addPropertyToFeatureOfInterest(Individual featureOfInterest, Individual property) {
        this.getABox().createObjectProperty(SSN.isPropertyOf.getURI());
        this.getABox().createObjectProperty(SSN.hasProperty.getURI());

        this.getABox().add(featureOfInterest, SSN.hasProperty, property);
        this.getABox().add(property, SSN.isPropertyOf, featureOfInterest);
    }

    private Individual createMeasurementCapability(MeasurementCapability measurementCapability) {
        this.getABox().createObjectProperty(SSN.forProperty.getURI());
        Individual thisMeasurementCapability = this.createMeasurementCapability(measurementCapability.getUri());
        Individual property = this.createProperty(measurementCapability.getForProperty().getUri());

        this.getABox().add(thisMeasurementCapability, SSN.forProperty, property);
        return thisMeasurementCapability;
    }

    private void addMeasurementCapabilityToSensingDevice(final Individual sensingDevice, final Individual measurementCapability) {
        this.getABox().createObjectProperty(SSN.hasMeasurementCapability.getURI());
        this.getABox().add(sensingDevice, SSN.hasMeasurementCapability, measurementCapability);
    }

    private Individual createMeasurementProperty(MeasurementProperty measurementProperty) {
        Individual thisMeasurementProperty = this.createMeasurementProperty(measurementProperty.getUri());

        // FIXME Implement expression handling and storing
        this.getABox().add(thisMeasurementProperty, DUL.hasDataValue, measurementProperty.toString(), TypeMapper.getInstance().getTypeByValue(measurementProperty.toString()));

        this.getABox().add(thisMeasurementProperty, RDFS.subClassOf, SSNMeasurementProperty.valueOf(measurementProperty.getClass().getCanonicalName()).getResource());
        return thisMeasurementProperty;
    }

    private void addMeasurementPropertyToMeasurementCapability(final Individual measurementCapability, final Individual measurementProperty) {
        this.getABox().createObjectProperty(SSN.hasMeasurementProperty.getURI());
        this.getABox().add(measurementCapability, SSN.hasMeasurementProperty, measurementProperty);
    }

    private Individual createCondition(Condition condition) {
        Individual thisCondition;
        if (condition instanceof ExpressionCondition) {
            thisCondition = createExpressionCondition((ExpressionCondition) condition);
        }
        else if (condition instanceof IntervalCondition) {
            thisCondition = createIntervalCondition((IntervalCondition) condition);
        }
        else {
            thisCondition = this.createCondition(condition.getUri());
        }
        Individual property = this.createProperty(condition.getOnProperty().getUri());
        this.getABox().add(thisCondition, RDFS.subClassOf, property);
        return thisCondition;
    }

    private Individual createIntervalCondition(IntervalCondition condition) {
        this.getABox().createObjectProperty(SSN.hasValue.getURI());

        Individual thisCondition = this.createCondition(condition.getUri());
        Individual interval = this.createInterval(URI.create(condition.getUri() + "/interval"), condition.getInterval(), condition.getUnit());

        this.getABox().add(thisCondition, SSN.hasValue, interval);
        return thisCondition;
    }

    private Individual createExpressionCondition(ExpressionCondition condition) {
        this.getABox().createObjectProperty(SSN.hasValue.getURI());

        Individual thisCondition = this.createCondition(condition.getUri());
        Individual expression = this.createExpression(URI.create(condition.getUri() + "/interval"), condition.getExpression(), condition.getUnit());

        this.getABox().add(thisCondition, SSN.hasValue, expression);

        return thisCondition;
    }

    private Individual createExpression(URI uri, String expression, String unit) {
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

    private Individual createInterval(URI uri, Interval interval, String unit) {
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
     * @return The measurement property individual.
     */
    private Individual createMeasurementProperty(URI uri) {
        this.getABox().createClass(SSN.MeasurementProperty.getURI());
        final Individual measurementProperty = this.getABox().createIndividual(uri.toString(), SSN.MeasurementProperty);
        return measurementProperty;
    }

    /**
     * Create a new measurement property with the given name under the given
     * namespace.
     * 
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The measurement property individual.
     */
    private Individual createMeasurementPropertyNS(final String name, final String namespace) {
        return this.createMeasurementProperty(URI.create(namespace + name));
    }

    /**
     * Create a new measurement capability with the given URI.
     * 
     * @param uri
     *            The URI.
     * @return The measurement capability individual.
     */
    private Individual createMeasurementCapability(URI uri) {
        this.getABox().createClass(SSN.MeasurementCapability.getURI());
        final Individual measurementCapability = this.getABox().createIndividual(uri.toString(), SSN.MeasurementCapability);
        return measurementCapability;
    }

    /**
     * Create a new measurement capability with the given name under the given
     * namespace.
     * 
     * @param sensingDeviceName
     *            The name of the sensing device
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The measurement capability individual.
     */
    private Individual createMeasurementCapabilityNS(final String sensingDeviceName, final String name, final String namespace) {
        return this.createMeasurementCapability(URI.create(namespace + sensingDeviceName + name));
    }

    /**
     * Create a new measurement capability with the given name under the given
     * namespace.
     * 
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The measurement capability individual.
     */
    private Individual createMeasurementCapabilityNS(final String name, final String namespace) {
        return this.createMeasurementCapability(URI.create(namespace + name));
    }

    /**
     * Create a new condition with the given URI.
     * 
     * @param uri
     *            The URI.
     * @return The condition individual.
     */
    private Individual createCondition(URI uri) {
        this.getABox().createClass(SSN.Condition.getURI());
        final Individual condition = this.getABox().createIndividual(uri.toString(), SSN.Condition);
        return condition;
    }

    /**
     * Create a new condition with the given name under the given
     * namespace.
     * 
     * @param sensingDeviceName
     *            The name of the sensing device
     *            param propertyName The name of the property
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The condition individual.
     */
    private Individual createConditionNS(final String sensingDeviceName, final String propertyName, final String name, final String namespace) {
        return this.createCondition(URI.create(namespace + sensingDeviceName + propertyName + name));
    }

    /**
     * Create a new condition with the given name under the given
     * namespace.
     * 
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The condition individual.
     */
    private Individual createConditionNS(final String name, final String namespace) {
        return this.createCondition(URI.create(namespace + name));
    }

    /**
     * Create a new feature of interest with the given URI.
     * 
     * @param uri
     *            The URI.
     * @return The feature of interest individual.
     */
    private Individual createFeatureOfInterest(URI uri) {
        this.getABox().createClass(SSN.FeatureOfInterest.getURI());
        final Individual featureOfInterest = this.getABox().createIndividual(uri.toString(), SSN.FeatureOfInterest);
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
     * Create a new feature of interest with the given name under the given
     * namespace.
     * 
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The feature of interest individual.
     */
    private Individual createFeatureOfInterestNS(final String name, final String namespace) {
        return this.createFeatureOfInterest(URI.create(namespace + name));
    }

    /**
     * Create a new sensing device with the given URI.
     * 
     * @param uri
     *            The URI.
     * @return The sensing device individual.
     */
    private Individual createSensingDevice(final URI uri) {
        this.getABox().createClass(SSN.SensingDevice.getURI());
        final Individual sensingDevice = this.getABox().createIndividual(uri.toString(), SSN.SensingDevice);
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
     * Create a new sensing device with the given name under the given
     * namespace.
     * 
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The sensing device individual.
     */
    private Individual createSensingDeviceNS(final String name, final String namespace) {
        return this.createSensingDevice(URI.create(namespace + name));
    }

    /**
     * Create a new property with the given URI.
     * 
     * @param uri
     *            The URI.
     * @return The property individual.
     */
    private Individual createProperty(final URI uri) {
        this.getABox().createClass(SSN.Property.getURI());
        final Individual property = this.getABox().createIndividual(uri.toString(), SSN.Property);
        return property;
    }

    /**
     * Create a new property with the given name under the given namespace.
     * 
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The property individual.
     */
    private Individual createPropertyNS(final String name, final String namespace) {
        return this.createProperty(URI.create(namespace + name));
    }

    /**
     * Create a new property of the given feature of interest with the given
     * name under the given namespace.
     * 
     * @param featureOfInterest
     *            The feature of interest.
     * @param name
     *            The name.
     * @param namespace
     *            The namespace.
     * @return The property individual.
     */
    private Individual createPropertyNS(final String featureOfInterest, final String name, final String namespace) {
        return this.createProperty(URI.create(namespace + featureOfInterest + name));
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
