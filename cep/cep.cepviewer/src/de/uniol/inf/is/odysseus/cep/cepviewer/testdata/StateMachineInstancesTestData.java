package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

import java.util.ArrayList;

public class StateMachineInstancesTestData {

	private ArrayList<StateMachineInstance> machines;
	private int machineNumber;

	public StateMachineInstancesTestData() {
		this.machineNumber = 0;
		machines = new ArrayList<StateMachineInstance>();
		// Automat 1
		State stateA1 = new State(0, false);
		State stateA2 = new State(1, false);
		State stateA3 = new State(2, true);
		Condition condA1 = new Condition("location == SHELF");
		Condition condA2 = new Condition("time - time' < 12 and !(id == id')");
		Condition condA3 = new Condition(
				"location == EXIT and id == id' and time - time' < 12");
		Transition transA1 = new Transition(0, stateA2, condA1, Action.CONSUME);
		Transition transA2 = new Transition(0, stateA2, condA2, Action.DISCARD);
		Transition transA3 = new Transition(1, stateA3, condA3, Action.CONSUME);
		stateA1.addTransition(transA1);
		stateA2.addTransition(transA2);
		stateA2.addTransition(transA3);
		State[] stateArrayA = { stateA1, stateA2, stateA3 };
		StateMachine statemachine = new StateMachine(stateArrayA, "Anfrage 1");
		StateMachineInstance instance = new StateMachineInstance(statemachine,
				1);
		instance.setCurrentState(stateA2);
		addStateMachine(instance);
		// Automat 2
		State stateB1 = new State(0, false);
		State stateB2 = new State(1, false);
		State stateB3 = new State(2, false);
		State stateB4 = new State(3, true);
		Condition condB1 = new Condition("location == SHELF");
		Condition condB2 = new Condition("time - time' < 12 and !(id == id')");
		Condition condB3 = new Condition(
				"location == EXIT and id == id' and time - time' < 12");
		Condition condB4 = new Condition(
		"lol");
		Transition transB1 = new Transition(0, stateB2, condB1, Action.CONSUME);
		Transition transB2 = new Transition(0, stateB2, condB2, Action.DISCARD);
		Transition transB3 = new Transition(1, stateB3, condB3, Action.CONSUME);
		Transition transB4 = new Transition(0, stateB4, condB4, Action.CONSUME);
		Transition transB5 = new Transition(1, stateB2, condB3, Action.CONSUME);
		stateB1.addTransition(transB1);
		stateB2.addTransition(transB2);
		stateB2.addTransition(transB3);
		stateB3.addTransition(transB4);
		stateB2.addTransition(transB5);
		State[] stateArrayB = { stateB1, stateB2, stateB3 , stateB4};
		StateMachine statemachineB = new StateMachine(stateArrayB, "Anfrage 2");
		StateMachineInstance instanceB = new StateMachineInstance(statemachineB,
				2);
		instanceB.setCurrentState(stateB3);
		addStateMachine(instanceB);
	}

	public ArrayList<StateMachineInstance> getMachines() {
		return machines;
	}

	private void addStateMachine(StateMachineInstance statemachine) {
		machines.add(statemachine);
		machineNumber++;
	}

}
