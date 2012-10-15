package de.uniol.inf.is.odysseus.benchmark.physical;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.StreamString;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class DatarateCalcPO<R extends IStreamObject<?>> extends AbstractPipe<R, StreamString<?>> {

	long updateRate = 1;
	long elementsRead = 0;
	long firstTimestamp = -1;
	long lastTimestamp = -1;

	public DatarateCalcPO() {

	}

	public DatarateCalcPO(DatarateCalcPO<R> datarateCalcPO) {
		super(datarateCalcPO);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	public long getUpdateRate() {
		return updateRate;
	}

	public void setUpdateRate(long updateRate) {
		this.updateRate = updateRate;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		elementsRead = 0;
		firstTimestamp = System.nanoTime();
		lastTimestamp = firstTimestamp;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void process_next(R object, int port) {
		elementsRead++;		
		if (elementsRead % updateRate == 0) {
			long now = System.nanoTime();
			long lastPeriod = now - lastTimestamp;
			long fullPeriod = now - firstTimestamp;
			double elementsLastPeriod = Math.round(updateRate / lastPeriod * 100.0)/100.0; 
			double elementsAll = Math.round(elementsRead / fullPeriod * 100.0)/100.0; 
			
			transfer(new StreamString(elementsLastPeriod+";"+elementsAll));
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public AbstractPipe<R, StreamString<?>> clone() {
		return new DatarateCalcPO<R>(this);
	}

}
