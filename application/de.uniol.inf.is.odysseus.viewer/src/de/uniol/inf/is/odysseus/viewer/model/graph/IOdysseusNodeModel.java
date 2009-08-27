package de.uniol.inf.is.odysseus.viewer.model.graph;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.ISubscriber;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.model.meta.IMetadataModel;

public interface IOdysseusNodeModel extends INodeModel<IPhysicalOperator>, IMetadataModel<IMonitoringData<?>>, ISubscriber<Object>{

}
