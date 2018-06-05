package de.uniol.inf.is.odysseus.console.executor.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ConsoleCommandExecutorPlugIn implements BundleActivator {

	private static final CommandProviderRegistry COMMAND_PROVIDER_REGISTRY = new CommandProviderRegistry(); 
	
	@Override
	public void start(BundleContext context) throws Exception {

	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

	public static CommandProviderRegistry getCommandProviderRegistry() {
		return COMMAND_PROVIDER_REGISTRY;
	}
}
