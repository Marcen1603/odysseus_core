package de.uniol.inf.is.odysseus.latency_relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.logicaloperator.latency.LatencyToPayloadAO;

public class LatencyToPayloadPO<M extends ILatency, T extends Tuple<M>> extends
		AbstractPipe<T, T> {

	final boolean append;
	final boolean small;

	public LatencyToPayloadPO(LatencyToPayloadAO operator) {
		this.append = operator.isAppend();
		this.small = operator.isSmall();
	}

	@Override
	public OutputMode getOutputMode() {
		if (append) {
			return OutputMode.MODIFIED_INPUT;
		} else {
			return OutputMode.NEW_ELEMENT;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {

		final int inputSize;
		if (append) {
			inputSize = object.size();
		} else {
			inputSize = 0;
		}
		final Tuple<ILatency> out;
		if (small) {
			out = new Tuple<ILatency>(inputSize + 1, false);
		} else {
			out = new Tuple<ILatency>(inputSize + 5, false);
		}

		if (append) {
			System.arraycopy(object.getAttributes(), 0, out.getAttributes(), 0,
					inputSize);
		}

		out.setAttribute(inputSize, object.getMetadata().getLatency());

		if (!small) {
			out.setAttribute(inputSize + 1, object.getMetadata()
					.getLatencyStart());
			out.setAttribute(inputSize + 2, object.getMetadata()
					.getLatencyEnd());
			out.setAttribute(inputSize + 3, object.getMetadata()
					.getMaxLatencyStart());
			out.setAttribute(inputSize + 4, object.getMetadata()
					.getMaxLatency());
		}
		out.setMetadata((ILatency) object.getMetadata().clone());
		out.setRequiresDeepClone(object.requiresDeepClone());
		transfer((T) out);

	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
