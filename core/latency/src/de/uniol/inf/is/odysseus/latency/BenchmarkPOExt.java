package de.uniol.inf.is.odysseus.latency;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class BenchmarkPOExt<R extends IMetaAttributeContainer<? extends ILatency>> extends
		AbstractPipe<R, R> {

	final double selectivity;
	double oldVal = 0;
	long processingTime = 300;

	LinkedList<R> buffer;
	
	/**
	 * 
	 * @param processingTime
	 * @param selectivity
	 *            Nur wenn Selektivitaeten sich zu maximal 1 adieren (z.B. 1/3)
	 *            wird in jedem Durchlauf maximal ein Element geschrieben,
	 *            ansonsten koennen auch mal mehrere geschrieben werden
	 */
	public BenchmarkPOExt(long processingTime, double selectivity) {
		super();
		this.processingTime = processingTime;
		this.selectivity = selectivity;
		this.buffer = new LinkedList<R>();
	};

	public BenchmarkPOExt(BenchmarkPOExt<R> benchmarkPO) {
		this.processingTime = benchmarkPO.processingTime;
		this.selectivity = benchmarkPO.selectivity;
	}

	
	long last = 0;
	@Override
	protected final void process_next(R object, int port) {
		long now = System.nanoTime();
//		System.out.println("next incoming in BPO: " + (now - last) + " with lat start: " + object.getMetadata().getLatencyStart());
		this.last = now;
		// elements will be processed after punctuation arrived
		this.buffer.addLast(object);
//		long end = System.nanoTime() + this.processingTime;
//		this.processData(end);
	}
	

	private void waitProcessingTime(long end) {
		while (System.nanoTime() < end)
			;
	}

	protected void setOldVal(R object, double d) {
		this.oldVal = d;
	}

	protected double getOldVal(R element) {
		return oldVal;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	@Override
	public String toString() {
		return super.toString() + " " + getName() + " {Sel " + selectivity
				+ " PTime " + processingTime + "}";
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public BenchmarkPOExt<R> clone() {
		return new BenchmarkPOExt(this);
	}

	private void processData(long endTime){
		
		waitProcessingTime(endTime);
		long latencyStart = 0;
		if(!buffer.isEmpty()){
			latencyStart = buffer.peekFirst().getMetadata().getLatencyStart();
		}
		while(!buffer.isEmpty()){
			R next = buffer.pollFirst();
//			double addNSSec = (double)next.getMetadata().getLatencyStart() - (double)latencyStart;
//			double addSec = addNSSec/1000000;
//			System.out.println("Will add " + addNSSec);
			next.getMetadata().setLatencyStart(latencyStart);
			transfer(next);
		}
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		long now = System.nanoTime();
//		System.out.println("next punc in BPO: " + (now - last));
		this.last = now;
		
		long end = System.nanoTime() + this.processingTime;
		
		if (selectivity == 1) {
			this.processData(end);
		} else {

			throw new RuntimeException("Not implemented yet.");
//			// Bestimmen, wie viele Objekte produziert werden sollen
//			// alle ganzzahligen Vielfachen der Selektitivitaet
//			// oldVal speichert die Reste
//			double tmpOldVal = getOldVal(object) + selectivity;
//			long toProduce = Math.round(Math.floor(tmpOldVal));
//			setOldVal(object, tmpOldVal - toProduce);
//			waitProcessingTime(end);
//			while (toProduce-- > 0) {
//				if (toProduce > 1) {
//					object = (R) object.clone();
//				}
//				transfer(object);
//			}
		}

		sendPunctuation(timestamp);
	}

}
