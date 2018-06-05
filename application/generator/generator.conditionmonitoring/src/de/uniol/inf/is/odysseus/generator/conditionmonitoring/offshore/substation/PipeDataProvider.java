package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model.Pipe;

public class PipeDataProvider extends AbstractDataGenerator implements Observer {

	private Pipe pipe;
	private long sleepTime = 1000;
	private double lastThroughput;

	public PipeDataProvider(Pipe pipe) {
		this.pipe = pipe;
		this.pipe.addObserver(this);
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(this.sleepTime);

		// Make the tuple for the state
		DataTuple tuple = new DataTuple();
		tuple.addDouble(this.lastThroughput);

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
		return new PipeDataProvider(pipe);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Double) {
			this.lastThroughput = (double) arg;
		}
	}

}
