package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalTimestampAttributeTimeIntervalMFactory<M extends ITimeInterval>
		implements IMetadataFactory<M, RelationalTuple<M>> {

	private int attrPos;

	public RelationalTimestampAttributeTimeIntervalMFactory(int attrPos) {
		this.attrPos = attrPos;
	}

	public M createMetadata(RelationalTuple<M> inElem) {
		Number startN = (Number) inElem.getAttribute(attrPos);
		long startTime = startN.longValue();
		PointInTime start = new PointInTime(startTime, 0);
		inElem.getMetadata().setStart(start);
		return inElem.getMetadata();
	}

	public M createMetadata() {
		throw new UnsupportedOperationException();
	}
}