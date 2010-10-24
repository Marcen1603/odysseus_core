package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

public class StateMachine {

	private State[] states;
	private String string;

	public StateMachine(State[] states, String string) {
		this.states = states;
		this.string = string;
	}

	public State[] getStates() {
		return states;
	}
	
	public String getString() {
		return this.string;
	}
	
}
