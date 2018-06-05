package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model.Pump;

public class PumpDataProvider extends AbstractDataGenerator {

	private Pump pump;
	private long sleepTime = 1000;

	public PumpDataProvider(Pump pump) {
		this.pump = pump;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(this.sleepTime);

		// Make the tuple for the state
		DataTuple tuple = new DataTuple();
		tuple.addDouble(this.pump.getCurrentFlowRate());
		tuple.addDouble(this.pump.getLastInFlow());
		tuple.addDouble(this.pump.getLastOutFlow());
		tuple.addDouble(this.pump.getCurrentVibration());

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
		return new PumpDataProvider(pump);
	}

}
