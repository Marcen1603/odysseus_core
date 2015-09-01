package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropStreamCommand extends AbstractDropStreamOrViewCommand {

	private static final long serialVersionUID = -3096747328228230340L;

	public DropStreamCommand(String streamname, boolean ifExits, ISession caller) {
		super(streamname,ifExits, caller);
	}

	
	
}
