package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model.Pump;

public class PumpStateDataProvider extends AbstractDataGenerator implements Observer {

	private Pump pump;
	private long sleepTime = 100;
	private boolean isRunning;
	private boolean changed;

	public PumpStateDataProvider(Pump pump) {
		this.pump = pump;
		this.pump.addObserver(this);
		this.isRunning = this.pump.isRunning();
		this.changed = true;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		while (!changed) {
			Thread.sleep(this.sleepTime);
		}
		changed = false;

		DataTuple tuple = new DataTuple();
		tuple.addBoolean(isRunning);

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
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
		return new PumpStateDataProvider(pump);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (pump.isRunning() != this.isRunning) {
			this.isRunning = pump.isRunning();
			this.changed = true;
		}
	}

}
