package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;

abstract public class AbstractPunctuationBuffer<W extends IMetaAttributeContainer<?>, R>  extends BufferedPipe<W>{
	final protected List<PointInTime> punctuationStorage = new ArrayList<PointInTime>();

	/**
	 * Tests if the given input object matches with a number of stored punctuations
	 * and is used to handle punctuations when they get out of date (cleaning the
	 * punctuationStorage, send punctuations, ...).
	 * @param object input data with a timestamp
	 */
	protected void updatePunctuationData(W object) {
		
		if (punctuationStorage.size() > 0) {
			ITimeInterval time = (ITimeInterval) object.getMetadata();
			PointInTime start = time.getStart();
			
			List<PointInTime> toDelete = new ArrayList<PointInTime>();
			
			for (PointInTime each : punctuationStorage) {
				if(start.afterOrEquals(each)) {
					sendPunctuation(each);
					cleanInternalStates(each, object);
					toDelete.add(each);
				}
			}
			
			for(PointInTime each : toDelete) {
				punctuationStorage.remove(each);
			}
		}
	}	
	
	/**
	 * 
	 * @param punctuation
	 * @param current
	 */
	abstract protected void cleanInternalStates(PointInTime punctuation, IMetaAttributeContainer<?> current);
	
	@Override
	public void processPunctuation(PointInTime timestamp) {
		if(!punctuationStorage.contains(timestamp)) {
			punctuationStorage.add(timestamp);
		}
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
