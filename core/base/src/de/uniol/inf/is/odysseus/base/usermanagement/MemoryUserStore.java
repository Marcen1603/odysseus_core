package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.store.MemoryStore;

public class MemoryUserStore extends MemoryStore<String, User> implements IUserStore {
	
	@Override
	public User getUserByName(String username) {
		return getByName(username);
	}

	@Override
	public void storeUser(User user) {
		store(user);
	}
	
	Collection<User> getUsers() {
		return getAll();
	}
	
}
