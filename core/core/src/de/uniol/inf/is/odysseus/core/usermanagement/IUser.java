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
package de.uniol.inf.is.odysseus.core.usermanagement;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public interface IUser extends IAbstractEntity, Principal, Comparable<IUser>, Serializable {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Principal#getName()
	 */
	@Override
	String getName();

	/**
	 * @return Whether the user account is active or not
	 */
	boolean isActive();

	public void setActive(boolean state);
	
	/**
	 * @return The roles of the user
	 */
	List<? extends IRole> getRoles();
	
	public void addRole(IRole role);
	public void removeRole(IRole role);

	/**
	 * @return The privileges of the user
	 */
	List<? extends IPrivilege> getPrivileges();
	public void addPrivilege(IPrivilege privilege);

	/**
	 * Validates if the given password is correct for this user.
	 * 
	 * @param password
	 * @return
	 */
	boolean validatePassword(byte[] password);
	
	public void setPassword(byte[] password);
	public void setName(String name);

}
