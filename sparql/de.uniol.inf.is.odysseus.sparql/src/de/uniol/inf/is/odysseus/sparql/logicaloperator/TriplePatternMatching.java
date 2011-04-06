/*
 * TriplePatternMatching.java
 *
 * Created on 12. November 2007, 11:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sparql.parserLR.helper.Triple;
import de.uniol.inf.is.odysseus.sparql.parserLR.helper.Variable;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;

/**
 * This operator repesents a triple pattern matching operator. This logical
 * operator differs only in one point from a basic triple selection operator.
 * A triple pattern matching operator can have graph variable to set. This means,
 * if a triple pattern matching operator is inside a GRAPH VAR clause, the variable
 * VAR has to be set to the name of the graph from which the sparql solution
 * has been taken.
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 */
public class TriplePatternMatching extends AbstractLogicalOperator{
    
	private Triple triple;
	
    /**
     * The variable to set in every sparql solution.
     */
    private Variable graphVar;
    
    /**
     * the value of graph variable, that has to be set
     * in every sparql solution.
     */
    private String stream_name;
    
    
    /**
     * the name of the virtual source. Each triple pattern
     * must have a unique virtual source name, since
     * e. g. two triple pattern over the same stream will
     * result in a self join over this stream. While in CQL
     * for this we have the "AS" clause in SPARQL we don't have
     * such a clause. Therefore the virtual source names will
     * be set automatically.
     */
    private String sourceName;
    
    /**
     * The outputschema of this operator is set
     * to subject variable | predicate variable | object variable
     * each only if the variable is available. e. g. a triple pattern
     * ?x :pred ?y results in a schema |x|y|
     */
    private SDFAttributeList outputSchema;
    
    public TriplePatternMatching(Triple t){
        super();
        this.triple = t;
        this.calcOutputSchema();
    }
    
    private TriplePatternMatching(TriplePatternMatching tpm){
    	this.triple = tpm.triple;
        this.graphVar = tpm.getGraphVar();
        this.stream_name = tpm.getStream_name();
        this.outputSchema = tpm.outputSchema;
    }
    
    public TriplePatternMatching(Triple t, Variable n, String stream_name){
        super();
        this.triple = t;
        this.graphVar = n;
        this.stream_name = stream_name;
        this.calcOutputSchema();
    }
    
    public TriplePatternMatching clone(){
        return new TriplePatternMatching(this);
    }
    
    @Override
    public String toString(){
        String retval = super.toString();
        if(this.graphVar != null){
            retval += "GRAPH " + this.graphVar + " = " + this.stream_name;
        }
        return retval;
    }
    
	private void calcOutputSchema(){
		SDFAttributeList l = new SDFAttributeList();
		if (triple.getSubject().isVariable()){
			l.add(new SDFAttribute(this.sourceName, triple.getSubject().getName()));
		}
		if (triple.getPredicate().isVariable()){
			l.add(new SDFAttribute(this.sourceName, triple.getPredicate().getName()));
		}
		if (triple.getObject().isVariable()){
			l.add(new SDFAttribute(this.sourceName, triple.getObject().getName()));
		}
// Wozu braucht man das?
//		if (getInputAO() != null && getInputSchema() != null){
//			l.addAll(getInputSchema());
//		}
		
		// adding the graphVar
		if(this.graphVar != null && this.stream_name != null){
			boolean alreadyAdded = false;
			SDFAttribute graphVarAtt = new SDFAttribute(this.sourceName, this.graphVar.getName());
			for(SDFAttribute a : l){
				if(a.getQualName().equals(graphVarAtt.getQualName())){
					alreadyAdded = true;
				}
			}
			if(!alreadyAdded){
				l.add(graphVarAtt);
			}
		}
		
//		this.setOutputSchema(l);
		
		// also calc out id size
//		this.calcOutIDSize();
	}
    
//    public void calcOutIDSize(){
//    	// the input ao might be null
//    	// because the method calcOutElements
//    	// will already be called in the
//    	// constructor of BasicTripleSelectionAO
//    	if(this.getInputAO() != null){
//    		this.setInputIDSize(this.getInputAO().getOutputIDSize());
//    		this.setOutputIDSize(this.getInputIDSize());
//    	}
//    }

    public Variable getGraphVar() {
        return graphVar;
    }

    public void setGraphVar(Variable graphVar) {
        this.graphVar = graphVar;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputSchema;
	}
}
