package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractUserManagementEntity implements Serializable {

	private static final long serialVersionUID = 6486357855125784276L;
	protected List<Privilege> privileges;

	AbstractUserManagementEntity() {
		this.privileges = new ArrayList<Privilege>();
	}

	public List<Privilege> getPrivileges() {
		return this.privileges;
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
			if (priv.getObject().equals(objecturi)) {
				return priv;
			}
		}
		return null;
	}

}