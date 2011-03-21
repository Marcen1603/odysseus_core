package de.uniol.inf.is.odysseus.markov.model;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.markov.model.statemachine.State;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Transition;

public class HiddenMarkovModel {

	private String name;
	private List<State> states = new ArrayList<State>();
	private List<Observation> observations = new ArrayList<Observation>();
	private State startState;

	public HiddenMarkovModel(String name){
		this.name = name;
	}
	
	public HiddenMarkovModel(HiddenMarkovModel hmm) {
		this.name = hmm.name;
		this.states = hmm.states;
		this.observations = hmm.observations;
		this.startState = hmm.startState;
	}

	public String getName(){
		return this.name;
	}

	public void addState(State state){
		this.states.add(state);
	}
	
	public List<State> getStates(){
		return this.states;
	}	
	
	public State getStartState() {
		return startState;
	}

	public void setStartState(State startState) {
		this.startState = startState;
	}

	@Override
	public String toString() {	
		String newline = "\n";
		StringBuilder sb = new StringBuilder();
		sb.append("Hidden Markov Model: "+this.name).append(newline);
		sb.append(" - States: "+this.states.toString()).append(newline);
		sb.append(" - Observations: "+this.observations.toString()).append(newline);
		sb.append(" - Transitions: ").append(newline);
		String indent = "                ";
		for(State s: this.states){
			for(Transition t : s.getOutgoingTransitions()){
				sb.append(indent+" "+s+" "+t).append(newline);
			}
		}
		sb.append(" - Emissions: ").append(newline);
		for(State s: this.states){
			for(Transition t : s.getEmissions()){
				sb.append(indent+" "+s+" "+t).append(newline);
			}
		}
		sb.append(" - Start: "+this.startState).append(newline);		
		for(Transition t : this.startState.getOutgoingTransitions()){
			sb.append(indent+" "+this.startState+" "+t).append(newline);
		}				
		return sb.toString();
	}
	
	public void addObservation(Observation observation){
		this.observations.add(observation);
	}
	
	public List<Observation> getObservations(){
		return this.observations;
	}
	
	public boolean isStateExisting(String name){
		State other = new State(name);
		for(State s : this.states){
			if(s.equals(other)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isObservationExisting(String name){
		Observation other = new Observation(name);
		for(Observation o : this.observations){
			if(o.equals(other)){
				return true;
			}
		}
		return false;
	}

	public State getState(String name) {
		for(State s: this.states){
			if(s.getName().toUpperCase().equals(name.toUpperCase())){
				return s;
			}
		}
		return null;		
	}	
	
	public Observation getObservation(String name) {
		for(Observation o: this.observations){
			if(o.getName().toUpperCase().equals(name.toUpperCase())){
				return o;
			}
		}
		return null;		
	}	
	
	public List<Transition> getTransitions(){
		List<Transition> transitions = new ArrayList<Transition>();
		for(State state : this.states){
			transitions.addAll(state.getOutgoingTransitions());
		}
		return transitions;
	}
	
	public List<Transition> getIncomingTransitions(State s){
		List<Transition> transitions = new ArrayList<Transition>();
		for(Transition t : this.getTransitions()){
			if(t.getNextState().equals(s)){
				transitions.add(t);
			}
		}
		return transitions;
	}
	
	@Override
	public HiddenMarkovModel clone(){
		return new HiddenMarkovModel(this);
	}
	
}
