package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model.Windturbine;

public class WindturbineDataProvider extends AbstractDataGenerator {

	private List<Windturbine> windturbines;
	private long sleepTime = 1000;

	public WindturbineDataProvider(List<Windturbine> windturbines) {
		this.windturbines = windturbines;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(this.sleepTime);
		
		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		int i = 0;
		for (Windturbine windturbine : windturbines) {
			// Make the tuple for the state
			DataTuple tuple = new DataTuple();
			tuple.addInteger(i);
			tuple.addDouble(windturbine.getEnergyOutput());
			tuple.addDouble(windturbine.getRotationSpeed());
			tuple.addDouble(windturbine.getWindspeed());
			dataTuplesList.add(tuple);
			i++;
		}
		return dataTuplesList;
	}

	@Override
	public void close() {

	}

	@Override
	protected void process_init() {

	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new WindturbineDataProvider(windturbines);
	}

}
