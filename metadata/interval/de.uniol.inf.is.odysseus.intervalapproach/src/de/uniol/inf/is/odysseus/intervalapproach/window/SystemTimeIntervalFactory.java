package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

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
