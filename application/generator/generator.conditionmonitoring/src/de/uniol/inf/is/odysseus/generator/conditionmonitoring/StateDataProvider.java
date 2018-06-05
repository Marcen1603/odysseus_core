package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

/**
 * This data generator generates states due to a specified model.
 * 
 * @author Tobias Brandt
 *
 */
public class StateDataProvider extends AbstractDataGenerator {

	private int sleepTime;
	private State state;

	public StateDataProvider() {
		sleepTime = 10;

		// Create a state-model
		State running = new State("running");
		State breakInUse = new State("break");
		State lowerSpeed = new State("lower speed");
		State higherSpeed = new State("higher speed");

		running.addFollowingState(breakInUse, 0.2);
		running.addFollowingState(running, 0.8);

		breakInUse.addFollowingState(lowerSpeed, 0.95);
		breakInUse.addFollowingState(higherSpeed, 0.05);

		lowerSpeed.addFollowingState(running, 1);
		higherSpeed.addFollowingState(running, 1);

		this.state = running;

	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(this.sleepTime);

		// Make the tuple for the state
		DataTuple tuple = new DataTuple();
		tuple.addString(this.state.getStateName());

		// Switch to next state
		this.state = this.state.nextState();

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void process_init() {
		// TODO Auto-generated method stub
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new StateDataProvider();
	}

}
