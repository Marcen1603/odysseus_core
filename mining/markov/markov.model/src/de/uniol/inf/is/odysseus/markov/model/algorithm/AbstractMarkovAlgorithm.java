package de.uniol.inf.is.odysseus.markov.model.algorithm;

import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;

public abstract class AbstractMarkovAlgorithm implements IMarkovAlgorithm{
	
	protected HiddenMarkovModel hmm;
	private boolean initiated = false;
	
	@Override
	public void create(HiddenMarkovModel hmm) {
		this.hmm = hmm;		
	}

	public abstract void init(Observation o);
	
	public abstract void step(Observation o);

	@Override
	public synchronized void next(Observation o) {
		if(!this.initiated ){
			this.initiated = true;
			init(o);
		}else{
			step(o);
		}
		
	}
	

}
