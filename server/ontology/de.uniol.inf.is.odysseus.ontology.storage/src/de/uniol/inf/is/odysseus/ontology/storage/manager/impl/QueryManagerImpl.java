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
package de.uniol.inf.is.odysseus.ontology.storage.manager.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryCancelledException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.common.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.common.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.common.model.Property;
import de.uniol.inf.is.odysseus.ontology.common.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.common.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.common.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Accuracy;
import de.uniol.inf.is.odysseus.ontology.common.model.property.DetectionLimit;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Drift;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Frequency;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Latency;
import de.uniol.inf.is.odysseus.ontology.common.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.common.model.property.MeasurementRange;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Precision;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Resolution;
import de.uniol.inf.is.odysseus.ontology.common.model.property.ResponseTime;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Selectivity;
import de.uniol.inf.is.odysseus.ontology.common.model.property.Sensitivity;
import de.uniol.inf.is.odysseus.ontology.storage.manager.QueryManager;
import de.uniol.inf.is.odysseus.ontology.storage.vocabulary.DUL;
import de.uniol.inf.is.odysseus.ontology.storage.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.storage.vocabulary.QU;
import de.uniol.inf.is.odysseus.ontology.storage.vocabulary.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class QueryManagerImpl implements QueryManager {
    private static final Logger LOG = LoggerFactory.getLogger(QueryManagerImpl.class);
    private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI() + "> ";
    private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI() + "> ";
    private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI() + "> ";
    private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI() + "> ";
    private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI() + "> ";
    private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI() + "> ";
    private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
    private static final String ODY_PREFIX = "PREFIX ody: <" + ODYSSEUS.getURI() + "> ";
    private static final String QUERY_PREFIX;
    private static final long TIMEOUT1 = 1000l;
    private static final long TIMEOUT2 = 1000l;
    private final List<SensingDevice> sensingDeviceCache = new ArrayList<SensingDevice>();
    private final List<FeatureOfInterest> featuresOfInterestCache = new ArrayList<FeatureOfInterest>();
    private final List<Property> propertiesCache = new ArrayList<Property>();
    private final OntModel inferenceModel;

    static {
        final StringBuilder prefix = new StringBuilder();
        prefix.append(QueryManagerImpl.SSN_PREFIX);
        prefix.append(QueryManagerImpl.XSD_PREFIX);
        prefix.append(QueryManagerImpl.RDF_PREFIX);
        prefix.append(QueryManagerImpl.RDFS_PREFIX);
        prefix.append(QueryManagerImpl.OWL_PREFIX);
        prefix.append(QueryManagerImpl.DUL_PREFIX);
        prefix.append(QueryManagerImpl.QU_PREFIX);
        prefix.append(QueryManagerImpl.ODY_PREFIX);
        QUERY_PREFIX = prefix.toString();
    }

    /**
     * Class constructor.
     * 
     */
    public QueryManagerImpl(final OntModel inferenceModel) {
        this.inferenceModel = inferenceModel;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<String> getAllFeatureOfInterestNames() {
        final List<String> names = new ArrayList<String>();
        final String queryString = "SELECT ?uri  WHERE { ?uri rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.FeatureOfInterest.getLocalName() + " }";
        final Query query = QueryFactory.create(QueryManagerImpl.QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getInferenceModel());

        qExec.setTimeout(QueryManagerImpl.TIMEOUT1, QueryManagerImpl.TIMEOUT2);
        try {
            final ResultSet result = qExec.execSelect();
            while (result.hasNext()) {

                final QuerySolution solution = result.next();
                names.add(this.getInferenceModel().getProperty(solution.get("uri").asResource(), RDFS.label).getString());
            }
        }
        catch (final QueryCancelledException e) {
            QueryManagerImpl.LOG.error(e.getMessage(), e);
        }
        finally {
            qExec.close();
        }
        return names;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getAllSensingDevices() {
        if (this.sensingDeviceCache.isEmpty()) {
            final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
            final ExtendedIterator<? extends OntResource> instances = sensingDeviceClass.listInstances();

            final List<SensingDevice> sensingDevices = new ArrayList<SensingDevice>();
            while (instances.hasNext()) {
                final OntResource instance = instances.next();
                sensingDevices.add(this.getSensingDevice(instance));
            }
            this.sensingDeviceCache.addAll(sensingDevices);
        }
        return Collections.unmodifiableList(this.sensingDeviceCache);
    }

    @Override
    public SensingDevice getSensingDevice(final URI uri) {
        final OntResource instance = this.getInferenceModel().getOntResource(uri.toString());
        return this.getSensingDevice(instance);
    }

    /**
     * 
     * {@inheritDoc}
     */

    public SensingDevice getSensingDevice(final OntResource instance) {
        final String name = instance.getProperty(RDFS.label).getString();
        final SensingDevice sensingDevice = new SensingDevice(URI.create(instance.getURI()), name);
        final NodeIterator measurementCapabilityIter = instance.listPropertyValues(SSN.hasMeasurementCapability);
        while (measurementCapabilityIter.hasNext()) {
            final OntResource measurementCapabilityResource = measurementCapabilityIter.next().as(OntResource.class);
            final MeasurementCapability measurementCapability = this.getMeasurementCapability(measurementCapabilityResource);
            if (measurementCapability != null) {
                sensingDevice.addMeasurementCapability(measurementCapability);
            }
        }

        return sensingDevice;
    }

    public List<SensingDevice> getSensingDevice(final String name) {
        final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
        final ExtendedIterator<? extends RDFNode> instances = sensingDeviceClass.listLabels(name);

        final List<SensingDevice> sensingDevices = new ArrayList<SensingDevice>();
        while (instances.hasNext()) {
            final OntResource instance = instances.next().as(OntResource.class);
            sensingDevices.add(this.getSensingDevice(instance));
        }
        return sensingDevices;

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<FeatureOfInterest> getAllFeaturesOfInterest() {
        if (this.featuresOfInterestCache.isEmpty()) {
            final OntClass featureOfInterestClass = this.getInferenceModel().getOntClass(SSN.FeatureOfInterest.getURI());
            final ExtendedIterator<? extends OntResource> instances = featureOfInterestClass.listInstances(true);

            final List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
            while (instances.hasNext()) {
                final OntResource instance = instances.next();
                featuresOfInterest.add(this.getFeatureOfInterest(instance));
            }
            this.featuresOfInterestCache.addAll(featuresOfInterest);
        }
        return Collections.unmodifiableList(this.featuresOfInterestCache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureOfInterest getFeatureOfInterest(final URI uri) {
        final OntResource instance = this.getInferenceModel().getOntResource(uri.toString());
        return this.getFeatureOfInterest(instance);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public FeatureOfInterest getFeatureOfInterest(final OntResource instance) {
        final String name = instance.getProperty(RDFS.label).getString();
        final FeatureOfInterest featureOfInterest = new FeatureOfInterest(URI.create(instance.getURI()), name);
        final NodeIterator propertyIter = instance.listPropertyValues(SSN.hasProperty);
        while (propertyIter.hasNext()) {
            final OntResource propertyResource = propertyIter.next().as(OntResource.class);
            featureOfInterest.addProperty(this.getProperty(propertyResource));
        }

        return featureOfInterest;
    }

    public List<FeatureOfInterest> getFeatureOfInterest(final String name) {
        final OntClass featureOfInterestClass = this.getInferenceModel().getOntClass(SSN.FeatureOfInterest.getURI());
        final ExtendedIterator<? extends RDFNode> instances = featureOfInterestClass.listLabels(name);

        final List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
        while (instances.hasNext()) {
            final OntResource instance = instances.next().as(OntResource.class);
            featuresOfInterest.add(this.getFeatureOfInterest(instance));
        }
        return featuresOfInterest;
    }

    /**
     * @return
     */
    @Override
    public List<Property> getAllProperties() {
        if (this.propertiesCache.isEmpty()) {
            final OntClass propertyClass = this.getInferenceModel().getOntClass(SSN.Property.getURI());
            final List<Property> properties = new ArrayList<Property>();
            for (final ExtendedIterator<? extends OntResource> instances = propertyClass.listInstances(true); instances.hasNext();) {
                final OntResource instance = instances.next();
                properties.add(this.getProperty(instance));
            }

            this.propertiesCache.addAll(properties);
        }
        return Collections.unmodifiableList(this.propertiesCache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Property getProperty(final URI uri) {
        final OntResource instance = this.getInferenceModel().getOntResource(uri.toString());
        return this.getProperty(instance);
    }

    /**
     * {@inheritDoc}
     */
    public Property getProperty(final OntResource instance) {
        final String name = instance.getProperty(RDFS.label).getString();
        final Property property = new Property(URI.create(instance.getURI()), name);
        return property;

    }

    public List<SensingDevice> getSensingDevices(final String featureOfInterest, final String sensingDevice, final String measurementCapability) {
        final List<SensingDevice> sensingDevices = new ArrayList<SensingDevice>();

        final String queryString = "SELECT *  WHERE { " + "?foi rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.FeatureOfInterest.getLocalName() + " ; " + "ssn:" + SSN.hasProperty.getLocalName()
                + " ?p . " + "?sd rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.SensingDevice.getLocalName() + " ; " + "ssn:" + SSN.observes.getLocalName() + " ?p ; " + "ssn:"
                + SSN.hasMeasurementCapability.getLocalName() + " ?mc . " + "?mc  rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.MeasurementCapability.getLocalName() + " ; " + "ssn:"
                + SSN.forProperty.getLocalName() + " ?p . " + "?foi rdfs:" + RDFS.label.getLocalName() + " \"" + featureOfInterest + "\" . " + "?sd rdfs:" + RDFS.label.getLocalName() + " \""
                + sensingDevice + "\" . " + "?mc rdfs:" + RDFS.label.getLocalName() + " \"" + measurementCapability + "\"" + "}";

        final Query query = QueryFactory.create(QueryManagerImpl.QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getInferenceModel());

        // qExec.setTimeout(TIMEOUT1, TIMEOUT2);
        try {
            final ResultSet result = qExec.execSelect();
            while (result.hasNext()) {

                final QuerySolution solution = result.next();
                sensingDevices.add(this.getSensingDevice(solution.get("sd").as(OntResource.class)));
            }
        }
        catch (final QueryCancelledException e) {
            QueryManagerImpl.LOG.error(e.getMessage(), e);
        }
        finally {
            qExec.close();
        }
        return sensingDevices;
    }

    public List<String> getAttributes(final Property property) {
        final List<String> attributes = new ArrayList<String>();

        // final String queryString = "SELECT *  WHERE { "
        // + "?foi ssn:"+ SSN.hasProperty.getLocalName() + " ?p . "
        // + "?sd  ssn:"+SSN.observes.getLocalName()+" ?p ; "
        // + "ssn:" + SSN.hasMeasurementCapability.getLocalName() + " ?mc ."
        // + "?mc  ssn:"+SSN.forProperty.getLocalName()+" ?p ."
        // + ""
        // + "?foi  ssn:"+RDFS.label.getLocalName()+" ?foiLabel ."
        // + "?sd  ssn:"+RDFS.label.getLocalName()+" ?sdLabel ."
        // + "?mc  ssn:"+RDFS.label.getLocalName()+" ?mcLabel ."
        // + "}";

        final String queryString = "SELECT *  WHERE { " + "?foi rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.FeatureOfInterest.getLocalName() + " ; " + "ssn:" + SSN.hasProperty.getLocalName()
                + " ?p . " + "?sd rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.SensingDevice.getLocalName() + " ; " + "ssn:" + SSN.observes.getLocalName() + " ?p ; " + "ssn:"
                + SSN.hasMeasurementCapability.getLocalName() + " ?mc . " + "?mc  rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.MeasurementCapability.getLocalName() + " ; " + "ssn:"
                + SSN.forProperty.getLocalName() + " ?p . " + "<" + property.getUri().toString() + "> rdfs:" + RDFS.subClassOf.getLocalName() + " ?p . "

                + "?foi rdfs:" + RDFS.label.getLocalName() + " ?foiLabel . " + "?sd rdfs:" + RDFS.label.getLocalName() + " ?sdLabel . " + "?mc rdfs:" + RDFS.label.getLocalName() + " ?mcLabel" + "}";

        final Query query = QueryFactory.create(QueryManagerImpl.QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getInferenceModel());

        // qExec.setTimeout(TIMEOUT1, TIMEOUT2);
        try {
            final ResultSet result = qExec.execSelect();
            while (result.hasNext()) {

                final QuerySolution solution = result.next();
                attributes.add(solution.get("foiLabel").asLiteral().getString() + ":" + solution.get("sdLabel").asLiteral().getString() + "#" + solution.get("mcLabel").asLiteral().getString());
            }
        }
        catch (final QueryCancelledException e) {
            QueryManagerImpl.LOG.error(e.getMessage(), e);
        }
        finally {
            qExec.close();
        }

        return attributes;
    }

    /**
     * 
     * {@inheritDoc}
     */

    public List<SensingDevice> getSensingDevicesByObservedProperty(final URI uri) {
        final OntClass propertyClass = this.getInferenceModel().getOntClass(SSN.Property.getURI());
        final ExtendedIterator<Statement> instances = propertyClass.listProperties(SSN.observedBy);

        final List<SensingDevice> sensingDevices = new ArrayList<SensingDevice>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            sensingDevices.add(this.getSensingDevice(stmt.getObject().as(OntResource.class)));
        }

        return sensingDevices;
    }

    /**
     * 
     * {@inheritDoc}
     */

    public List<SensingDevice> getSensingDevicesBySDFAttribute(final SDFAttribute attribute) {
        // TODO Implement SDFAttribute mapping
        // return
        // this.getSensingDevicesByObservedProperty(URI.create(ODYSSEUS.NS +
        // attribute.getAttributeName()));

        throw new IllegalArgumentException("Not implemented yet");
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<FeatureOfInterest> getFeaturesOfInterestByAttribute(final SDFAttribute attribute) {
        // TODO Implement SDFAttribute mapping
        // return
        // this.getSensingDevicesByObservedProperty(URI.create(ODYSSEUS.NS +
        // attribute.getAttributeName()));
        throw new IllegalArgumentException("Not implemented yet");
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<FeatureOfInterest> getFeaturesOfInterestByProperty(final URI uri) {
        final OntClass propertyClass = this.getInferenceModel().getOntClass(SSN.Property.getURI());
        final ExtendedIterator<Statement> instances = propertyClass.listProperties(SSN.isPropertyOf);

        final List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            featuresOfInterest.add(this.getFeatureOfInterest(stmt.getObject().as(OntResource.class)));
        }

        return featuresOfInterest;
    }

    /**
     * 
     * {@inheritDoc}
     */

    public List<Property> getAllPropertiesObservedBySensingDevice(final URI uri) {
        final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
        final ExtendedIterator<Statement> instances = sensingDeviceClass.listProperties(SSN.observes);

        final List<Property> properties = new ArrayList<Property>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            properties.add(this.getProperty(stmt.getObject().as(OntResource.class)));
        }

        return properties;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<Property> getAllPropertiesByFeatureOfInterest(final OntResource featureOfInterest) {
        final OntClass featureOfInterestClass = this.getInferenceModel().getOntClass(SSN.FeatureOfInterest.getURI());
        final ExtendedIterator<Statement> instances = featureOfInterestClass.listProperties(SSN.hasProperty);

        final List<Property> properties = new ArrayList<Property>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            properties.add(this.getProperty(stmt.getObject().as(OntResource.class)));
        }

        return properties;
    }

    @SuppressWarnings("unused")
    private List<MeasurementCapability> getAllMeasurementCapabilitiesFromSensingDevice(final OntResource sensingDevice) {
        final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
        final ExtendedIterator<Statement> instances = sensingDeviceClass.listProperties(SSN.hasMeasurementCapability);

        final List<MeasurementCapability> measurementCapabilities = new ArrayList<MeasurementCapability>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            measurementCapabilities.add(this.getMeasurementCapability(stmt.getObject().as(OntResource.class)));
        }

        return measurementCapabilities;
    }

    @SuppressWarnings("unused")
    private List<MeasurementCapability> getMeasurementCapabilitiesForSDFAttribute(final SDFAttribute attribute) {
        final String sensingDevice = attribute.getSourceName();
        final String name = attribute.getAttributeName();

        final OntClass measurementCapabilityClass = this.getInferenceModel().getOntClass(SSN.MeasurementCapability.getURI());
        final ExtendedIterator<? extends RDFNode> instances = measurementCapabilityClass.listLabels(name);

        final List<MeasurementCapability> measurementCapabilities = new ArrayList<MeasurementCapability>();
        while (instances.hasNext()) {
            final OntResource instance = instances.next().as(OntResource.class);
            final ExtendedIterator<Statement> sensingDeviceInstances = instance.listProperties(SSN.hasMeasurementCapability);
            while (sensingDeviceInstances.hasNext()) {
                final OntResource sensingDeviceInstance = sensingDeviceInstances.next().getObject().as(OntResource.class);
                if (sensingDeviceInstance.hasLabel(sensingDevice, "en")) {
                    measurementCapabilities.add(this.getMeasurementCapability(instance));
                    break;
                }
            }

        }
        return measurementCapabilities;

    }

    private MeasurementCapability getMeasurementCapability(final OntResource measurementCapabilityResource) {

        final Statement propertyStmt = measurementCapabilityResource.getProperty(SSN.forProperty);
        final String name = measurementCapabilityResource.getProperty(RDFS.label).getString();
        final Property property = new Property(URI.create(propertyStmt.getResource().getURI()));
        final MeasurementCapability measurementCapability = new MeasurementCapability(URI.create(measurementCapabilityResource.getURI()), name, property);

        final StmtIterator conditionIter = measurementCapabilityResource.listProperties(SSN.inCondition);

        while (conditionIter.hasNext()) {
            final OntResource conditionResource = conditionIter.next().getObject().as(OntResource.class);
            final Condition condition = this.getConditon(conditionResource);
            if (condition != null) {
                measurementCapability.addCondition(condition);
            }
        }

        final StmtIterator measurementPropertyIter = measurementCapabilityResource.listProperties(SSN.hasMeasurementProperty);
        while (measurementPropertyIter.hasNext()) {
            final OntResource measurementPropertyResource = measurementPropertyIter.next().getObject().as(OntResource.class);
            final MeasurementProperty measurementProperty = this.getMeasurementProperty(measurementPropertyResource);
            if (measurementProperty != null) {
                measurementCapability.addMeasurementProperty(measurementProperty);
            }
        }
        return measurementCapability;
    }

    private Condition getConditon(final OntResource condition) {
        final Statement propertyStmt = condition.getProperty(RDFS.subClassOf);
        final Property property;
        if (propertyStmt != null) {
            property = new Property(URI.create(propertyStmt.getResource().getURI()));
        }
        else {
            property = new Property(URI.create(condition.getURI()));
        }

        final String expression = this.getConditionExpression(condition);
        return new ExpressionCondition(URI.create(condition.getURI()), property, expression);
    }

    private MeasurementProperty getMeasurementProperty(final OntResource measurementProperty) {
        final String expression = this.getMeasurementPropertyExpression(measurementProperty);
        // FIXME Use correct property class

        if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Accuracy)) {
            return new Accuracy(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.DetectionLimit)) {
            return new DetectionLimit(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Drift)) {
            return new Drift(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Frequency)) {
            return new Frequency(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Latency)) {
            return new Latency(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.MeasurementRange)) {
            return new MeasurementRange(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Precision)) {
            return new Precision(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.ResponseTime)) {
            return new ResponseTime(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Resolution)) {
            return new Resolution(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Sensitivity)) {
            return new Sensitivity(URI.create(measurementProperty.getURI()), expression);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Selectivity)) {
            return new Selectivity(URI.create(measurementProperty.getURI()), expression);
        }
        return new MeasurementProperty(URI.create(measurementProperty.getURI()), measurementProperty.getURI(), expression);

    }

    private String getConditionExpression(final OntResource condition) {
        String expression = null;

        final Statement conditionStmt = this.getInferenceModel().getProperty(condition, SSN.hasValue);
        if (conditionStmt != null) {
            final Resource region = conditionStmt.getResource();
            final Statement expressionValueStmt = this.getInferenceModel().getProperty(region, ODYSSEUS.hasExpression);
            if (expressionValueStmt != null) {
                expression = this.getInferenceModel().getProperty(expressionValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getString();
            }

        }
        return expression;
    }

    private String getMeasurementPropertyExpression(final OntResource measurementProperty) {
        String expression = null;

        final Statement measurementPropertyStmt = this.getInferenceModel().getProperty(measurementProperty, SSN.hasValue);

        if (measurementPropertyStmt != null) {
            final Resource region = measurementPropertyStmt.getResource();
            final Statement expressionValueStmt = this.getInferenceModel().getProperty(region, ODYSSEUS.hasExpression);
            if (expressionValueStmt != null) {
                expression = this.getInferenceModel().getProperty(expressionValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getString();
            }
        }
        return expression;
    }

    private OntModel getInferenceModel() {
        return this.inferenceModel;
    }

}
