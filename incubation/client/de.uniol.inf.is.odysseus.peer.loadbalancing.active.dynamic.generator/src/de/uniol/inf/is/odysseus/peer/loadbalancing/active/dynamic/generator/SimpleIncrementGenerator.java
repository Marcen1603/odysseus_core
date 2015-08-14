package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.generator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

public class SimpleIncrementGenerator extends AbstractDataGenerator {

	public static final int SERVER_DEFAULT_PORT = 31337;
	private final int THREAD_SLEEP_MILLIS = 100;
	private long counter = 0;
	
	@Override
	public List<DataTuple> next() throws InterruptedException {
		
		Thread.sleep(THREAD_SLEEP_MILLIS);
		
		DataTuple tuple  = new DataTuple();
		tuple.addAttribute(new Long(System.currentTimeMillis()));
		tuple.addAttribute(new Long(counter));
		counter++;
		List<DataTuple> returnList = new ArrayList<DataTuple>();
		returnList.add(tuple);
		return  returnList;
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
		return new SimpleIncrementGenerator();
	}

}
