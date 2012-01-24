package de.uniol.inf.is.odysseus.usermanagement.mem.service.domain;

import de.uniol.inf.is.odysseus.usermanagement.AbstractRole;

public class Role extends AbstractRole {

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
