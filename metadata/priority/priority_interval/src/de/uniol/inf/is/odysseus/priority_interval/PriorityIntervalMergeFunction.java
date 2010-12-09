package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityIntervalMergeFunction implements
		IInlineMetadataMergeFunction<IPriority> {

	@Override
	public void mergeInto(IPriority result, IPriority inLeft, IPriority inRight) {
		
		result
				.setPriority(inLeft.getPriority() > inRight.getPriority() ? inLeft
						.getPriority()
						: inRight.getPriority());
		
		((ITimeInterval)result).setStart(PointInTime.max(((ITimeInterval)inLeft).getStart(), ((ITimeInterval)inRight).getStart()));
		((ITimeInterval)result).setEnd(PointInTime.min(((ITimeInterval)inLeft).getEnd(), ((ITimeInterval)inRight).getEnd()));
	}

	@Override
	public PriorityIntervalMergeFunction clone()  {
		return new PriorityIntervalMergeFunction();
	}
	
}
