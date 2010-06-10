package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Installs {@link IPOEventListener} for datarates and selectivity on operators.
 * 
 * @author Tobias Witt
 *
 */
public class InstallMetadataListenerVisitor implements INodeVisitor<IPhysicalOperator, Object> {

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
		if (!op.isSink() && !((ISource<?>)op).providesMonitoringData(MonitoringDataTypes.DATARATE.name)) {
			ISource<?> source = (ISource<?>) op;
			IMonitoringData<?> mData = MonitoringDataTypes.createMetadata(MonitoringDataTypes.DATARATE.name, source);
			source.subscribe((IPOEventListener)mData, POEventType.PushDone);
			// activate periodical datarate update
			mData = source.getMonitoringData(MonitoringDataTypes.DATARATE.name, StandardOptimizer.MONITORING_PERIOD);
			source.subscribe((IPOEventListener)mData, POEventType.PushDone);
		}
		// install selectivity listener on every pipe operator
		if (op.isPipe() && !((IPipe<?,?>)op).providesMonitoringData(MonitoringDataTypes.SELECTIVITY.name)) {
			try{
				MonitoringDataTypes.createMetadata(MonitoringDataTypes.SELECTIVITY.name, (IPipe<?, ?>) op);
			}catch(Exception e){
				e.printStackTrace();
			}
			// subscribe is done in constructor
		}
		/* TODO: latency messen
		 * es gibt keinen operator ausser ObjectTrackingMetadataFactory, der latency setzt
		 * LatencyCalculationPipe setzt Ende
		 * Benchmark benutzt latency
		 */
	}

}
