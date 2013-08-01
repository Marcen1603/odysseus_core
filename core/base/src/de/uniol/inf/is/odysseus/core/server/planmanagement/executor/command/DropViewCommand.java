package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropViewCommand extends AbstractDropStreamOrViewCommand{

	public DropViewCommand(String name, boolean ifExits, ISession caller) {
		super(name,ifExits, caller);
	}
	
}
