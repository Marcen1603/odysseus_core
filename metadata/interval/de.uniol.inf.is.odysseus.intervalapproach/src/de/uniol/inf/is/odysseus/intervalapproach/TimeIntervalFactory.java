package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;

public class TimeIntervalFactory implements IMetadataFactory<ITimeInterval, Object> {

	@Override
	public ITimeInterval createMetadata(Object inElem) {
		return this.createMetadata();
	}

	@Override
	public ITimeInterval createMetadata() {
		return new TimeInterval();
	}

}
