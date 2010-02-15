package de.uniol.inf.is.odysseus.bpel.correlationservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private CorrelationService_CorrelationServiceSOAP_Server server;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		this.server = new CorrelationService_CorrelationServiceSOAP_Server();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (this.server != null){
			this.server.stop();
		}
	}

}
