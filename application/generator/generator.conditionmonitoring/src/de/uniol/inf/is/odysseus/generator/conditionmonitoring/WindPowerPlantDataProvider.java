package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

/**
 * This is a very simple data generator which aims to evolve to a (physically
 * not really correct) wind power plant simulation. Used to test anomaly / outlier detection.
 * 
 * @author Tobias Brandt
 *
 */
public class WindPowerPlantDataProvider extends AbstractDataGenerator {

	private int plantId;
	private PlantDataProvider plantDataProvider;
	private int sleepTime = 100;

	private boolean stopped;

	public WindPowerPlantDataProvider(int plantId, PlantDataProvider plantDataProvider) {
		this.plantId = plantId;
		this.plantDataProvider = plantDataProvider;
		this.stopped = false;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(this.sleepTime);

		// Make the tuple for one plant
		DataTuple tuple = new DataTuple();
		// The id of the wind power plant
		tuple.addInteger(plantId);
		// The rotation speed of the windturbine, should be between 5 and 18 if
		// running
		if (!stopped)
			tuple.addAttribute(plantDataProvider.getRotationSpeed());
		else
			tuple.addAttribute(0.0);

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}

	public void stopPlant() {
		this.stopped = true;
	}

	public void restartPlant() {
		this.stopped = false;
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
