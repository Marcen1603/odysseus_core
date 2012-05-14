package de.uniol.inf.is.odysseus.markov.model.algorithm;

import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;

public interface IMarkovAlgorithm {

	public void create(HiddenMarkovModel hmm);
	
	public void next(Observation o);
	
	public String getName();
		
}
