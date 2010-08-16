package de.uniol.inf.is.odysseus.parser.pql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class Activator implements BundleActivator {

	private static final String PRIORITY = "PRIORITY";
	private static IParameter<?> priorityParameter = new IntegerParameter(PRIORITY, REQUIREMENT.OPTIONAL);

	@Override
	public void start(BundleContext context) throws Exception {
		PQLParser.addQueryParameter(priorityParameter);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		PQLParser.removeQueryParameter(PRIORITY);
	}

}
