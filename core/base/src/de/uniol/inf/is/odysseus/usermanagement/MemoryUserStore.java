package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.store.MemoryStore;

public class MemoryUserStore extends MemoryStore<String, User> implements
		IUserStore {

	@Override
	public User getUserByName(String username) {
		return get(username);
	}

	@Override
	public void storeUser(User user) {
		put(user.getUsername(), user);
	}

	@Override
	public Collection<User> getUsers() {
		return values();
	}

	@Override
	public User removeByName(String username) {
		return remove(username);
	}

}
