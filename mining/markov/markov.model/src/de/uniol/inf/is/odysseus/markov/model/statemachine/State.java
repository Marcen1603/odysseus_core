package de.uniol.inf.is.odysseus.markov.model.statemachine;

import java.util.ArrayList;
import java.util.List;

public class State {

	private List<Transition> outgoingTransitions = new ArrayList<Transition>();
	private List<Transition> emissionTransitions = new ArrayList<Transition>();
	private String name;

	public State(String name) {
		this.name = name;
	}

	public void addTransition(Transition transition) {
		this.outgoingTransitions.add(transition);
		transition.setSourceState(this);
	}

	public void addTransitions(List<Transition> transitions) {
		for (Transition t : transitions) {
			this.addTransition(t);
		}
	}

	public void addEmission(Transition transition) {
		this.emissionTransitions.add(transition);
		transition.setSourceState(this);
	}

	public void addEmissions(List<Transition> transitions) {
		for(Transition t : transitions){
			this.addEmission(t);
		}
	}

	public List<Transition> getEmissions() {
		return this.emissionTransitions;
	}

	public Transition getEmissionToObservation(Observation o) {
		for (Transition t : this.emissionTransitions) {
			if (t.getNextState().getName().equals(o.getName())) {
				return t;
			}
		}
		return null;
	}

	public Transition getTransitionToState(State s) {
		for (Transition t : this.getOutgoingTransitions()) {
			if (t.getNextState().getName().equals(s.getName())) {
				return t;
			}
		}
		return null;
	}

	public List<Transition> getOutgoingTransitions() {
		return outgoingTransitions;
	}

	public void setOutgoingTransitions(List<Transition> outgoingTransitions) {
		this.outgoingTransitions = outgoingTransitions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "State (" + this.name + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof State) {
			State other = (State) obj;
			if (name == null) {
				if (other.name == null) {
					return true;
				}
                return false;
			}
			if (name.toUpperCase().equals(other.name.toUpperCase())) {
				return true;
			}
            return false;
		}
        return false;
	}

}
