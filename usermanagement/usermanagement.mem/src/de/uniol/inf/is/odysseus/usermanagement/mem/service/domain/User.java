package de.uniol.inf.is.odysseus.usermanagement.mem.service.domain;

import de.uniol.inf.is.odysseus.usermanagement.AbstractUser;

public class User extends AbstractUser {

	public User() {
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
