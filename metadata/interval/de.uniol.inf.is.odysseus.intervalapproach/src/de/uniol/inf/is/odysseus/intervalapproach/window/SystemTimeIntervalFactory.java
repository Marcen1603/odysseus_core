package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;

public class SystemTimeIntervalFactory <M extends ITimeInterval, T extends IMetaAttribute<M>> implements IMetadataFactory<M, T>{

	@Override
	public M createMetadata(T inElem) {
		M metadata = inElem.getMetadata();
		metadata.setStart(PointInTime.currentPointInTime());
		return metadata;
	}

	@Override
	public M createMetadata() {
		throw new UnsupportedOperationException();
	}

}
