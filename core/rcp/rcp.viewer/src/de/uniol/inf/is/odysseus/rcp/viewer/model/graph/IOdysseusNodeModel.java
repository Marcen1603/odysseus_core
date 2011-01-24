package de.uniol.inf.is.odysseus.rcp.viewer.model.graph;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.ISubscriber;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataModel;

public interface IOdysseusNodeModel extends INodeModel<IPhysicalOperator>, IMetadataModel<IMonitoringData<?>>, ISubscriber<Object>{

}
