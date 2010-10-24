package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

public class StateMachineInstance {
	
	private int instanceId;
	private StateMachine machine;
	private State currentState;
	
	public StateMachineInstance(StateMachine machine, int instanceId) {
		this.machine = machine;
		this.instanceId = instanceId;
		this.currentState = machine.getStates()[0];
	}

	public StateMachine getMachine() {
		return machine;
	}
	
	public int getInstanceId() {
		return this.instanceId;
	}

	public State getCurrentState() {
		return currentState;
	}

}
