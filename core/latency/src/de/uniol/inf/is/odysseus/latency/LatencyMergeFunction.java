package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.metadata.IInlineMetadataMergeFunction;

public class LatencyMergeFunction implements IInlineMetadataMergeFunction<ILatency> {

	@Override
	public void mergeInto(ILatency result, ILatency inLeft, ILatency inRight) {
		//only start timestamp get merged, 'cause the end timestamp should not be set,
		//when two elements get merged
		result.setLatencyStart(Math.max(inLeft.getLatencyStart(), inRight.getLatencyStart()));
	}

	@Override
	public LatencyMergeFunction clone() {
		return new LatencyMergeFunction();
	}
}
