package de.uniol.inf.is.odysseus.base.usermanagement;

public interface IUserStore {

	public User getUserByName(String username);
	public void storeUser(User user) throws UserStoreException;

}
