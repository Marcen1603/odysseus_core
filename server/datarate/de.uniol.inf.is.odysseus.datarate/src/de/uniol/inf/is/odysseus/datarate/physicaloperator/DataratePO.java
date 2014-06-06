package de.uniol.inf.is.odysseus.datarate.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.datarate.logicaloperator.DatarateAO;

public class DataratePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private int updateRate;
	
	private long elementsRead = 0;
	private long lastTimestamp = -1;
	private double currentDatarate = 0;
	
	public DataratePO( DatarateAO ao ) {
		updateRate = ao.getUpdateRate();
	}
	
	public DataratePO( DataratePO<T> other ) {
		updateRate = other.updateRate;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		elementsRead = 0;
		lastTimestamp = System.nanoTime();
	}
	
	@Override
	protected void process_next(T object, int port) {
		if( updateRate > 0 && object.getMetadata() instanceof IDatarate ) {
			
			elementsRead++;		
			if (elementsRead == updateRate) {
				long now = System.nanoTime();
				long lastPeriodNano = now - lastTimestamp;
				
				double lastDataRateNano = updateRate / (double)lastPeriodNano;
				
				currentDatarate = lastDataRateNano * 1000000000.0;
				lastTimestamp = now;
				elementsRead = 0;
			}
			
		}
		
		((IDatarate)object.getMetadata()).setDatarate(currentDatarate);
		transfer(object);
	}
	
	@Override
	protected DataratePO<T> clone() throws CloneNotSupportedException {
		return new DataratePO<T>(this);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if( !(ipo instanceof DataratePO)) {
			return false;
		}
		return ipo == this;
	}
	
	@Override
	public String getName() {
		return "Datarate";
	}
}
