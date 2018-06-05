package de.uniol.inf.is.odysseus.core.usermanagement;


public class Tenant extends AbstractTenant {

	private static final long serialVersionUID = 3158190620777593491L;

	
	@Override
	public String getId() {
		return getName();
	}

	@Override
	public Long getVersion() {
		return null;
	}
	

}
