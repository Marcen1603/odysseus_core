package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class SimplePriorityMinLatency extends AbstractPriorityMinLatency{

	public SimplePriorityMinLatency(IPartialPlan plan, boolean useIter) {
		super(plan, useIter);
	}

	@Override
	protected void executePriorisationActivation(ISource<?> source, List<ISource<?>> opPath) {
		// Muss nichts weiter machen, da die Strategie nur Teilplaene bevorzugt,
		// die zu einem Join fuehren, dass priorisierte Datenstromelemente enthalten koennte.
	}

}
