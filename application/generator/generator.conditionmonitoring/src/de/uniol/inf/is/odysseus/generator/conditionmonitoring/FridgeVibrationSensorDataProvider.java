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
	private boolean motorOn;
	private boolean paused;

	private float secondsSwitchedOn = 2;
	private float secondsSwitchedOff = 12;
	// To reset after an anomaly
	private float originalSecondsSwitchedOff = secondsSwitchedOff;
	
	private float randomFactorSecondsSwitchedOn = 5;
	private float randomFactorSecondsSwitchedOff = 10;

	private int sleepTime = 0;
	private int sleepTimeRandomFactor = 0;

	private long timeSwitchedLastOn;
	private long timeSwitchedLastOff;

	public FridgeVibrationSensorDataProvider() {
		// Use standard values
	}

	/**
	 * If you want to use custom intervals and times, you can do this here
	 * 
	 * @param sleepTime
	 * @param sleepTimeRandomFactor
	 */
	public FridgeVibrationSensorDataProvider(int sleepTime, int sleepTimeRandomFactor) {
		this.sleepTime = sleepTime;
		this.sleepTimeRandomFactor = sleepTimeRandomFactor;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {

		double value = 0;

		int sleepTime = this.sleepTime + (int) (Math.random() * this.sleepTimeRandomFactor);
		Thread.sleep(sleepTime);

		if (paused) {
			value = vibrationsGenerator.nextValue();
			motorOn = false;
			timeSwitchedLastOff = System.currentTimeMillis();
			timeSwitchedLastOn = System.currentTimeMillis();
			return createTuple(value);
		}

		if (motorOn) {
			if (keepSwitchedOn()) {
				value = vibrationsGenerator.nextValue();
			} else {
				motorOn = false;
				timeSwitchedLastOff = System.currentTimeMillis();
				System.out.println("Switched to off");
			}
		}

		if (!motorOn) {
			if (keepSwitchedOff()) {
				value = offVibrationGenerator.nextValue();
			} else {
				value = vibrationsGenerator.nextValue();
				motorOn = true;
				timeSwitchedLastOn = System.currentTimeMillis();
				secondsSwitchedOff = originalSecondsSwitchedOff;
				System.out.println("Switched to on");
			}
		}

		return createTuple(value);
	}

	private List<DataTuple> createTuple(double value) {
		// Create a tuple for that data
		DataTuple tuple = new DataTuple();
		tuple.addDouble(value);

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}

	private boolean keepSwitchedOff() {
		if (this.timeSwitchedLastOff + ((int) (this.secondsSwitchedOff * 1000) + (int) (this.randomFactorSecondsSwitchedOff * 1000)) > System.currentTimeMillis()) {
			return true;
		}

		// Switch the fridge off
		return false;
	}

	private boolean keepSwitchedOn() {
		if (this.timeSwitchedLastOn + ((int)(this.secondsSwitchedOn * 1000) + (int) (this.randomFactorSecondsSwitchedOn * 1000)) > System.currentTimeMillis()) {
			return true;
		}

		// Switch the fridge off
		return false;
	}

	@Override
	public void close() {
	}

	@Override
	protected void process_init() {
		// This is simple vibration - not too realistic, but ok. m/s^2
		vibrationsGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(1.5)), 0, 2.0, -5, 5);
		offVibrationGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(0.1)), 0, 0.15, -0.2,
				0.2);
		paused = false;
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return this;
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

	public void startCooling() {
		paused = false;
		motorOn = true;
	}
	
	public void createAnomaly() {
		double additionalTimeOff = (Math.random() + 0.5) * (secondsSwitchedOff / 10);
		secondsSwitchedOff += additionalTimeOff;
		System.out.println("Ok, we will have " + additionalTimeOff + " additional off seconds.");
	}

}
