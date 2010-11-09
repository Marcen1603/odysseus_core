package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import de.uniol.inf.is.odysseus.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class PlanMonitor extends AbstractPlanMonitor<Double> {

	@SuppressWarnings("rawtypes")
	public PlanMonitor(IQuery target, boolean onlyRoots, String monitoringType,
			long monitoringPeriod) {
		super(target, onlyRoots, monitoringType);
		for (IPhysicalOperator p : monitoredOps) {
			if (monitoringPeriod <= 0) {
				if (!p.providesMonitoringData(monitoringType)) {
					p.addMonitoringData(monitoringType, MonitoringDataTypes
							.createMetadata(monitoringType, p));
				}
			} else {
				p.getMonitoringData(
						(IPeriodicalMonitoringData) MonitoringDataTypes
								.createMetadata(monitoringType, p),
						monitoringPeriod);
			}
		}
	}

	public PlanMonitor(PlanMonitor monitor) {
		super(monitor);
	}

	@Override
	public Double getValue() {
		return null;
	}

	@Override
	public void reset() {
		reset(getType());
	}

	@Override
	public PlanMonitor clone() {
		return new PlanMonitor(this);
	}

	@Override
	public Double getValue(IPhysicalOperator operator) {
		return getValue(operator, getType());
	}

	protected void reset(String type) {
		for (IPhysicalOperator p : monitoredOps) {
			p.getMonitoringData(type).reset();
		}
	}
	
}
