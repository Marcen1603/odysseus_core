package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.usermanagement.IAbstractEntity;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;

public abstract class AbstractPrivilege implements IPrivilege, Serializable {

	private static final long serialVersionUID = -4914303846915212339L;

	private String objectURI;

	final private Set<IPermission> permissions = new HashSet<IPermission>();
    protected String id = UUID.randomUUID().toString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Privilege#getPermissions()
	 */
	@Override
	public Set<IPermission> getPermissions() {
		return this.permissions;
	}

	/**
	 * @param operations
	 *            The operations to set.
	 */
	public void setPermissions(final Set<IPermission> permissions) {
		this.permissions.clear();
		this.permissions.addAll(permissions);
	}

	/**
	 * @param permission
	 */
	public void addPermission(final IPermission permission) {
		this.permissions.add(permission);
	}

	/**
	 * @param permission
	 */
	public void removePermission(final IPermission permission) {
		this.permissions.remove(permission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Privilege#getObjectURI()
	 */
	@Override
	public String getObjectURI() {
		return this.objectURI;
	}

	/**
	 * @param objectURI
	 *            The objectURI to set.
	 */
	public void setObjectURI(final String objectURI) {
		this.objectURI = objectURI;
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void update(IAbstractEntity entity) {
		if (entity instanceof AbstractPrivilege){
			AbstractPrivilege priv = (AbstractPrivilege) entity;
			this.objectURI = priv.objectURI;
			this.permissions.clear();
			this.permissions.addAll(priv.permissions);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ObjectURI: ").append(getObjectURI()).append("\n")
				.append("Permissions:\n");
		for (IPermission permission : getPermissions()) {
			sb.append("\t").append(permission.toString()).append("\n");
		}

		return sb.toString();
	}
}
