package de.uniol.inf.is.odysseus.markov.model.statemachine;

public class Transition {

	private double probability;
	private State nextState;
	private State sourceState;
	
	public Transition(State nextState, double probabilty){
		this.probability = probabilty;
		this.nextState = nextState;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
	}	
	
	@Override
	public String toString() {
		return " --> "+this.nextState+ " : "+this.probability;		
	}

	public void setSourceState(State s){
		this.sourceState = s;
	}
	
	public State getSourceState() {	
		return this.sourceState;
	}
	
}
