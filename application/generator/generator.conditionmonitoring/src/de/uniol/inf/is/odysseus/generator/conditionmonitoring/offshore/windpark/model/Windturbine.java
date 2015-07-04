package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model;

import java.util.Timer;
import java.util.TimerTask;

public class Windturbine {

	// Data roughly from the Siemens D7 platform

	// In kW
	public final static double MAX_POWER_OUTPUT = 7000;
	// In m/s
	public final static double MIN_WINDSPEED = 3;
	public final static double MAX_POWER_WINDSPEED = 13;
	public final static double MAX_WINDSPEED = 25;
	// In 1/min
	public final static double MIN_ROTATION_SPEED = 5;
	public final static double MAX_ROTATION_SPEED = 11;

	private Wind wind;

	private double rotationSpeed;
	private double energyOutput;

	public Windturbine(Wind wind) {
		this.wind = wind;

		long firstDelay = 0; // 0 seconds
		long rotationPeriod = 1 * 1000; // 1 second
		Timer rotationTimer = new Timer();
		rotationTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				rotate();
			}
		}, firstDelay, rotationPeriod);
	}

	private void rotate() {
		if (wind.getWindSpeed() > MIN_WINDSPEED && wind.getWindSpeed() < MAX_WINDSPEED) {
			// the turbine is able to produce energy
			double usefulWindSpeed = wind.getWindSpeed() > MAX_POWER_WINDSPEED ? MAX_POWER_WINDSPEED
					: wind.getWindSpeed();
			this.rotationSpeed = MIN_ROTATION_SPEED + ((usefulWindSpeed - MIN_WINDSPEED)
					* ((MAX_ROTATION_SPEED - MIN_ROTATION_SPEED) / (MAX_POWER_WINDSPEED - MIN_WINDSPEED)));
			double random = Math.random();
			if (random > 0.5) {
				this.rotationSpeed += Math.random();
			} else {
				this.rotationSpeed -= Math.random();
				if (this.rotationSpeed < 0) {
					this.rotationSpeed = 0;
				}
			}
		} else {
			this.rotationSpeed = 0;
		}
		this.energyOutput = ((this.rotationSpeed - MIN_ROTATION_SPEED) / (MAX_ROTATION_SPEED - MIN_ROTATION_SPEED)) * MAX_POWER_OUTPUT;
		if (this.energyOutput < 0)
			this.energyOutput = 0;
	}

	public double getRotationSpeed() {
		return rotationSpeed;
	}

	public double getEnergyOutput() {
		return energyOutput;
	}
	
	public double getWindspeed() {
		return wind.getWindSpeed();
	}

}
