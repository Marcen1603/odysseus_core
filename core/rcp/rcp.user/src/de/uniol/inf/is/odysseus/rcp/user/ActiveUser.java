package de.uniol.inf.is.odysseus.rcp.user;

import org.eclipse.core.runtime.Assert;

import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class ActiveUser {

	private static User activeUser; 
	
	private ActiveUser() {
		
	}
	
	public static void setActiveUser( User user ) {
		Assert.isNotNull(user, "user");
		activeUser = user;
		StatusBarManager.getInstance().setMessage(StatusBarManager.USER_ID, user.getUsername());
	}
	
	public static User getActiveUser() { // TODO: Bessere Bezeichnung
		return activeUser;
	}
}
