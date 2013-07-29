package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor;

import java.util.Collection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class StandardExecutorPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		StandardExecutor executor = StandardExecutor.getInstance();
		
		Collection<IPhysicalQuery> queries = executor.getExecutionPlan().getQueries();
		for( IPhysicalQuery query : queries ) {
			executor.stopQuery(query.getID());
		}
	}
}
