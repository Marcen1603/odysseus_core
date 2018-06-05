package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.generator.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.noise.JitterNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;

public class PlantDataProvider {

	private AbstractSingleValueGenerator rotationSpeedGenerator;
	private AbstractSingleValueGenerator windSpeedGenerator;

	private double rotationSpeed;
	private double windSpeed;

	// m/s
	private double maxWindSpeed = 120;
	private double minWindSpeed = 0;

	// 1/min
	private double maxRotationSpeed = 18;
	private double minRotationSpeed = 0;

	public PlantDataProvider() {
		this.rotationSpeedGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(0.2)), 5.0, 0.1,
				minRotationSpeed, maxRotationSpeed);
		this.rotationSpeedGenerator.init();

		this.windSpeedGenerator = new AlternatingGenerator(new RandomErrorModel(new JitterNoise(0.2)), 20.0, 0.5,
				minWindSpeed, maxWindSpeed);
		this.windSpeedGenerator.init();

		// Start a timer to update the values synchronously
		long delay = 1 * 100; // 0.1 seconds
		long period = delay;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				PlantDataProvider.this.windSpeed = PlantDataProvider.this.windSpeedGenerator.nextValue();
				PlantDataProvider.this.rotationSpeed = PlantDataProvider.this.windSpeed / 6.666;
			}
		}, delay, period);
	}

	public double getRotationSpeed() {
		// Add a bit noise, cause the wind power plants differ slightly
		JitterNoise noise = new JitterNoise(0.1);
		return noise.addNoise(rotationSpeed);
	}

	public double getWindSpeed() {
		return this.windSpeed;
	}

}
