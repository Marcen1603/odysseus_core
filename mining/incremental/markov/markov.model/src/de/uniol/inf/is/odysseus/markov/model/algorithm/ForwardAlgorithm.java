package de.uniol.inf.is.odysseus.markov.model.algorithm;

import java.util.HashMap;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.markov.model.statemachine.State;

public class ForwardAlgorithm extends AbstractMarkovAlgorithm {

	private HashMap<State, Double> values = new HashMap<State, Double>();

	@Override
	public void init(Observation o) {
		for (State s : this.hmm.getStates()) {
			double val = this.hmm.getStartState().getTransitionToState(s).getProbability() * s.getEmissionToObservation(o).getProbability();
			this.values.put(s, val);
		}

	}

	@Override
	public void step(Observation o) {
		HashMap<State, Double> temp = new HashMap<State, Double>();
		for (State sj : this.hmm.getStates()) {
			double sum = 0.0;
			for (State si : this.hmm.getStates()) {
				sum = sum + (this.values.get(si) * si.getTransitionToState(sj).getProbability() * sj.getEmissionToObservation(o).getProbability());
			}
			temp.put(sj, sum);
		}
		
		for(Entry<State, Double> e : temp.entrySet()){
			System.out.println("New Prob: "+e.getKey()+": "+e.getValue());
			this.values.put(e.getKey(), e.getValue());
		}
		
	}

	@Override
	public String getName() {
		return "Forward";
	}

}
