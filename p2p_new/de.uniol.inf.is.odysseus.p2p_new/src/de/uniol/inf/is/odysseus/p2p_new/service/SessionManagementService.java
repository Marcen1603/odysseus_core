package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SessionManagementService {

	private static final Logger LOG = LoggerFactory.getLogger(SessionManagementService.class);
	private static ISessionManagement sessionManagement;
	private static ISession activeSession;
	
	public final void bindSessionManagement(ISessionManagement sm) {
		sessionManagement = sm;
		activeSession = sessionManagement.login("System", "manager".getBytes());
		
		LOG.debug("Bound Session Management {}", sm);
	}
	
	public final void unbindSessionManagement(ISessionManagement sm) {
		if( sm == sessionManagement ) {
			sessionManagement = null;
			activeSession = null;
			
			LOG.debug("Unbound Session Management");
		}
	}
	
	public static ISessionManagement getSessionManagement() {
		return sessionManagement;
	}
	
	public static ISession getActiveSession() {
		return activeSession;
	}
}
