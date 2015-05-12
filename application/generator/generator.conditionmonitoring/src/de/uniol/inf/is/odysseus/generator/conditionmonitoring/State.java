package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class State {

	private String stateName;
	private List<State> followingStates;
	private List<Double> probabilities;

	public State(String name) {
		stateName = name;
		followingStates = new ArrayList<State>();
		probabilities = new ArrayList<Double>();
	}

	public void addFollowingState(State nextState, double probability) {
		followingStates.add(nextState);
		probabilities.add(probability);
	}

	public State nextState() {
		// We need to know the probability sum from all possible following
		// states
		double probabilitySum = 0;
		for (Double prob : probabilities)
			probabilitySum += prob;

		// Generate the number which indicates, which next state we choose
		Random random = new Random();
		double randomSum = probabilitySum * random.nextFloat();

		// Find the next state:
		// Sum the probabilities of the possible states until the generated
		// number is reached
		State nextState = null;
		double sum = 0;
		int i = 0;
		while (sum < randomSum) {
			nextState = followingStates.get(i);
			sum += probabilities.get(i);
			i++;
		}
		return nextState;
	}

	public String getStateName() {
		return stateName;
	}

}
