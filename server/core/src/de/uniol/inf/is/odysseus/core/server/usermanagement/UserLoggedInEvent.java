package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class UserLoggedInEvent extends AbstractSessionEvent {

	public UserLoggedInEvent(ISession session) {
		super(session);
	}

}
