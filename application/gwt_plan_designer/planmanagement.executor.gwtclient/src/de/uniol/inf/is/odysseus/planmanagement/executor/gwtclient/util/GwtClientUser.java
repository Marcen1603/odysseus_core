package de.uniol.inf.is.odysseus.planmanagement.executor.gwtclient.util;

import java.io.Serializable;


/**
 * 
 * @author Merlin Wasmann
 *
 */

public class GwtClientUser implements Serializable {
	private static final long serialVersionUID = 3910564805403061255L;
	private String name;
	private byte[] password;
	private boolean active;

	public GwtClientUser(String name, byte[] password, boolean active) {
		this.name = name;
		this.password = password;
		this.active = active;
	}
	
	
	public String getId() {
		return getName();
	}


	
	public int compareTo(GwtClientUser o) {
		return this.getName().compareTo(o.getName());
	}

	
	public String getName() {
		return this.name;
	}

	
	public boolean isActive() {
		return this.active;
	}

	
	public void setActive(boolean state) {
		this.active = state;
	}

	
	public boolean validatePassword(byte[] password) {
		if (this.password.length != 0 && password.length != 0) {
			return this.password.equals(password);
		}
		return false;
	}

	
	public void setPassword(byte[] password) {
		this.password = password;
	}

	
	public void setName(String name) {
		this.name = name;
	}

}
