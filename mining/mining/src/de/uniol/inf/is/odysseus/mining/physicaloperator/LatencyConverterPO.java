package de.uniol.inf.is.odysseus.mining.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class LatencyConverterPO extends AbstractPipe<Tuple<? extends ILatency>, Tuple<? extends ILatency>> {

	private double factor;
	private int sample;
	private int counter = 0;
	private double beforeSum = 0.0;
	private double afterSum = 0.0;
	private double transferSum = 0.0;

	public LatencyConverterPO(double factor, int sample) {
		super();
		this.factor = factor;
		this.sample = sample;
	}

	public LatencyConverterPO(LatencyConverterPO clone) {
		this.factor = clone.factor;
		this.sample = clone.sample;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.counter = 0;
		this.beforeSum = 0.0;
		this.afterSum = 0.0;
		this.transferSum = 0.0;
	}

	@Override
	protected void process_next(Tuple<? extends ILatency> object, int port) {
		counter++;
		// latency till clustering
		double timeBeforeClustering = 0.0;
		long tillClusteringEnd = 0;
		if (object.getMetadata("LATENCY_BEFORE") != null) {
			tillClusteringEnd = ((Long) object.getMetadata("LATENCY_BEFORE"));
			timeBeforeClustering = (tillClusteringEnd - object.getMetadata().getLatencyStart()) / factor;			
		}

		// latency after clustering
		long afterClusteringEnd = 0;
		double timeForClustering = 0.0;
		if(object.getMetadata("LATENCY_AFTER")!=null){
			afterClusteringEnd = ((Long) object.getMetadata("LATENCY_AFTER"));
			timeForClustering = (afterClusteringEnd - tillClusteringEnd) / factor;
		}
		

		// total latency till calclatency		
		long latency = object.getMetadata().getLatencyEnd();
		double transferLatency = (latency - afterClusteringEnd) / factor;

		beforeSum = beforeSum + timeBeforeClustering;
		afterSum = afterSum + timeForClustering;
		transferSum = transferSum + transferLatency;

		if (counter == sample) {
			Tuple<ILatency> t = new Tuple<>(4, false);
			double beforeMean = beforeSum / counter;
			double afterMean = afterSum / counter;
			double transferMean = transferSum / counter;
			double total = beforeMean + afterMean + transferMean;
			t.setAttribute(0, beforeMean);
			t.setAttribute(1, afterMean);
			t.setAttribute(2, transferMean);
			t.setAttribute(3, total);
			t.setMetadata(object.getMetadata().clone());
			transfer(t);
			counter = 0;
			this.beforeSum = 0.0;
			this.afterSum = 0.0;
			this.transferSum = 0.0;
		}
	}

	@Override
	public AbstractPipe<Tuple<? extends ILatency>, Tuple<? extends ILatency>> clone() {
		return new LatencyConverterPO(this);
	}

	@Override
	protected void process_done() {
		System.out.println("LatencyCalculation - done (open=" + isOpen() + ") - " + this);
	}

}
