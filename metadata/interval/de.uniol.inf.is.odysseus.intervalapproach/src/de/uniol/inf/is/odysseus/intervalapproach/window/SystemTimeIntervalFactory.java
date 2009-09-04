package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;

/**
 * @author Jonas Jacobi
 */
public class SystemTimeIntervalFactory <M extends ITimeInterval, T extends IMetaAttribute<M>> implements IMetadataUpdater<M, T>{

	@Override
	public void updateMetadata(T inElem) {
		M metadata = inElem.getMetadata();
		metadata.setStart(PointInTime.currentPointInTime());
	}

}
