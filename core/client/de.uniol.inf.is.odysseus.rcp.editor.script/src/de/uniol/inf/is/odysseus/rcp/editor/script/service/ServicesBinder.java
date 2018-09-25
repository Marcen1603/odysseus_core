package de.uniol.inf.is.odysseus.rcp.editor.script.service;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public class ServicesBinder {

	private static IExecutor serverExecutor;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}
	
	public static IExecutor getExecutor() {
		return serverExecutor;
	}
}
