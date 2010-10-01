package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class TimeIntervalInlineMetadataMergeFunction implements
		IInlineMetadataMergeFunction<ITimeInterval> {
	
	
	public TimeIntervalInlineMetadataMergeFunction(
			TimeIntervalInlineMetadataMergeFunction timeIntervalInlineMetadataMergeFunction) {

	}
	
	public TimeIntervalInlineMetadataMergeFunction(){};

	@Override
	public void mergeInto(ITimeInterval result, ITimeInterval inLeft,
			ITimeInterval inRight) {
		result.setStart(PointInTime.max(inLeft.getStart(), inRight.getStart()));
		result.setEnd(PointInTime.min(inLeft.getEnd(), inRight.getEnd()));
	}

	@Override
	public TimeIntervalInlineMetadataMergeFunction clone()  {
		return new TimeIntervalInlineMetadataMergeFunction(this);
	}
	
}
