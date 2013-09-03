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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import de.uniol.inf.is.odysseus.probabilistic.sensor.Activator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensorOntology {

    private static final String SOURCE = "http://purl.oclc.org/NET/ssnx/ssn";
    private static final String NS = SOURCE + "#";
    private static final String ONS = "ossn";

    public static void main(String[] args) throws MalformedURLException {
        OntModel m = ModelFactory.createOntologyModel();

        @SuppressWarnings("unused")
		Model base = m.getBaseModel();
        OntDocumentManager dm = m.getDocumentManager();
        URL file;
        if (Activator.getContext() != null) {
            file = Activator.getContext().getBundle().getEntry("SSN.owl");
        }
        else {
            file = SensorOntology.class.getResource("/SSN.owl");
        }
        dm.addAltEntry("http://purl.oclc.org/NET/ssnx/ssn", file.toExternalForm());
        m.read("http://purl.oclc.org/NET/ssnx/ssn");

        OntClass sensingDevice = m.getOntClass(NS + "SensingDevice");
        Individual ard = m.createIndividual(ONS + "arduino", sensingDevice);
       System.out.println(ard);
        // list the asserted types
        for (Iterator<Resource> i = ard.listRDFTypes(false); i.hasNext();) {
            System.out.println(ard.getURI() + " is asserted in class " + i.next());
        }
        // m.write(System.out, "Turtle");
    }
}
