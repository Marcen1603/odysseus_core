package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropViewCommand extends AbstractDropStreamOrViewCommand{

	private static final long serialVersionUID = -6185259377588010814L;

	public DropViewCommand(String name, boolean ifExits, ISession caller) {
		super(name,ifExits, caller);
	}
	
}
