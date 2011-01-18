package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Collection;

import de.uniol.inf.is.odysseus.usermanagement.User;

class UserContentNode {
	public Collection<User> users;

	public UserContentNode(Collection<User> users) {
		this.users = users;
	}
}
