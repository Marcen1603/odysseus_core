package de.uniol.inf.is.odysseus.rcp.editor.script;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

public class ServicesBinder {

	private static IOdysseusScriptParser scriptParser;
	private static IServerExecutor serverExecutor;

	// called by OSGi-DS
	public static void bindOdysseusScriptParser(IOdysseusScriptParser serv) {
		scriptParser = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusScriptParser(IOdysseusScriptParser serv) {
		if (scriptParser == serv) {
			scriptParser = null;
		}
	}
	
	public static IOdysseusScriptParser getOdysseusScriptParser() {
		return scriptParser;
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}
	
	public static IServerExecutor getServerExecutor() {
		return serverExecutor;
	}
}
