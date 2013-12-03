package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractExecutorCommand implements IExecutorCommand {

	final private ISession caller;
	final private Collection<Integer> empty = new ArrayList<>();

	public AbstractExecutorCommand(ISession caller) {
		this.caller = caller;
	}

	@Override
	public ISession getCaller() {
		return caller;
	}
	
	public Collection<Integer> getEmptyCollection() {
		return empty;
	}
		
}
