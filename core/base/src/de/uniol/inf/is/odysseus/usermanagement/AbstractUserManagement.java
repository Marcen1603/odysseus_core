package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.store.StoreException;

abstract public class AbstractUserManagement {

	private Map<String, User> loggedIn = new HashMap<String, User>();

	private IUserStore userStore = null;

	private int sessionID = -1;

	public AbstractUserManagement(IUserStore userStore) {
		this.userStore = userStore;
	}

	/**
	 * Register new User with plain text password
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 */
	public void registerUser(User caller, String username, String password)
			throws UsernameAlreadyUsedException, UserStoreException {
		// TODO: Rechte von caller überprüfen
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
		fireUserManagementListener();
	}

	/**
	 * Update Users password with plain text password
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 */
	public void updateUserPassword(User caller, String username, String password)
			throws UsernameNotExistException, UserStoreException {
		// TODO: Rechte von caller überprüfen
		User user = userStore.getUserByName(username);
		if (user != null) {
			user.setPassword(password);
			userStore.storeUser(user);
		} else {
			throw new UsernameNotExistException(username);
		}
		fireUserManagementListener();
	}

	/**
	 * change User to Admin
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 */
	public void updateUserAdmin(User caller, String username)
			throws UsernameNotExistException, UserStoreException,
			HasNoPermissionException {
		if (caller.isAdmin()) {
			User storeuser = userStore.getUserByName(username);
			if (storeuser != null) {
				storeuser.grantAdmin();
				userStore.storeUser(storeuser);
			} else {
				throw new UsernameNotExistException(username);
			}
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException(caller.getUsername());
		}
	}

	/**
	 * change admin to user
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 */
	public void updateAdminUser(User caller, String username)
			throws UsernameNotExistException, UserStoreException,
			HasNoPermissionException {
		if (caller.isAdmin()) {
			User storeuser = userStore.getUserByName(username);
			if (storeuser != null) {
				storeuser.revokeAdmin();
				userStore.storeUser(storeuser);
			} else {
				throw new UsernameNotExistException(username);
			}
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException(caller.getUsername());
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
				user.setSession(new Session(getSessionId()));
			}
		}
		fireUserManagementListener();
		return user;
	}

	private synchronized int getSessionId() {
		return ++sessionID;
	}

	public void logout(String username) {
		User user = loggedIn.remove(username);
		user.setSession(null);
		fireUserManagementListener();
	}

	protected boolean hasNoUsers() {
		return userStore.isEmpty();
	}

	public void clearUserStore() throws StoreException {
		userStore.clear();
	}

	public User findUser(String username, User caller) {
		// Todo: Testen ob caller das darf
		return userStore.getUserByName(username);
	}

	public Collection<User> getUsers() {
		return userStore.getUsers();
	}

	private List<IUserManagementListener> listeners = new CopyOnWriteArrayList<IUserManagementListener>();

	public void addTenantManagementListener(IUserManagementListener l) {
		listeners.add(l);
	}

	public void removeTenantManagementListener(IUserManagementListener l) {
		listeners.remove(l);
	}

	public void fireUserManagementListener() {
		for (IUserManagementListener l : listeners) {
			l.usersChangedEvent();
		}
	}

	public boolean isLoggedIn(String username) {
		return loggedIn.containsKey(username);
	}

}
