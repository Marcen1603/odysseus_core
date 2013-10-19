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
package de.uniol.inf.is.odysseus.probabilistic.sensor.ontology;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.probabilistic.sensor.Activator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "all" })
public class SensorOntology {
    private final String STORE = "sensorstore";
    private final String MODEL = "sensors";
    private Model tBox;
    private OntModel model;
    private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI() + "> ";
    private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI() + "> ";
    private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI() + "> ";
    private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI() + "> ";
    private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI() + "> ";
    private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI() + "> ";
    private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
    private static final String ODY_PREFIX = "PREFIX ody: <" + ODYSSEUS.getURI() + "> ";

    public Individual createSensingDevice(String name) {
        return createSensingDevice(name, null, new ArrayList<Individual>(), new ArrayList<Individual>());
    }

    public Individual createSensingDevice(String name, String[] propertyNames) {
        List<Individual> properties = new ArrayList<Individual>();
        for (String propertyName : propertyNames) {
            Individual property = createProperty(propertyName);
            properties.add(property);
        }
        return createSensingDevice(name, null, properties, new ArrayList<Individual>());
    }

    public Individual createSensingDevice(String name, List<Individual> properties) {
        return createSensingDevice(name, null, properties, new ArrayList<Individual>());
    }

    public Individual createSensingDevice(String name, String comment, List<Individual> properties, List<Individual> measurementCapabilities) {
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

    public Individual createCondition(String name, Number min, Number max, String unit) {
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

    public Individual addMeasurementCapabilityFor(String name, Individual sensor, Individual property, Individual condition) {
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

    public Individual createProperty(String name) {
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

    public void addMeasurementProperty(String name, Individual measurementCapability, Resource resource, Number min, Number max, String unit) {
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

    public ResultSet findSensor(String queryString) {
        // getABox().write(System.out, FileUtils.langXMLAbbrev);
        StringBuilder prefix = new StringBuilder();
        prefix.append(SSN_PREFIX);
        prefix.append(XSD_PREFIX);
        prefix.append(RDF_PREFIX);
        prefix.append(RDFS_PREFIX);
        prefix.append(OWL_PREFIX);
        prefix.append(DUL_PREFIX);
        prefix.append(QU_PREFIX);
        prefix.append(ODY_PREFIX);
        Query query = QueryFactory.create(prefix.toString() + queryString);
        query.setPrefix("rdfs", RDFS.getURI());
        QueryExecution qExec = QueryExecutionFactory.create(query, getABox());

        ResultSet result = qExec.execSelect();
        ResultSetFormatter.out(System.out, result, query);
        qExec.close();

        return result;
    }

    private Model getTBox() {
        if (tBox == null) {
            tBox = ModelFactory.createDefaultModel();
            URL file;
            if (Activator.getContext() != null) {
                file = Activator.getContext().getBundle().getEntry("SSN.owl");
            }
            else {
                file = SensorOntology.class.getResource("/SSN.owl");
            }
            tBox.read(file.toExternalForm());

        }
        return tBox;
    }

    private OntModel getABox() {
        if (model == null) {
            File root = new File(STORE);
            root.mkdir();

            ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());
            Model aBox = maker.openModel(MODEL, false);

            Reasoner reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(getTBox());
            OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RULE_INF);
            spec.setReasoner(reasoner);
            this.model = ModelFactory.createOntologyModel(spec, aBox);
            this.model.setNsPrefix("ssn", SSN.getURI());
            this.model.setNsPrefix("xsd", XSD.getURI());
            this.model.setNsPrefix("rdf", RDF.getURI());
            this.model.setNsPrefix("rdfs", RDFS.getURI());
            this.model.setNsPrefix("owl", OWL.getURI());
            this.model.setNsPrefix("dul", DUL.getURI());
            this.model.setNsPrefix("qu", QU.getURI());
            this.model.setNsPrefix("ody", ODYSSEUS.getURI());
        }
        return model;
    }

    public static void main(String[] args) throws MalformedURLException {
        SensorOntology sensorOntology = new SensorOntology();

        Individual condition = sensorOntology.createCondition("condition", 2, 3, "Celsius");

        Individual position = sensorOntology.createProperty("position");
        Individual temperature = sensorOntology.createProperty("temperature");

        Individual sensingDevice = sensorOntology.createSensingDevice("sensor2", Arrays.asList(new Individual[] { position, temperature }));

        List<Individual> capabilities = new ArrayList<Individual>();
        capabilities.add(sensorOntology.addMeasurementCapabilityFor("capability1", sensingDevice, temperature, condition));
        capabilities.add(sensorOntology.addMeasurementCapabilityFor("capability2", sensingDevice, position, condition));

        sensorOntology.addMeasurementProperty("accuracy", capabilities.get(1), SSN.Accuracy, 3.0, 5.0, "Meter");

        sensorOntology.findSensor("SELECT ?uri  WHERE { ?uri ssn:observes ody:temperature }");
    }
}
