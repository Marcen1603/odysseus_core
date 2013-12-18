package de.uniol.inf.is.odysseus.test;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ExecutorProvider {

	private static IServerExecutor executor;

	public static IServerExecutor getExeuctor() {
		return executor;
	}

	public void bindExecutor(IExecutor ex) {
		executor = (IServerExecutor) ex;
	}

	public void unbindExecutor(IExecutor ex) {
		executor = null;
	}
}
