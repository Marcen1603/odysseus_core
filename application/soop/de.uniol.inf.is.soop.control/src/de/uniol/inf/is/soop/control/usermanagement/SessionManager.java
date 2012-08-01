/**
 * 
 */
package de.uniol.inf.is.soop.control.usermanagement;

//import java.security.MessageDigest;
import java.util.HashMap;

import de.uniol.inf.is.soop.control.dsms.DatastreamManagementSystemManager;
import de.uniol.inf.is.soop.control.sensorregistry.SensorRegistryManager;
import de.uniol.inf.is.soop.control.workflow.WorkflowEngineManager;

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
	
	
public Session createSession(String username, String password) throws Exception{
		
		Session session = null;
		
	
		
		UserManager um = UserManager.getInstance();
		
		User u = um.getUserByName(username);
		
		/* build hash to compare */
		//byte[] bytesOfMessage = (password + UserManager.getPasswordSalt()).getBytes("UTF-8");
		//String inputHash =  MessageDigest.getInstance("MD5").digest(bytesOfMessage).toString();

		if (u instanceof User /* TODO FIX encoding probs && u.getHashedPassword().equals(inputHash)*/){
			
			session = new Session(u);
			
			session.setWfEngine(WorkflowEngineManager.getInstance().getWorkflowEngine("localODE"));
			session.setDsms(DatastreamManagementSystemManager.getInstance().getDatastreamManagementSystemInstance("localOdysseus"));
			session.setSensorRegistry(SensorRegistryManager.getInstance().getSensorRegistryInstance("localSensorRegistry"));
			
			addSession(session); 
			
		} else {
			throw new Exception("Invalid credentials");
		}
		
		return session;
	}
	
	
	public Session getSessionByToken(String token) {
		return currentSessions.get(token);
	}
	
	protected void addSession(Session s){
		currentSessions.put(s.getToken(), s);
	}
	
}
