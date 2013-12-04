package de.uniol.inf.is.odysseus.opcua;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.security.provider.ISecurityProvider;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}


	private static ISecurityProvider securityProvider;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	
	public void bindSecurityProvider(ISecurityProvider secProvider){
		securityProvider = secProvider;
	}
	
	public void unbindSecurityProvider(ISecurityProvider secProv){
		securityProvider = null;
	}

	public static ISecurityProvider getSecurityProvider() {
		return securityProvider;
	}

	public static void setSecurityProvider(ISecurityProvider securityProvider) {
		Activator.securityProvider = securityProvider;
	}
}
