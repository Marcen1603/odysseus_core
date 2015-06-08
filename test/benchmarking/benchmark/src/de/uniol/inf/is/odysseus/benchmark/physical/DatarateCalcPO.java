package de.uniol.inf.is.odysseus.benchmark.physical;

import de.uniol.inf.is.odysseus.core.collection.Document;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class DatarateCalcPO<R extends IStreamObject<?>> extends AbstractPipe<R, Document<?>> {

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
			long lastPeriodNano = now - lastTimestamp;
			long fullPeriodNano = now - firstTimestamp;
			
			double lastDataRateNano = updateRate / (double)lastPeriodNano;
			double fullDataRateNano = elementsRead / (double)fullPeriodNano;
			
			double lastDataRateSecond = lastDataRateNano * 1000000000.0;
			double fullDataRateSecond = fullDataRateNano * 1000000000.0;
			
//			System.out.println(String.format("%-10.3f; %-10.3f", lastDataRateSecond, fullDataRateSecond));
			transfer(new Document(String.format("%-10.3f; %-10.3f", lastDataRateSecond, fullDataRateSecond)));
			lastTimestamp = now;
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
