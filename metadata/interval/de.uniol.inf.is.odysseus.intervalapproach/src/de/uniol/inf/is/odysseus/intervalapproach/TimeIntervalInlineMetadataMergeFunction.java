package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IInlineMetadataMergeFunction;

public class TimeIntervalInlineMetadataMergeFunction implements
		IInlineMetadataMergeFunction<ITimeInterval> {

	@Override
	public void mergeInto(ITimeInterval result, ITimeInterval inLeft,
			ITimeInterval inRight) {
		result.setStart(PointInTime.max(inLeft.getStart(), inRight.getStart()));
		result.setEnd(PointInTime.min(inLeft.getEnd(), inRight.getEnd()));
	}

}
