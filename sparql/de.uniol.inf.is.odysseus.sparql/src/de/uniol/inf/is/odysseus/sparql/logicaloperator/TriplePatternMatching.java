/*
 * TriplePatternMatching.java
 *
 * Created on 12. November 2007, 11:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.sparql.parser.helper.SPARQLDirectAttributeResolver;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Triple;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Variable;

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
    
	private static int sourceNameCounter = 0;
	
	
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
    
    
    public Triple getTriple() {
		return triple;
	}

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
    
    /**
     * The restrict list defines which attributes of an input tuple
     * will be in the output tuple.
     */
//    private int[] restrictList;
    
    
//    public int[] getRestrictList() {
//		return restrictList;
//	}

	/**
     * The is the select predicate for the physical operator.
     * It will be generated as follows
     * 
     */
    private IPredicate selectionPredicate;
    
    public TriplePatternMatching(Triple t){
        super();
        this.triple = t;
    }
    
    private TriplePatternMatching(TriplePatternMatching tpm){
    	this.triple = tpm.triple;
        this.graphVar = tpm.getGraphVar();
        this.stream_name = tpm.getStream_name();
        this.outputSchema = tpm.outputSchema;
        this.selectionPredicate = tpm.selectionPredicate;
    }
    
    public TriplePatternMatching(Triple t, Variable n, String stream_name){
        super();
        this.triple = t;
        this.graphVar = n;
        this.stream_name = stream_name;
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
    
	private SDFAttributeList calcOutputSchema(){
		SDFAttributeList outputSchema = new SDFAttributeList();
		if (triple.getSubject().isVariable()){
			outputSchema.add(new SDFAttribute(this.sourceName, triple.getSubject().getName()));
		}
		if (triple.getPredicate().isVariable()){
			outputSchema.add(new SDFAttribute(this.sourceName, triple.getPredicate().getName()));
		}
		if (triple.getObject().isVariable()){
			outputSchema.add(new SDFAttribute(this.sourceName, triple.getObject().getName()));
		}
// Wozu braucht man das?
//		if (getInputAO() != null && getInputSchema() != null){
//			l.addAll(getInputSchema());
//		}
		
		// adding the graphVar
		if(this.graphVar != null && this.stream_name != null){
			boolean alreadyAdded = false;
			SDFAttribute graphVarAtt = new SDFAttribute(this.sourceName, this.graphVar.getName());
			for(SDFAttribute a : outputSchema){
				if(a.getQualName().equals(graphVarAtt.getQualName())){
					alreadyAdded = true;
				}
			}
			if(!alreadyAdded){
				outputSchema.add(graphVarAtt);
			}
		}
		
		return outputSchema;
	}
	
//	private int[] calcRestrictList(){
//		
//		int[] restrictList = new int[this.outputSchema.size()];
//		
//		int curI = 0;
//		if(this.triple.getSubject().isVariable()){
//			// 0 is always the index of the subject attribute in the input schema;
//			restrictList[curI++] = 0;
//		}
//		if(this.triple.getPredicate().isVariable()){
//			// 1 is always the index of the predicate attribute in the input schema.
//			restrictList[curI++] = 1;
//		}
//		if(this.triple.getObject().isVariable()){
//			// 2 is always the index of the object attribute in the input schema.
//			restrictList[curI++] = 2;
//		}
//		return restrictList;
//	}
	
	public void initPredicate(){
		SDFAttributeList inputSchema = this.getInputSchema(0);
		System.out.println("TriplePatternMatching.initPredicate: inputSchema = " + inputSchema);
		
		IAttributeResolver attrRes = new SPARQLDirectAttributeResolver(inputSchema);
		ArrayList<SDFExpression> exprs = new ArrayList<SDFExpression>();
		
		if(!this.triple.getSubject().isVariable()){
			String exprStr = inputSchema.getAttribute(0).getPointURI() + " == '" + this.triple.getSubject().getName() + "'";
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
			exprs.add(expr);
		}
		
		if(!this.triple.getPredicate().isVariable()){
			String exprStr = inputSchema.getAttribute(1).getPointURI() + " == '" + this.triple.getPredicate().getName() + "'";
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
			exprs.add(expr);
		}
		
		if(!this.triple.getObject().isVariable()){
			String exprStr = inputSchema.getAttribute(2).getPointURI() + " == '" + this.triple.getObject().getName() + "'";
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
			exprs.add(expr);
		}
		
		IPredicate pred = null;
		
		if(exprs.size() > 1){
			RelationalPredicate firstPredicate = new RelationalPredicate(exprs.get(0));
			firstPredicate.init(inputSchema, null);
			
			IPredicate left = firstPredicate;
			for(int i = 1; i<exprs.size(); i++){
				RelationalPredicate right = new RelationalPredicate(exprs.get(i));
				right.init(inputSchema, null);
				AndPredicate tempAnd = new AndPredicate(left, right);
				left = tempAnd;
			}
			
			pred = left;
		}
		
		// there is only one predicate
		else if(exprs.size() == 1){
			RelationalPredicate firstRelational = new RelationalPredicate(exprs.get(0));
			firstRelational.init(inputSchema, null);
			pred = firstRelational;
		}
		
		// there is no predicate
		else{
			pred = new TruePredicate();
		}
		
		this.selectionPredicate = pred;
	}
	
	@Override
	public IPredicate getPredicate(){
		return this.selectionPredicate;
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
		if(this.outputSchema == null){
			// the source name must be a unique artificial name, since
			// every triple pattern needs its own source name.
			this.sourceName = "s" + TriplePatternMatching.sourceNameCounter++;
			this.outputSchema = this.calcOutputSchema();
			//this.restrictList = this.calcRestrictList();
		}
		return this.outputSchema;
	}
}
