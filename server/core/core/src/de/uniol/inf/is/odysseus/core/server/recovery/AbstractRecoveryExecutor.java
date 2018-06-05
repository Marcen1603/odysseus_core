package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * A recovery executor represents a complete non-distributed recovery (NDR)
 * strategy by calling certain {@link IRecoveryComponent}s in a certain order.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractRecoveryExecutor implements IRecoveryExecutor {

	private final Logger log = setLogger();

	/**
	 * The configuration for the recovery executor.
	 */
	protected Properties config;

	/**
	 * The recovery components in order.
	 */
	protected List<IRecoveryComponent> components = new ArrayList<>();

	/**
	 * Initializes the recovery components with the properties. Should be called
	 * by {@link #newInstance(Properties)}.
	 * 
	 * @param config
	 *            The configuration for the recovery executor.
	 * @param components
	 *            The recovery components in order.
	 */
	protected void init(Properties config, List<IRecoveryComponent> components) {
		this.config = config;
		for (IRecoveryComponent component : components) {
			component.initialize(config);
			this.components.add(component);
		}
	}

	/**
	 * Sets the logger for the recovery executor.
	 */
	protected abstract Logger setLogger();

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		this.log.info("Activate backup for queries with the following ids:'{}'", getQueryIds(queries));
		List<ILogicalQuery> modifiedQueries = new ArrayList<>(queries);
		for (IRecoveryComponent component : this.components) {
			modifiedQueries = component.activateBackup(qbConfig, session, modifiedQueries, caller);
		}
		return modifiedQueries;
	}

	/**
	 * Helper method to extract query ids from list of queries.
	 */
	private List<Integer> getQueryIds(List<ILogicalQuery> queries) {
		List<Integer> ids = new ArrayList<>();
		for (ILogicalQuery query : queries) {
			ids.add(query.getID());
		}
		return ids;
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		this.log.info("Activate recovery for queries with the following ids:'{}'", getQueryIds(queries));
		List<ILogicalQuery> modifiedQueries = new ArrayList<>(queries);
		for (IRecoveryComponent component : this.components) {
			modifiedQueries = component.activateRecovery(qbConfig, session, modifiedQueries, caller);
		}
		return modifiedQueries;
	}

	@Override
	public boolean isRecoveryNeeded() {
		return this.config != null && this.config.containsKey("recoveryneeded")
				&& Boolean.parseBoolean(this.config.getProperty("recoveryneeded"));
	}

}