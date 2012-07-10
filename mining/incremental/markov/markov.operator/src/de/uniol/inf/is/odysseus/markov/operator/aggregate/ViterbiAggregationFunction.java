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
package de.uniol.inf.is.odysseus.markov.operator.aggregate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.markov.model.statemachine.State;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Transition;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class ViterbiAggregationFunction extends AbstractAggregateFunction<Tuple<?>, Tuple<?>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6157305306298530863L;
	private HiddenMarkovModel hmm;	
	
	public ViterbiAggregationFunction(HiddenMarkovModel hmm) {
		super("VITERBI");
		this.hmm = hmm;		
	}	

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		
		Observation o = tupleToObservation(in);
		MarkovPartialAggregate<Tuple<?>> mpa = new MarkovPartialAggregate<Tuple<?>>();
		System.out.println("------- init -------");
		for (State s : hmm.getStates()) {
			double prob = s.getEmissionToObservation(o).getProbability() * hmm.getStartState().getTransitionToState(s).getProbability();
			mpa.getValues().put(s, prob);
			System.out.println("Start prob for " + s.getName() + " is " + prob);
		}
		mpa.setCurrentState(mpa.getCurrentMaxPropabilityState());
		return mpa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
		System.out.println("------- merge -------");		
		//MarkovPartialAggregate<Tuple<?>> mpa = (MarkovPartialAggregate<Tuple<?>>)p;
		MarkovPartialAggregate<Tuple<?>> mpa = (MarkovPartialAggregate<Tuple<?>>) p.clone();
		Observation o = tupleToObservation(toMerge);
		double stateMax = 0.0;
		for (State s : hmm.getStates()) {
			double prob = s.getEmissionToObservation(o).getProbability();
			double max = 0.0;
			for (Transition t : this.hmm.getIncomingTransitions(s)) {
				double temp = t.getProbability() * mpa.getValues().get(t.getSourceState());
				if (temp > max) {
					max = temp;					
				}
			}
			if(max>stateMax){
				mpa.setCurrentState(s);
				stateMax = max;
			}
			prob = max * prob;
			mpa.getValues().put(s, prob);
			System.out.println("Next prob for " + s.getName() + " is " + prob);
		}		
		return mpa;
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		System.out.println("------- evaluate -------");
		MarkovPartialAggregate<Tuple<?>> mpa = (MarkovPartialAggregate<Tuple<?>>)p;
		@SuppressWarnings("rawtypes")
        Tuple<?> tuple = new Tuple(1, false);
		State s = mpa.getCurrentState();
		tuple.setAttribute(0, s.getName()+mpa.getValues().get(s));		
		return tuple;
	}
	
//	private State getMaxState(MarkovPartialAggregate<Tuple<?>> mpa){
//		State s = null;
//		Double max = 0.0;
//		for(Entry<State, Double> e : mpa.getValues().entrySet()){
//			if(e.getValue()>max){
//				s = e.getKey();
//				max = e.getValue();
//			}
//		}
//		return s;
//	}

	private static Observation tupleToObservation(Tuple<?> in){
		return new Observation(in.getAttribute(1).toString());
	}
	
}
