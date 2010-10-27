package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class PlanMonitor extends AbstractPlanMonitor<Double> {

	final String monitoringType;

	public PlanMonitor(IQuery target, boolean onlyRoots, String monitoringType,
			long monitoringPeriod) {
		super(target, onlyRoots);
		this.monitoringType = monitoringType;
		for (IPhysicalOperator p : monitoredOps) {
			if (!p.providesMonitoringData(monitoringType)){
				MonitoringDataTypes.createMetadata(monitoringType, p);
			}
			if (monitoringPeriod > 0){
				p.getMonitoringData(monitoringType, monitoringPeriod);
			}
		}
	}

	public PlanMonitor(PlanMonitor monitor) {
		super(monitor);
		this.monitoringType = monitor.monitoringType;
	}

	@Override
	public Double getValue() {
		return null;
	}

	@Override
	public void reset() {
		reset(monitoringType);
	}

	@Override
	public PlanMonitor clone() {
		return new PlanMonitor(this);
	}

	@Override
	public Double getValue(IPhysicalOperator operator) {
		return getValue(operator, monitoringType);
	}
	
	protected void reset(String type){
		for (IPhysicalOperator p : monitoredOps) {
			p.getMonitoringData(type).reset();
		}				
	}


}
