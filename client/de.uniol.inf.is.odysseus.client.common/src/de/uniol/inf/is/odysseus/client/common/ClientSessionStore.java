package de.uniol.inf.is.odysseus.client.common;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.NoSessionException;

public class ClientSessionStore {

	private static final Map<String, ISession> sessionStore = new HashMap<>();
	private static final Map<ISession, Object> sessionContext = new HashMap<>();

	public static final void addSession(String key, ISession session) {
		sessionStore.put(key, session);
	}

	public static final void removeSession(String key) {
		ISession s = sessionStore.remove(key);
		if (s != null) {
			sessionContext.remove(s);
		}
	}
	
	
	public static final ISession getFirstSession(){
		if (sessionStore.size() >= 1){
			return sessionStore.entrySet().iterator().next().getValue();
		}
		throw new NoSessionException();
	}
	
	public static final ISession getSession(String key){
		ISession session = sessionStore.get(key);
		if (session != null){
			return session;
		}
		throw new NoSessionException();
	}
	
	public static final Object putSessionContext(ISession session, Object information) {
		return sessionContext.put(session, information);
	}

	
	public static final Object getSessionContext(ISession session) {
		return sessionContext.get(session);
	}
}
