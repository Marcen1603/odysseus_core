package de.uniol.inf.is.odysseus.usermanagement.client;

import de.uniol.inf.is.odysseus.usermanagement.User;

public interface IActiveUserListener {
	public void activeUserChanged(User user);
}
