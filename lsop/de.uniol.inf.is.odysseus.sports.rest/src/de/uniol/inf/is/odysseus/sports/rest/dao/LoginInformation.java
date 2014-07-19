package de.uniol.inf.is.odysseus.sports.rest.dao;

import java.io.Serializable;

public class LoginInformation implements Serializable {


	private static final long serialVersionUID = -4182272643327250938L;

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
