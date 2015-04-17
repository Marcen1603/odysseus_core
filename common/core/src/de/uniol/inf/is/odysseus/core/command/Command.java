package de.uniol.inf.is.odysseus.core.command;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public abstract class Command 
{
	public abstract boolean run(IStreamObject<?> input);
}
