package de.uniol.inf.is.odysseus.core.command;

public interface ICommandProvider
{
	Command getCommandByName(String commandName);
}
