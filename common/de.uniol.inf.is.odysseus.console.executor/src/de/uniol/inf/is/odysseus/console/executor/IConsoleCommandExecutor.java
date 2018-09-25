package de.uniol.inf.is.odysseus.console.executor;

import com.google.common.collect.ImmutableCollection;

public interface IConsoleCommandExecutor {

	public String executeConsoleCommand( String commandString ) throws ConsoleCommandNotFoundException, ConsoleCommandExecutionException;
	public String getHelpString();
	public ImmutableCollection<String> getCommands();
	
}
