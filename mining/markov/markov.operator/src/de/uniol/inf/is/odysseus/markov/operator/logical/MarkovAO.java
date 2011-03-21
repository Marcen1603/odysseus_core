package de.uniol.inf.is.odysseus.markov.operator.logical;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.algorithm.IMarkovAlgorithm;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MarkovAO extends AbstractLogicalOperator implements OutputSchemaSettable{

	
	private static final long serialVersionUID = 2707805704896762434L;
	
	private SDFAttributeList outputSchema;
	private HiddenMarkovModel hmm;
	private IMarkovAlgorithm algorithm;

	public MarkovAO(HiddenMarkovModel hmm, IMarkovAlgorithm algorithm){
		this.hmm = hmm;		
		this.algorithm = algorithm;
	}
	
	public MarkovAO(MarkovAO markovAO){
		super(markovAO);
		this.outputSchema = markovAO.outputSchema;
		this.hmm = markovAO.hmm.clone();
		this.algorithm = markovAO.algorithm;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputSchema;
	}

	@Override
	public MarkovAO clone() {
		return new MarkovAO(this);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema;		
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		 if(port==0){ 
             setOutputSchema(outputSchema); 
        }else{ 
             throw new IllegalArgumentException("no such port: " + port); 
        }    
	}

	public HiddenMarkovModel getHiddenMarkovModel() {
		return hmm;
	}

	public void setHiddenMarkovModel(HiddenMarkovModel hmm) {
		this.hmm = hmm;
	}
	
	
	public IMarkovAlgorithm getAlgorithm(){
		return this.algorithm;
	}
	
	

}
