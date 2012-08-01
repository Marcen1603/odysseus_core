/**
 * 
 */
package de.uniol.inf.is.soop.webApp.session;

import java.util.List;

/**
 * @author jbrode
 *
 */
public class User {
	
	private int id;
	
	private String username;
	
	private String realname;
	
	private String hashedPassword;
	
	private List<Usergroup> groups;

	public User(int id, String username, String realname, String hashedPassword, List<Usergroup> groups){
		this.id = id;
		this.username = username;
		this.realname = realname;
		this.hashedPassword = hashedPassword;
		this.groups = groups;
	}
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * @return the groups
	 */
	public List<Usergroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Usergroup> groups) {
		this.groups = groups;
	}


	/**
	 * @return the hashedPassword
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}


	/**
	 * @param hashedPassword the hashedPassword to set
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	
	
	

}
