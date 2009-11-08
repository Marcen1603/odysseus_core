package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.loadshedding;

import java.util.List;

import de.uniol.inf.is.odysseus.loadshedding.LoadManager;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority.AbstractPriorityMinLatency;

public class AuroraMinLatencyLoadShedding extends AbstractPriorityMinLatency{

	public AuroraMinLatencyLoadShedding(IPartialPlan plan, boolean useIter) {
		super(plan, useIter);
	}
	
	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan plan) {
		
		LoadManager manager = LoadManager.getInstance();
		manager.addCapacities(plan);		
		
		List<IIterableSource<?>> toSchedule = plan.getIterableSource();
		List<IIterableSource<?>> execList = init(toSchedule);

		return execList;
	}

	@Override
	protected void executePriorisationActivation(ISource<?> source,
			List<ISource<?>> opPath) {
	}

}
