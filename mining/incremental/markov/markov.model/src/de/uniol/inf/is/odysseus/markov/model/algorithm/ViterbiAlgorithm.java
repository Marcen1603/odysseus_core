package de.uniol.inf.is.odysseus.markov.model.algorithm;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.markov.model.statemachine.State;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Transition;

public class ViterbiAlgorithm extends AbstractMarkovAlgorithm {

	private HashMap<State, Double> values = new HashMap<State, Double>();

	
	@Override
	public void init(Observation o) {	
		for (State s : hmm.getStates()) {
			double prob = s.getEmissionToObservation(o).getProbability() * hmm.getStartState().getTransitionToState(s).getProbability();
			values.put(s, prob);
			System.out.println("Start prob for " + s.getName() + " is " + prob);
		}

	}

	@Override
	public void step(Observation o) {
		// every next step
		for (State s : hmm.getStates()) {
			double prob = s.getEmissionToObservation(o).getProbability();
			double max = 0.0;
			for (Transition t : this.hmm.getIncomingTransitions(s)) {
				double temp = t.getProbability() * values.get(t.getSourceState());
				if (temp > max) {
					max = temp;
				}
			}
			prob = max * prob;
			values.put(s, prob);
			System.out.println("Next prob for " + s.getName() + " is " + prob);
		}

	}

	@Override
	public String getName() {
		return "Viterbi";
	}

}
