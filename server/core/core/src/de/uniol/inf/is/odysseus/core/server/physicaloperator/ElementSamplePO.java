package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SampleAO;

public class ElementSamplePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private final int sampleRate;
	private int current = 0;

	public ElementSamplePO(SampleAO sampleAO) {
		this.sampleRate = sampleAO.getSampleRate();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		current++;
		if (current == sampleRate) {
			transfer(object);
			current = 0;
		} else {
			transfer(object, 1);
		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
