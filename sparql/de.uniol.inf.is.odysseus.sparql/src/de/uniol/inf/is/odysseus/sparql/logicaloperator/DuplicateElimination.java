/*
 * DuplicateElimination.java
 *
 * Created on 9. November 2007, 12:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.sparql.logicaloperator;


import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * This class represents a duplicate elimination.
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 */
public class DuplicateElimination extends UnaryLogicalOp{
    
	private static final long serialVersionUID = 8103342490843231167L;

	/** Creates a new instance of DuplicateElimination */
    public DuplicateElimination() {
    }
    
    public DuplicateElimination(DuplicateElimination original){
        super((UnaryLogicalOp)original);
    }
    
//    public void calcOutIDSize(){
//    	this.setInputIDSize(this.getInputAO().getOutputIDSize());
//    	this.setOutputIDSize(this.getInputIDSize());
//    }
    
    @Override
    public DuplicateElimination clone(){
        return new DuplicateElimination(this);
    }

	@Override
	public SDFSchema getOutputSchema() {
		// TODO Auto-generated method stub
		return this.getInputSchema();
	}
}
