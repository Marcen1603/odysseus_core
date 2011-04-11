package de.uniol.inf.is.odysseus.sparql.physicalops.base.convert;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Node_Variable;
import com.hp.hpl.jena.graph.Triple;

import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.MetaTriple;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.querytranslation.logicalops.streaming.sparql.TriplePatternMatching;

public class TriplePatternMatchingPO extends AbstractPipe<MetaTriple, NodeList>{

	private Triple queryTriple;
	
	private Node_Variable graphVar;
	
	private String stream_name;
	
	public TriplePatternMatchingPO(TriplePatternMatching tpm){
		this.queryTriple = tpm.getTriple();
		this.graphVar = tpm.getGraphVar();
		this.stream_name = tpm.getStream_name();
	}
	
	protected synchronized void process_next(MetaTriple object, int port, boolean isReadOnly) {
		
		// TODO: Entfernen des Tests auf selectPredicate --> Restrukturierung!!
		if (this.evaluate(object)) {
			this.transfer(this.transform(object));
		}

	}
	
	private boolean evaluate(MetaTriple element){
		boolean matches = true;
		
		Node n = queryTriple.getSubject(); 
		if (!n.isVariable()){
			matches = matches && queryTriple.subjectMatches(element.getSubject());
			if (!matches){
				return false;
			}
				
		}
		// testing the graph var
		else if(n.isVariable() && this.graphVar != null && this.stream_name != null && ((Node_Variable)n).getName().equals(this.graphVar.getName())){
			try{
				Node stream_node = Node.createURI(stream_name);
				matches = matches && element.subjectMatches(stream_node);
				if(!matches){
					return false;
				}
			}catch(Exception e){
				// do nothing, may be the uri
				// invalid (e. g. for testing)
			}
		}
		n = queryTriple.getPredicate();
		if (!n.isVariable()){
			matches = matches && queryTriple.predicateMatches(element.getPredicate());
			if (!matches){
				return false;
			}
		}
		// testing the graph var
		else if(n.isVariable() && this.graphVar != null && this.stream_name != null && ((Node_Variable)n).getName().equals(this.graphVar.getName())){
			try{
				Node stream_node = Node.createURI(stream_name);
				matches = matches && element.predicateMatches(stream_node);
				if(!matches){
					return false;
				}
			}catch(Exception e){
				// do nothing, may be the uri
				// invalid (e. g. for testing)
			}
		}
		n = queryTriple.getObject();
		if (!matches && !n.isVariable()){
			matches = matches && queryTriple.objectMatches(element.getObject());
			if (!matches){
				return false;
			}
		}
		// testing the graph var
		else if(n.isVariable() && this.graphVar != null && this.stream_name != null && ((Node_Variable)n).getName().equals(this.graphVar.getName())){
			try{
				Node stream_node = Node.createURI(stream_name);
				matches = matches && element.objectMatches(stream_node);
				if(!matches){
					return false;
				}
			}catch(Exception e){
				// do nothing, may be the uri
				// invalid (e. g. for testing)
			}
		}
		
		return matches;
	}
	
	private NodeList transform(MetaTriple element){
		
		NodeList b = new NodeList();
		
		boolean graphVarAdded = false;
		
		Node n = queryTriple.getSubject();
		if(n.isVariable()){
			b.add(element.getSubject());
		}
		// is this the graph var ?
		if(n.isVariable() && this.graphVar != null && this.stream_name != null && ((Node_Variable)n).getName().equals(this.graphVar.getName())){
			graphVarAdded = true;
		}
		
		n = queryTriple.getPredicate();
		if(n.isVariable()){
			b.add(element.getPredicate());
		}
		// is this the graph var ?
		if(n.isVariable() && this.graphVar != null && this.stream_name != null && ((Node_Variable)n).getName().equals(this.graphVar.getName())){
			graphVarAdded = true;
		}
		
		n = queryTriple.getObject();
		if(n.isVariable()){
			b.add(element.getObject());
		}
		// is this the graph var ?
		if(n.isVariable() && this.graphVar != null && this.stream_name != null && ((Node_Variable)n).getName().equals(this.graphVar.getName())){
			graphVarAdded = true;
		}
		
		// has the graph var already been added?
		if(!graphVarAdded && this.stream_name != null){
			b.add(Node.createURI(this.stream_name));
		}
		
		b.setMetadata(element.getMetadata().clone());
		return b;
	}
	
	
	public void process_close(){
	}
}
