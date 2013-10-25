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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerException;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sensor.Activator;
import de.uniol.inf.is.odysseus.probabilistic.sensor.manager.impl.PredicateManagerImpl;
import de.uniol.inf.is.odysseus.probabilistic.sensor.manager.impl.QueryManagerImpl;
import de.uniol.inf.is.odysseus.probabilistic.sensor.manager.impl.SourceManagerImpl;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementProperty;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.QU;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.SSN;

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

    private SourceManagerImpl sourceManager;
    private PredicateManagerImpl predicateManager;
    private QueryManagerImpl queryManager;

    /**
     * Class constructor.
     * 
     */
    public SensorOntology() {
        this.sourceManager = new SourceManagerImpl(getABox());
        this.predicateManager = new PredicateManagerImpl(getABox());
        this.queryManager = new QueryManagerImpl(getABox());
    }

    /**
     * @param name
     *            The name of the sensing device
     * @param schema
     *            The schema of the sensing device stream
     */
    public void createSensingDevice(SensingDevice sensingDevice) {
        sourceManager.createSensingDevice(sensingDevice);

    }

    public List<SensingDevice> getSensingdeviceByProperty(SDFAttribute attribute) {
        return queryManager.getSensingDevicesByObservedProperty(attribute);
    }

    public List<SensingDevice> getAllSensingDevices() {
        return queryManager.getAllSensingDevices();
    }

    public SensingDevice getSensingDevice(String name) {
        return queryManager.getSensingDevice(URI.create(ODYSSEUS.NS + name));
    }

    public Individual createCondition(String name, Number min, Number max, String unit) {
        if (getABox().supportsTransactions()) {
            getABox().begin();
        }

        getABox().createClass(SSN.MeasurementRange.getURI());

        Individual property = getABox().createIndividual(ODYSSEUS.NS + "temperature", SSN.Property);
        Individual condition = getABox().createIndividual(ODYSSEUS.NS + name, property);
        getABox().add(condition, RDFS.subClassOf, SSN.Condition);
        // getABox().add(condition, RDF.type, SSN.MeasurementRange);

        getABox().createClass(DUL.Region.getURI());
        Individual condition_interval = getABox().createIndividual(ODYSSEUS.NS + name + "_interval", DUL.Region);

        getABox().createClass(DUL.Amount.getURI());
        Individual minValue = getABox().createIndividual(ODYSSEUS.NS + name + "_interval_inf", DUL.Amount);
        getABox().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        getABox().add(minValue, DUL.isClassifiedBy, unit);

        Individual maxValue = getABox().createIndividual(ODYSSEUS.NS + name + "_interval_sup", DUL.Amount);
        getABox().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        getABox().add(maxValue, DUL.isClassifiedBy, unit);

        getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMinValue, minValue);

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
        getABox().add(value, ODYSSEUS.hasMeasurementPropertyMaxValue, maxValue);

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

    private Model getTBox() throws IOException {
        if (tBox == null) {
            tBox = ModelFactory.createDefaultModel();
            URL file;
            if (Activator.getContext() != null) {
                file = Activator.getContext().getBundle().getEntry("owl/SSN.owl");
            }
            else {
                file = SensorOntology.class.getResource("owl/SSN.owl");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.openConnection().getInputStream()));
            tBox.read(reader, null);

        }
        return tBox;
    }

    private OntModel getABox() {
        if (model == null) {
            File root = new File(STORE);
            root.mkdir();

            ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());
            Model aBox = maker.openModel(MODEL, false);

            Reasoner reasoner = null;
            try {
                reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(getTBox());
            }
            catch (ReasonerException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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

    public void out() {
        getABox().write(System.out, FileUtils.langXMLAbbrev);
    }

    public static void main(String[] args) throws MalformedURLException {
        SensorOntology sensorOntology = new SensorOntology();

        SDFAttribute temperature = new SDFAttribute("sourceName", "temperature", SDFDatatype.DOUBLE);
        SDFAttribute pressure = new SDFAttribute("sourceName", "temperature", SDFDatatype.DOUBLE);

        Condition temperatureCondition = new Condition(URI.create(ODYSSEUS.NS + "temperatureCondition"), temperature, new Interval(2.0, 50.0));

        MeasurementProperty pressureAccurancy = new MeasurementProperty(MeasurementProperty.Property.Accurancy, new Interval(0.0, 0.5));

        MeasurementCapability pressureCapability = new MeasurementCapability(URI.create(ODYSSEUS.NS + "presureCapability"), pressure);
        pressureCapability.addCondition(temperatureCondition);
        pressureCapability.addMeasurementProperty(pressureAccurancy);

        MeasurementCapability temperatureCapability = new MeasurementCapability(URI.create(ODYSSEUS.NS + "temperatureCapability"), temperature);

        SensingDevice temperatureSensor = new SensingDevice(URI.create(ODYSSEUS.NS + "temperatureSensor"), new SDFSchema("local", Tuple.class, temperature));
        temperatureSensor.addMeasurementCapability(temperatureCapability);

        SensingDevice pressureSensor = new SensingDevice(URI.create(ODYSSEUS.NS + "pressureSensor"), new SDFSchema("local", Tuple.class, pressure));
        pressureSensor.addMeasurementCapability(pressureCapability);

        sensorOntology.createSensingDevice(temperatureSensor);
        sensorOntology.createSensingDevice(pressureSensor);
        //
        // Individual condition = sensorOntology.createCondition("condition", 2,
        // 3, "Celsius");
        //
        // Individual position = sensorOntology.createProperty("position");
        // Individual temperature =
        // sensorOntology.createProperty("temperature");
        //
        // Individual sensingDevice =
        // sensorOntology.createSensingDevice("sensor2", Arrays.asList(new
        // Individual[] { position, temperature }));
        //
        // List<Individual> capabilities = new ArrayList<Individual>();
        // capabilities.add(sensorOntology.addMeasurementCapabilityFor("capability1",
        // sensingDevice, temperature, condition));
        // capabilities.add(sensorOntology.addMeasurementCapabilityFor("capability2",
        // sensingDevice, position, condition));
        //
        // sensorOntology.addMeasurementProperty("accuracy",
        // capabilities.get(1), SSN.Accuracy, 3.0, 5.0, "Meter");
        sensorOntology.out();
        // sensorOntology.findSensor("SELECT ?uri  WHERE { ?uri ssn:observes ody:temperature }");
        // sensorOntology.findSensor("SELECT ?uri  WHERE { ?uri ssn:inCondition ody:temperature }");
    }

}
