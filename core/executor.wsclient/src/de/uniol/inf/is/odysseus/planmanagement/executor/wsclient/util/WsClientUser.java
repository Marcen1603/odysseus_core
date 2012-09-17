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
package de.uniol.inf.is.odysseus.planmanagement.executor.wsclient.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.IAbstractEntity;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class WsClientUser implements IUser {

	private static final long serialVersionUID = 3910564805403061255L;
	private String name;
	private byte[] password;
	private boolean active;

	public WsClientUser(String name, byte[] password, boolean active) {
		this.name = name;
		this.password = password;
		this.active = active;
	}
	
	@Override
	public String getId() {
		return getName();
	}


	@Override
	public int compareTo(IUser o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public void setActive(boolean state) {
		this.active = state;
	}

	@Override
	public boolean validatePassword(byte[] password) {
		if (this.password.length != 0 && password.length != 0) {
			return this.password.equals(password);
		}
		return false;
	}

	@Override
	public void setPassword(byte[] password) {
		this.password = password;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/****************************************************************
	 *                 not needed here                              *
	 ***************************************************************/

	@Override
	public Long getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(IAbstractEntity entity) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public List<? extends IRole> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRole(IRole role) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addRole(IRole role) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRole(IRole role) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends IPrivilege> getPrivileges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPrivilege(IPrivilege privilege) {
		// TODO Auto-generated method stub

	}

}
