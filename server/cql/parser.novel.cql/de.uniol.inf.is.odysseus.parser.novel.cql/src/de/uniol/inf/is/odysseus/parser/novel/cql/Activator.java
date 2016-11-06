package de.uniol.inf.is.odysseus.parser.novel.cql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.USAGE;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

public class Activator implements BundleActivator
{

	private static final String PRIORITY = "PRIORITY";
	private static IParameter<?> priorityParameter = new IntegerParameter(PRIORITY, REQUIREMENT.OPTIONAL, USAGE.RECENT);
	
	@Override
	public void start(BundleContext arg0) throws Exception 
	{
		CQLParser.addQueryParameter(priorityParameter);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception 
	{
		CQLParser.removeQueryParameter(PRIORITY);
	}

}
