package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

public class Transition {

	private int id;
	private State nextState;
	private Condition condition;
	private EAction action;
	
	public Transition(int id, State nextState, Condition condition,
			EAction action) {
		this.id = id;
		this.nextState = nextState;
		this.condition = condition;
		this.action = action;
	}

	public int getId() {
		return id;
	}

	public State getNextState() {
		return nextState;
	}

	public Condition getCondition() {
		return condition;
	}

	public EAction getAction() {
		return action;
	}
	
}
