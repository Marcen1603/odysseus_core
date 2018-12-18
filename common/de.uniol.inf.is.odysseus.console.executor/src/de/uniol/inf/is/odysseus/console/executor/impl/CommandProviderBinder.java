package de.uniol.inf.is.odysseus.console.executor.impl;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandProviderBinder {

	private static final Logger LOG = LoggerFactory.getLogger(CommandProviderBinder.class);
	
	public void bindCommandProvider( CommandProvider provider ) {
		LOG.debug("Binding command provider {}", provider);
		
		ConsoleCommandExecutorPlugIn.getCommandProviderRegistry().register(provider);
	}
	
	public void unbindCommandProvider( CommandProvider provider ) {
		LOG.debug("Unbinding command provider {}", provider);
		
		ConsoleCommandExecutorPlugIn.getCommandProviderRegistry().unregister(provider);
	}
}
