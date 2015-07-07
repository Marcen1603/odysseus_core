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

	public final static double BREAK_DECELERATION = 0.1;
	public final static double ACCELERATION = 0.15;

	private Wind wind;

	private double rotationSpeed;
	private double energyOutput;

	private boolean isDefect;

	public Windturbine(Wind wind) {
		this.wind = wind;

		long firstDelay = 0; // 0 seconds
		long rotationPeriod = 1 * 500; // 1 second
		Timer rotationTimer = new Timer();
		rotationTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				rotate();
			}
		}, firstDelay, rotationPeriod);
	}

	private void rotate() {
		if (!isDefect) {
			if (wind.getWindSpeed() > MIN_WINDSPEED && wind.getWindSpeed() < MAX_WINDSPEED) {
				// the turbine is able to produce energy
				double usefulWindSpeed = wind.getWindSpeed() > MAX_POWER_WINDSPEED ? MAX_POWER_WINDSPEED
						: wind.getWindSpeed();
				double targetSpeed = MIN_ROTATION_SPEED + ((usefulWindSpeed - MIN_WINDSPEED)
						* ((MAX_ROTATION_SPEED - MIN_ROTATION_SPEED) / (MAX_POWER_WINDSPEED - MIN_WINDSPEED)));
				if (this.rotationSpeed < targetSpeed) {
					this.rotationSpeed += ACCELERATION * (targetSpeed - this.rotationSpeed);
					if (this.rotationSpeed > targetSpeed) {
						this.rotationSpeed = targetSpeed;
					}
				} else {
					this.rotationSpeed -= ACCELERATION * (this.rotationSpeed - targetSpeed);
					if (this.rotationSpeed < 0) {
						this.rotationSpeed = 0;
					}
				}
				double random = Math.random();
				if (random > 0.5) {
					this.rotationSpeed += Math.random() * 0.025;
				} else {
					this.rotationSpeed -= Math.random() * 0.025;
					if (this.rotationSpeed < 0) {
						this.rotationSpeed = 0;
					}
				}
			} else {
				// Slow down the turbine until it's off
				this.rotationSpeed -= this.rotationSpeed * BREAK_DECELERATION;
				if (this.rotationSpeed < 0.25) {
					this.rotationSpeed = 0;
				}
			}
			this.energyOutput = ((this.rotationSpeed - MIN_ROTATION_SPEED) / (MAX_ROTATION_SPEED - MIN_ROTATION_SPEED))
					* MAX_POWER_OUTPUT;
			if (this.energyOutput < 0) {
				this.energyOutput = 0;
			}
		} else {
			this.rotationSpeed = 0;
			this.energyOutput = 0;
		}
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

	public void setDefect(boolean defect) {
		this.isDefect = defect;
	}

}