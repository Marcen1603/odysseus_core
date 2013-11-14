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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.manager.QueryManager;
import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.model.condition.IntervalCondition;
import de.uniol.inf.is.odysseus.ontology.model.property.Accuracy;
import de.uniol.inf.is.odysseus.ontology.model.property.DetectionLimit;
import de.uniol.inf.is.odysseus.ontology.model.property.Drift;
import de.uniol.inf.is.odysseus.ontology.model.property.Frequency;
import de.uniol.inf.is.odysseus.ontology.model.property.Latency;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementRange;
import de.uniol.inf.is.odysseus.ontology.model.property.Precision;
import de.uniol.inf.is.odysseus.ontology.model.property.Resolution;
import de.uniol.inf.is.odysseus.ontology.model.property.ResponseTime;
import de.uniol.inf.is.odysseus.ontology.model.property.Selectivity;
import de.uniol.inf.is.odysseus.ontology.model.property.Sensitivity;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.QU;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class QueryManagerImpl implements QueryManager {
    private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI() + "> ";
    private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI() + "> ";
    private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI() + "> ";
    private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI() + "> ";
    private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI() + "> ";
    private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI() + "> ";
    private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
    private static final String ODY_PREFIX = "PREFIX ody: <" + ODYSSEUS.getURI() + "> ";
    private static final String QUERY_PREFIX;
    private final OntModel aBox;

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
    public QueryManagerImpl(final OntModel aBox) {
        this.aBox = aBox;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<URI> getAllSensingDeviceURIs() {
        final String queryString = "SELECT ?uri  WHERE { ?uri rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.SensingDevice.getLocalName() + " }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<URI> getAllFeatureOfInterestURIs() {
        final String queryString = "SELECT ?uri  WHERE { ?uri rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.FeatureOfInterest.getLocalName() + " }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getAllSensingDevices() {
        final List<URI> uris = this.getAllSensingDeviceURIs();
        final List<SensingDevice> sensingDevices = new ArrayList<SensingDevice>();
        for (final URI uri : uris) {
            final SensingDevice sensingDevice = this.getSensingDevice(uri);
            if (sensingDevice != null) {
                sensingDevices.add(sensingDevice);
            }
        }
        return sensingDevices;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<FeatureOfInterest> getAllFeaturesOfInterest() {
        final List<URI> uris = this.getAllSensingDeviceURIs();
        final List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
        for (final URI uri : uris) {
            final FeatureOfInterest featureOfInterest = this.getFeatureOfInterest(uri);
            if (featureOfInterest != null) {
                featuresOfInterest.add(featureOfInterest);
            }
        }
        return featuresOfInterest;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public SensingDevice getSensingDevice(final URI uri) {
        final SensingDevice sensingDevice = new SensingDevice(uri);
        final List<URI> measurementCapabilityURIs = this.getAllMeasurementCapabilityURIsFromSensingDevice(uri);
        for (final URI measurementCapabilityURI : measurementCapabilityURIs) {
            final Resource measurementCapabilityResource = this.getABox().getResource(measurementCapabilityURI.toString());
            final MeasurementCapability measurementCapability = this.getMeasurementCapability(measurementCapabilityResource);
            if (measurementCapability != null) {
                sensingDevice.addMeasurementCapability(measurementCapability);
            }
        }

        return sensingDevice;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public FeatureOfInterest getFeatureOfInterest(final URI uri) {
        final FeatureOfInterest featureOfInterest = new FeatureOfInterest(uri);
        final List<URI> propertyURIs = this.getAllPropertyURIsByFeatureOfInterest(uri);
        for (final URI propertyURI : propertyURIs) {
            final Resource propertyResource = this.getABox().getResource(propertyURI.toString());
            final Property property = new Property(URI.create(propertyResource.getURI()));
            featureOfInterest.addProperty(property);
        }

        return featureOfInterest;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<URI> getAllPropertyURIs() {
        final String queryString = "SELECT ?uri  WHERE { ?uri rdf:" + RDF.type.getLocalName() + " ssn:" + SSN.Property.getLocalName() + " }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getSensingDevicesByObservedProperty(final URI uri) {
        final List<URI> sensingDeviceURIs = this.getSensingDeviceURIsByObservedProperty(uri);
        final List<SensingDevice> sensingDevices = new ArrayList<SensingDevice>();
        for (final URI sensingDeviceURI : sensingDeviceURIs) {
            sensingDevices.add(this.getSensingDevice(sensingDeviceURI));
        }
        return sensingDevices;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<FeatureOfInterest> getFeaturesOfInterestByProperty(final URI uri) {
        final List<URI> featureOfInterestURIs = this.getFeatureOfInterestURIsByProperty(uri);
        final List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
        for (final URI featureOfInterestURI : featureOfInterestURIs) {
            featuresOfInterest.add(this.getFeatureOfInterest(featureOfInterestURI));
        }
        return featuresOfInterest;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getSensingDevicesByObservedProperty(final SDFAttribute attribute) {
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
    public List<FeatureOfInterest> getFeaturesOfInterestByProperty(final SDFAttribute attribute) {
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
    @Override
    public List<URI> getSensingDeviceURIsByObservedProperty(final URI uri) {
        final String queryString = "SELECT ?uri  WHERE { ?uri ssn:" + SSN.observes.getLocalName() + " <" + uri.toString() + "> }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<URI> getFeatureOfInterestURIsByProperty(final URI uri) {
        final String queryString = "SELECT ?uri  WHERE { ?uri ssn:" + SSN.hasProperty.getLocalName() + " <" + uri.toString() + "> }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<URI> getAllPropertyURIsObservedBySensingDevice(final URI uri) {
        final String queryString = "SELECT ?uri  WHERE { <" + uri.toString() + "> ssn:" + SSN.observes.getLocalName() + " ?uri }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public List<URI> getAllPropertyURIsByFeatureOfInterest(final URI uri) {
        final String queryString = "SELECT ?uri  WHERE { <" + uri.toString() + "> ssn:" + SSN.hasProperty.getLocalName() + " ?uri }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    private List<URI> getAllMeasurementCapabilityURIsFromSensingDevice(final URI uri) {
        final String queryString = "SELECT ?uri  WHERE { <" + uri.toString() + "> ssn:" + SSN.hasMeasurementCapability.getLocalName() + " ?uri }";
        final Query query = QueryFactory.create(QUERY_PREFIX + queryString);
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());
        final ResultSet result = qExec.execSelect();
        final List<URI> uris = new ArrayList<URI>();
        while (result.hasNext()) {
            final QuerySolution solution = result.next();
            uris.add(URI.create(solution.get("uri").asResource().getURI()));
        }
        qExec.close();
        return uris;
    }

    private MeasurementCapability getMeasurementCapability(final Resource measurementCapabilityResource) {
        final Statement propertyStmt = this.getABox().getProperty(measurementCapabilityResource, SSN.forProperty);
        if (propertyStmt != null) {
            final Property property = new Property(URI.create(propertyStmt.getResource().getURI()));
            final MeasurementCapability measurementCapability = new MeasurementCapability(URI.create(measurementCapabilityResource.getURI()), property);

            QueryExecution qExec;
            ResultSet result;

            final String conditionQueryString = "SELECT ?uri  WHERE { <" + measurementCapabilityResource.getURI().toString() + "> ssn:" + SSN.inCondition.getLocalName() + " ?uri }";
            final Query conditionQuery = QueryFactory.create(QUERY_PREFIX + conditionQueryString);

            qExec = QueryExecutionFactory.create(conditionQuery, this.getABox());
            result = qExec.execSelect();
            while (result.hasNext()) {
                final QuerySolution solution = result.next();
                final Condition condition = this.getConditon(solution.get("uri").asResource());
                if (condition != null) {
                    measurementCapability.addCondition(condition);
                }
            }
            qExec.close();

            final String measurementPropertyQueryString = "SELECT ?uri  WHERE { <" + measurementCapabilityResource.getURI().toString() + "> ssn:" + SSN.hasMeasurementProperty.getLocalName()
                    + " ?uri }";
            final Query measurementPropertyQuery = QueryFactory.create(QUERY_PREFIX + measurementPropertyQueryString);
            qExec = QueryExecutionFactory.create(measurementPropertyQuery, this.getABox());
            result = qExec.execSelect();
            while (result.hasNext()) {
                final QuerySolution solution = result.next();
                final MeasurementProperty measurementProperty = this.getMeasurementProperty(solution.get("uri").asResource());
                if (measurementProperty != null) {
                    measurementCapability.addMeasurementProperty(measurementProperty);
                }
            }
            qExec.close();
            return measurementCapability;
        }
        return null;
    }

    private Condition getConditon(final Resource condition) {
        final Statement propertyStmt = this.getABox().getProperty(condition, RDFS.subClassOf);
        final Property property;
        if (propertyStmt != null) {
            property = new Property(URI.create(propertyStmt.getResource().getURI()));
        }
        else {
            property = new Property(URI.create(condition.getURI()));
        }

        String expression = getConditionExpression(condition);
        if (expression == null) {
            final Interval interval = this.getConditionInterval(condition);
            return new IntervalCondition(URI.create(condition.getURI()), property, interval);
        }
        else {
            return new ExpressionCondition(URI.create(condition.getURI()), property, expression);
        }
    }

    private MeasurementProperty getMeasurementProperty(final Resource measurementProperty) {

        @SuppressWarnings("unused")
        final Interval interval = this.getMeasurementPropertyInterval(measurementProperty);
        // FIXME Use correct property class

        if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Accuracy)) {
            return new Accuracy(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.DetectionLimit)) {
            return new DetectionLimit(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Drift)) {
            return new Drift(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Frequency)) {
            return new Frequency(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Latency)) {
            return new Latency(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.MeasurementRange)) {
            return new MeasurementRange(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Precision)) {
            return new Precision(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.ResponseTime)) {
            return new ResponseTime(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Resolution)) {
            return new Resolution(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Sensitivity)) {
            return new Sensitivity(URI.create(measurementProperty.getURI()));
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(SSN.Selectivity)) {
            return new Selectivity(URI.create(measurementProperty.getURI()));
        }
        return new MeasurementProperty(URI.create(measurementProperty.getURI()), measurementProperty);

    }

    private Interval getConditionInterval(final Resource condition) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;

        final Statement conditionStmt = this.getABox().getProperty(condition, SSN.hasValue);
        if (conditionStmt != null) {
            final Resource region = conditionStmt.getResource();
            final Statement minValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMinValue);
            final Statement maxValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMaxValue);
            if (minValueStmt != null) {
                min = this.getABox().getProperty(minValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getDouble();
            }
            if (maxValueStmt != null) {
                max = this.getABox().getProperty(maxValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getDouble();
            }
        }
        return new Interval(min, max);
    }

    private String getConditionExpression(final Resource condition) {
        String expression = null;

        final Statement conditionStmt = this.getABox().getProperty(condition, SSN.hasValue);
        if (conditionStmt != null) {
            final Resource region = conditionStmt.getResource();
            final Statement expressionValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasExpression);
            if (expressionValueStmt != null) {
                expression = this.getABox().getProperty(expressionValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getString();
            }

        }
        return expression;
    }

    private Interval getMeasurementPropertyInterval(final Resource measurementProperty) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;

        final Statement measurementPropertyStmt = this.getABox().getProperty(measurementProperty, SSN.hasValue);
        if (measurementPropertyStmt != null) {
            final Resource region = measurementPropertyStmt.getResource();
            final Statement minValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMinValue);
            final Statement maxValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMaxValue);
            if (minValueStmt != null) {
                min = this.getABox().getProperty(minValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getDouble();
            }
            if (maxValueStmt != null) {
                max = this.getABox().getProperty(maxValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getDouble();
            }
        }
        return new Interval(min, max);
    }

    private OntModel getABox() {
        return this.aBox;
    }

}
