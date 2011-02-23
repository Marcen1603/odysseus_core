package de.uniol.inf.is.odysseus.interval.pql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static final String TIMESTAMPORDERVALIDATOR = "TIMESTAMPORDERVALIDATOR";

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		OperatorBuilderFactory.putOperatorBuilderType(TIMESTAMPORDERVALIDATOR, TimeStampOrderValidatorBuilder.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType(TIMESTAMPORDERVALIDATOR);

	}

}
