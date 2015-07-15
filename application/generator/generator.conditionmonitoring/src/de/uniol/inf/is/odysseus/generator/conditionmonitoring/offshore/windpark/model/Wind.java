package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model;

import java.util.Timer;
import java.util.TimerTask;

public class Wind {

	private double windSpeed;
	private int periodLength;
	private int periodCounter;
	private String period;

	private final static String RISE = "rise";
	private final static String DOWNTURN = "downturn";
	private final static String STABLE = "stable";
	
	private final static double MAX_SPEED = 22;
	private final static double MIN_SPEED = 5;

	public Wind() {
		// Start at about 10 m/s
		this.windSpeed = 10 + Math.random();

		long delay = 0; // 0 seconds
		long windChangePeriod = 1 * 1000; // 10 seconds
		Timer switchTimer = new Timer();
		switchTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				changeWindspeed();
			}
		}, delay, windChangePeriod);
	}

	private void changeWindspeed() {
		periodCounter++;
		if (periodCounter >= periodLength) {
			// A new period (we decide, if the wind rises, falls or is stable)
			double random = Math.random();
			
			
			double prob = 0.8;
			if (windSpeed > MAX_SPEED) {
				// Higher probability to lower the speed
				prob = 0.9;
			} else if (windSpeed < MIN_SPEED) {
				prob = 0.7;
			}
			
			if (random < 0.6) {
				period = STABLE;
			} else if (random < prob) {
				period = DOWNTURN;
			} else {
				period = RISE;
			}

			periodCounter = 0;
			periodLength = (int) (Math.random() * 21);
		}

		double amount = Math.random() * 0.25;

		if (period.equals(STABLE)) {
			double random = Math.random();
			if (random < 0.5) {
				windSpeed += Math.random() * 0.1;
			} else {
				windSpeed -= Math.random() * 0.1;
				if (windSpeed < 0) {
					windSpeed = 0;
				}
			}
		} else if (period.equals(DOWNTURN)) {
			// Remove something
			windSpeed -= amount;
			if (windSpeed < 0)
				windSpeed = 0;
		} else {
			windSpeed += amount;
		}
	}

	/**
	 * 
	 * @return The current windspeed in m/s
	 */
	public double getWindSpeed() {
		return this.windSpeed;
	}
	
	public void changeWindspeed(double amount) {
		this.windSpeed += amount;
	}

}
