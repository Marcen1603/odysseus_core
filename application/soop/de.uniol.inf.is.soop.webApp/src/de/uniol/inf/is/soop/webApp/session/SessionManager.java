/**
 * 
 */
package de.uniol.inf.is.soop.webApp.session;

import java.util.HashMap;


/**
 * @author jbrode
 *
 */
public class SessionManager {

	private static SessionManager manager = null;
	
	private HashMap<String, Session> currentSessions = new HashMap<String, Session>();

	public static SessionManager getInstance(){
		if(manager  == null ) {
			manager = new SessionManager();
		}	
		return manager;
	}
	
	private SessionManager(){}
		
	
	public Session getSessionByToken(String token) {
		return currentSessions.get(token);
	}
	
	protected void addSession(Session s){
		currentSessions.put(s.getToken(), s);
	}
	
}
