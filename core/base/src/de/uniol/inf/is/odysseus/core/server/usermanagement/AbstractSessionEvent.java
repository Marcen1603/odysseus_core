package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class AbstractSessionEvent implements ISessionEvent {
	ISession session;
	
	public AbstractSessionEvent(ISession session) {
		this.session = session;
	}
	
	public ISession getSession() {
		return session;
	}

}
