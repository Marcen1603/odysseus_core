package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

public class ManualDataProvider extends AbstractDataGenerator {

	private boolean sendNewValue = false;
	private double newValue;
	private Object lock = new Object();

	@Override
	public List<DataTuple> next() throws InterruptedException {

		while (!sendNewValue) {
			Thread.sleep(1);
		}

		synchronized (lock) {
			sendNewValue = false;
			// Create a tuple for that data
			DataTuple tuple = new DataTuple();
			tuple.addDouble(newValue);

			List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
			dataTuplesList.add(tuple);
			return dataTuplesList;
		}

	}

	public void sendNewValue(double value) {
		synchronized (lock) {
			this.newValue = value;
			sendNewValue = true;
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void process_init() {
		// TODO Auto-generated method stub

	}

	@Override
	public IDataGenerator newCleanInstance() {
		return this;
	}

}
