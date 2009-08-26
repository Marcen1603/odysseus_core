package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;

public interface IObservablePhysicalOperator extends IPhysicalOperator, IPOEventSender,
IMonitoringDataProvider{

}
