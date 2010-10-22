package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.util.IGraphNodeVisitor;

/**
 * Installs {@link IPOEventListener} for datarates and selectivity on operators.
 * 
 * @author Tobias Witt
 *
 */
public class InstallMetadataListenerVisitor implements IGraphNodeVisitor<IPhysicalOperator, Object> {


	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public void nodeAction(IPhysicalOperator op) {
		// install datarate listener on sources
		if (!op.isSink() && !((ISource<?>)op).providesMonitoringData(MonitoringDataTypes.DATARATE.name)) {
			ISource<?> source = (ISource<?>) op;
//			IMonitoringData<?> mData = null;
//			mData = 
			MonitoringDataTypes.createMetadata(MonitoringDataTypes.DATARATE.name, source);
			//source.subscribe((IPOEventListener)mData, POEventType.PushDone);
			// activate periodical datarate update
			//mData = 
			source.getMonitoringData(MonitoringDataTypes.DATARATE.name, StandardOptimizer.MONITORING_PERIOD);
			//source.subscribe((IPOEventListener)mData, POEventType.PushDone);
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

	@Override
	public void afterFromSinkToSourceAction(IPhysicalOperator sink,
			IPhysicalOperator source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFromSourceToSinkAction(IPhysicalOperator source,
			IPhysicalOperator sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFromSinkToSourceAction(IPhysicalOperator sink,
			IPhysicalOperator source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFromSourceToSinkAction(IPhysicalOperator source,
			IPhysicalOperator sink) {
		// TODO Auto-generated method stub
		
	}

}
