package de.uniol.inf.is.odysseus.usermanagement.mem.service.domain;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractRole;

public class Role extends AbstractRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6993076819523998478L;

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
