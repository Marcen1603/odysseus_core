/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
