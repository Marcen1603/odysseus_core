package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;

/**
 * Used to create IntervalPriorty metadata, if a ITimeInterval is already available inside
 * the plan.
 * @author jan
 * @deprecated only for testing purposes. don't use it!! ;)
 */
public class PriorityMetaUpdater extends AbstractMetadataUpdater<ITimeInterval,IMetaAttributeContainer<ITimeInterval>>{

	@Override
	public void updateMetadata(IMetaAttributeContainer<ITimeInterval> inElem) {
		ITimeInterval interval = (ITimeInterval) inElem.getMetadata();
		IntervalPriority priority = new IntervalPriority();
		priority.setStart(interval.getStart());
		priority.setEnd(interval.getEnd());
		inElem.setMetadata(priority);
	}

}
