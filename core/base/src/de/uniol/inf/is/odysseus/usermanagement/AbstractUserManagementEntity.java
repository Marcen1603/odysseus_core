package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractUserManagementEntity implements Serializable {

	private static final long serialVersionUID = 6486357855125784276L;
	protected List<Privilege> privileges;
	private boolean active;
	private String name;
	protected boolean systemProtection = false;

	AbstractUserManagementEntity() {
		this.privileges = new ArrayList<Privilege>();
		this.active = true;
	}

	public boolean isSystemProtected() {
		return systemProtection;
	}

	public void setSystemProtection(User caller) {
		if (AccessControl.hasPermission(UserManagementAction.SET_SYSTEM_USER,
				UserManagementAction.alias, caller)) {
			this.systemProtection = true;
		}
	}

	void addPrivilege(Privilege priv) {
		if (!this.privileges.contains(priv)) {
			this.privileges.add(priv);
		} else {

		}
	}

	protected void deaktivateUser() {
		this.active = false;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractUserManagementEntity other = (AbstractUserManagementEntity) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}

	public String getName() {
		return this.name;
	}

	public List<Privilege> getPrivileges() {
		return this.privileges;
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

	public boolean isActiv() {
		return this.active;
	}

	void removePrivilege(String objecturi) {
		for (Privilege priv : this.privileges) {
			if (priv.getObject().equals(objecturi)) {
				this.privileges.remove(priv);
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

}