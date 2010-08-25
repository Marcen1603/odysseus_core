package de.uniol.inf.is.odysseus.scars.operator.test.po;

import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorFactory;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.util.LoggerHelper;

public class TestPO<T> extends AbstractPipe<T, T>{

	private String name;
	private ISystemMonitor monitor;
	private ISystemMonitorFactory factory;
	
	public TestPO(String debugName) {
		this.name = debugName;
	}
	
	public TestPO(TestPO<T> copy ) {
		
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	public void process_open(){
		ServiceTracker tracker = new ServiceTracker(Activator.bc, ISystemMonitorFactory.class.getName(), null);
		tracker.open();
		this.monitor = ((ISystemMonitorFactory)tracker.getService()).newSystemMonitor(); 
		this.monitor.initialize();
	}

	@Override
	protected void process_next(T object, int port) {
		//LoggerHelper.getInstance(this.name).debug("TESTPO: Dataelement processed : " + object);
		transfer(object);
	}
	
	public void process_done(){
		LoggerHelper.getInstance(this.name).info("AvgMem: " + this.monitor.getAverageMemoryUsage());
		LoggerHelper.getInstance(this.name).info("MaxMem: " + this.monitor.getMaxMemoryUsage());
		this.monitor.stop();
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new TestPO<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
}
