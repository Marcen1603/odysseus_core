package de.uniol.inf.is.odysseus.base.usermanagement;

import de.uniol.inf.is.odysseus.base.store.MemoryStore;

public class MemoryUserStore extends MemoryStore<String, User> implements IUserStore {
	
	@Override
	public User getUserByName(String username) {
		return get(username);
	}

	@Override
	public void storeUser(User user) {
		put(user.getUsername(),user);
	}

	
}
