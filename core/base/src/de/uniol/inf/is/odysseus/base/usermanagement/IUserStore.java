package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.store.StoreException;

public interface IUserStore {

	public User getUserByName(String username);
	public void storeUser(User user) throws UserStoreException;
	public boolean isEmpty();
	public Collection<User> getUsers();
	public void clear() throws StoreException;

}
