package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor;

import java.util.Collection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.util.OSGI;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class StandardExecutorPlugIn implements BundleActivator {

	static private final ISession superUser =  SessionManagement.instance.loginSuperUser(null);

	@Override
	public void start(BundleContext context) throws Exception {

	}

	@Override
	public void stop(BundleContext context) throws Exception {
				
		StandardExecutor executor = (StandardExecutor) OSGI.get(IExecutor.class);

		Collection<IPhysicalQuery> queries = executor.getExecutionPlan(superUser).getQueries(superUser);
		for( IPhysicalQuery query : queries ) {
			executor.stopQuery(query.getID(), superUser);
		}
	}
}
