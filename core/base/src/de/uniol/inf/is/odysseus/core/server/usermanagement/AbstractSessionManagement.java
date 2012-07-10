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

import de.uniol.inf.is.odysseus.core.server.usermanagement.policy.LogoutPolicy;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

abstract public class AbstractSessionManagement<USER extends IUser> implements ISessionManagement {

	protected IGenericDAO<USER, String> userDAO;

	private final SessionStore sessionStore = SessionStore.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.service.SessionmanagementService
	 * #login(java.lang.String, byte[])
	 */
	@Override
	public ISession login(final String username, final byte[] password) {
		final IUser user = userDAO.findByName(username);
		if (user != null && user.isActive() && user.validatePassword(password)) {
			return updateSessionStore(user);
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
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.service.SessionmanagementService
	 * #logout(de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
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
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.service.SessionmanagementService
	 * #isValid(de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
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
