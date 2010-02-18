package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * 
 * @author Tobias Witt
 *
 */
public class InstallMetadataListenerVisitor implements INodeVisitor<IPhysicalOperator, Object> {
	
	private AdvancedOptimizer optimizer;
	
	public InstallMetadataListenerVisitor(AdvancedOptimizer optimizer) {
		this.optimizer = optimizer;
	}

	@Override
	public void ascend(IPhysicalOperator to) {
	}

	@Override
	public void descend(IPhysicalOperator to) {
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public void node(IPhysicalOperator op) {
		// install datarate listener on sources
		if (!op.isSink() && !((ISource<?>)op).getProvidedMonitoringData().contains(MonitoringDataTypes.DATARATE.name)) {
			ISource<?> source = (ISource<?>) op;
			IMonitoringData<?> mData = MonitoringDataTypes.createMetadata(MonitoringDataTypes.DATARATE.name, source);
			source.addMonitoringData(MonitoringDataTypes.DATARATE.name, mData);
			source.subscribe((POEventListener)mData, POEventType.PushDone);
			this.optimizer.getSourceDatarates().add(mData);
		}
	}

}
