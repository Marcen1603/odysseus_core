package de.uniol.inf.is.odysseus.server.opcua.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public class ExecutorServiceBinding {

	private final Logger log = LoggerFactory.getLogger(ExecutorServiceBinding.class);

	private static IExecutor executor;

	public void bindExecutor(IExecutor exec) {
		log.info("Got executor ({})...", exec.getClass().getSimpleName());
		executor = exec;
	}

	public void unbindExecutor(IExecutor exec) {
		log.info("Lost executor ({})...", exec.getClass().getSimpleName());
		executor = null;
	}

	public static IExecutor getExecutor() {
		return executor;
	}
}