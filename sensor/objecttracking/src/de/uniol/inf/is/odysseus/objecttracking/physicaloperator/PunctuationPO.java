package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.util.LoggerHelper;

/**
 * This operator inserts a punctuation after each x-th element in the
 * stream. This can be parameterized in PQLHack.
 * 
 * @author Andre Bolles
 *
 * @param <T>
 * @param <M>
 */
public class PunctuationPO<T extends IMetaAttributeContainer<M>, M extends ILatency & IProbability & ITimeInterval> extends AbstractPipe<T, T>{

	private int punctuationElemCount;
	
	public PunctuationPO(int puncCount){
		super();
		this.punctuationElemCount = puncCount;
	}
	
	private PunctuationPO(PunctuationPO old){
		super(old);
		this.punctuationElemCount = old.punctuationElemCount;
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		return new PunctuationPO(this);
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return AbstractPipe.OutputMode.MODIFIED_INPUT;
	}

	int elemCount; 
	@Override
	protected void process_next(T object, int port) {
		this.transfer(object);
		this.elemCount++;
		if(this.elemCount % this.punctuationElemCount == 0){
			this.sendPunctuation(object.getMetadata().getStart());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
		
	}
	
}
