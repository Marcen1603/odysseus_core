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
		return getByName(username);
	}

	@Override
	public void storeUser(User user) throws UserStoreException {
		try {
			store(user.getUsername(),user);
		} catch (StoreException e) {
			throw new UserStoreException(e);
		}
	}


}
