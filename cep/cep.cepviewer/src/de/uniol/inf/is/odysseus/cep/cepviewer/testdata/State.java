package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

public class State {

	private int id;
	private Transition[] transition;
	private boolean accepting;
	private int transNumber;
	
	public State(int id, boolean accepting) {
		this.id = id;
		this.transition = new Transition[4];
		this.accepting = accepting;
		this.transNumber = 0;
	}

	public int getId() {
		return id;
	}

	public Transition[] getTransition() {
		return transition;
	}

	public boolean isAccepting() {
		return accepting;
	}
	
	public void addTransition(Transition newTransition) {
		transition[transNumber] = newTransition;
		transNumber++;
	}
	
}
