package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;

public class AuroraMinCost extends AbstractExecListScheduling {

	public AuroraMinCost(IPartialPlan plan) {
		super(plan);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPartialPlan plan) {
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		for (IPhysicalOperator curRoot : plan.getRoots()) {
			if(curRoot.isSink()){
				postOrderAdd((ISink<?>)curRoot, execList);
			}
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
		for (PhysicalSubscription<? extends ISource<?>> sub : sink.getSubscribedToSource()) {
			if (execList.contains(sub.getTarget())) {
				continue;
			}
			if (!(sub.getTarget() instanceof ISink<?>)) {
				continue;
			} else {
				postOrderAdd((ISink<?>) sub.getTarget(), execList);
			}
		}
		if (sink instanceof IIterableSource<?> && this.getPlan().getIterableSource().contains(sink)) {
			execList.add((IIterableSource<?>) sink);
		}
	}
}
