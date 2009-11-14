package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

/**
 * Provides basic functionality for interval based operators to handle data
 * streams in presence of punctuations.
 * 
 * @author Jan Steinke, Jonas Jacobi
 * 
 * @param <W>
 * @param <R>
 */
public abstract class AbstractPunctuationPipe<W extends IMetaAttributeContainer<?>, R>
		extends AbstractPipe<R,W> implements IPunctuationPipe<R,W> {
	
    protected PunctuationStorage<W,R> storage = new PunctuationStorage<W,R>(this); 

	protected void updatePunctuationData(W object) {
		storage.updatePunctuationData(object);
	}

	@Override
	//make sure the punctuation storage contains a list for every input port,
	//as the number of input ports may increase after a subscription
	public void subscribeTo(ISource<? extends R> source, int sinkPort,
			int sourcePort) {
		super.subscribeTo(source, sinkPort, sourcePort);
		storage.subscribePort(getInputPortCount());
	}

	abstract public boolean cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current);

	@Override
	public void processPunctuation(PointInTime timestamp) {
		storage.storePunctuation(timestamp);
	}

	@Override
	public void transfer(W object) {
		transfer(object, 0);
		updatePunctuationData(object);
	};

	public PunctuationStorage<W,R> getStorage() {
		return storage;
	}	
	
}
