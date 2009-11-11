package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.intervalapproach.IDummyDataCreationFunction;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityDataCreationFunction<K extends IMetaAttribute,T extends IMetaAttributeContainer<K>> implements IDummyDataCreationFunction<K,T>{

	Class<K> type;
	
	public PriorityDataCreationFunction(Class<K> type) {
		this.type = type;
	}
	
	@Override
	public T createMetadata(T source) {
		try {
			source.setMetadata(type.newInstance());
			return source;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasMetadata(T source) {
		if(((IPriority)source.getMetadata()).getPriority() > 0) {
			return true;
		}
		
		return false;
	}
/*
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
	}*/


}
