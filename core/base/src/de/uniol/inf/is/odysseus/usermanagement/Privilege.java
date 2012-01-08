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
import java.util.List;

/**
 * Privileges stores actions (permissions). A Privilege is owned by one user or
 * role and identified by the object name.
 * 
 * @see de.uniol.inf.is.odysseus.usermanagement.IUserAction.java
 * @author Christian van G�ns
 * @deprecated Replaced by {@link IPrivilege}
 */
@Deprecated
public final class Privilege implements Serializable {

	private static final long serialVersionUID = -1623632077911032763L;
	private final int ID;
	private String privname;
	private final AbstractUserManagementEntity owner;
	private String objecturi;
	private List<IUserAction> operations;

	Privilege(String objecturi, AbstractUserManagementEntity owner,
			List<IUserAction> operation, int privid) {
		this.ID = privid;
		this.objecturi = objecturi;
		this.operations = operation;
		this.owner = owner;
		this.privname = owner + "::" + objecturi;
	}

	/**
	 * Adds a new action (permissions) to a privilege.
	 * 
	 * @param newOperation
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantPermission(IUser, IPermission, String, ISession)}
	 */
	@Deprecated
	void addOperation(IUserAction newOperation) {
		this.operations.add(newOperation);
	}

	/**
	 * Adds a list of new actions (permissions) to a privilege.
	 * 
	 * @param operations
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantPermissions(IUser, java.util.Set, String, ISession)}
	 */
	@Deprecated
	public void addOperations(List<IUserAction> operations) {
		for (IUserAction action : operations) {
			if (!this.operations.contains(action)) {
				addOperation(action);
			}
		}
	}

	/**
	 * Returns a privileges id.
	 * 
	 * @return int
	 */
	public int getID() {
		return this.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objecturi == null) ? 0 : objecturi.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Privilege other = (Privilege) obj;
		if (objecturi == null) {
			if (other.objecturi != null)
				return false;
		} else if (!objecturi.equals(other.objecturi))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	/**
	 * return the objects uri (name).
	 * 
	 * @return String
	 */
	public String getObject() {
		return this.objecturi;
	}

	/**
	 * Returns a list of all actions (permissions) contained in a privilege.
	 * 
	 * @return List<IUserAction>
	 */
	public List<IUserAction> getOperations() {
		return this.operations;
	}

	/**
	 * Returns the owner of the privilege. The owner can be a user or role. To
	 * check if the owner is a user call the method <b>ownerIsUser</b>.
	 * 
	 * @return String
	 */
	public String getOwner() {
		return this.owner.toString();
	}

	/**
	 * Returns the name of the privilege.
	 * 
	 * @return String
	 */
	public String getPrivname() {
		return this.privname;
	}

	/**
	 * Returns whether the privilege has actions or not.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		if (this.operations.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether the owner is a instance of a user.
	 * 
	 * @return boolean
	 */
	public boolean ownerIsUser() {
		if (this.owner instanceof User) {
			return true;
		}
		return false;
	}

	/**
	 * Removes a action (permissions) from a privilege.
	 * 
	 * @param operation
	 * @deprecated Replaced by
	 *             {@link IUserManagement#revokePermission(IUser, IPermission, String, ISession)}
	 */
	@Deprecated
	public void removeOperation(IUserAction operation) {
		this.operations.remove(operation);
	}

	/**
	 * Removes a list of actions (permissions) from a privilege.
	 * 
	 * @param operations
	 * @deprecated Replaced by
	 *             {@link IUserManagement#revokePermissions(IUser, java.util.Set, String, ISession)}
	 */
	@Deprecated
	public void removeOperations(List<IUserAction> operations) {
		this.operations.removeAll(operations);
	}

	/**
	 * Sets a new object uri the action actions (permissions) in a privilege are
	 * for.
	 * 
	 * @param object
	 */
	void setObject(String newobjecturi) {
		this.objecturi = newobjecturi;
	}

	/**
	 * Sets a new privilege name.
	 * 
	 * @param newprivname
	 */
	void setPrivname(String newprivname) {
		this.privname = newprivname;
	}

	@Override
	public String toString() {
		return this.privname;
	}

}