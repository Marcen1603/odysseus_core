package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserStore implements IUserStore {

	private Map<String, User> registeredUsers = new HashMap<String, User>();
	
	@Override
	public User getUserByName(String username) {
		return registeredUsers.get(username);
	}

	@Override
	public void storeUser(User user) {
		registeredUsers.put(user.getUsername(), user);
	}
	
	Collection<User> getUsers() {
		return Collections.unmodifiableCollection(registeredUsers.values());
	}
	
}
