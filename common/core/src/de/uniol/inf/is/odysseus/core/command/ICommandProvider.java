package de.uniol.inf.is.odysseus.core.command;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ICommandProvider
{
	Command getCommandByName(String commandName, SDFSchema schema);
}
