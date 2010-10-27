package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface IPlanMonitor<T> extends IMonitoringData<T> {

	public boolean treatsOnlyRoots();
	public T getValue(IPhysicalOperator operator, String type);

}
