package de.uniol.inf.is.odysseus.usermanagement.client;

import de.uniol.inf.is.odysseus.usermanagement.User;

public class ActiveUser {
	
	// TODO: Listener for User Change

	private static User activeUser; 
	
	private ActiveUser() {
	}
	
	public synchronized static void setActiveUser( User user ) {
		activeUser = user;
	}
	
	public synchronized static User getActiveUser() { 
		return activeUser;
	}
}
