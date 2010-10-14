package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {

	private static final long serialVersionUID = 2452410871566925898L;
	private int ID = 0;
	private String rolename;
	// private List<Role> subroles;
	private List<Privilege> privileges;

	Role(String rolename, List<Privilege> privileges) {
		this.rolename = rolename;
		// auto id implementieren
		// this.ID = getNewID();
	}

	Role(String rolename) {
		this(rolename, null);
	}

	public int getID() {
		return this.ID;
	}

	String getRolename() {
		return this.rolename;
	}

	// List<Role> getSubroles() {
	// return this.subroles;
	// }

	List<Privilege> getPrivileges() {
		return this.privileges;
	}

	void setRolename(String rolename) {
		this.rolename = rolename;
	}

	// void addSubrole(Role newsubrole) {
	// this.subroles.add(newsubrole);
	// }

	void addPrivilege(Privilege newprivilege) {
		this.privileges.add(newprivilege);
	}

	// void removeSubrole(Role role) {
	// this.subroles.remove(role);
	// }

	void removePrivilege(Privilege priv) {
		this.privileges.remove(priv);
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
		if (rolename == null) {
			if (other.rolename != null)
				return false;
		} else if (!rolename.equals(other.rolename)) {
			return false;
		} else if (this.privileges.equals(other.privileges)) {
			return true;
		}

		return true;
	}

	@Override
	public String toString() {
		return this.rolename;
	}

}
