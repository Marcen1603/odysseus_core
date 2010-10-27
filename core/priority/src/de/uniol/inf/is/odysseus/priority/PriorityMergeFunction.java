package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.metadata.IInlineMetadataMergeFunction;

/**
 * @author Jonas Jacobi
 */
public class PriorityMergeFunction implements
		IInlineMetadataMergeFunction<IPriority> {

	@Override
	public void mergeInto(IPriority result, IPriority inLeft, IPriority inRight) {
		result
				.setPriority(inLeft.getPriority() > inRight.getPriority() ? inLeft
						.getPriority()
						: inRight.getPriority());
	}
	
	@Override
	public PriorityMergeFunction clone() {
		return new PriorityMergeFunction();
	}

}
