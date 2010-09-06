package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PunctuationAO extends UnaryLogicalOp{

	int punctuationCount;
	
	public PunctuationAO(){
		super();
	}
	
	public PunctuationAO(int punctuationCount){
		super();
		this.punctuationCount = punctuationCount;
	}
	
	public PunctuationAO(PunctuationAO old){
		this.punctuationCount = old.punctuationCount;
	}
	
	public int getPunctuationCount(){
		return this.punctuationCount;
	}
	
	public void setPunctuationCount(int punctuationCount){
		this.punctuationCount = punctuationCount;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new PunctuationAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Auto-generated method stub
		return this.getInputSchema();
	}

}
