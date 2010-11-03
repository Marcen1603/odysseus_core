package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

import java.util.ArrayList;

public class State {

	private int id;
	private ArrayList<Transition> transition;
	private boolean accepting;
	private int transNumber;
	
	public State(int id, boolean accepting) {
		this.id = id;
		this.transition = new ArrayList<Transition>();
		this.accepting = accepting;
		this.transNumber = 0;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Transition> getTransition() {
		return transition;
	}

	public boolean isAccepting() {
		return accepting;
	}
	
	public void addTransition(Transition newTransition) {
		transition.add(newTransition);
		transNumber++;
	}
	
}
