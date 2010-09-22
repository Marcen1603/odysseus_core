package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractUserManagement {

	private Map<String, User> loggedIn = new HashMap<String, User>();

	private IUserStore userStore = null;
	
	public AbstractUserManagement(IUserStore userStore) {
		this.userStore = userStore; 
	}

	/**
	 * Register new User with plain text password
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException 
	 */
	public void registerUser(User caller, String username, String password)
			throws UsernameAlreadyUsedException, UserStoreException {
		// TODO: Rechte von caller �berpr�fen
		registerUserInt(username, password);
	}

	protected void registerUserInt(String username, String password)
			throws UserStoreException, UsernameAlreadyUsedException {
		User user = userStore.getUserByName(username);
		if (user == null) {
			user = new User(username, password);
			userStore.storeUser(user);
		} else {
			throw new UsernameAlreadyUsedException();
		}
	}
	
	
	/**
	 * Register new User with plain text password
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException 
	 */
	public void updateUser(User caller, String username, String password)
			throws UsernameNotExistException, UserStoreException {
		// TODO: Rechte von caller �berpr�fen
		User user = userStore.getUserByName(username);
		if (user != null) {
			user.setPassword(password);
			userStore.storeUser(user);
		} else {
			throw new UsernameNotExistException(username);
		}
	}
	

	/**
	 * Login user with non hash password
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username, String password) {
		return login(username, password, false);
	}

	/**
	 * Get user with hash password
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User getUser(String username, String password) {
		return login(username, password, true);
	}

	private User login(String username, String password,
			boolean passwordIsHashed) {
		User user = userStore.getUserByName(username);
		if (user != null) {
			if (!user.validatePassword(password, passwordIsHashed)) {
				user = null;
			} else {
				loggedIn.put(username, user);
			}
		}
		return user;
	}
	
	public void logout(String username) {
		loggedIn.remove(username);
	}
	
	protected boolean hasNoUsers() {
		return userStore.isEmpty();
	}

}
