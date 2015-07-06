package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model.Windturbine;

public class Transformer implements Observer {

	private static final double HEAT_INTERCHANGE_DEGREE = 0.1;
	private static final double HEATUP_DEGREE = 0.02;
	private static final double STANDARD_WATER_FLOW_LITRES = 2000;
	private static final double MAX_TEMPERATURE = 200;

	private Pipe coolPipe;
	private double temperature;
	private List<Windturbine> windturbines;
	private double maxEnergy;
	private double lastTransformedEnergy;

	public Transformer(Pipe coolPipe, double maxEnergy, List<Windturbine> windturbines) {
		this.coolPipe = coolPipe;
		this.coolPipe.addObserver(this);
		this.temperature = 20;
		this.maxEnergy = maxEnergy;
		this.windturbines = windturbines;

		long startUpDelay = 0; // 0 seconds
		long stepDelay = 1 * 1000; // 1 second
		final Timer heatUpTimer = new Timer();
		heatUpTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				heatUp();
			}
		}, startUpDelay, stepDelay);
	}

	private void heatUp() {
		double dif = MAX_TEMPERATURE - this.temperature;
		this.lastTransformedEnergy = calcEnergyProduction();
		this.temperature += (dif * HEATUP_DEGREE * (this.lastTransformedEnergy / maxEnergy));
	}

	private void coolDown(double amountOfWater) {
		double dif = this.temperature - Pipe.WATER_TEMPERATURE;
		this.temperature = this.temperature
				- (dif * HEAT_INTERCHANGE_DEGREE * (amountOfWater / STANDARD_WATER_FLOW_LITRES));
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Double) {
			double flow = (double) arg;
			coolDown(flow);
		}
	}

	public double getTemperature() {
		return this.temperature;
	}

	private double calcEnergyProduction() {
		double amount = 0;
		for (Windturbine windturbine : windturbines) {
			amount += windturbine.getEnergyOutput();
		}

		return amount;
	}

	public double getLastTransformedEnergy() {
		return lastTransformedEnergy;
	}

	public void setLastTransformedEnergy(double lastTransformedEnergy) {
		this.lastTransformedEnergy = lastTransformedEnergy;
	}

}
