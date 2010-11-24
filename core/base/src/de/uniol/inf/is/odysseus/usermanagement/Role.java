package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;

/**
 * The role is a container for privileges. You can grant a role to user. User
 * that have a role also have the roles privileges.
 * 
 * A role can also be a group. A group can't contain privileges.
 * 
 * @author Christian van Göns
 * 
 */
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.hashCode() == obj.hashCode())
			return true;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (getRolename() == null) {
			if (other.getRolename() != null)
				return false;
		} else if (!getRolename().equals(other.getRolename())) {
			return false;
		} else if (this.privileges.equals(other.getPrivileges())) {
			return true;
		}
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
