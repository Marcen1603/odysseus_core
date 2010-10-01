package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

/**
 * @author Jonas Jacobi
 */
public class SystemTimeIntervalFactory <M extends ITimeInterval, T extends IMetaAttributeContainer<M>> extends AbstractMetadataUpdater<M, T>{

	@Override
	public void updateMetadata(T inElem) {
		M metadata = inElem.getMetadata();
		metadata.setStart(PointInTime.currentPointInTime());
	}

}
