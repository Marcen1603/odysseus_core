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
package de.uniol.inf.is.odysseus.probabilistic.sensor.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.QU;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class QueryManagerImpl {
    private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI() + "> ";
    private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI() + "> ";
    private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI() + "> ";
    private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI() + "> ";
    private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI() + "> ";
    private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI() + "> ";
    private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
    private static final String ODY_PREFIX = "PREFIX ody: <" + ODYSSEUS.getURI() + "> ";
    private final OntModel aBox;

    /**
     * Class constructor.
     * 
     */
    public QueryManagerImpl(OntModel aBox) {
        this.aBox = aBox;
    }

    public List<String> getSensingDevicesByObservedProperty(String qualName) {
        String query = "SELECT ?uri  WHERE { ?uri ssn:observes ody:" + qualName + " }";
        ResultSet result = executeQuery(query);
        List<String> uris = new ArrayList<String>();
        while (result.hasNext()) {
            QuerySolution solution = result.next();
            RDFNode uri = solution.get("uri");
            uris.add(uri.asResource().getURI());
        }
        return uris;
    }

    public ResultSet executeQuery(String queryString) {
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
        QueryExecution qExec = QueryExecutionFactory.create(query, getABox());
        ResultSet result = qExec.execSelect();

        return result;
    }

    private OntModel getABox() {
        return aBox;
    }
}
