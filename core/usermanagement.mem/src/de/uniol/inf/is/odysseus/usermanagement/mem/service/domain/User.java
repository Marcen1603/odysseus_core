package de.uniol.inf.is.odysseus.usermanagement.mem.service.domain;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractUser;

public class User extends AbstractUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8512622582549600682L;

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
