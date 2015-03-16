package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

class QueryDistributorThread extends Thread {
	
	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributorThread.class);
	private static final Object distributeMonitor = new Object();
	
	private final IServerExecutor serverExecutor;
	private final ISession caller;
	private final Collection<ILogicalQuery> queriesToDistribute;
	private final QueryBuildConfiguration config;
	
	public QueryDistributorThread(final IServerExecutor serverExecutor, final ISession caller, final Collection<ILogicalQuery> queriesToDistribute, final QueryBuildConfiguration config) {
		Preconditions.checkNotNull(serverExecutor, "Server executor must not be null!");
		Preconditions.checkNotNull(caller, "Caller must not be null!");
		Preconditions.checkNotNull(queriesToDistribute, "List of queries to distribute must not be null!");
		Preconditions.checkNotNull(config, "Query build configuration must not be null!");
		
		this.serverExecutor = serverExecutor;
		this.caller = caller;
		this.queriesToDistribute = queriesToDistribute;
		this.config = config;
		
		setName("");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		try {
			synchronized( distributeMonitor ) {
				QueryDistributor.distributeImpl(serverExecutor, caller, queriesToDistribute, config);
			}
		} catch (QueryDistributionException ex) {
			LOG.error("Could not distribute queries", ex);
		}
	}
}
