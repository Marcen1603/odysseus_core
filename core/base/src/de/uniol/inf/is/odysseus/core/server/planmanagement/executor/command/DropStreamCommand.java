package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropStreamCommand extends AbstractDropStreamOrViewCommand {

	public DropStreamCommand(String streamname, boolean ifExits, ISession caller) {
		super(streamname,ifExits, caller);
	}

	
	
}
