package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.Collection;

public interface IUserStore {

	public User getUserByName(String username);
	public void storeUser(User user) throws UserStoreException;
	public boolean isEmpty();
	public Collection<User> getUsers();

}
