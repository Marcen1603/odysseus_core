package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * The gap recovery executor represents a recovery strategy, which backups and
 * recovery query states (incl. sinks and sources).
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class GapRecoveryExecutor extends AbstractRecoveryExecutor {

	@Override
	public String getName() {
		return "GapRecovery";
	}

	/**
	 * Nothing to do, because recovery of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries) {
		// Nothing to do.
		return queries;
	}

	/**
	 * Nothing to do, because backup of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries) {
		// Nothing to do.
		return queries;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		GapRecoveryExecutor instance = new GapRecoveryExecutor();
		instance.mConfig = config;
		return instance;
	}

}