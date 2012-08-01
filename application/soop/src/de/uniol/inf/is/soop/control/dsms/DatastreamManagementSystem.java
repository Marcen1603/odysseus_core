package de.uniol.inf.is.soop.control.dsms;

public class DatastreamManagementSystem {
	private String id;
	
	private String webserviceUrl;
	
	private String username;
	
	private String password;

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
	 * @return the webserviceUrl
	 */
	public String getWebserviceUrl() {
		return webserviceUrl;
	}

	/**
	 * @param webserviceUrl the webserviceUrl to set
	 */
	public void setWebserviceUrl(String webserviceUrl) {
		this.webserviceUrl = webserviceUrl;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
