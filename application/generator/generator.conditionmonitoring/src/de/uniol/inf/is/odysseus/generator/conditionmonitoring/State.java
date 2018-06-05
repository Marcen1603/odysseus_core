package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a state with a defined list of following states and for
 * each state a probability to get to this state
 * 
 * @author Tobias Brandt
 *
 */
public class State {

	private String stateName;
	private List<State> followingStates;
	private List<Double> probabilities;

	/**
	 * Creates a new state
	 * 
	 * @param name
	 *            The name of the state
	 */
	public State(String name) {
		stateName = name;
		followingStates = new ArrayList<State>();
		probabilities = new ArrayList<Double>();
	}

	/**
	 * Adds a following state.
	 * 
	 * @param nextState
	 *            The state which follows this state
	 * @param probability
	 *            The probability to reach this state
	 */
	public void addFollowingState(State nextState, double probability) {
		followingStates.add(nextState);
		probabilities.add(probability);
	}

	/**
	 * Calculates, which is the next state. Therefore, a random number is used
	 * which indicates, which of the possible following states is chosen. Is the
	 * sum of the probabilities is greater than 1, this is taken in respect.
	 * 
	 * @return The next state.
	 */
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

	/**
	 * 
	 * @return The name of the state
	 */
	public String getStateName() {
		return stateName;
	}

}
