package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

abstract public class AbstractDynamicScheduling extends
		AbstractScheduling {

	protected List<IIterableSource<?>> operators;
	
	public AbstractDynamicScheduling(IPartialPlan plan) {
		super(plan);
		operators = new LinkedList<IIterableSource<?>>(plan.getIterableSource());
	}
	
	@Override
	public boolean isDone() {
		return operators.isEmpty();
	}

	@Override
	public void sourceDone(IIterableSource<?> source) {
		operators.remove(source);
	}


}
