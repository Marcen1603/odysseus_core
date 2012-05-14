package de.uniol.inf.is.odysseus.markov.operator.logical;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;

public class MarkovAO extends AggregateAO{

	
	private static final long serialVersionUID = 2707805704896762434L;
		
	private HiddenMarkovModel hmm;	

	public MarkovAO(HiddenMarkovModel hmm){
		this.hmm = hmm;		
	}
	
	public MarkovAO(MarkovAO markovAO){
		super(markovAO);		
		this.hmm = markovAO.hmm;
	}
	
	@Override
	public MarkovAO clone() {
		return new MarkovAO(this);
	}
	
	public HiddenMarkovModel getHiddenMarkovModel() {
		return hmm;
	}

	public void setHiddenMarkovModel(HiddenMarkovModel hmm) {
		this.hmm = hmm;
	}
	
	

}
