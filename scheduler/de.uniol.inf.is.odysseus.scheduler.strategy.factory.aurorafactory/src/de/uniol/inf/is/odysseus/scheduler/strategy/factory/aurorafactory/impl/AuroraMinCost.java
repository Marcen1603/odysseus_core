package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListSchedulingStrategy;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class AuroraMinCost extends AbstractExecListSchedulingStrategy {

	public AuroraMinCost(IPartialPlan plan, boolean useIter) {
		super(plan, true);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPartialPlan plan) {
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		for (ISink<?> sink : plan.getRoots()) {
			postOrderAdd(sink, execList);
		}
		return execList;
	}
	
	@Override
	public void applyChangedPlan()
	{
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}

	private void postOrderAdd(ISink<?> sink, List<IIterableSource<?>> execList) {
		for (Subscription<? extends ISource<?>> sub : sink.getSubscribedTo()) {
			if (execList.contains(sub.target)) {
				continue;
			}
			if (!(sub.target instanceof ISink)) {
				continue;
			} else {
				postOrderAdd((ISink<?>) sub.target, execList);
			}
		}
		if (sink instanceof IIterableSource && this.getPlan().getIterableSource().contains(sink)) {
			execList.add((IIterableSource<?>) sink);
		}
	}
}
