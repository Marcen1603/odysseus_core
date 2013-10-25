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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
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

    public void createSensingDevice(SensingDevice source) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        Individual sensingDevice = this.createSensingDevice(source.getUri());
        List<Individual> properties = new ArrayList<Individual>();
        for (SDFAttribute attribute : source.getSchema().getAttributes()) {
            Individual property = createPropertyNS(attribute.getAttributeName(), ODYSSEUS.NS);
            this.addPropertyToSensingDevice(sensingDevice, property);
            properties.add(property);
        }
        if (!source.getCapabilities().isEmpty()) {
            for (MeasurementCapability capability : source.getAllCapabilities()) {
                this.addMeasurementCapabilityToSensingDevice(sensingDevice, capability);
            }
        }
        if (getABox().supportsTransactions()) {
            getABox().commit();
        }
    }

    private void addMeasurementCapabilityToSensingDevice(Individual sensingDevice, MeasurementCapability capability) {
        getABox().createClass(SSN.MeasurementCapability.getURI());
        Individual measurementCapability = getABox().createIndividual(capability.getUri().toString(), SSN.MeasurementCapability);

        SDFAttribute attribute = capability.getAttribute();
        Individual property = createPropertyNS(attribute.getAttributeName(), ODYSSEUS.NS);
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

    private void addConditionToMeasurementCapability(Individual measurementCapability, Condition c) {
        SDFAttribute attribute = c.getAttribute();
        Individual property = createPropertyNS(attribute.getAttributeName(), ODYSSEUS.NS);

        Individual condition = getABox().createIndividual(ODYSSEUS.NS + c.getName(), SSN.Condition);
        getABox().add(condition, RDFS.subClassOf, property.asResource());

        getABox().createClass(DUL.Region.getURI());
        Individual condition_interval = getABox().createIndividual(ODYSSEUS.NS + c.getName() + "/interval", DUL.Region);

        getABox().createClass(DUL.Amount.getURI());
        Individual minValue = getABox().createIndividual(ODYSSEUS.NS + c.getName() + "/interval/inf", DUL.Amount);
        getABox().add(minValue, DUL.hasDataValue, new Double(c.getInterval().inf()).toString(), TypeMapper.getInstance().getTypeByValue(c.getInterval().inf()));

        Individual maxValue = getABox().createIndividual(ODYSSEUS.NS + c.getName() + "/interval/sup", DUL.Amount);
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

    private Individual createSensingDevice(URI uri) {
        getABox().createClass(SSN.SensingDevice.getURI());
        Individual sensingDevice = getABox().createIndividual(uri.toString(), SSN.SensingDevice);
        return sensingDevice;
    }

    @SuppressWarnings("unused")
    private Individual createSensingDeviceNS(String name, String namespace) {
        return createSensingDevice(URI.create(namespace + name));
    }

    private Individual createProperty(URI uri) {
        getABox().createClass(SSN.Property.getURI());
        Individual property = getABox().createIndividual(uri.toString(), SSN.Property);
        return property;
    }

    private Individual createPropertyNS(String name, String namespace) {
        return createProperty(URI.create(namespace + name));
    }

    private OntModel getABox() {
        return aBox;
    }
}
