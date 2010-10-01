package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalTimestampAttributeTimeIntervalMFactory
		extends AbstractMetadataUpdater<ITimeInterval, RelationalTuple<? extends ITimeInterval>> {

	private int startAttrPos = -1;
	private int endAttrPos = -1;

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos, int endAttrPos ) {
		this.startAttrPos = startAttrPos;
		this.endAttrPos = endAttrPos;
	}

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos) {
		this(startAttrPos, -1);
	}
	
	@Override
	public void updateMetadata(RelationalTuple<? extends ITimeInterval> inElem) {
		PointInTime start = extractTimestamp(inElem, startAttrPos);
		inElem.getMetadata().setStart(start);
		if (endAttrPos > 0){
			PointInTime end = extractTimestamp(inElem, endAttrPos);
			inElem.getMetadata().setEnd(end);
		}
	}

	private PointInTime extractTimestamp(
			RelationalTuple<? extends ITimeInterval> inElem, int attrPos) {
		Number timeN = (Number) inElem.getAttribute(attrPos);
		PointInTime time = new PointInTime(timeN);
		return time;
	}

}