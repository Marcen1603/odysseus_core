package de.uniol.inf.is.odysseus.usermanagement.mem.service.domain;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractTenant;

public class Tenant extends AbstractTenant {

	private static final long serialVersionUID = 3158190620777593491L;

	public Tenant() {
	}
	
	@Override
	public String getId() {
		return getName();
	}

	@Override
	public Long getVersion() {
		return null;
	}

}
