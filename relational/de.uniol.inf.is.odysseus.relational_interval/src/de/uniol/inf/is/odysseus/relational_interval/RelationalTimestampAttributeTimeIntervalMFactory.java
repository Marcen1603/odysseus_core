package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Jonas Jacobi
 */
public class RelationalTimestampAttributeTimeIntervalMFactory
		implements IMetadataUpdater<ITimeInterval, RelationalTuple<? extends ITimeInterval>> {

	private int attrPos;

	public RelationalTimestampAttributeTimeIntervalMFactory(int attrPos) {
		this.attrPos = attrPos;
	}

	@Override
	public void updateMetadata(RelationalTuple<? extends ITimeInterval> inElem) {
		Number startN = (Number) inElem.getAttribute(attrPos);
		long startTime = startN.longValue();
		PointInTime start = new PointInTime(startTime);
		inElem.getMetadata().setStart(start);
	}

}