package de.uniol.inf.is.odysseus.markov.operator.aggregate;

import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.markov.model.statemachine.State;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Transition;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class ViterbiAggregationFunction extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>>{

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
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple<?> in) {
		
		Observation o = tupleToObservation(in);
		MarkovPartialAggregate<RelationalTuple<?>> mpa = new MarkovPartialAggregate<RelationalTuple<?>>();
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
	public IPartialAggregate<RelationalTuple<?>> merge(IPartialAggregate<RelationalTuple<?>> p, RelationalTuple<?> toMerge, boolean createNew) {
		System.out.println("------- merge -------");		
		//MarkovPartialAggregate<RelationalTuple<?>> mpa = (MarkovPartialAggregate<RelationalTuple<?>>)p;
		MarkovPartialAggregate<RelationalTuple<?>> mpa = (MarkovPartialAggregate<RelationalTuple<?>>) p.clone();
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
	public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
		System.out.println("------- evaluate -------");
		MarkovPartialAggregate<RelationalTuple<?>> mpa = (MarkovPartialAggregate<RelationalTuple<?>>)p;
		RelationalTuple<?> tuple = new RelationalTuple(1);
		State s = mpa.getCurrentState();
		tuple.setAttribute(0, s.getName()+mpa.getValues().get(s));		
		return tuple;
	}
	
//	private State getMaxState(MarkovPartialAggregate<RelationalTuple<?>> mpa){
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

	private Observation tupleToObservation(RelationalTuple<?> in){
		return new Observation(in.getAttribute(1).toString());
	}
	
}
