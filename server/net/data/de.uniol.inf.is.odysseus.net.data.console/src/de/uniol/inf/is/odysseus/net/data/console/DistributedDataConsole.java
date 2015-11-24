package de.uniol.inf.is.odysseus.net.data.console;

import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class DistributedDataConsole implements CommandProvider {

	private static IDistributedDataManager distributedDataManager;

	// called by OSGi-DS
	public static void bindDistributedDataManager(IDistributedDataManager serv) {
		distributedDataManager = serv;
	}

	// called by OSGi-DS
	public static void unbindDistributedDataManager(IDistributedDataManager serv) {
		if (distributedDataManager == serv) {
			distributedDataManager = null;
		}
	}
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Distributed Data Commands ---\n");
		return sb.toString();
	}

}
