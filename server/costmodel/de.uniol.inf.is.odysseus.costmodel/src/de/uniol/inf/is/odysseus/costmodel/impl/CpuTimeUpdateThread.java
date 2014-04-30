package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public abstract class CpuTimeUpdateThread extends Thread {
	
	private final IServerExecutor executor;
	
	private boolean isRunning = true;
	
	public CpuTimeUpdateThread( IServerExecutor executor ) {
		Preconditions.checkNotNull(executor, "ServerExecutor must not be null!");
		
		this.executor = executor;
		
		setDaemon(true);
		setName("CpuTimes updater");
	}
	
	@Override
	public void run() {
		while( isRunning ) {
			Collection<IPhysicalQuery> physicalQueries = Lists.newArrayList(executor.getExecutionPlan().getQueries());
			for( IPhysicalQuery physicalQuery : physicalQueries ) {
				for( IPhysicalOperator physicalOperator : physicalQuery.getPhysicalChilds() ) {
					IMonitoringData<Double> cpuTime = physicalOperator.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
					if( cpuTime != null ) {
						updateCpuTime( physicalOperator.getClass(), cpuTime.getValue() );
					}
				}
			}
			
			try {
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	protected abstract void updateCpuTime( Class<? extends IPhysicalOperator> operator, double cpuTime );
	
	public void stopRunning() {
		isRunning = false;
	}
}