package de.uniol.inf.is.odysseus.parser.pql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	private static final String ACCESS = "ACCESS";
	private static final String SELECT = "SELECT";
	private static final String JOIN = "JOIN";

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		PQLParser.addOperatorBuilder(ACCESS, new AccessAOBuilder());
		PQLParser.addOperatorBuilder(SELECT, new SelectAOBuilder());
		PQLParser.addOperatorBuilder(JOIN, new JoinAOBuilder());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		PQLParser.removeOperatorBuilder(ACCESS);
		PQLParser.removeOperatorBuilder(SELECT);
		PQLParser.removeOperatorBuilder(JOIN);
	}

}
