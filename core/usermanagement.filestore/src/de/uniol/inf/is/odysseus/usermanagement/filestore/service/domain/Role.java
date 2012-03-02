package de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractRole;

public class Role extends AbstractRole {

	private static final long serialVersionUID = -6431417144323771668L;

	public Role() {
		super();
	}

	@Override
	public String getId() {
		return getName();
	}

	@Override
	public Long getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

}
