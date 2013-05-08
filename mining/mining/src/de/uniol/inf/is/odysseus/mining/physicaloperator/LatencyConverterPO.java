package de.uniol.inf.is.odysseus.mining.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class LatencyConverterPO extends AbstractPipe<Tuple<? extends ILatency>, Tuple<? extends ILatency>> {

	private double factor;
	private int sample;
	private int counter = 0;
	private double latencySum = 0.0;

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
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.counter = 0;
	}

	@Override
	protected void process_next(Tuple<? extends ILatency> object, int port) {
		counter++;
		long latency = object.getMetadata().getLatency();
		double newlatency = latency / factor;
		latencySum = latencySum + newlatency;
		if (counter == sample) {
			Tuple<ILatency> t = new Tuple<>(2, false);
			t.setAttribute(0, latencySum / counter);
			t.setAttribute(1, object.getMetadata("CLUSTERING_DURATION"));
			t.setMetadata(object.getMetadata().clone());
			transfer(t);
			counter = 0;
			latencySum = 0.0;
		}
	}

	@Override
	public AbstractPipe<Tuple<? extends ILatency>, Tuple<? extends ILatency>> clone() {
		return new LatencyConverterPO(this);
	}

}
