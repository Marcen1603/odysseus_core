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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hp.hpl.jena.ontology.BooleanClassDescription;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
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
@SuppressWarnings({"all"})
public class SensorOntology {

    private static final String SOURCE = "http://purl.oclc.org/NET/ssnx/ssn";
    private static final String NS = SOURCE + "#";
    private static final String ONS = "ssn";

    private Map m_anonIDs = new HashMap();
    private int m_anonCount = 0;

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
        OntModel ontologyModel = ModelFactory.createOntologyModel();

        URL file;
        if (Activator.getContext() != null) {
            file = Activator.getContext().getBundle().getEntry("SSN.owl");
        }
        else {
            file = SensorOntology.class.getResource("/SSN.owl");
        }
        ontologyModel.read(file.toExternalForm());

        ontologyModel.prepare();
        // list the asserted types
        ExtendedIterator<OntClass> classes = ontologyModel.listClasses();
        while (classes.hasNext()) {
            OntClass ontClass = classes.next();
            if (!ontClass.isAnon()) {
                String label = ontClass.getLabel(null);
                String comment = ontClass.getComment(null);
                if (comment != null) {
                    System.out.println(label + "\n\t" + comment + "...");
                }
                else {
                    System.out.println(label);
                }
                System.out.println();
            }
        }
        // m.write(System.out, "Turtle");
        OntClass accuracy = ontologyModel.getOntClass(NS + "Accuracy");
        OntClass frequency = ontologyModel.getOntClass(NS + "Frequency");
        OntClass precision = ontologyModel.getOntClass(NS + "Precision");
        OntClass resolution = ontologyModel.getOntClass(NS + "Resolution");
        OntClass drift = ontologyModel.getOntClass(NS + "Drift");
        OntClass latency = ontologyModel.getOntClass(NS + "Latency");
        OntClass detectionLimit = ontologyModel.getOntClass(NS + "Detection Limit");
        OntClass measurementRange = ontologyModel.getOntClass(NS + "Measurement Range");
        OntClass responseTime = ontologyModel.getOntClass(NS + "Response Time");
        OntClass sensitivity = ontologyModel.getOntClass(NS + "Sensitivity");
        OntClass selectivity = ontologyModel.getOntClass(NS + "Selectivity");

        OntClass measurementProperty = ontologyModel.getOntClass(NS + "MeasurementProperty");
        OntClass sensingDevice = ontologyModel.getOntClass(NS + "SensingDevice");
        OntClass sensor = ontologyModel.getOntClass(NS + "sensor");

        Property hasMeasurementProperty = ontologyModel.getProperty(NS + "has measurement property");

        sensorOntology.describeClass(System.out, accuracy);

        sensorOntology.describeClass(System.out, sensingDevice);
        // m.write(System.out, "Turtle");
        // Create a new query passing a String containing the RDQL to execute

        File root = new File("sensorstore");
        root.mkdir();
        ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());

        Model model = maker.openModel("sensors", false);
        model.setNsPrefix("ssn", NS);
        model.setNsPrefix("xsd", XSD.getURI());
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("rdfs", RDFS.getURI());
        model.setNsPrefix("owl", OWL.getURI());
        // Turtle Format: FileUtils.langTurtle


        if (model.supportsTransactions()) {
            model.begin();
        }

        Individual mySensor = ontologyModel.createIndividual("http://localhost/mysensor", sensingDevice);
        model.add(mySensor, RDF.type, sensingDevice);

        Individual myAccurancy = ontologyModel.createIndividual("http://localhost/mysensor/accurancy", accuracy);
        model.add(myAccurancy, RDF.type, accuracy);
        model.add(myAccurancy, OWL.hasValue, "1.0");
        model.add(mySensor, hasMeasurementProperty, myAccurancy);

        if (model.supportsTransactions()) {
            model.commit();
        }

        // Create an OWL reasoner
        Reasoner owlReasoner = ReasonerRegistry.getOWLReasoner();

        // Bind the reasoner to the WordNet ontology model
        Reasoner wnReasoner = owlReasoner.bindSchema(ontologyModel);

        // Use the reasoner to create an inference model
        InfModel infModel = ModelFactory.createInfModel(wnReasoner, model);
        Query query = QueryFactory.create("SELECT     ?SensingDevice WHERE { ?SensingDevice  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"Arduino\" }");
        QueryExecution qExec = QueryExecutionFactory.create(query, infModel);
        ResultSetFormatter.out(System.out, qExec.execSelect(), query);
        model.write(System.out, FileUtils.langTurtle);
        System.out.println(infModel.validate());
        if (model.supportsTransactions()) {
            model.close();
        }
        maker.close();
    }
}
