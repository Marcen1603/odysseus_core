/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public interface ISessionManagement {
	/**
	 * @param username
	 * @param password
	 * @return
	 */
	ISession login(String username, byte[] password, ITenant tenant);

	/**
	 * Creates a session for a given user.
	 * 
	 * @param username
	 *            The name of the user to login.
	 * @param tenant
	 *            The tenant.
	 * @param caller
	 *            A session object with SUDO_LOGIN rights.
	 * @return A session object for the given user or null, if the caller has no
	 *         permission to create the session.
	 */
	ISession loginAs(String username, ITenant tenant, ISession caller);

	/**
	 * @param caller
	 */
	void logout(ISession caller);

	/**
	 * @param session
	 * @param caller
	 * @return
	 */
	boolean isValid(ISession session);

	ISession login(String token);

	void subscribe(ISessionListener listener);

	void unsubscribe(ISessionListener listener);

	ISession loginSuperUser(Object secret, String tenantname);

	ISession loginSuperUser(Object secret);

	}
