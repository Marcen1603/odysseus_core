package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

@SuppressWarnings("rawtypes")
public abstract class AbstractPlanMonitor<T> extends AbstractMonitoringData<T>
		implements IPlanMonitor<T> {

	final List<IPhysicalOperator> monitoredOps = new ArrayList<IPhysicalOperator>();
	private boolean onlyRoots;

	public AbstractPlanMonitor(IQuery target, boolean onlyRoots, String type) {
		super(target, type);
		this.onlyRoots = onlyRoots;
		monitoredOps.addAll(target.getRoots());
		if (!onlyRoots) {
			monitoredOps.addAll(target.getPhysicalChilds());
		}
	}

	@SuppressWarnings("unchecked")
	public AbstractPlanMonitor(AbstractPlanMonitor monitor) {
		super(monitor.getTarget(), monitor.getType());
		monitoredOps.addAll(monitor.monitoredOps);
		onlyRoots = monitor.onlyRoots;
	}

	@Override
	public boolean treatsOnlyRoots() {
		return onlyRoots;
	}

	@Override
	public IQuery getTarget() {
		return (IQuery) super.getTarget();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getValue(IPhysicalOperator operator, String type) {
		return (T) operator.getMonitoringData(type).getValue();
	}

	abstract public T getValue(IPhysicalOperator operator);
}
