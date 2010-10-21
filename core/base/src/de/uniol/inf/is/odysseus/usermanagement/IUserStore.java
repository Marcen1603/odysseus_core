package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collection;
import de.uniol.inf.is.odysseus.store.StoreException;

public interface IUserStore {

	public User getUserByName(String username);
	public User removeByName(String username) throws StoreException;
	public void storeUser(User user) throws UserStoreException;
	public boolean isEmpty();
	public Collection<User> getUsers();
	public void clear() throws StoreException;

}
