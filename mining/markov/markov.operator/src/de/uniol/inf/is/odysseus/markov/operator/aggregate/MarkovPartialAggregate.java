package de.uniol.inf.is.odysseus.markov.operator.aggregate;

import java.util.HashMap;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.markov.model.statemachine.State;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class MarkovPartialAggregate<R> implements IPartialAggregate<R>{
	
	private HashMap<State, Double> values = new HashMap<State, Double>();
	private State currentState;	
	
	public MarkovPartialAggregate(){
		this.values = new HashMap<State, Double>();
	}
	
	public MarkovPartialAggregate(MarkovPartialAggregate<R> markovPartialAggregate) {
		HashMap<State, Double> copiedvalues = new HashMap<State, Double>();
		for(Entry<State, Double> e : markovPartialAggregate.values.entrySet()){
			copiedvalues.put(e.getKey(), e.getValue());
		}
		this.values = copiedvalues;
	}

	@Override
	public MarkovPartialAggregate<R> clone(){
		return new MarkovPartialAggregate<R>(this);
	}

	public HashMap<State, Double> getValues() {
		return values;
	}

	public void setValues(HashMap<State, Double> values) {
		this.values = values;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}		
	
	public State getCurrentMaxPropabilityState(){
		double max = 0.0;
		State current = null;
		for(Entry<State, Double> e : this.values.entrySet()){
			if(e.getValue()>max){
				current = e.getKey();
				max = e.getValue();
			}
		}
		return current;
	}
	
}
