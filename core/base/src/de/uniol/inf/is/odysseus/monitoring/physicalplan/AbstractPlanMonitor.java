package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public abstract class AbstractPlanMonitor<T> extends AbstractMonitoringData<T> {

	final List<IPhysicalOperator> monitoredOps;
	private boolean onlyRoots;
	
	public AbstractPlanMonitor(IQuery target, boolean onlyRoots) {
		super(target);
		this.onlyRoots = onlyRoots;
		monitoredOps = target.getRoots();
		if (!onlyRoots){
			monitoredOps.addAll(target.getPhysicalChilds());
		}
	}
	
	public AbstractPlanMonitor(AbstractPlanMonitor monitor) {
		super(monitor.getTarget());
		monitoredOps = new ArrayList<IPhysicalOperator>(monitor.monitoredOps);
		onlyRoots = monitor.onlyRoots;
	}

	protected boolean treatsOnlyRoots() {
		return onlyRoots;
	}


	@Override
	public IQuery getTarget() {
		return (IQuery) super.getTarget();
	}

}
