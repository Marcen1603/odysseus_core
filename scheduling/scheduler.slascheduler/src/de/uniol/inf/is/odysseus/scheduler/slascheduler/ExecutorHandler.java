package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

public class ExecutorHandler {
	
	private static IExecutor executor;
	
	public void bindExecutor(IExecutor e) {
		executor = e;
	}
	
	public void unbindExecutor(IExecutor e) {
		executor = null;
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}

}
