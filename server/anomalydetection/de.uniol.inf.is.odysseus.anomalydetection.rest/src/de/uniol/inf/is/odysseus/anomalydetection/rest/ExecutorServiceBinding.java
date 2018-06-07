package de.uniol.inf.is.odysseus.anomalydetection.rest;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ExecutorServiceBinding {
	private static IServerExecutor executor = null;

	public static IServerExecutor getExecutor() {
		return executor;
	}

	public void bindExecutor(IExecutor ex) {
		executor = (IServerExecutor) ex;
	}

	public void unbindExecutor(IExecutor ex) {
		executor = null;
	}
}
