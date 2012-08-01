/**
 * 
 */
package de.uniol.inf.is.soop.control.usermanagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.uniol.inf.is.soop.control.dsms.DatastreamManagementSystem;
import de.uniol.inf.is.soop.control.sensorregistry.SensorRegistry;
import de.uniol.inf.is.soop.control.workflow.WorkflowEngine;

/**
 * @author jbrode
 *
 */
public class Session {

	String id;
	
	String token;
	
	User user;
	
	WorkflowEngine wfEngine;
	
	DatastreamManagementSystem dsms;
	
	SensorRegistry sensorRegistry;
	
	private Session() {}
	
	public Session(User u){
		user = u;
		token = generateToken(u);
	}
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the wfEngine
	 */
	public WorkflowEngine getWfEngine() {
		return wfEngine;
	}

	/**
	 * @param wfEngine the wfEngine to set
	 */
	public void setWfEngine(WorkflowEngine wfEngine) {
		this.wfEngine = wfEngine;
	}

	/**
	 * @return the dsms
	 */
	public DatastreamManagementSystem getDsms() {
		return dsms;
	}

	/**
	 * @param dsms the dsms to set
	 */
	public void setDsms(DatastreamManagementSystem dsms) {
		this.dsms = dsms;
	}
	
	/**
	 * @return the sensorRegistry
	 */
	public SensorRegistry getSensorRegistry() {
		return sensorRegistry;
	}

	/**
	 * @param sensorRegistry the sensorRegistry to set
	 */
	public void setSensorRegistry(SensorRegistry sensorRegistry) {
		this.sensorRegistry = sensorRegistry;
	}

	private String generateToken(User u){
		String token = null;
		
		try {
			token = MessageDigest.getInstance("MD5").digest(
					((System.currentTimeMillis() * Math.random()) + u.getUsername()).getBytes()).toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}
	
}
