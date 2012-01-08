/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;

/**
 * The role is a container for privileges. You can grant a role to user. User
 * that have a role also have the roles privileges.
 * 
 * A role can also be a group. A group can't contain privileges.
 * 
 * @author Christian van G�ns
 * @deprecated Replaced by {@link IRole}
 */
@Deprecated
public final class Role extends AbstractUserManagementEntity implements
		Serializable {

	private static final long serialVersionUID = 2452410871566925898L;
	private final int ID;
	private boolean isGroup;

	Role(String rolename, int newid) {
		setName(rolename);
		this.ID = newid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (ID != other.ID)
			return false;
		return true;
	}



	/**
	 * Returns the id of the role.
	 * 
	 * @return Int
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Returns the role name.
	 * 
	 * @return String
	 */
	public String getRolename() {
		return getName();
	}

	/**
	 * Sets the role name.
	 * 
	 * @param rolename
	 */
	void setRolename(String rolename) {
		setName(rolename);
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Defines the role as group.
	 * 
	 * @param isGroup
	 */
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	/**
	 * Returns true if the role is a group.
	 * 
	 * @return boolean
	 */
	public boolean isGroup() {
		return isGroup;
	}

}
