package de.uniol.inf.is.odysseus.base.usermanagement;

public class TenantNotFoundException extends Exception {

	private static final long serialVersionUID = 2941196614123239524L;
	
	public TenantNotFoundException(String name) {
		super(name);
	}	

}
