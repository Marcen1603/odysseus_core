package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

public class Role extends AbstractAccessControlObject implements Serializable {

	private static final long serialVersionUID = 2452410871566925898L;
	private final int ID;
	private String rolename;

	Role(String rolename, List<Privilege> privileges, int newid) {
		this.rolename = rolename;
		this.ID = newid;
	}

	Role(String rolename, int newid) {
		this(rolename, null, newid);
	}

	public int getID() {
		return this.ID;
	}

	String getRolename() {
		return this.rolename;
	}

	void setRolename(String rolename) {
		this.rolename = rolename;
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
		if (this.rolename == null) {
			if (other.getRolename() != null)
				return false;
		} else if (!this.rolename.equals(other.getRolename())) {
			return false;
		} else if (this.privileges.equals(other.getPrivileges())) {
			return true;
		}

		return true;
	}

	@Override
	public String toString() {
		return this.rolename;
	}

}
