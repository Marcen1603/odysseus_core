package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model.Valve;

public class ValveDataProvider extends AbstractDataGenerator implements Observer {

	private Valve valve;
	private long sleepTime = 100;
	private boolean changed;

	public ValveDataProvider(Valve valve) {
		this.changed = true;
		this.valve = valve;
		this.valve.addObserver(this);
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		while (!this.changed) {
			Thread.sleep(this.sleepTime);
		}

		this.changed = false;

		// Make the tuple for the state
		DataTuple tuple = new DataTuple();
		tuple.addBoolean(this.valve.isOpen());

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
		return new ValveDataProvider(valve);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.changed = true;
	}

}
