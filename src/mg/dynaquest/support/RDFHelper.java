package mg.dynaquest.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mg.dynaquest.wrapper.access.HttpAccess;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFException;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.QueryExecution;
import com.hp.hpl.jena.rdql.QueryResults;
import com.hp.hpl.jena.rdql.ResultBinding;

public class RDFHelper {

	public static ArrayList<Resource> getNodesWithRDFType(Model model, String rdfType)
			throws RDFException {
		return findNodes(model,"http://www.w3.org/1999/02/22-rdf-syntax-ns#type", rdfType);
	}
	
	public static ArrayList<Resource> findNodes(Model model, String predicate,
			String object) throws RDFException {
		ArrayList<Resource> listOfNodes = new ArrayList<Resource>();
		String queryString = "select ?result " + "WHERE (?result,<" + predicate
				+ ">,<" + object + ">)";
		Query query = new Query(queryString);
		query.setSource(model);
		// Für jeden Knoten
		QueryExecution qe = new QueryEngine(query);
		QueryResults results = qe.exec();
		while (results.hasNext()) {
			ResultBinding resBinding = (ResultBinding) results.next();
			listOfNodes.add((Resource)resBinding.get("result"));
		}
		return listOfNodes;
	}

	public static Model readRDF(BufferedReader in, String baseURI)
			throws RDFException {
		Model model = ModelFactory.createDefaultModel();
		// read the RDF/XML file
		model.read(in, baseURI);
		return model;
	}

	public static Model readModel(String baseURI) throws MalformedURLException,
            IOException, FileNotFoundException, RDFException {
        BufferedReader reader = null;
        if (baseURI.startsWith("file://")) {
            reader = new BufferedReader(new FileReader(baseURI.substring(7)));
        } else {
            reader = HttpAccess.get(new URL(baseURI), 1);
        }
        Model model = readRDF(reader, baseURI);
        return model;
    }
	
    static public List<Resource> getRootNodes(Model model) throws RDFException {
        // Root-Nodes sind alle Knoten die nicht als Objekte auftreten, sondern
        // nur
        // Subjekte sind
        ResIterator subjectIter = model.listSubjects();
        List<Resource> allSubjects = new ArrayList<Resource>();
        while (subjectIter.hasNext()) {
            allSubjects.add((Resource)subjectIter.next());
        }
        subjectIter = model.listSubjects();
        while (subjectIter.hasNext()) {
            NodeIterator objectIter = model.listObjects();
            Resource subject = subjectIter.nextResource();
            while (objectIter.hasNext()) {
                if (subject.equals(objectIter.next())) {
                    //logger.debug("Entferne " + subject);
                    allSubjects.remove(subject);
                }
            }
        }
        return allSubjects;

    }

	

}
