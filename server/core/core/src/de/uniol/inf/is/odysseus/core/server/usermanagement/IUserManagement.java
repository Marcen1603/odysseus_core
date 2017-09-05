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

import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IUserManagement {

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	IRole findRole(String name, ISession caller);

	/**
	 * Test if a user has a role
	 * 
	 * @param name
	 * @param role
	 * @return
	 */
	boolean hasRole(String name, String role, ISession caller);

	/**
	 * @param roleId
	 * @param caller
	 * @return
	 */
	IRole getRole(String roleId, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	List<? extends IRole> getRoles(ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	IUser findUser(String name, ISession caller);

	/**
	 * @param userId
	 * @param caller
	 * @return
	 */
	IUser getUser(String userId, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	List<? extends IUser> getUsers(ISession caller);

	/**
	 * @param caller
	 * @param permission
	 * @param objectURI
	 * @return
	 */
	boolean hasPermission(ISession caller, IPermission permission, String objectURI);

	void addUserManagementListener(IUserManagementListener tenantView);

	String getType();

	boolean isInitialized(ITenant tenant);

	void initialize(ITenant tenant);

	IUser findUser(ITenant tenant, String username);

}
