package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

public class ContextVibrationDataProvider extends AbstractDataGenerator {

	private String[] states;
	private int[] meanValues;

	private int currentState;
	private final double SWITCH_PROBABILITY = 0.05;
	private final double ANOMALY_PROBABILITY = 0.005;
	private final double ANOMALY_VALUE = 5;

	public ContextVibrationDataProvider() {
		states = new String[3];
		states[0] = "low";
		states[1] = "medium";
		states[2] = "high";

		meanValues = new int[3];
		meanValues[0] = 10;
		meanValues[1] = 100;
		meanValues[2] = 200;

		currentState = 0;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		int sleepTime = 100;
		Thread.sleep(sleepTime);

		double random = Math.random();
		if (random < SWITCH_PROBABILITY) {
			currentState = nextState();
		}

		double vibrationValue = meanValues[currentState] + (Math.random() * 2);
		
		random = Math.random();
		if (random < ANOMALY_PROBABILITY) {
			vibrationValue += ANOMALY_VALUE;
		}

		// Create a tuple for that data
		DataTuple tuple = new DataTuple();
		tuple.addString(states[currentState]);
		tuple.addDouble(vibrationValue);

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}

	private int nextState() {
		int state = (int) (Math.random() * (states.length));
		return state;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void process_init() {

	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new ContextVibrationDataProvider();
	}
}
