package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class DefaultTIDummyDataCreation implements IDummyDataCreationFunction<ITimeInterval, IMetaAttributeContainer<ITimeInterval>>{

	public DefaultTIDummyDataCreation(
			DefaultTIDummyDataCreation defaultTIDummyDataCreation) {
		
	}

	public DefaultTIDummyDataCreation(){};
	
	@SuppressWarnings("unchecked")
	@Override
	public IMetaAttributeContainer<ITimeInterval> createMetadata(
			IMetaAttributeContainer<ITimeInterval> source) {
		return (IMetaAttributeContainer<ITimeInterval>) source.clone();
	}

	@Override
	public boolean hasMetadata(IMetaAttributeContainer<ITimeInterval> source) {
		return true;
	}

	@Override
	public DefaultTIDummyDataCreation clone() {
		return new DefaultTIDummyDataCreation(this);
	}

}
