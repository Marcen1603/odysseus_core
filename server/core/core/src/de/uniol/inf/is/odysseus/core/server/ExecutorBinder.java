package de.uniol.inf.is.odysseus.core.server;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ExecutorBinder {
	
	static IServerExecutor executor;

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exec) {
		if (exec == executor) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exec) {
		if (exec instanceof IServerExecutor){
			executor = (IServerExecutor) exec;
		}
	}	
	
	static public boolean isBound(){
		return executor != null;
	}
	
	static public IServerExecutor getExecutor(){
		if (isBound()){
			return executor;
		}else{
			throw new RuntimeException("No Executor bound!");
		}
	}
}
