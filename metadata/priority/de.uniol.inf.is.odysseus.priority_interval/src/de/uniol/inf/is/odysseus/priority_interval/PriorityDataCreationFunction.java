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
			((IPriority)source.getMetadata()).setPriority((byte) 0);
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
	
	public PriorityDataCreationFunction<K,T> clone() throws CloneNotSupportedException{
		return new PriorityDataCreationFunction<K, T>(type);
	}

}
