package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.noise.JitterNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;

/**
 * A very simple data generator which aims to simulate the vibration curve of a
 * fridge. When it's not cooling, is has only little vibration. When it's
 * cooling, it has a high vibration.
 * 
 * @author Tobias Brandt
 *
 */
public class FridgeVibrationSensorDataProvider extends AbstractDataGenerator {

	private ISingleValueGenerator vibrationsGenerator;
	private ISingleValueGenerator offVibrationGenerator;
	private int counter;
	private boolean motorOn;
	private boolean paused;

	private int motorOnTuples = 60;
	private int motorOffTuples = 200;

	@Override
	public List<DataTuple> next() throws InterruptedException {
		int sleepTime = 100 + (int) (Math.random() * 20);
		Thread.sleep(sleepTime);
		
		motorOnTuples = 60 + (int) (Math.random() * 20);
		motorOffTuples = 200 + (int) (Math.random() * 10);

		double value = 0;

		if (motorOn && !paused) {
			counter++;
			if (counter >= motorOnTuples) {
				motorOn = false;
				counter = 0;
				System.out.println("Switch to off");
			}

			// Let's vibrate
			value = vibrationsGenerator.nextValue();
		} else {
			counter++;
			if (counter >= motorOffTuples) {
				motorOn = true;
				counter = 0;
				System.out.println("Switch to on");
			}

			// Only the minimal vibration when switched off
			value = offVibrationGenerator.nextValue();
		}

		// Create a tuple for that data
		DataTuple tuple = new DataTuple();
		tuple.addDouble(value);

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}

	@Override
	public void close() {
	}

	@Override
	protected void process_init() {
		// This is simple vibration - not too realistic, but ok. m/s^2
		vibrationsGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(1.5)), 0, 2.0, -5, 5);
		offVibrationGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(0.1)), 0, 0.15, -0.2, 0.2);
		paused = false;
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return this;
	}

	public void pause() {
		paused = true;
		counter = 0;
	}

	public void resume() {
		paused = false;
		counter = 0;
	}
	
	

}
