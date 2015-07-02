package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

/**
 * This is a very simple data generator which aims to evolve to a (physically
 * not really correct) wind power plant simulation. Used to test anomaly /
 * outlier detection.
 * 
 * @author Tobias Brandt
 * @since 13.05.2015
 *
 */
public class WindPowerPlantDataProvider extends AbstractDataGenerator {

	private int plantId;
	private PlantDataProvider plantDataProvider;
	private int sleepTime = 500;

	private double factor = 1;
	
	private final double ANOMALY_PROBABILITY = 0.005;

	public WindPowerPlantDataProvider(int plantId, PlantDataProvider plantDataProvider) {
		this.plantId = plantId;
		this.plantDataProvider = plantDataProvider;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(this.sleepTime);

		// Make the tuple for one plant
		DataTuple tuple = new DataTuple();

		// The id of the wind power plant
		tuple.addInteger(plantId);

		// The windSpeed (equal for each turbine)
		tuple.addAttribute(plantDataProvider.getWindSpeed());

		// The rotation speed
		double rotationSpeed = factor * plantDataProvider.getRotationSpeed();
		double random = Math.random();
		if (random < ANOMALY_PROBABILITY) {
			rotationSpeed = 42;
		}
		tuple.addAttribute(rotationSpeed);
		
		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}
	
	public void reducePlant() {
		this.factor = 0.8;
	}

	public void stopPlant() {
		this.factor = 0;
	}

	public void restartPlant() {
		this.factor = 1;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void process_init() {

	}

	@Override
	public IDataGenerator newCleanInstance() {
		return this;
	}

}
