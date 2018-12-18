package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server;

import de.uniol.inf.is.odysseus.security.provider.ISecurityProvider;

public class SecurityProviderServiceBinding {

	private static ISecurityProvider provider;
	
	public static ISecurityProvider getSecurityProvider() {
		return provider;
	}
	
	public void bindSecurityProvider(ISecurityProvider secProvider) {
		provider = secProvider;
	}
	
	public void unbindSecurityProvider(ISecurityProvider secProvider) {
		provider = null;
	}

}
