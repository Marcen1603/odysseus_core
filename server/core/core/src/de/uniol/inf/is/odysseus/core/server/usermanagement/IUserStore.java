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

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * 
 * @deprecated Replaced by {@link ISessionManagement}
 * 
 */
@Deprecated
public interface IUserStore {

	/**
	 * 
	 * @deprecated Replaced by
	 *             {@link ISessionManagement#findUser(String, ISession)}
	 */
	@Deprecated
	public ISession getUserByName(String username);

	/**
	 * 
	 * @deprecated Replaced by
	 *             {@link ISessionManagement#deleteUser(ISession, ISession)}
	 */
	@Deprecated
	public ISession removeByName(String username) throws StoreException;

	/**
	 * 
	 * @deprecated Replaced by
	 *             {@link ISessionManagement#createUser(String, ISession)}
	 */
	@Deprecated
	public void storeUser(ISession user) throws UserStoreException;

	public boolean isEmpty();

	/**
	 * 
	 * @deprecated Replaced by {@link ISessionManagement#getUsers(ISession)}
	 */
	@Deprecated
	public Collection<ISession> getUsers();

	public void clear() throws StoreException;

}
