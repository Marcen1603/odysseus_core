package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;
import de.uniol.inf.is.odysseus.priority.PriorityPO;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListSchedulingStrategy;

public abstract class AbstractPriorityMinLatency extends
		AbstractExecListSchedulingStrategy {

	public AbstractPriorityMinLatency(IPartialPlan plan, boolean useIter) {
		super(plan, true);
	}

	private void initMetadata(List<ISource<?>> opPath) {
		for (ISource<?> s : opPath) {
			if (!s.providesMonitoringData("estimated productivity")) {
				s.addMonitoringData("estimated productivity",
						new StaticValueMonitoringData<Double>(s,
								"estimated productivity", 1d));
			}

			if (!s.providesMonitoringData("estimated processing cost")) {
				s.addMonitoringData("estimated processing cost",
						new StaticValueMonitoringData<Double>(s,
								"estimated processing cost", 1d));
			}
		}
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPartialPlan plan) {
		List<IIterableSource<?>> toSchedule = plan.getIterableSource();

		List<IIterableSource<?>> execList = init(toSchedule);

		return execList;
	}

	@Override
	public void applyChangedPlan() {
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}

	private List<IIterableSource<?>> init(List<IIterableSource<?>> toSchedule) {
		List<FESortedPair<Double, List<IIterableSource<?>>>> pathes = new LinkedList<FESortedPair<Double, List<IIterableSource<?>>>>();
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		// Calc for every to schedule operator the path to the root;

		List<Integer> joins = new ArrayList<Integer>();

		for (ISource<?> s : toSchedule) {
			List<IIterableSource<?>> schPath = new LinkedList<IIterableSource<?>>();
			List<ISource<?>> opPath = new LinkedList<ISource<?>>();

			getPathToRoot(s, schPath, opPath, null);
			initMetadata(opPath);
			FESortedPair<Double, List<IIterableSource<?>>> pWithCost = new FESortedPair<Double, List<IIterableSource<?>>>(
					calcPathCost(opPath), schPath);
			if (hasToBePrefered(s, opPath, joins)) {
				pWithCost.setE1(-1.0);
				executePriorisationActivation(s, opPath);
			}

			pathes.add(pWithCost);
		}
		// Sort pathes respecting cost
		Collections.sort(pathes);

		// Add Pathes to execList
		for (FESortedPair<Double, List<IIterableSource<?>>> p : pathes) {
			execList.addAll(p.getE2());
		}

		return execList;
	}

	private boolean hasToBePrefered(ISource<?> source, List<ISource<?>> opPath,
			List<Integer> joins) {

		boolean priority = false;
		boolean join = false;
		int tmpHash = 0;

		for (ISource<?> operator : opPath) {

			if (operator instanceof PriorityPO<?>
					|| operator instanceof PostPriorisationPO<?>) {
				priority = true;
			}

			if (operator instanceof JoinTIPO<?, ?>) {
				join = true;
				if (joins.contains(operator.hashCode())) {
					return true;
				} else {
					tmpHash = operator.hashCode();
				}
			}

			if (priority && join) {
				joins.add(tmpHash);
				return true;
			}
		}

		return false;
	}

	abstract protected void executePriorisationActivation(ISource<?> source,
			List<ISource<?>> opPath);

}
