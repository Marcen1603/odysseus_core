package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class UserLoggedOutEvent extends AbstractSessionEvent {

	public UserLoggedOutEvent(ISession session) {
		super(session);
	}

}
