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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.ontology.manager.QueryManager;
import de.uniol.inf.is.odysseus.ontology.model.Condition;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
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
    @Override
    public SensingDevice getSensingDevice(final URI uri) {
        final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();

        final List<URI> propertyURIs = this.getAllPropertyURIsObservedBySensingDevice(uri);
        for (final URI propertyURI : propertyURIs) {
            final Resource propertyResource = this.getABox().getResource(propertyURI.toString());
            final String sourceName = propertyResource.toString().substring(0, propertyResource.toString().lastIndexOf("#"));
            attributes.add(new SDFAttribute(sourceName, propertyURI.getFragment(), SDFDatatype.OBJECT));
        }
        final SensingDevice sensingDevice = new SensingDevice(uri, new SDFSchema("", IStreamObject.class, attributes));
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
    @Override
    public List<SensingDevice> getSensingDevicesByObservedProperty(final SDFAttribute attribute) {
        return this.getSensingDevicesByObservedProperty(URI.create(ODYSSEUS.NS + attribute.getAttributeName()));
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
        final Statement attributeStmt = this.getABox().getProperty(measurementCapabilityResource, SSN.forProperty);
        if (attributeStmt != null) {
            final URI attributeURI = URI.create(attributeStmt.getResource().getURI());
            final String sourceName = attributeURI.toString().substring(0, attributeURI.toString().lastIndexOf("#"));
            final SDFAttribute attribute = new SDFAttribute(sourceName, attributeURI.getFragment(), SDFDatatype.OBJECT);
            final MeasurementCapability measurementCapability = new MeasurementCapability(attributeURI, attribute);

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
        final Statement attributeStmt = this.getABox().getProperty(condition, RDFS.subClassOf);
        if (attributeStmt != null) {
            final URI attributeURI = URI.create(attributeStmt.getResource().getURI());
            final String sourceName = attributeURI.toString().substring(0, attributeURI.toString().lastIndexOf("#"));
            final SDFAttribute attribute = new SDFAttribute(sourceName, attributeURI.getFragment(), SDFDatatype.OBJECT);
            final Interval interval = this.getConditionInterval(condition);
            return new Condition(attribute, interval);
        }
        return null;
    }

    private MeasurementProperty getMeasurementProperty(final Resource measurementProperty) {

        final Interval interval = this.getMeasurementPropertyInterval(measurementProperty);
        // FIXME Use correct property class

        if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Accurancy.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Accurancy, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.DetectionLimit.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.DetectionLimit, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Drift.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Drift, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Frequency.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Frequency, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Latency.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Latency, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.MeasurementRange.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.MeasurementRange, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Precision.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Precision, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.ResponseTime.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.ResponseTime, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Resolution.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Resolution, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Sensitivity.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Sensitivity, interval);
        }
        else if (measurementProperty.getPropertyResourceValue(RDFS.subClassOf).equals(MeasurementProperty.Property.Selectivity.getResource())) {
            return new MeasurementProperty(MeasurementProperty.Property.Selectivity, interval);
        }
        return new MeasurementProperty(MeasurementProperty.Property.Accurancy, interval);

    }

    private Interval getConditionInterval(final Resource condition) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;

        final Statement conditionStmt = this.getABox().getProperty(condition, SSN.hasValue);
        if (conditionStmt != null) {
            final Resource region = conditionStmt.getResource();
            final Statement minValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMeasurementPropertyMinValue);
            final Statement maxValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMeasurementPropertyMaxValue);
            if (minValueStmt != null) {
                min = this.getABox().getProperty(minValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getDouble();
            }
            if (maxValueStmt != null) {
                max = this.getABox().getProperty(maxValueStmt.getObject().asResource(), DUL.hasDataValue).getObject().asLiteral().getDouble();
            }
        }
        return new Interval(min, max);
    }

    private Interval getMeasurementPropertyInterval(final Resource measurementProperty) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;

        final Statement measurementPropertyStmt = this.getABox().getProperty(measurementProperty, SSN.hasValue);
        if (measurementPropertyStmt != null) {
            final Resource region = measurementPropertyStmt.getResource();
            final Statement minValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMeasurementPropertyMinValue);
            final Statement maxValueStmt = this.getABox().getProperty(region, ODYSSEUS.hasMeasurementPropertyMaxValue);
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
