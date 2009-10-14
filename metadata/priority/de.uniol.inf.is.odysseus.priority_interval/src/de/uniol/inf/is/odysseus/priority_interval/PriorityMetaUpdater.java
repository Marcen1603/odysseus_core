package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.priority.IPriority;

/**
 * Used to create IntervalPriorty metadata, if a ITimeInterval is already available inside
 * the plan.
 * @author jan
 *
 */
public class PriorityMetaUpdater implements IMetadataUpdater<ITimeInterval,IMetaAttributeContainer<ITimeInterval>>{

	@Override
	public void updateMetadata(IMetaAttributeContainer<ITimeInterval> inElem) {
		ITimeInterval interval = (ITimeInterval) inElem.getMetadata();
		IntervalPriority priority = new IntervalPriority();
		priority.setStart(interval.getStart());
		priority.setEnd(interval.getEnd());
		inElem.setMetadata(priority);
	}

}
