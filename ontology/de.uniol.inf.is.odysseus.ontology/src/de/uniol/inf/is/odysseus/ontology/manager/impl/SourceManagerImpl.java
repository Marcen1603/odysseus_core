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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.model.Condition;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;

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
    public SourceManagerImpl(final OntModel aBox) {
        this.aBox = aBox;
    }

    public void createSensingDevice(final SensingDevice source) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        final Individual sensingDevice = this.createSensingDevice(source.getUri());
        final List<Individual> properties = new ArrayList<Individual>();
        for (final SDFAttribute attribute : source.getSchema().getAttributes()) {
            final Individual property = this.createPropertyNS(attribute.getAttributeName(), ODYSSEUS.NS);
            this.addPropertyToSensingDevice(sensingDevice, property);
            properties.add(property);
        }
        if (!source.getCapabilities().isEmpty()) {
            for (final MeasurementCapability capability : source.getAllCapabilities()) {
                this.addMeasurementCapabilityToSensingDevice(sensingDevice, capability);
            }
        }
        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
    }

    private void addMeasurementCapabilityToSensingDevice(final Individual sensingDevice, final MeasurementCapability capability) {
        this.getABox().createClass(SSN.MeasurementCapability.getURI());
        final Individual measurementCapability = this.getABox().createIndividual(capability.getUri().toString(), SSN.MeasurementCapability);

        final SDFAttribute attribute = capability.getAttribute();
        final Individual property = this.createPropertyNS(attribute.getAttributeName(), ODYSSEUS.NS);
        this.getABox().createObjectProperty(SSN.forProperty.getURI());
        this.getABox().add(measurementCapability, SSN.forProperty, property);

        if (!capability.getConditions().isEmpty()) {
            for (final Condition condition : capability.getConditions()) {
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
        this.getABox().add(sensingDevice, SSN.hasMeasurementCapability, measurementCapability);
    }

    private void addConditionToMeasurementCapability(final Individual measurementCapability, final Condition c) {
        final SDFAttribute attribute = c.getAttribute();
        final Individual property = this.createPropertyNS(attribute.getAttributeName(), ODYSSEUS.NS);

        final Individual condition = this.getABox().createIndividual(ODYSSEUS.NS + c.getName(), SSN.Condition);
        this.getABox().add(condition, RDFS.subClassOf, property.asResource());

        this.getABox().createClass(DUL.Region.getURI());
        final Individual condition_interval = this.getABox().createIndividual(ODYSSEUS.NS + c.getName() + "/interval", DUL.Region);

        this.getABox().createClass(DUL.Amount.getURI());
        final Individual minValue = this.getABox().createIndividual(ODYSSEUS.NS + c.getName() + "/interval/inf", DUL.Amount);
        this.getABox().add(minValue, DUL.hasDataValue, new Double(c.getInterval().inf()).toString(), TypeMapper.getInstance().getTypeByValue(c.getInterval().inf()));

        final Individual maxValue = this.getABox().createIndividual(ODYSSEUS.NS + c.getName() + "/interval/sup", DUL.Amount);
        this.getABox().add(maxValue, DUL.hasDataValue, new Double(c.getInterval().sup()).toString(), TypeMapper.getInstance().getTypeByValue(c.getInterval().sup()));
        if (c.getUnit() != null) {
            this.getABox().add(minValue, DUL.isClassifiedBy, c.getUnit());
            this.getABox().add(maxValue, DUL.isClassifiedBy, c.getUnit());
        }
        this.getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMinValue, minValue);

        this.getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMaxValue, maxValue);

        this.getABox().createObjectProperty(SSN.hasValue.getURI());
        this.getABox().add(condition, SSN.hasValue, condition_interval);

        this.getABox().add(measurementCapability, SSN.inCondition, condition);
    }

    private void addPropertyToSensingDevice(final Individual sensingDevice, final Individual property) {
        this.getABox().createObjectProperty(SSN.observes.getURI());
        this.getABox().add(sensingDevice, SSN.observes, property);
    }

    private Individual createSensingDevice(final URI uri) {
        this.getABox().createClass(SSN.SensingDevice.getURI());
        final Individual sensingDevice = this.getABox().createIndividual(uri.toString(), SSN.SensingDevice);
        return sensingDevice;
    }

    @SuppressWarnings("unused")
    private Individual createSensingDeviceNS(final String name, final String namespace) {
        return this.createSensingDevice(URI.create(namespace + name));
    }

    private Individual createProperty(final URI uri) {
        this.getABox().createClass(SSN.Property.getURI());
        final Individual property = this.getABox().createIndividual(uri.toString(), SSN.Property);
        return property;
    }

    private Individual createPropertyNS(final String name, final String namespace) {
        return this.createProperty(URI.create(namespace + name));
    }

    private OntModel getABox() {
        return this.aBox;
    }
}
