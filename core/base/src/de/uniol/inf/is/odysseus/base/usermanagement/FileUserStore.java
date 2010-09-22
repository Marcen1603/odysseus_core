package de.uniol.inf.is.odysseus.base.usermanagement;

import java.io.IOException;

import de.uniol.inf.is.odysseus.base.store.FileStore;
import de.uniol.inf.is.odysseus.base.store.StoreException;

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


}
