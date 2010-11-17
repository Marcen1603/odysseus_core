package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;

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
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (getName() == null) {
			if (other.getRolename() != null)
				return false;
		} else if (!getName().equals(other.getRolename())) {
			return false;
		} else if (this.privileges.equals(other.getPrivileges())) {
			return true;
		}

		return true;
	}

	public int getID() {
		return this.ID;
	}

	public String getRolename() {
		return getName();
	}

	void setRolename(String rolename) {
		setName(rolename);
	}

	@Override
	public String toString() {
		return getName();
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup; 
	}
	
	public boolean isGroup() {
		return isGroup;
	}

}
