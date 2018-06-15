package de.uniol.inf.is.odysseus.server.moitoringCPU.query;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.server.moitoringCPU.CPUUsage.SystemUsage;

public class MonitoringCPU implements IUpdateEventListener {

	private static MonitoringCPU instance;
	@SuppressWarnings("unused")
	private static SystemUsage thread = new SystemUsage();
	private IServerExecutor executor;

	public MonitoringCPU(){
		instance = this;
	}
	
	public MonitoringCPU getInstance(){
		return instance;
	}
	public void bindExecutor(IExecutor ex) throws PlanManagementException {
		executor = (IServerExecutor) ex;
		String name = executor.getName();
		if (name == null) {
			name = "";
		}
		executor.addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
	}

	public void unbindExecutor(IExecutor ex) {
		if (executor == (IServerExecutor) ex) {
			executor.removeUpdateEventListener(this, IUpdateEventListener.QUERY, null);
			executor = null;
		}
	}
	@Override
	public void eventOccured(String type) {
		startMonitoring();
	}
	//TODO: Monitor CPU-Usage per Thread
	private void startMonitoring(){
//		if (!thread.isAlive())
//		thread.start();
	}
}
