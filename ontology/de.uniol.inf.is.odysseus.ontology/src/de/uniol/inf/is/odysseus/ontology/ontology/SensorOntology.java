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
package de.uniol.inf.is.odysseus.ontology.ontology;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
import de.uniol.inf.is.odysseus.ontology.Activator;
import de.uniol.inf.is.odysseus.ontology.manager.impl.PredicateManagerImpl;
import de.uniol.inf.is.odysseus.ontology.manager.impl.QueryManagerImpl;
import de.uniol.inf.is.odysseus.ontology.manager.impl.SourceManagerImpl;
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

    private final SourceManagerImpl sourceManager;
    private final PredicateManagerImpl predicateManager;
    private final QueryManagerImpl queryManager;

    /**
     * Class constructor.
     * 
     */
    public SensorOntology() {
        this.sourceManager = new SourceManagerImpl(this.getABox());
        this.predicateManager = new PredicateManagerImpl(this.getABox());
        this.queryManager = new QueryManagerImpl(this.getABox());
    }

    /**
     * @param name
     *            The name of the sensing device
     * @param schema
     *            The schema of the sensing device stream
     */
    public void createSensingDevice(final SensingDevice sensingDevice) {
        this.sourceManager.createSensingDevice(sensingDevice);

    }

    public List<SensingDevice> getSensingdeviceByProperty(final SDFAttribute attribute) {
        return this.queryManager.getSensingDevicesByObservedProperty(attribute);
    }

    public List<SensingDevice> getAllSensingDevices() {
        return this.queryManager.getAllSensingDevices();
    }

    public SensingDevice getSensingDevice(final String name) {
        return this.queryManager.getSensingDevice(URI.create(ODYSSEUS.NS + name));
    }

    public Individual createCondition(final String name, final Number min, final Number max, final String unit) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        this.getABox().createClass(SSN.MeasurementRange.getURI());

        final Individual property = this.getABox().createIndividual(ODYSSEUS.NS + "temperature", SSN.Property);
        final Individual condition = this.getABox().createIndividual(ODYSSEUS.NS + name, property);
        this.getABox().add(condition, RDFS.subClassOf, SSN.Condition);
        // getABox().add(condition, RDF.type, SSN.MeasurementRange);

        this.getABox().createClass(DUL.Region.getURI());
        final Individual condition_interval = this.getABox().createIndividual(ODYSSEUS.NS + name + "_interval", DUL.Region);

        this.getABox().createClass(DUL.Amount.getURI());
        final Individual minValue = this.getABox().createIndividual(ODYSSEUS.NS + name + "_interval_inf", DUL.Amount);
        this.getABox().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        this.getABox().add(minValue, DUL.isClassifiedBy, unit);

        final Individual maxValue = this.getABox().createIndividual(ODYSSEUS.NS + name + "_interval_sup", DUL.Amount);
        this.getABox().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        this.getABox().add(maxValue, DUL.isClassifiedBy, unit);

        this.getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMinValue, minValue);

        this.getABox().add(condition_interval, ODYSSEUS.hasMeasurementPropertyMaxValue, maxValue);

        this.getABox().createObjectProperty(SSN.hasValue.getURI());
        this.getABox().add(condition, SSN.hasValue, condition_interval);

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
        return condition;
    }

    public Individual addMeasurementCapabilityFor(final String name, final Individual sensor, final Individual property, final Individual condition) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        this.getABox().createClass(SSN.MeasurementCapability.getURI());
        final Individual measurementCapability = this.getABox().createIndividual(ODYSSEUS.NS + name, SSN.MeasurementCapability);

        this.getABox().createObjectProperty(SSN.forProperty.getURI());
        this.getABox().add(measurementCapability, SSN.forProperty, property);

        if (condition != null) {
            this.getABox().createObjectProperty(SSN.inCondition.getURI());
            this.getABox().add(measurementCapability, SSN.inCondition, condition);
        }
        this.getABox().createObjectProperty(SSN.hasMeasurementCapability.getURI());
        this.getABox().add(sensor, SSN.hasMeasurementCapability, measurementCapability);

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
        return measurementCapability;
    }

    public Individual createProperty(final String name) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }

        this.getABox().createClass(SSN.Property.getURI());
        final Individual property = this.getABox().createIndividual(ODYSSEUS.NS + name, SSN.Property);

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
        return property;
    }

    public void addMeasurementProperty(final String name, final Individual measurementCapability, final Resource resource, final Number min, final Number max, final String unit) {
        if (this.getABox().supportsTransactions()) {
            this.getABox().begin();
        }
        this.getABox().createClass(resource.getURI());
        final Individual individual = this.getABox().createIndividual(ODYSSEUS.NS + name, resource);
        this.getABox().createResource(ODYSSEUS.NS + name, resource);

        this.getABox().createClass(DUL.Region.getURI());
        final Individual value = this.getABox().createIndividual(ODYSSEUS.NS + name + "_interval", DUL.Region);

        this.getABox().createClass(DUL.UnitOfMeasure.getURI());
        final Individual unitOfMeasure = this.getABox().createIndividual(ODYSSEUS.NS + unit, DUL.UnitOfMeasure);

        this.getABox().createClass(DUL.Amount.getURI());

        final Individual minValue = this.getABox().createIndividual(ODYSSEUS.NS + name + "_interval_inf", DUL.Amount);
        this.getABox().createDatatypeProperty(DUL.hasDataValue.getURI());
        this.getABox().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        this.getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        this.getABox().add(minValue, DUL.isClassifiedBy, unitOfMeasure);

        final Individual maxValue = this.getABox().createIndividual(ODYSSEUS.NS + name + "_interval_sup", DUL.Amount);
        this.getABox().createDatatypeProperty(DUL.hasDataValue.getURI());
        this.getABox().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        this.getABox().createObjectProperty(DUL.isClassifiedBy.getURI());
        this.getABox().add(maxValue, DUL.isClassifiedBy, unitOfMeasure);

        final ObjectProperty minValueProperty = this.getABox().createObjectProperty(ODYSSEUS.hasMeasurementPropertyMinValue.getURI());
        minValueProperty.addProperty(RDFS.subPropertyOf, DUL.hasPart);
        this.getABox().add(value, ODYSSEUS.hasMeasurementPropertyMinValue, minValue);

        final ObjectProperty maxValueProperty = this.getABox().createObjectProperty(ODYSSEUS.hasMeasurementPropertyMaxValue.getURI());
        maxValueProperty.addProperty(RDFS.subPropertyOf, DUL.hasPart);
        this.getABox().add(value, ODYSSEUS.hasMeasurementPropertyMaxValue, maxValue);

        this.getABox().createObjectProperty(SSN.hasValue.getURI());
        this.getABox().add(individual, SSN.hasValue, value);

        this.getABox().createObjectProperty(SSN.hasMeasurementProperty.getURI());
        this.getABox().add(measurementCapability, SSN.hasMeasurementProperty, individual);

        if (this.getABox().supportsTransactions()) {
            this.getABox().commit();
        }
    }

    public ResultSet findSensor(final String queryString) {
        // getABox().write(System.out, FileUtils.langXMLAbbrev);
        final StringBuilder prefix = new StringBuilder();
        prefix.append(SensorOntology.SSN_PREFIX);
        prefix.append(SensorOntology.XSD_PREFIX);
        prefix.append(SensorOntology.RDF_PREFIX);
        prefix.append(SensorOntology.RDFS_PREFIX);
        prefix.append(SensorOntology.OWL_PREFIX);
        prefix.append(SensorOntology.DUL_PREFIX);
        prefix.append(SensorOntology.QU_PREFIX);
        prefix.append(SensorOntology.ODY_PREFIX);
        final Query query = QueryFactory.create(prefix.toString() + queryString);
        query.setPrefix("rdfs", RDFS.getURI());
        final QueryExecution qExec = QueryExecutionFactory.create(query, this.getABox());

        final ResultSet result = qExec.execSelect();
        ResultSetFormatter.out(System.out, result, query);
        qExec.close();

        return result;
    }

    private Model getTBox() throws IOException {
        if (this.tBox == null) {
            this.tBox = ModelFactory.createDefaultModel();
            URL file;
            if (Activator.getContext() != null) {
                file = Activator.getContext().getBundle().getEntry("owl/SSN.owl");
            }
            else {
                file = SensorOntology.class.getResource("owl/SSN.owl");
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(file.openConnection().getInputStream()));
            this.tBox.read(reader, null);

        }
        return this.tBox;
    }

    private OntModel getABox() {
        if (this.model == null) {
            final File root = new File(this.STORE);
            root.mkdir();

            final ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());
            final Model aBox = maker.openModel(this.MODEL, false);

            Reasoner reasoner = null;
            try {
                reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(this.getTBox());
            }
            catch (ReasonerException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RULE_INF);
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
        return this.model;
    }

    public void out() {
        this.getABox().write(System.out, FileUtils.langXMLAbbrev);
    }

    public static void main(final String[] args) throws MalformedURLException {
        final SensorOntology sensorOntology = new SensorOntology();

        final SDFAttribute temperature = new SDFAttribute("sourceName", "temperature", SDFDatatype.DOUBLE);
        final SDFAttribute pressure = new SDFAttribute("sourceName", "temperature", SDFDatatype.DOUBLE);

        final Condition temperatureCondition = new Condition(URI.create(ODYSSEUS.NS + "temperatureCondition"), temperature, new Interval(2.0, 50.0));

        final MeasurementProperty pressureAccurancy = new MeasurementProperty(MeasurementProperty.Property.Accurancy, new Interval(0.0, 0.5));

        final MeasurementCapability pressureCapability = new MeasurementCapability(URI.create(ODYSSEUS.NS + "presureCapability"), pressure);
        pressureCapability.addCondition(temperatureCondition);
        pressureCapability.addMeasurementProperty(pressureAccurancy);

        final MeasurementCapability temperatureCapability = new MeasurementCapability(URI.create(ODYSSEUS.NS + "temperatureCapability"), temperature);

        final SensingDevice temperatureSensor = new SensingDevice(URI.create(ODYSSEUS.NS + "temperatureSensor"), new SDFSchema("local", Tuple.class, temperature));
        temperatureSensor.addMeasurementCapability(temperatureCapability);

        final SensingDevice pressureSensor = new SensingDevice(URI.create(ODYSSEUS.NS + "pressureSensor"), new SDFSchema("local", Tuple.class, pressure));
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
