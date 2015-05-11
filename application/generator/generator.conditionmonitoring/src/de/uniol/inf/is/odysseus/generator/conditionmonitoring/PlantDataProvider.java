package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.generator.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.noise.JitterNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;

public class PlantDataProvider {

	private AbstractSingleValueGenerator rotationSpeedGenerator;

	private double rotationSpeed;

	public PlantDataProvider() {
		this.rotationSpeedGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(0.2)), 5.0, 0.1, 0.0, 18);
		this.rotationSpeedGenerator.init();

		// Start a timer to update the values synchronously
		long delay = 5 * 1000; // 5 seconds
		long period = delay;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				PlantDataProvider.this.rotationSpeed = PlantDataProvider.this.rotationSpeedGenerator.nextValue();
			}
		}, delay, period);
	}

	public double getRotationSpeed() {
		// Add a bit noise, cause the wind power plants differ slightly
		JitterNoise noise = new JitterNoise(0.1);
		return noise.addNoise(rotationSpeed);
	}

}
