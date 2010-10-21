package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

public class AbstractUserManagementEntity {

	protected List<Privilege> privileges;

	AbstractUserManagementEntity() {
		this.privileges = new ArrayList<Privilege>();
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public Privilege getPrivilegeByName(String name) {
		for (Privilege priv : privileges) {
			if (priv.getPrivname().equals(name)) {
				return priv;
			}
		}
		return null;
	}

	void addPrivilege(Privilege priv) {
		if (!this.privileges.contains(priv)) {
			this.privileges.add(priv);
		} else {

		}
	}

	void removePrivilege(String objecturi) {
		for (Privilege priv : this.privileges) {
			if (priv.getObject().equals(objecturi)) {
				this.privileges.remove(priv);
			}
		}
	}

	/**
	 * return the corresponding Privilege if the user has privileges on the
	 * given object
	 * 
	 * @param obj
	 * @return
	 */
	public Privilege hasObject(String objecturi) {
		for (Privilege priv : this.privileges) {
			if (priv.getObject().toString().equals(objecturi)) {
				return priv;
			}
		}
		return null;
	}

	/**
	 * return the corresponding Privilege if the user has the given privileges
	 * 
	 * @param hasrole
	 * @return
	 */
	public Privilege hasPrivilege(String objecturi) {
		for (Privilege priv : this.privileges) {
			if (priv.getObject().equals(objecturi)) {
				return priv;
			}
		}
		return null;
	}

}