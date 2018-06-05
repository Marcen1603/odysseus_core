package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Pump extends Observable {

	// In liters per minute
	private double maxFlowRate;
	private double currentFlowRate;

	private double lastInFlow;
	private double lastOutFlow;
	private Pipe inPipe;
	private Pipe outPipe;

	// To simulate failures
	private boolean hasFailure;
	private boolean hasDefect;

	public Pump(double maxFlowRate, Pipe inPipe, Pipe outPipe) {
		this.maxFlowRate = maxFlowRate;
		this.inPipe = inPipe;
		this.outPipe = outPipe;
		this.hasFailure = false;
		this.hasDefect = false;
	}

	public void startPump() {
		if (hasFailure) {
			return;
		}
		// Start pump
		final int neededSteps = 20;
		long startUpDelay = 0; // 0 seconds
		long stepDelay = 5 * 100; // 0.5 seconds
		final Timer startUpTimer = new Timer();
		startUpTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (hasFailure) {
					currentFlowRate = 0;
					startUpTimer.cancel();
				} else {
					currentFlowRate += maxFlowRate / neededSteps;
					if (currentFlowRate >= maxFlowRate) {
						currentFlowRate = maxFlowRate;
						setChanged();
						notifyObservers(currentFlowRate);
						startUpTimer.cancel();
					}
				}

			}
		}, startUpDelay, stepDelay);
	}

	public void shutDownPump() {
		// Start pump
		final int neededSteps = 20;
		long startUpDelay = 0; // 0 seconds
		long stepDelay = 5 * 100; // 0.5 seconds
		final Timer shutDownTimer = new Timer();
		shutDownTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				currentFlowRate -= maxFlowRate / neededSteps;
				if (currentFlowRate <= 0) {
					currentFlowRate = 0;
					setChanged();
					notifyObservers(currentFlowRate);
					shutDownTimer.cancel();
				}
			}
		}, startUpDelay, stepDelay);
	}

	public void doStep() {
		this.lastInFlow = inPipe.suck(currentFlowRate);
		this.lastOutFlow = outPipe.push(this.lastInFlow);
	}

	public boolean isRunning() {
		return this.currentFlowRate > 0;
	}

	public double getCurrentFlowRate() {
		return this.currentFlowRate;
	}

	public double getLastInFlow() {
		return lastInFlow;
	}

	public double getLastOutFlow() {
		return lastOutFlow;
	}

	/**
	 * In absolute values: mm/s from about 0 to 10
	 * 
	 * @return
	 */
	public double getCurrentVibration() {
		double vibration = ((this.lastInFlow / this.maxFlowRate) * 10) + Math.random();
		if (!hasDefect)
			return vibration;
		return vibration * (1 + (Math.random() * 0.5));
	}

	public void setDefect(boolean hasDefect) {
		this.hasDefect = hasDefect;
	}

	public boolean isDefect() {
		return this.hasDefect;
	}

	public void setFailure(boolean failure) {
		this.hasFailure = failure;
		if (hasFailure)
			this.currentFlowRate = 0;
	}

	public boolean isFailed() {
		return this.hasFailure;
	}
}
