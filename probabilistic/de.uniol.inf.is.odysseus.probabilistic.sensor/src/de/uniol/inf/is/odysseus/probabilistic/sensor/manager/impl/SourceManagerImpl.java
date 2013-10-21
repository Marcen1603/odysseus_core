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
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
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

    public void createSensingDevice(SensingDevice sensingDevice) {

    }

    public void addMeasurementCapability(String sensingDevice, MeasurementCapability measurementCapability) {

    }

    private Individual createSensingDevice(String name) {
        return createSensingDevice(name, null, new ArrayList<Individual>(), new ArrayList<Individual>());
    }



    private Individual createSensingDevice(String name, String comment, List<Individual> properties, List<Individual> measurementCapabilities) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }
        getABox().createClass(SSN.SensingDevice.getURI());
        Individual sensingDevice = getABox().createIndividual(ODYSSEUS.NS + name, SSN.SensingDevice);
        getABox().add(sensingDevice, RDFS.subClassOf, SSN.SensingDevice);

        for (Individual property : properties) {
            getABox().add(sensingDevice, SSN.observes, property);
        }

        for (Individual measurementCapability : measurementCapabilities) {
            getABox().add(sensingDevice, SSN.hasMeasurementCapability, measurementCapability);
        }
        if (comment != null) {
            getABox().add(sensingDevice, RDFS.comment, name);
        }

        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
        return sensingDevice;
    }

    private Individual createCondition(String name, Number min, Number max, String unit) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        getABox().createClass(SSN.MeasurementRange.getURI());
        Individual condition = getABox().createIndividual(ODYSSEUS.NS + name, SSN.MeasurementRange);

        getABox().createClass(DUL.Region.getURI());
        Individual condition_interval = getABox().createIndividual(ODYSSEUS.NS + name + "_interval", DUL.Region);

        getABox().createClass(DUL.Amount.getURI());
        Individual minValue = getABox().createIndividual(ODYSSEUS.NS + name + "_interval_inf", DUL.Amount);
        getABox().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        getABox().add(minValue, DUL.isClassifiedBy, unit);

        Individual maxValue = getABox().createIndividual(ODYSSEUS.NS + name + "_interval_sup", DUL.Amount);
        getABox().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        getABox().add(maxValue, DUL.isClassifiedBy, unit);

        getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMinValue, maxValue);

        getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMaxValue, maxValue);

        getABox().createObjectProperty(SSN.hasValue.getURI());
        getABox().add(condition, SSN.hasValue, condition_interval);

        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
        return condition;
    }

    private Individual addMeasurementCapabilityFor(String name, Individual sensor, Individual property, Individual condition) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        getABox().createClass(SSN.MeasurementCapability.getURI());
        Individual measurementCapability = getABox().createIndividual(ODYSSEUS.NS + name, SSN.MeasurementCapability);

        getABox().createObjectProperty(SSN.forProperty.getURI());
        getABox().add(measurementCapability, SSN.forProperty, property);

        if (condition != null) {
            getABox().createObjectProperty(SSN.inCondition.getURI());
            getABox().add(measurementCapability, SSN.inCondition, condition);
        }
        getABox().createObjectProperty(SSN.hasMeasurementCapability.getURI());
        getABox().add(sensor, SSN.hasMeasurementCapability, measurementCapability);

        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
        return measurementCapability;
    }

    private Individual createProperty(String name) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        getABox().createClass(SSN.Property.getURI());
        Individual property = getABox().createIndividual(ODYSSEUS.NS + name, SSN.Property);

        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
        return property;
    }

    private void addMeasurementProperty(String name, Individual measurementCapability, Resource resource, Number min, Number max, String unit) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }
        getABox().createClass(resource.getURI());
        Individual individual = getABox().createIndividual(ODYSSEUS.NS + name, resource);
        getABox().createResource(ODYSSEUS.NS + name, resource);

        getABox().createClass(DUL.Region.getURI());
        Individual value = getABox().createIndividual(ODYSSEUS.NS + name + "_interval", DUL.Region);

        getABox().createClass(DUL.UnitOfMeasure.getURI());
        Individual unitOfMeasure = getABox().createIndividual(ODYSSEUS.NS + unit, DUL.UnitOfMeasure);

        getABox().createClass(DUL.Amount.getURI());

        Individual minValue = getABox().createIndividual(ODYSSEUS.NS + name + "_interval_inf", DUL.Amount);
        getABox().createDatatypeProperty(DUL.hasDataValue.getURI());
        getABox().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        getABox().add(minValue, DUL.isClassifiedBy, unitOfMeasure);

        Individual maxValue = getABox().createIndividual(ODYSSEUS.NS + name + "_interval_sup", DUL.Amount);
        getABox().createDatatypeProperty(DUL.hasDataValue.getURI());
        getABox().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        getABox().add(maxValue, DUL.isClassifiedBy, unitOfMeasure);

        ObjectProperty minValueProperty = getABox().createObjectProperty(ODYSSEUS.hasMeasurementPropertyMinValue.getURI());
        minValueProperty.addProperty(RDFS.subPropertyOf, DUL.hasPart);
        getABox().add(value, ODYSSEUS.hasMeasurementPropertyMinValue, minValue);

        ObjectProperty maxValueProperty = getABox().createObjectProperty(ODYSSEUS.hasMeasurementPropertyMaxValue.getURI());
        maxValueProperty.addProperty(RDFS.subPropertyOf, DUL.hasPart);
        getABox().add(value, ODYSSEUS.hasMeasurementPropertyMinValue, maxValue);

        getABox().createObjectProperty(SSN.hasValue.getURI());
        getABox().add(individual, SSN.hasValue, value);

        getABox().createObjectProperty(SSN.hasMeasurementProperty.getURI());
        getABox().add(measurementCapability, SSN.hasMeasurementProperty, individual);

        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
    }

    private OntModel getABox() {
        return aBox;
    }
}
