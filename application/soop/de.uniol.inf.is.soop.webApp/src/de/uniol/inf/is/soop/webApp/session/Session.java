/**
 * 
 */
package de.uniol.inf.is.soop.webApp.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jbrode
 *
 */
public class Session {

	String id;
	
	String token;
	
	User user;
	
	private Session() {}
	
	public Session(User u, String token){
		user = u;
		this.token = token;
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

	
	
	
}
