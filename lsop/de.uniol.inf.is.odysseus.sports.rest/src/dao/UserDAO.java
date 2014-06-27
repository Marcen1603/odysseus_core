package dao;

import java.io.Serializable;

/**
 * Simple user model for testing REST
 * @author Thomas
 *
 */
public class UserDAO implements Serializable{

	private static final long serialVersionUID = 3877983907166603599L;
	private String username;
	
	public UserDAO() {
		//default
		this.username = "";
	}
	
	public UserDAO(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
