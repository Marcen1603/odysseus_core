package de.uniol.inf.is.odysseus.console.executor.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.console.executor.ConsoleCommandExecutionException;
import de.uniol.inf.is.odysseus.console.executor.ConsoleCommandNotFoundException;
import de.uniol.inf.is.odysseus.console.executor.IConsoleCommandExecutor;

public class ConsoleCommandExecutor implements IConsoleCommandExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(ConsoleCommandExecutor.class);

	@Override
	public String executeConsoleCommand(String commandString) throws ConsoleCommandNotFoundException, ConsoleCommandExecutionException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(commandString), "Commandstring to execute must be non-empty or null!");

		LOG.debug("Starting to execute '{}'", commandString);

		String[] splitted = commandString.split("\\ ", 2);
		String command = splitted[0];
		String parameters = splitted.length > 1 ? splitted[1] : null;

		LOG.debug("Command: {}", command);
		LOG.debug("Parameters: {}", parameters);

		if (command.equals("help")) {
			return getHelpString();
		}

		StringBuilderCommandInterpreter commandInterpreter = new StringBuilderCommandInterpreter(parameters != null ? parameters.split("\\ ") : new String[0]);
		CommandProviderRegistry registry = ConsoleCommandExecutorPlugIn.getCommandProviderRegistry();

		Optional<Method> optMethod = registry.getCommandMethod(command);
		Optional<CommandProvider> optProvider = registry.getCommandProvider(command);

		if (!optMethod.isPresent() || !optProvider.isPresent()) {
			throw new ConsoleCommandNotFoundException(command);
		}
		
		LOG.debug("Method to execute: {}", optMethod.get());
		LOG.debug("CommandProvider to call: {}", optMethod.get());
		
		try {
			optMethod.get().invoke(optProvider.get(), commandInterpreter);
			// delegateCi contains output of command now
			return commandInterpreter.getText();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ConsoleCommandExecutionException(command, e);
		}
	}

	@Override
	public String getHelpString() {
		LOG.debug("Printing help");

		StringBuilderCommandInterpreter commandInterpreter = new StringBuilderCommandInterpreter(new String[0]);

		for (CommandProvider provider : ConsoleCommandExecutorPlugIn.getCommandProviderRegistry().getCommandProviders()) {
			commandInterpreter.println(provider.getHelp());
		}

		return commandInterpreter.getText();
	}
	
	@Override
	public ImmutableCollection<String> getCommands() {
		Collection<String> commandList = Lists.newArrayList();
		
		for (CommandProvider provider : ConsoleCommandExecutorPlugIn.getCommandProviderRegistry().getCommandProviders()) {
			
			for( Method method : provider.getClass().getMethods() ) {
				String methodName = method.getName();
				if(methodName.startsWith("_")) {
					commandList.add(methodName.substring(1));
				}
			}
		}	
		
		return ImmutableList.copyOf(commandList);
	}
}
