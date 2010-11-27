package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * 
 * @see de.uniol.inf.is.odysseus.usermanagement.User.java
 * @see de.uniol.inf.is.odysseus.usermanagement.Role.java
 * @author Christian van Göns
 * 
 */
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

	/**
	 * Returns whether the entity (user or role) has system status. With system
	 * status a entity is revoke save.
	 * 
	 * @return boolean
	 */
	public boolean isSystemProtected() {
		return systemProtection;
	}

	/**
	 * Sets system status to the entity (user or role).
	 * 
	 * @param caller
	 */
	public void setSystemProtection(User caller) {
		if (AccessControl.hasPermission(UserManagementAction.SET_SYSTEM_USER,
				UserManagementAction.alias, caller)) {
			this.systemProtection = true;
		}
	}

	/**
	 * Adds a privilege to the entity (user or role).
	 * 
	 * @param privilege
	 */
	void addPrivilege(Privilege priv) {
		if (!this.privileges.contains(priv)) {
			this.privileges.add(priv);
		} else {

		}
	}

	/**
	 * Disables an entity.
	 */
	protected void deaktivate() {
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

	/**
	 * Returns the name of the entity (user or role).
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns a list of all privileges of a entity.
	 * 
	 * @return List<Privilege>
	 */
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

	/**
	 * Returns whether the entity is active or disabled.
	 * 
	 * @see deaktivate()
	 * @return boolean
	 */
	public boolean isActiv() {
		return this.active;
	}

	/**
	 * Removes a privilege from a given entity specified by uri.
	 * 
	 * @param objecturi
	 */
	void removePrivilege(String objecturi) {
		for (Privilege priv : this.privileges) {
			if (priv.getObject().equals(objecturi)) {
				this.privileges.remove(priv);
			}
		}
	}

	/**
	 * Sets a new entity name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}