package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.intervalapproach.IDummyDataCreationFunction;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PriorityDataCreationFunction implements IDummyDataCreationFunction<IntervalPriority, IMetaAttributeContainer<IntervalPriority>>{

	@SuppressWarnings("unchecked")
	@Override
	public IMetaAttributeContainer<IntervalPriority> createMetadata(
			IMetaAttributeContainer<IntervalPriority> source) {
		IMetaAttributeContainer<IntervalPriority> dummy = (IMetaAttributeContainer<IntervalPriority>) source.clone();
		
		dummy.getMetadata().setPriority((byte) 0);
		return dummy;
	}

	@Override
	public boolean hasMetadata(IMetaAttributeContainer<IntervalPriority> source) {
		if(source.getMetadata().getPriority() > 0) {
			return true;
		}
		
		return false;
	}


}
