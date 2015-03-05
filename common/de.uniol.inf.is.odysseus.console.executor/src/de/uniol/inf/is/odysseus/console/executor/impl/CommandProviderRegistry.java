package de.uniol.inf.is.odysseus.console.executor.impl;

import java.lang.reflect.Method;
import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class CommandProviderRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(CommandProviderRegistry.class);
	
	private final Collection<CommandProvider> commandProviders = Lists.newArrayList();

	
	public void register( CommandProvider provider ) {
		Preconditions.checkNotNull(provider, "Command provider to register must not be null!");
		
		LOG.debug("Registering command provider {}", provider);
		
		if( !commandProviders.contains(provider)) {
			commandProviders.add(provider);
		} else {
			LOG.warn("Tried to register a already registered command provider {}", provider);
		}
	}
	
	public void unregister( CommandProvider provider ) {
		Preconditions.checkNotNull(provider, "Command provider to unregister must not be null!");

		LOG.debug("Unregistering command provider {}", provider);

		if( !commandProviders.remove(provider) ) {
			LOG.warn("Tried to remove an not registered command provider {}", provider);
		}
	}
	
	public ImmutableCollection<CommandProvider> getCommandProviders() {
		return ImmutableList.copyOf(commandProviders);
	}
	
	public Optional<Method> getCommandMethod(String command) {
		for (CommandProvider provider : commandProviders) {
			try {
				return Optional.of(provider.getClass().getMethod("_" + command, CommandInterpreter.class));
			} catch (NoSuchMethodException e) {
			}
		}

		return Optional.absent();
	}

	public Optional<CommandProvider> getCommandProvider(String command) {
		for (CommandProvider provider : commandProviders) {
			try {
				provider.getClass().getMethod("_" + command, CommandInterpreter.class);
				return Optional.of(provider);
			} catch (NoSuchMethodException e) {
			}
		}

		return Optional.absent();
	}	
}
