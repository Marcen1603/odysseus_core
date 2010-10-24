package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

public class StateMachineInstancesTestData {

	private StateMachineInstance[] machines;
	private int machineNumber;

	public StateMachineInstancesTestData() {
		this.machineNumber = 0;
		machines = new StateMachineInstance[20];
		for(int i = 0; i < 20; i++) {
			State stateA1 = new State(0, false);
			State stateA2 = new State(1, false);
			State stateA3 = new State(2, true);
			Condition condA1 = new Condition("location == SHELF");
			Condition condA2 = new Condition("time - time' < 12 and !(id == id')");
			Condition condA3 = new Condition("location == EXIT and id == id' and time - time' < 12");
			Transition transA1 = new Transition(0, stateA2, condA1, Action.CONSUME);
			Transition transA2 = new Transition(0, stateA2, condA2, Action.DISCARD);
			Transition transA3 = new Transition(1, stateA3, condA3, Action.CONSUME);
			stateA1.addTransition(transA1);
			stateA2.addTransition(transA2);
			stateA2.addTransition(transA3);
			State[] stateArrayA = {stateA1, stateA2, stateA3};
			StateMachine statemachine = new StateMachine(stateArrayA, "Anfrage 1");
			StateMachineInstance instance = new StateMachineInstance(statemachine, i);
			addStateMachine(instance);
		}
	}

	public StateMachineInstance[] getMachines() {
		return machines;
	}
	
	private void addStateMachine(StateMachineInstance statemachine) {
		machines[machineNumber] = statemachine;
		machineNumber++;
	}
	
}
