package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model;

import java.util.ArrayList;
import java.util.List;

public class OffshoreWindparkManager {

	public static final int NUMBER_WINDTURBINES = 80;
	
	private List<Windturbine> windturbines;
	private Wind wind;

	public OffshoreWindparkManager() {

		wind = new Wind();

		windturbines = new ArrayList<>();
		for (int i = 0; i < NUMBER_WINDTURBINES; i++) {
			Windturbine windturbine = new Windturbine(wind);
			windturbines.add(windturbine);
		}
	}

	public List<Windturbine> getWindturbines() {
		return windturbines;
	}

	public Wind getWind() {
		return wind;
	}
	
	public double getMaximumEnergyOutput() {
		return NUMBER_WINDTURBINES * Windturbine.MAX_POWER_OUTPUT;
	}
	
	

}
