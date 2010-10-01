package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractDynamicScheduling;

/**
 * Schedules only <code>IIterableSource</code>s. It will always
 * schedule the source (mostly buffers) containing the most elements.
 * @author Jonas Jacobi, Marco Grawunder
 */

public class BiggestQueueScheduling extends AbstractDynamicScheduling {
	
	public BiggestQueueScheduling(IPartialPlan plan) {
		super(plan);
	}

	@Override
	public IIterableSource<?> nextSource() {
		int maxSize = 0; // Treat only Sources with at least one Element
		IIterableSource<?> currentSource = null;
		for (IIterableSource<?> curSource : operators) {
			int curSize = ((IBuffer<?>) curSource).size();
			if (curSize > maxSize) {
				maxSize = curSize;
				currentSource = curSource;
			}
		}
		return currentSource;
	}	
	
	@Override
	public void applyChangedPlan() {
		// Nothing to do
	}

}
