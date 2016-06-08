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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.Accuracy;
import cc.kuka.odysseus.ontology.common.model.property.DetectionLimit;
import cc.kuka.odysseus.ontology.common.model.property.Drift;
import cc.kuka.odysseus.ontology.common.model.property.Frequency;
import cc.kuka.odysseus.ontology.common.model.property.Latency;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementRange;
import cc.kuka.odysseus.ontology.common.model.property.Precision;
import cc.kuka.odysseus.ontology.common.model.property.Resolution;
import cc.kuka.odysseus.ontology.common.model.property.ResponseTime;
import cc.kuka.odysseus.ontology.common.model.property.Selectivity;
import cc.kuka.odysseus.ontology.common.model.property.Sensitivity;
import cc.kuka.odysseus.ontology.storage.manager.QueryManager;
import cc.kuka.odysseus.ontology.storage.vocabulary.DUL;
import cc.kuka.odysseus.ontology.storage.vocabulary.ODYSSEUS;
import cc.kuka.odysseus.ontology.storage.vocabulary.QU;
import cc.kuka.odysseus.ontology.storage.vocabulary.SSN;

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

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
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
    private final List<SensingDevice> sensingDeviceCache = new ArrayList<>();
    private final List<FeatureOfInterest> featuresOfInterestCache = new ArrayList<>();
    private final List<Property> propertiesCache = new ArrayList<>();
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

    public List<String> getAllFeatureOfInterestNames() {
        final List<String> names = new ArrayList<>();
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
            if (sensingDeviceClass != null) {
                final ExtendedIterator<? extends OntResource> instances = sensingDeviceClass.listInstances();

                final List<SensingDevice> sensingDevices = new ArrayList<>();
                while (instances.hasNext()) {
                    final OntResource instance = instances.next();
                    sensingDevices.add(this.getSensingDevice(instance));
                }
                this.sensingDeviceCache.addAll(sensingDevices);
            }
        }
        return Collections.unmodifiableList(this.sensingDeviceCache);
    }

    @Override
    public SensingDevice getSensingDevice(final URI uri) {
        final OntResource instance = this.getInferenceModel().getOntResource(uri.toString());
        return this.getSensingDevice(instance);
    }

    public SensingDevice getSensingDevice(final OntResource instance) {
        final String name = instance.getProperty(RDFS.label).getString();
        final SensingDevice sensingDevice = new SensingDevice(URI.create(instance.getURI()), name);
        final NodeIterator measurementCapabilityIter = instance.listPropertyValues(SSN.hasMeasurementCapability);
        while (measurementCapabilityIter.hasNext()) {
            final OntResource measurementCapabilityResource = measurementCapabilityIter.next().as(OntResource.class);
            final MeasurementCapability measurementCapability = this.getMeasurementCapability(measurementCapabilityResource);
            if (measurementCapability != null) {
                sensingDevice.add(measurementCapability);
            }
        }

        return sensingDevice;
    }

    public List<SensingDevice> getSensingDevice(final String name) {
        final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
        final ExtendedIterator<? extends RDFNode> instances = sensingDeviceClass.listLabels(name);

        final List<SensingDevice> sensingDevices = new ArrayList<>();
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
            if (featureOfInterestClass != null) {
                final ExtendedIterator<? extends OntResource> instances = featureOfInterestClass.listInstances(true);

                final List<FeatureOfInterest> featuresOfInterest = new ArrayList<>();
                while (instances.hasNext()) {
                    final OntResource instance = instances.next();
                    featuresOfInterest.add(this.getFeatureOfInterest(instance));
                }
                this.featuresOfInterestCache.addAll(featuresOfInterest);
            }
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
            featureOfInterest.add(this.getProperty(propertyResource));
        }

        return featureOfInterest;
    }

    public List<FeatureOfInterest> getFeatureOfInterest(final String name) {
        final OntClass featureOfInterestClass = this.getInferenceModel().getOntClass(SSN.FeatureOfInterest.getURI());
        final ExtendedIterator<? extends RDFNode> instances = featureOfInterestClass.listLabels(name);

        final List<FeatureOfInterest> featuresOfInterest = new ArrayList<>();
        while (instances.hasNext()) {
            final OntResource instance = instances.next().as(OntResource.class);
            featuresOfInterest.add(this.getFeatureOfInterest(instance));
        }
        return featuresOfInterest;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<Property> getAllProperties() {
        if (this.propertiesCache.isEmpty()) {
            final OntClass propertyClass = this.getInferenceModel().getOntClass(SSN.Property.getURI());
            final List<Property> properties = new ArrayList<>();
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

    public Property getProperty(final OntResource instance) {
        final String name = instance.getProperty(RDFS.label).getString();
        final Property property = new Property(URI.create(instance.getURI()), name);
        return property;

    }

    public List<SensingDevice> getSensingDevices(final String sensingDevice, final String measurementCapability) {
        final List<SensingDevice> sensingDevices = new ArrayList<>();

        final String queryString = "SELECT *  WHERE { " + "?sd rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.SensingDevice.getLocalName() + " ; " + "ssn:" + SSN.observes.getLocalName() + " ?p ; "
                + "ssn:" + SSN.hasMeasurementCapability.getLocalName() + " ?mc . " + "?mc  rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.MeasurementCapability.getLocalName() + " ; " + "ssn:"
                + SSN.forProperty.getLocalName() + " ?p . " + "?sd rdfs:" + RDFS.label.getLocalName() + " \"" + sensingDevice + "\" . " + "?mc rdfs:" + RDFS.label.getLocalName() + " \""
                + measurementCapability + "\"" + "}";

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

    public List<SensingDevice> getSensingDevices(final String featureOfInterest, final String sensingDevice, final String measurementCapability) {
        final List<SensingDevice> sensingDevices = new ArrayList<>();

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
        final List<String> attributes = new ArrayList<>();

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
                + SSN.forProperty.getLocalName() + " ?p . " + "<" + property.uri().toString() + "> rdfs:" + RDFS.subClassOf.getLocalName() + " ?p . "

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

    public List<SensingDevice> getSensingDevicesByObservedProperty(final URI uri) {
        final OntClass propertyClass = this.getInferenceModel().getOntClass(SSN.Property.getURI());
        final ExtendedIterator<Statement> instances = propertyClass.listProperties(SSN.observedBy);

        final List<SensingDevice> sensingDevices = new ArrayList<>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            sensingDevices.add(this.getSensingDevice(stmt.getObject().as(OntResource.class)));
        }

        return sensingDevices;
    }

    public List<SensingDevice> getSensingDevicesBySDFAttribute(final SDFAttribute attribute) {
        // TODO Implement SDFAttribute mapping
        // return
        // this.getSensingDevicesByObservedProperty(URI.create(ODYSSEUS.NS +
        // attribute.getAttributeName()));

        throw new IllegalArgumentException("Not implemented yet");
    }

    public List<FeatureOfInterest> getFeaturesOfInterestByAttribute(final SDFAttribute attribute) {
        // TODO Implement SDFAttribute mapping
        // return
        // this.getSensingDevicesByObservedProperty(URI.create(ODYSSEUS.NS +
        // attribute.getAttributeName()));
        throw new IllegalArgumentException("Not implemented yet");
    }

    public List<FeatureOfInterest> getFeaturesOfInterestByProperty(final URI uri) {
        final OntClass propertyClass = this.getInferenceModel().getOntClass(SSN.Property.getURI());
        final ExtendedIterator<Statement> instances = propertyClass.listProperties(SSN.isPropertyOf);

        final List<FeatureOfInterest> featuresOfInterest = new ArrayList<>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            featuresOfInterest.add(this.getFeatureOfInterest(stmt.getObject().as(OntResource.class)));
        }

        return featuresOfInterest;
    }

    public List<Property> getAllPropertiesObservedBySensingDevice(final URI uri) {
        final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
        final ExtendedIterator<Statement> instances = sensingDeviceClass.listProperties(SSN.observes);

        final List<Property> properties = new ArrayList<>();
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

        final List<Property> properties = new ArrayList<>();
        while (instances.hasNext()) {
            final Statement stmt = instances.next();
            properties.add(this.getProperty(stmt.getObject().as(OntResource.class)));
        }

        return properties;
    }

    @Override
    public void clearCache() {
        this.sensingDeviceCache.clear();
        this.featuresOfInterestCache.clear();
        this.propertiesCache.clear();
    }

    @SuppressWarnings("unused")
    private List<MeasurementCapability> getAllMeasurementCapabilitiesFromSensingDevice(final OntResource sensingDevice) {
        final OntClass sensingDeviceClass = this.getInferenceModel().getOntClass(SSN.SensingDevice.getURI());
        final ExtendedIterator<Statement> instances = sensingDeviceClass.listProperties(SSN.hasMeasurementCapability);

        final List<MeasurementCapability> measurementCapabilities = new ArrayList<>();
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

        final List<MeasurementCapability> measurementCapabilities = new ArrayList<>();
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
                measurementCapability.add(condition);
            }
        }

        final StmtIterator measurementPropertyIter = measurementCapabilityResource.listProperties(SSN.hasMeasurementProperty);
        while (measurementPropertyIter.hasNext()) {
            final OntResource measurementPropertyResource = measurementPropertyIter.next().getObject().as(OntResource.class);
            if (measurementPropertyResource != null) {
                try {
                    final MeasurementProperty measurementProperty = this.getMeasurementProperty(measurementPropertyResource);
                    if (measurementProperty != null) {
                        measurementCapability.add(measurementProperty);
                    }
                }
                catch (final NullPointerException e) {
                    QueryManagerImpl.LOG.error("Invalid measurement property in " + measurementPropertyResource.toString() + ", check ontology", e);
                }
            }
        }
        return measurementCapability;
    }

    private Condition getConditon(final OntResource condition) {
        final Resource resource = condition.getPropertyResourceValue(RDFS.subClassOf);
        final Property property = new Property(URI.create(resource.getURI()));
        final String name = condition.getProperty(RDFS.label).getString();
        final String expression = this.getConditionExpression(condition);
        return new Condition(URI.create(condition.getURI()), name, property, expression);
    }

    private MeasurementProperty getMeasurementProperty(final OntResource measurementProperty) {
        Objects.requireNonNull(measurementProperty);
        final String expression = this.getMeasurementPropertyExpression(measurementProperty);
        final String name = measurementProperty.getProperty(RDFS.label).getString();
        final Resource resource = measurementProperty.getPropertyResourceValue(RDFS.subClassOf);

        if (resource.equals(SSN.Accuracy)) {
            return new Accuracy(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.DetectionLimit)) {
            return new DetectionLimit(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Drift)) {
            return new Drift(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Frequency)) {
            return new Frequency(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Latency)) {
            return new Latency(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.MeasurementRange)) {
            return new MeasurementRange(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Precision)) {
            return new Precision(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.ResponseTime)) {
            return new ResponseTime(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Resolution)) {
            return new Resolution(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Sensitivity)) {
            return new Sensitivity(URI.create(measurementProperty.getURI()), name, expression);
        }
        else if (resource.equals(SSN.Selectivity)) {
            return new Selectivity(URI.create(measurementProperty.getURI()), name, expression);
        }
        return new MeasurementProperty(URI.create(measurementProperty.getURI()), name, resource.getURI(), expression);
    }

    private String getConditionExpression(final OntResource condition) {
        String expression = "";
        final Statement conditionStmt = this.getInferenceModel().getProperty(condition, DUL.hasDataValue);
        if (conditionStmt != null) {
            expression = conditionStmt.getObject().asLiteral().getString();
        }
        return expression;

    }

    private String getMeasurementPropertyExpression(final OntResource measurementProperty) {
        String expression = "";
        final Statement measurementPropertyStmt = this.getInferenceModel().getProperty(measurementProperty, DUL.hasDataValue);

        if (measurementPropertyStmt != null) {
            expression = measurementPropertyStmt.getObject().asLiteral().getString();
        }
        return expression;
    }

    private OntModel getInferenceModel() {
        return this.inferenceModel;
    }

}
