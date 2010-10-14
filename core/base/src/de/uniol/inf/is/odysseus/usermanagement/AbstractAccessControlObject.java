package de.uniol.inf.is.odysseus.usermanagement;

import java.util.List;

public class AbstractAccessControlObject {

	protected List<Privilege> privileges;

	AbstractAccessControlObject() {

	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	void addPrivilege(Privilege priv) {
		if (!this.privileges.contains(priv)) {
			this.privileges.add(priv);
		}
	}

	void removePrivilege(Privilege removepriv) {
		for (Privilege priv : this.privileges) {
			if (priv.equals(priv)) {
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
	public Privilege hasObject(Object obj) {
		for (Privilege priv : this.privileges) {
			if (priv.getObject().equals(obj)) {
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
	public Privilege hasPrivilege(Privilege haspriv) {
		for (Privilege priv : this.privileges) {
			if (priv.equals(haspriv)) {
				return priv;
			}
		}
		return null;
	}

}