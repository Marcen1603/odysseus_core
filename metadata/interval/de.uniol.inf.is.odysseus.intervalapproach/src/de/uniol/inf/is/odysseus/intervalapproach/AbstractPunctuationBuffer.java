package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;

/**
 * Provides basic functionality for interval based buffers to handle data
 * streams in presence of punctuations.
 * 
 * @author Jan Steinke
 * 
 * @param <W>
 * @param <R>
 */
abstract public class AbstractPunctuationBuffer<W extends IMetaAttributeContainer<?>, R>  extends BufferedPipe<W> implements IPunctuationPipe<W,W>{
	final protected List<PointInTime> punctuationStorage = new ArrayList<PointInTime>();

	
	protected PunctuationStorage<W,W> storage = new PunctuationStorage<W,W>(this); 
	
	protected void updatePunctuationData(W object) {
		storage.updatePunctuationData(object);
	}	
	
	abstract public boolean cleanInternalStates(PointInTime punctuation, IMetaAttributeContainer<?> current);
	
	@Override
	public void processPunctuation(PointInTime timestamp) {
		storage.subscribePort(1);
		storage.storePunctuation(timestamp);
	}	
	
	@Override
	public void transfer(W object) {
		transfer(object,0);
		updatePunctuationData(object);
	};	
	
	@Override
	public void transferNext() {
		super.transferNext();
	}

	@Override
	abstract public OutputMode getOutputMode();

	
}
