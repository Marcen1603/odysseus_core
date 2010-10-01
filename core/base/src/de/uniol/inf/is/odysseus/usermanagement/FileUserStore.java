package de.uniol.inf.is.odysseus.usermanagement;

import java.io.IOException;
import java.util.Collection;

import de.uniol.inf.is.odysseus.store.FileStore;
import de.uniol.inf.is.odysseus.store.StoreException;

public class FileUserStore extends FileStore<String,User> implements IUserStore {
	
	public FileUserStore(String path) throws IOException{
		super(path);
	}
	
	@Override
	public User getUserByName(String username) {
		return get(username);
	}

	@Override
	public void storeUser(User user) throws UserStoreException {
		try {
			put(user.getUsername(),user);
		} catch (StoreException e) {
			throw new UserStoreException(e);
		}
	}

	@Override
	public Collection<User> getUsers() {
		return values();
	}

	

}
