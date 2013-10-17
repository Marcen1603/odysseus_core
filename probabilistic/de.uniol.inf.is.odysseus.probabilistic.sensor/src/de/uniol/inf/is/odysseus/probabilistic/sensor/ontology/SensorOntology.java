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
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.hp.hpl.jena.ontology.BooleanClassDescription;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
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
    private OntModel ontologyModel;
    private Model model;
    private Map m_anonIDs = new HashMap();
    private int m_anonCount = 0;

    public Individual createSensingDevice(String name, List<Individual> measurementCapabilities) {
        if (getModel().supportsTransactions()) {
            getModel().begin();
        }

        Individual sensingDevice = getOntologyModel().createIndividual(ODYSSEUS.NS + name, SSN.SensingDevice);

        for (Individual measurementCapability : measurementCapabilities) {
            getModel().add(sensingDevice, SSN.hasMeasurementCapability, measurementCapability);
        }
        getModel().add(sensingDevice, RDFS.label, name);
        getModel().add(sensingDevice, RDFS.subClassOf, SSN.SensingDevice);
        if (getModel().supportsTransactions()) {
            getModel().commit();
        }
        return sensingDevice;
    }

    public Individual createCondition(String name, Number min, Number max, String unit) {

        if (getModel().supportsTransactions()) {
            getModel().begin();
        }

        Individual condition = getOntologyModel().createIndividual(ODYSSEUS.NS + name, SSN.MeasurementRange);
        getModel().add(condition, RDF.type, SSN.MeasurementRange);
        getModel().add(condition, RDFS.label, name);

        Individual condition_interval = getOntologyModel().createIndividual(ODYSSEUS.NS + name, DUL.Region);
        getModel().add(condition_interval, RDF.type, DUL.Region);

        Individual minValue = getOntologyModel().createIndividual(ODYSSEUS.NS + name, DUL.Amount);
        getModel().add(minValue, RDF.type, DUL.Amount);
        getModel().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        getModel().add(minValue, DUL.isClassifiedBy, unit);

        Individual maxValue = getOntologyModel().createIndividual(ODYSSEUS.NS + name, DUL.Amount);
        getModel().add(maxValue, RDF.type, DUL.Amount);
        getModel().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        getModel().add(maxValue, DUL.isClassifiedBy, unit);

        getModel().add(condition_interval, SSN.hasMeasurementProperty, minValue);
        getModel().add(condition_interval, SSN.hasMeasurementProperty, maxValue);

        getModel().add(condition, SSN.hasValue, condition_interval);

        if (getModel().supportsTransactions()) {
            getModel().commit();
        }
        return condition;
    }

    public Individual createMeasurementCapabilityFor(String name, String property, Individual condition) {
        if (getModel().supportsTransactions()) {
            getModel().begin();
        }

        Individual measurementCapability = getOntologyModel().createIndividual(ODYSSEUS.NS + name, SSN.MeasurementCapability);
        getModel().add(measurementCapability, RDFS.label, name);
        getModel().add(measurementCapability, SSN.forProperty, property);
        if (condition != null) {
            getModel().add(measurementCapability, SSN.inCondition, condition);
        }
        getModel().add(measurementCapability, RDF.type, SSN.MeasurementCapability);

        if (getModel().supportsTransactions()) {
            getModel().commit();
        }
        return measurementCapability;
    }

    public void addMeasurementCapability(String name, Individual measurementCapability, Resource resource, Number min, Number max, String unit) {
        if (getModel().supportsTransactions()) {
            getModel().begin();
        }

        Individual individual = getOntologyModel().createIndividual(ODYSSEUS.NS + name, resource);
        getModel().add(individual, RDFS.label, name);
        getModel().add(individual, RDF.type, resource);

        Individual value = getOntologyModel().createIndividual(ODYSSEUS.NS + name, DUL.Region);
        getModel().add(value, RDF.type, DUL.Region);

        Individual minValue = getOntologyModel().createIndividual(ODYSSEUS.NS + name, DUL.Amount);
        getModel().add(minValue, RDF.type, DUL.Amount);
        getModel().add(minValue, DUL.hasDataValue, min.toString(), TypeMapper.getInstance().getTypeByValue(min));
        getModel().add(minValue, DUL.isClassifiedBy, unit);

        Individual maxValue = getOntologyModel().createIndividual(ODYSSEUS.NS + name, DUL.Amount);
        getModel().add(maxValue, RDF.type, DUL.Amount);
        getModel().add(maxValue, DUL.hasDataValue, max.toString(), TypeMapper.getInstance().getTypeByValue(max));
        getModel().add(maxValue, DUL.isClassifiedBy, unit);

        getModel().add(value, SSN.hasMeasurementProperty, minValue);
        getModel().add(value, SSN.hasMeasurementProperty, maxValue);

        getModel().add(individual, OWL.hasValue, value);

        getModel().add(measurementCapability, SSN.hasMeasurementCapability, individual);

        if (getModel().supportsTransactions()) {
            getModel().commit();
        }
    }

    public ResultSet findSensor(String queryString) {
        getModel().write(System.out, FileUtils.langTurtle);
        // Create an OWL reasoner
        Reasoner owlReasoner = ReasonerRegistry.getOWLReasoner();

        // Bind the reasoner to the WordNet ontology model
        Reasoner wnReasoner = owlReasoner.bindSchema(getOntologyModel());

        // Use the reasoner to create an inference model
        InfModel infModel = ModelFactory.createInfModel(wnReasoner, getModel());

        // infModel.setNsPrefix("ssn", SSN.getURI());
        // infModel.setNsPrefix("xsd", XSD.getURI());
        // infModel.setNsPrefix("rdf", RDF.getURI());
        // infModel.setNsPrefix("rdfs", RDFS.getURI());
        // infModel.setNsPrefix("owl", OWL.getURI());
        // infModel.setNsPrefix("ody", ODYSSEUS.getURI());
        // Query query =
        // QueryFactory.create("SELECT     ?SensingDevice WHERE { ?SensingDevice  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"Arduino\" }");
        Query query = QueryFactory.create(queryString);

        QueryExecution qExec = QueryExecutionFactory.create(query, infModel);
        ResultSet result = qExec.execSelect();

        ResultSetFormatter.out(System.out, result, query);
        qExec.close();

        return result;
    }

    private OntModel getOntologyModel() {
        if (ontologyModel == null) {
            ontologyModel = ModelFactory.createOntologyModel();
            URL file;
            if (Activator.getContext() != null) {
                file = Activator.getContext().getBundle().getEntry("SSN.owl");
            }
            else {
                file = SensorOntology.class.getResource("/SSN.owl");
            }
            ontologyModel.read(file.toExternalForm());

            ontologyModel.prepare();
        }
        return ontologyModel;
    }

    private Model getModel() {
        if (model == null) {
            File root = new File(STORE);
            System.out.println(root.getAbsolutePath());
            root.mkdir();
            ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());

            model = maker.openModel("sensors", false);
            model.setNsPrefix("ssn", SSN.getURI());
            model.setNsPrefix("xsd", XSD.getURI());
            model.setNsPrefix("rdf", RDF.getURI());
            model.setNsPrefix("rdfs", RDFS.getURI());
            model.setNsPrefix("owl", OWL.getURI());
            model.setNsPrefix("ody", ODYSSEUS.getURI());
        }
        return model;
    }

    public void describeClass(PrintStream out, OntClass cls) {
        renderClassDescription(out, cls);
        out.println();

        // sub-classes
        for (Iterator i = cls.listSuperClasses(true); i.hasNext();) {
            out.print("  is a sub-class of ");
            renderClassDescription(out, (OntClass) i.next());
            out.println();
        }

        // super-classes
        for (Iterator i = cls.listSubClasses(true); i.hasNext();) {
            out.print("  is a super-class of ");
            renderClassDescription(out, (OntClass) i.next());
            out.println();
        }
    }

    public void renderClassDescription(PrintStream out, OntClass c) {
        if (c.isUnionClass()) {
            renderBooleanClass(out, "union", c.asUnionClass());
        }
        else if (c.isIntersectionClass()) {
            renderBooleanClass(out, "intersection", c.asIntersectionClass());
        }
        else if (c.isComplementClass()) {
            renderBooleanClass(out, "complement", c.asComplementClass());
        }
        else if (c.isRestriction()) {
            renderRestriction(out, c.asRestriction());
        }
        else {
            if (!c.isAnon()) {
                out.print("Class ");
                renderURI(out, prefixesFor(c), c.getURI());
                out.print(' ');
            }
            else {
                renderAnonymous(out, c, "class");
            }
        }
    }

    protected void renderRestriction(PrintStream out, Restriction r) {
        if (!r.isAnon()) {
            out.print("Restriction ");
            renderURI(out, prefixesFor(r), r.getURI());
        }
        else {
            renderAnonymous(out, r, "restriction");
        }

        out.println();

        renderRestrictionElem(out, "    on property", r.getOnProperty());
        out.println();

        if (r.isAllValuesFromRestriction()) {
            renderRestrictionElem(out, "    all values from", r.asAllValuesFromRestriction().getAllValuesFrom());
        }
        if (r.isSomeValuesFromRestriction()) {
            renderRestrictionElem(out, "    some values from", r.asSomeValuesFromRestriction().getSomeValuesFrom());
        }
        if (r.isHasValueRestriction()) {
            renderRestrictionElem(out, "    has value", r.asHasValueRestriction().getHasValue());
        }
    }

    protected void renderRestrictionElem(PrintStream out, String desc, RDFNode value) {
        out.print(desc);
        out.print(" ");
        renderValue(out, value);
    }

    protected void renderValue(PrintStream out, RDFNode value) {
        if (value.canAs(OntClass.class)) {
            renderClassDescription(out, (OntClass) value.as(OntClass.class));
        }
        else if (value instanceof Resource) {
            Resource r = (Resource) value;
            if (r.isAnon()) {
                renderAnonymous(out, r, "resource");
            }
            else {
                renderURI(out, r.getModel(), r.getURI());
            }
        }
        else if (value instanceof Literal) {
            out.print(((Literal) value).getLexicalForm());
        }
        else {
            out.print(value);
        }
    }

    protected void renderURI(PrintStream out, PrefixMapping prefixes, String uri) {
        out.print(prefixes.shortForm(uri));
    }

    protected PrefixMapping prefixesFor(Resource n) {
        return n.getModel().getGraph().getPrefixMapping();
    }

    protected void renderAnonymous(PrintStream out, Resource anon, String name) {
        String anonID = (String) m_anonIDs.get(anon.getId());
        if (anonID == null) {
            anonID = "a-" + m_anonCount++;
            m_anonIDs.put(anon.getId(), anonID);
        }

        out.print("Anonymous ");
        out.print(name);
        out.print(" with ID ");
        out.print(anonID);
    }

    protected void renderBooleanClass(PrintStream out, String op, BooleanClassDescription boolClass) {
        out.print(op);
        out.println(" of {");

        for (Iterator i = boolClass.listOperands(); i.hasNext();) {
            out.print("      ");
            renderClassDescription(out, (OntClass) i.next());
            out.println();
        }
        out.print("  } ");
    }

    public static void main(String[] args) throws MalformedURLException {
        SensorOntology sensorOntology = new SensorOntology();
        // OntModel ontologyModel = ModelFactory.createOntologyModel();
        //
        // URL file;
        // if (Activator.getContext() != null) {
        // file = Activator.getContext().getBundle().getEntry("SSN.owl");
        // }
        // else {
        // file = SensorOntology.class.getResource("/SSN.owl");
        // }
        // ontologyModel.read(file.toExternalForm());
        //
        // ontologyModel.prepare();
        // // list the asserted types
        // ExtendedIterator<OntClass> classes = ontologyModel.listClasses();
        // while (classes.hasNext()) {
        // OntClass ontClass = classes.next();
        // if (!ontClass.isAnon()) {
        // String label = ontClass.getLabel(null);
        // String comment = ontClass.getComment(null);
        // if (ontClass.getNameSpace().equalsIgnoreCase(SSN.NS)) {
        // // if (comment != null) {
        // // System.out.println("/** " + label + ": " +
        // // comment.replace("\n", "") + " */");
        // // }
        // // else {
        // // System.out.println("/** " + label + ".*/");
        // // }
        // // System.out.println("public static final Resource " +
        // // ontClass.getLocalName() +
        // // " = m_model.createResource(NS + \"" +
        // // ontClass.getLocalName() + "\");")
        // }
        // // System.out.println();
        // }
        // }
        // // list the asserted properties
        // ExtendedIterator<OntProperty> properties =
        // ontologyModel.listAllOntProperties();
        // while (properties.hasNext()) {
        // OntProperty ontProperty = properties.next();
        //
        // String label = ontProperty.getLabel(null);
        // String comment = ontProperty.getComment(null);
        // if (ontProperty.getNameSpace().equalsIgnoreCase(DUL.NS)) {
        // if (comment != null) {
        // System.out.println("/** " + label + ": " + comment.replace("\n", "")
        // + " */");
        // }
        // else {
        // System.out.println("/** " + label + ".*/");
        // }
        // System.out.println("public static final Property " +
        // ontProperty.getLocalName() + " = m_model.createProperty(NS + \"" +
        // ontProperty.getLocalName() + "\");");
        // }
        // // System.out.println();
        // }

        Individual condition = sensorOntology.createCondition("condition", 2, 3, "Celsius");

        List<Individual> capabilities = new ArrayList<Individual>();
        capabilities.add(sensorOntology.createMeasurementCapabilityFor("capability1", "temperature", condition));
        capabilities.add(sensorOntology.createMeasurementCapabilityFor("capability2", "presure", condition));

        Individual sensingDevice = sensorOntology.createSensingDevice("sensor", capabilities);

        sensorOntology.addMeasurementCapability("accuracy", capabilities.get(0), SSN.Accuracy, 3.0, 5.0, "Meter");

        sensorOntology.findSensor("SELECT * WHERE { ?MeasurementCapability <http://purl.oclc.org/NET/ssnx/ssn#forProperty> \"presure\" }");
    }
}
