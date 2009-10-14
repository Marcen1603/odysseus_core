package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityIntervalMergeFunction implements
		IInlineMetadataMergeFunction<IPriority> {

	@Override
	public void mergeInto(IPriority result, IPriority inLeft, IPriority inRight) {
		
		result
				.setPriority(inLeft.getPriority() > inRight.getPriority() ? inLeft
						.getPriority()
						: inRight.getPriority());
		ITimeInterval mergedInterval = TimeInterval.intersection((ITimeInterval)inLeft, (ITimeInterval)inRight);
	
		((IntervalPriority) result).setStart(mergedInterval.getStart());
		((IntervalPriority) result).setEnd(mergedInterval.getEnd());
	}

}
