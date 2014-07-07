/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.server.usermanagement.policy.LogoutPolicy;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

abstract public class AbstractSessionManagement<USER extends IUser> implements
		ISessionManagement {


	private final SessionStore sessionStore = SessionStore.getInstance();

	abstract protected IGenericDAO<USER, String> getUserDAO(ITenant tenant);

	private final List<ISessionListener> sessionListener = new CopyOnWriteArrayList<>();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * SessionmanagementService #login(java.lang.String, byte[])
	 */
	@Override
	public ISession login(final String username, final byte[] password,
			ITenant tenant) {
		return login(username, password, tenant, true);
	}
	
	private ISession login(final String username, final byte[] password,
			ITenant tenant, boolean checkLogin) {
		IGenericDAO<USER, String> userDao = getUserDAO(tenant);
		if (userDao != null) {		
			final IUser user = userDao.findByName(username);
			if (user != null && user.isActive()
					&& ((checkLogin && user.validatePassword(password))||!checkLogin)) {
				final Session session = new Session(user, tenant);
				this.sessionStore.put(session.getId(), session);
				fire(new UserLoggedInEvent(session));
				return session;
			}
		}
		return null;
	}

	@Override
	public ISession login(String token) {
		for (ISession session : this.sessionStore.values()) {
			if (session.getToken().equals(token)) {
				return session;
			}
		}
		return null;
	}
	
	@Override
	public ISession loginSuperUser(Object secret, String tenantname) {
		// TODO: check secret
		ITenant tenant = UserManagementProvider.getTenant(tenantname);
		return login("System", null, tenant, false);
	}

	@Override
	public ISession loginSuperUser(Object secret) {
		return loginSuperUser(secret, UserManagementProvider.getDefaultTenant().getName());
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * SessionmanagementService
	 * #logout(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.Session)
	 */
	@Override
	public void logout(final ISession caller) {
		final ISession session = sessionStore.get(caller.getId());
		if (LogoutPolicy.allow(session.getUser(), caller.getUser())) {
			sessionStore.remove(session.getId());
			fire(new UserLoggedOutEvent(session));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * SessionmanagementService
	 * #isValid(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.Session,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public boolean isValid(final ISession session) {
		if (session.getUser() != null) {
			final ISession realSession = this.sessionStore.get(session.getId());
			//this.sessionStore.get(caller.getId());
			if (realSession.isValid()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void subscribe(ISessionListener listener) {
		this.sessionListener.add(listener);
	}
	
	@Override
	public void unsubscribe(ISessionListener listener) {
		this.sessionListener.remove(listener);
	}
	
	protected void fire(ISessionEvent event){
		for (ISessionListener l:sessionListener){
			l.sessionEventOccured(event);
		}
	}

}
