package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.usermanagement.policy.LogoutPolicy;

abstract public class AbstractSessionManagement<USER extends IUser> implements ISessionManagement {

	protected IGenericDAO<USER,String> userDAO;
	
	private final SessionStore sessionStore = SessionStore.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
	 * #login(java.lang.String, byte[])
	 */
	@Override
	public ISession login(final String username, final byte[] password) {
		final IUser user = userDAO.findByName(username);
		if (user.isActive() && user.validatePassword(password)) {
			return updateSessionStore(user);
		}
		return null;
	}
	
	protected ISession updateSessionStore(final IUser user) {
		if (this.sessionStore.containsKey(user.getId())) {
			this.sessionStore.remove(user.getId());
		}
		final Session session = new Session(user);
		this.sessionStore.put(session.getId(), session);
		return session;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
	 * #logout(de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void logout(final ISession caller) {
		final ISession session = sessionStore.get(caller.getId());
		if (LogoutPolicy.allow(session.getUser(), caller.getUser())) {
			sessionStore.remove(session.getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
	 * #isValid(de.uniol.inf.is.odysseus.usermanagement.domain.Session,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public boolean isValid(final ISession session, final ISession caller) {
		if (session.getUser() != null) {
			final ISession realSession = this.sessionStore.get(session.getId());
			this.sessionStore.get(caller.getId());
			if (realSession.isValid()) {
				return true;
			}
		}
		return false;
	}
	
}
