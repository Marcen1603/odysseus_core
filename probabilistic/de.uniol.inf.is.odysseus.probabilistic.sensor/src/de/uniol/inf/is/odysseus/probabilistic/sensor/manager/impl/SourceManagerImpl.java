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
package de.uniol.inf.is.odysseus.probabilistic.sensor.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementProperty;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SourceManagerImpl {
    private final OntModel aBox;

    /**
     * Class constructor.
     * 
     */
    public SourceManagerImpl(OntModel aBox) {
        this.aBox = aBox;
    }

    public void createSensingDevice(SensingDevice source) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        Individual sensingDevice = this.createSensingDevice(source.getName());
        List<Individual> properties = new ArrayList<Individual>();
        for (SDFAttribute attribute : source.getSchema().getAttributes()) {
            Individual property = createProperty(attribute.getAttributeName());
            this.addPropertyToSensingDevice(sensingDevice, property);
            properties.add(property);
        }
        if (!source.getCapabilities().isEmpty()) {
            for (MeasurementCapability capability : source.getCapabilities()) {
                this.addMeasurementCapabilityToSensingDevice(sensingDevice, capability);
            }
        }
        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
    }

    public void addMeasurementCapability(String sensingDevice, MeasurementCapability measurementCapability) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
    }

    private void addMeasurementCapabilityToSensingDevice(Individual sensingDevice, MeasurementCapability capability) {
        getABox().createClass(SSN.MeasurementCapability.getURI());
        Individual measurementCapability = getABox().createIndividual(ODYSSEUS.NS + capability.getName(), SSN.MeasurementCapability);

        SDFAttribute attribute = capability.getAttribute();
        Individual property = createProperty(attribute.getAttributeName());
        getABox().createObjectProperty(SSN.forProperty.getURI());
        getABox().add(measurementCapability, SSN.forProperty, property);

        if (!capability.getConditions().isEmpty()) {
            for (Condition condition : capability.getConditions()) {
                this.addConditionToMeasurementCapability(measurementCapability, condition);
            }
        }
        // if (!capability.getMeasurementProperties().isEmpty()) {
        // for (MeasurementProperty measurementProperty :
        // capability.getMeasurementProperties()) {
        // this.addMeasurementPropertyToMeasurementCapability(measurementCapability,
        // measurementProperty);
        // }
        // }
        getABox().add(sensingDevice, SSN.hasMeasurementCapability, measurementCapability);
    }

    private void addMeasurementPropertyToMeasurementCapability(Individual measurementCapability, MeasurementProperty property) {

        // getABox().createClass(property.getProperty());
        // Individual individual = getABox().createIndividual(ODYSSEUS.NS +
        // property.getName(), property.getProperty());
        // getABox().createResource(ODYSSEUS.NS + name, resource);

        // getABox().createClass(DUL.Region.getURI());
        // Individual value = getABox().createIndividual(ODYSSEUS.NS +
        // property.getName() + "_interval", DUL.Region);
        //
        // getABox().createClass(DUL.UnitOfMeasure.getURI());
        // Individual unitOfMeasure = getABox().createIndividual(ODYSSEUS.NS +
        // property.getUnit(), DUL.UnitOfMeasure);
        //
        // getABox().createClass(DUL.Amount.getURI());
        //
        // Individual minValue = getABox().createIndividual(ODYSSEUS.NS +
        // property.getName() + "_interval_inf", DUL.Amount);
        // getABox().createDatatypeProperty(DUL.hasDataValue.getURI());
        // getABox().add(minValue, DUL.hasDataValue, min.toString(),
        // TypeMapper.getInstance().getTypeByValue(min));
        // getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        // getABox().add(minValue, DUL.isClassifiedBy, unitOfMeasure);
        //
        // Individual maxValue = getABox().createIndividual(ODYSSEUS.NS +
        // property.getName() + "_interval_sup", DUL.Amount);
        // getABox().createDatatypeProperty(DUL.hasDataValue.getURI());
        // getABox().add(maxValue, DUL.hasDataValue, max.toString(),
        // TypeMapper.getInstance().getTypeByValue(max));
        // getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        // getABox().add(maxValue, DUL.isClassifiedBy, unitOfMeasure);
        //
        // ObjectProperty minValueProperty =
        // getABox().createObjectProperty(ODYSSEUS.hasMeasurementPropertyMinValue.getURI());
        // minValueProperty.addProperty(RDFS.subPropertyOf, DUL.hasPart);
        // getABox().add(value, ODYSSEUS.hasMeasurementPropertyMinValue,
        // minValue);
        //
        // ObjectProperty maxValueProperty =
        // getABox().createObjectProperty(ODYSSEUS.hasMeasurementPropertyMaxValue.getURI());
        // maxValueProperty.addProperty(RDFS.subPropertyOf, DUL.hasPart);
        // getABox().add(value, ODYSSEUS.hasMeasurementPropertyMinValue,
        // maxValue);
        //
        // getABox().createObjectProperty(SSN.hasValue.getURI());
        // getABox().add(individual, SSN.hasValue, value);
        //
        // getABox().createObjectProperty(SSN.hasMeasurementProperty.getURI());
        // getABox().add(measurementCapability, SSN.hasMeasurementProperty,
        // individual);

    }

    private void addConditionToMeasurementCapability(Individual measurementCapability, Condition c) {
        SDFAttribute attribute = c.getAttribute();
        Individual property = createProperty(attribute.getAttributeName());

        Individual condition = getABox().createIndividual(ODYSSEUS.NS + c.getName(), SSN.Condition);
        getABox().add(condition, RDFS.subClassOf, property.asResource());

        getABox().createClass(DUL.Region.getURI());
        Individual condition_interval = getABox().createIndividual(ODYSSEUS.NS + c.getName() + "_interval", DUL.Region);

        getABox().createClass(DUL.Amount.getURI());
        Individual minValue = getABox().createIndividual(ODYSSEUS.NS + c.getName() + "_interval_inf", DUL.Amount);
        getABox().add(minValue, DUL.hasDataValue, new Double(c.getInterval().inf()).toString(), TypeMapper.getInstance().getTypeByValue(c.getInterval().inf()));

        Individual maxValue = getABox().createIndividual(ODYSSEUS.NS + c.getName() + "_interval_sup", DUL.Amount);
        getABox().add(maxValue, DUL.hasDataValue, new Double(c.getInterval().sup()).toString(), TypeMapper.getInstance().getTypeByValue(c.getInterval().sup()));
        if (c.getUnit() != null) {
            getABox().add(minValue, DUL.isClassifiedBy, c.getUnit());
            getABox().add(maxValue, DUL.isClassifiedBy, c.getUnit());
        }
        getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMinValue, minValue);

        getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMaxValue, maxValue);

        getABox().createObjectProperty(SSN.hasValue.getURI());
        getABox().add(condition, SSN.hasValue, condition_interval);

        getABox().add(measurementCapability, SSN.inCondition, condition);
    }

    private void addPropertyToSensingDevice(Individual sensingDevice, Individual property) {
        getABox().createObjectProperty(SSN.observes.getURI());
        getABox().add(sensingDevice, SSN.observes, property);
    }

    private Individual createSensingDevice(String name) {
        return this.createSensingDeviceNS(name, ODYSSEUS.NS);
    }

    private Individual createSensingDeviceNS(String name, String namespace) {
        getABox().createClass(SSN.SensingDevice.getURI());
        Individual sensingDevice = getABox().createIndividual(namespace + name, SSN.SensingDevice);
        return sensingDevice;
    }

    private Individual createProperty(String name) {
        return createPropertyNS(name, ODYSSEUS.NS);
    }

    private Individual createPropertyNS(String name, String namespace) {
        getABox().createClass(SSN.Property.getURI());
        Individual property = getABox().createIndividual(namespace + name, SSN.Property);
        return property;
    }

    private OntModel getABox() {
        return aBox;
    }
}
