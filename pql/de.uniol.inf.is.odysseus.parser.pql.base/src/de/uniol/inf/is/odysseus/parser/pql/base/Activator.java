package de.uniol.inf.is.odysseus.parser.pql.base;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.parser.pql.PQLParser;

public class Activator implements BundleActivator {

	private static final String ACCESS = "ACCESS";

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		PQLParser.addOperatorBuilder(ACCESS, new AccessAOBuilder());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		PQLParser.removeOperatorBuilder(ACCESS);
	}

}
