package de.uniol.inf.is.odysseus.ac.standard.status.file;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class AdmissionStatusFilePlugIn implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		AdmissionStatusFilePlugIn.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		AdmissionStatusFilePlugIn.context = null;
	}

}
