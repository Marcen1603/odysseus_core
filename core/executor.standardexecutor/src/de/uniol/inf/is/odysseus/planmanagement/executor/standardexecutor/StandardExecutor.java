package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.datastructure.Plan;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.QueryAddException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.AbstractQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * StandardExecutor is the standard implementation of {@link IExecutor}. The
 * tasks of this object are:
 * 
 * - adding new queries - control scheduling, optimization and query processing
 * - send events of intern changes - providing execution informations
 * 
 * @author Wolf Bauer, Jonas Jacobi, Tobias Witt
 */
public class StandardExecutor extends AbstractExecutor {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 */
	public void activate() {
		// store buffer placement strategy in the configuration
		Iterator<String> iter;
		if (getRegisteredBufferPlacementStrategies() != null
				&& (iter = getRegisteredBufferPlacementStrategies().iterator())
						.hasNext()) {
			this.configuration.set(new SettingBufferPlacementStrategy(iter
					.next()));
		} else {
			this.configuration.set(new SettingBufferPlacementStrategy(null));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * setDefaultBufferPlacementStrategy(java.lang.String)
	 */
	@Override
	public void setDefaultBufferPlacementStrategy(String strategy) {
		this.configuration.set(new SettingBufferPlacementStrategy(strategy));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.IInfoProvider#getInfos()
	 */
	@Override
	public String getInfos() {
		String infos = "Executor: " + this;

		infos += AppEnv.LINE_SEPARATOR + AppEnv.LINE_SEPARATOR + "Optimizer: ";
		try {
			infos += AppEnv.LINE_SEPARATOR + optimizer().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPARATOR + e.getMessage();
		}

		infos += AppEnv.LINE_SEPARATOR + AppEnv.LINE_SEPARATOR
				+ "SchedulerManager: ";
		try {
			infos += AppEnv.LINE_SEPARATOR + schedulerManager().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPARATOR + e.getMessage();
		}

		infos += AppEnv.LINE_SEPARATOR + AppEnv.LINE_SEPARATOR + "Compiler: ";

		try {
			infos += AppEnv.LINE_SEPARATOR + compiler().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPARATOR + e.getMessage();
		}

		return infos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.AbstractExecutor#
	 * initializeIntern
	 * (de.uniol.inf.is.odysseus.planmanagement.executor.configuration
	 * .ExecutionConfiguration)
	 */
	@Override
	protected void initializeIntern(ExecutionConfiguration configuration) {
		// create a new Plan object
		this.plan = new Plan();
		// add ReoptimizeListener
		((Plan) this.plan).addReoptimizeListener(this);
		// create new execution plan object
		this.executionPlan = new ExecutionPlan();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.configuration.
	 * IValueChangeListener
	 * #settingChanged(de.uniol.inf.is.odysseus.planmanagement
	 * .configuration.IMapValue)
	 */
	@Override
	public void settingChanged(AbstractExecutionSetting<?> newValueContainer) {
		// no reactions necessary
	}

	/**
	 * Creates a list of queries based on a query as a string, a parser id and
	 * build parameters.
	 * 
	 * @param queryStr
	 *            query as a string (e. g. CQL). Can contain more then one query
	 *            (e. g. ";"-separated).
	 * @param parserID
	 *            ID of the parser for translation of query (e. g. CQLParser).
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for the new queries.
	 * @return List of created queries.
	 * @throws NoCompilerLoadedException
	 *             No compiler is set.
	 * @throws QueryParseException
	 *             An Exception occurred during parsing.
	 * @throws OpenFailedException
	 *             Opening an sink or source failed.
	 */
	private List<IQuery> createQueries(String queryStr, String parserID,
			User user, QueryBuildConfiguration parameters)
			throws NoCompilerLoadedException, QueryParseException,
			OpenFailedException {
		getLogger().debug("Translate Queries.");
		// translate query and build logical plans
		List<IQuery> queries = compiler().translateQuery(queryStr, parserID, user);
		getLogger().trace("Number of queries: " + queries.size());
		// create for each logical plan an intern query
		for (IQuery query : queries) {
			query.setBuildParameter(parameters);
			query.setQueryText(queryStr);
			query.setUser(user);
			// this executor processes reoptimize requests
			query.addReoptimizeListener(this);
		}

		return queries;
	}

	/**
	 * Optimize new queries and set the resulting execution plan. After setting
	 * the execution plan all new queries are stored in the global queries
	 * storage ({@link IPlan}).
	 * 
	 * @param newQueries
	 *            Queries to process.
	 * @throws NoOptimizerLoadedException
	 *             No optimizer is set.
	 * @throws QueryOptimizationException
	 *             An exception during optimization occurred.
	 */
	private void addQueries(List<IQuery> newQueries)
			throws NoOptimizerLoadedException, QueryOptimizationException {
		getLogger().debug("Optimize Queries. Count:" + newQueries.size());
		if (newQueries.isEmpty()) {
			return;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			setExecutionPlan(optimizer().preQueryAddOptimization(this,
					newQueries, ParameterDoRestruct.TRUE));
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}

		// store optimized queries
		for (IQuery optimizedQuery : newQueries) {
			this.plan.addQuery(optimizedQuery);
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_ADDED, optimizedQuery));
		}

		getLogger().info("Queries added (Count: " + newQueries.size() + ").");
	}

	/**
	 * Optimize new queries, if corresponding parameter is set true and set the
	 * resulting execution plan. After setting the execution plan all new
	 * queries are stored in the global queries storage ({@link IPlan}).
	 * 
	 * @param newQueries
	 *            Queries to process.
	 * @param doRestruct
	 *            If true, restructuring the query plan will be done. If false,
	 *            it will not be done.
	 * @param rulesToUse
	 *            Contains the names of the rules to be used for restructuring.
	 *            Other rules will not be used.
	 * @throws NoOptimizerLoadedException
	 *             No optimizer is set.
	 * @throws QueryOptimizationException
	 *             An exception during optimization occurred.
	 */
	private void addQueries(List<IQuery> newQueries, boolean doRestruct,
			Set<String> rulesToUse) throws NoOptimizerLoadedException,
			QueryOptimizationException {
		getLogger().debug("Optimize Queries. Count:" + newQueries.size());
		if (newQueries.isEmpty()) {
			return;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			setExecutionPlan(optimizer().preQueryAddOptimization(
					this,
					newQueries,
					rulesToUse,
					doRestruct ? ParameterDoRestruct.TRUE
							: ParameterDoRestruct.FALSE));
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}

		getLogger().info("Before adding these new Queries " + newQueries);

		// store optimized queries
		for (IQuery optimizedQuery : newQueries) {
			this.plan.addQuery(optimizedQuery);
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_ADDED, optimizedQuery));
		}

		getLogger().info("Queries added (Count: " + newQueries.size() + ").");
	}

	/**
	 * Optimize new queries, if corresponding parameter is set true and set the
	 * resulting execution plan. After setting the execution plan all new
	 * queries are stored in the global queries storage ({@link IPlan}).
	 * 
	 * @param newQueries
	 *            Queries to process.
	 * 
	 * @param doRestrcut
	 *            If true, restructuring will be done. If false, it will not.
	 * @throws NoOptimizerLoadedException
	 *             No optimizer is set.
	 * @throws QueryOptimizationException
	 *             An exception during optimization occurred.
	 */
	private void addQueries(List<IQuery> newQueries, boolean doRestruct)
			throws NoOptimizerLoadedException, QueryOptimizationException {
		getLogger().debug("Optimize Queries. Count:" + newQueries.size());
		if (newQueries.isEmpty()) {
			return;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			setExecutionPlan(optimizer().preQueryAddOptimization(
					this,
					newQueries,
					doRestruct ? ParameterDoRestruct.TRUE
							: ParameterDoRestruct.FALSE));
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}

		// store optimized queries
		for (IQuery optimizedQuery : newQueries) {
			this.plan.addQuery(optimizedQuery);
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_ADDED, optimizedQuery));
		}

		getLogger().info("Queries added (Count: " + newQueries.size() + ").");
	}

	/**
	 * Returns a ID list of the given queries.
	 * 
	 * @param newQueries
	 *            Queries for search.
	 * @return ID list of the given queries.
	 */
	private List<Integer> getQuerieIDs(List<IQuery> newQueries) {
		ArrayList<Integer> newIDs = new ArrayList<Integer>();

		for (IQuery query : newQueries) {
			newIDs.add(query.getID());
		}

		return newIDs;
	}

	/**
	 * Creates {@link QueryBuildConfiguration} of given
	 * {@link AbstractQueryBuildSetting}. If some parameter not set default
	 * settings are used.
	 * 
	 * @param parameters
	 *            Parameter for creating a {@link QueryBuildConfiguration} object.
	 * @return {@link QueryBuildConfiguration} with some assured parameters.
	 */
	private QueryBuildConfiguration getBuildParameter(
			AbstractQueryBuildSetting<?>... parameters) {
		QueryBuildConfiguration params = new QueryBuildConfiguration(parameters);
		// assure ParameterTransformationConfiguration
		if (params.getTransformationConfiguration() == null) {
			throw new RuntimeException(
					"No transformation configuration set. Abort query execution.");
		}
		// assure ParameterBufferPlacementStrategy
		if (params.getBufferPlacementStrategy() == null) {
			params.set(new ParameterBufferPlacementStrategy(this
					.getBufferPlacementStrategy((String) this.configuration
							.get(SettingBufferPlacementStrategy.class)
							.getValue())));
		}

		return params;
	}

	/**
	 * Get a {@link IBufferPlacementStrategy} by an ID.
	 * {@link IBufferPlacementStrategy} services are managed by the optimization
	 * module.
	 * 
	 * @param strategy
	 *            ID of the requested strategy.
	 * @return {@link IBufferPlacementStrategy} for an ID. Null if no
	 *         {@link IBufferPlacementStrategy} will be found.
	 */
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy) {
		try {
			this.executionPlanLock.lock();
			return optimizer().getBufferPlacementStrategy(strategy);
		} catch (NoOptimizerLoadedException e) {
			getLogger().error(
					"Error while using optimizer. Getting BufferplacementStrategy. "
							+ e.getMessage());
		} finally {
			this.executionPlanLock.unlock();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(java
	 * .lang.String, java.lang.String,
	 * de.uniol.inf.is.odysseus.planmanagement
	 * .query.querybuiltparameter.AbstractQueryBuildParameter<?>[])
	 */
	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			User user, AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException {
		getLogger().info("Start adding Queries. " + query);
		try {
			QueryBuildConfiguration params = getBuildParameter(parameters);
			List<IQuery> newQueries = createQueries(query, parserID, user,
					params);
			addQueries(newQueries);
			return getQuerieIDs(newQueries);
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			e.printStackTrace();
			throw new QueryAddException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(java
	 * .lang.String, java.lang.String, boolean
	 * de.uniol.inf.is.odysseus.planmanagement
	 * .query.querybuiltparameter.AbstractQueryBuildParameter<?>[])
	 */
	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			User user, boolean doRestruct, Set<String> rulesToUse,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException {
		getLogger().info("Start adding Queries. " + query);
		try {
			QueryBuildConfiguration params = getBuildParameter(parameters);
			List<IQuery> newQueries = createQueries(query, parserID, user,
					params);
			if (rulesToUse != null && !rulesToUse.isEmpty()) {
				addQueries(newQueries, doRestruct, rulesToUse);
			} else {
				addQueries(newQueries, doRestruct);
			}
			return getQuerieIDs(newQueries);
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			e.printStackTrace();
			throw new QueryAddException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(de
	 * .uniol.inf.is.odysseus.base.ILogicalOperator,
	 * de.uniol.inf.is.odysseus
	 * .planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter
	 * <?>[])
	 */
	@Override
	public int addQuery(ILogicalOperator logicalPlan, User user,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException {
		getLogger().info("Start adding Queries.");
		try {
			QueryBuildConfiguration params = getBuildParameter(parameters);
			ArrayList<IQuery> newQueries = new ArrayList<IQuery>();
			Query query = new Query(logicalPlan, params);
			query.setUser(user);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			addQueries(newQueries);
			return query.getID();
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(de
	 * .uniol.inf.is.odysseus.base.IPhysicalOperator,
	 * de.uniol.inf.is.odysseus
	 * .planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter
	 * <?>[])
	 */
	@Override
	public int addQuery(List<IPhysicalOperator> physicalPlan, User user,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException {
		getLogger().info("Start adding Queries.");
		try {
			QueryBuildConfiguration params = getBuildParameter(parameters);
			ArrayList<IQuery> newQueries = new ArrayList<IQuery>();
			Query query = new Query(physicalPlan, params);
			query.setUser(user);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			addQueries(newQueries);
			return query.getID();
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(de
	 * .uniol.inf.is.odysseus.planmanagement.executor.datastructure.Query)
	 */
	@Override
	public int addQuery(Query query) throws PlanManagementException {
		try {
			query.addReoptimizeListener(this);
			ArrayList<IQuery> newQueries = new ArrayList<IQuery>();
			newQueries.add(query);
			addQueries(newQueries, false);
			return query.getID();
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#removeQuery
	 * (int)
	 */
	@Override
	public void removeQuery(int queryID) throws PlanManagementException {
		getLogger().info("Start remove a query (ID: " + queryID + ").");

		Query queryToRemove = (Query) this.plan.getQuery(queryID);

		if (queryToRemove != null && optimizer() != null) {
			try {
				getLogger().info(
						"Try to aquire executionPlanLock (Currently "
								+ executionPlanLock.getHoldCount() + ").");
				executionPlanLock.lock();
				getLogger().info(
						"Try to aquire executionPlanLock (Currently "
								+ executionPlanLock.getHoldCount() + "). done");
				setExecutionPlan(optimizer().preQueryRemoveOptimization(this,
						queryToRemove, this.executionPlan));
				stopQuery(queryToRemove.getID());
				getLogger().info("Removing Query " + queryToRemove.getID());
				this.plan.removeQuery(queryToRemove.getID());
				getLogger().info("Removing Ownership " + queryToRemove.getID());
				queryToRemove.stop();
				queryToRemove.removeOwnerschip();
				WrapperPlanFactory.removeClosedSources();
				getLogger().debug(
						"Query " + queryToRemove.getID() + " removed.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_REMOVE, queryToRemove));
			} catch (QueryOptimizationException e) {
				getLogger().warn(
						"Query not removed. An Error while optimizing occurd (ID: "
								+ queryID + ").");
				throw new PlanManagementException(e);
			} finally {
				executionPlanLock.unlock();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#startQuery
	 * (int)
	 */
	@Override
	public void startQuery(int queryID) {
		Query queryToStart = (Query) this.plan.getQuery(queryID);
		if (queryToStart.isActive()) {
			getLogger().info("Query (ID: " + queryID + ") is already started.");
			return;
		}
		getLogger().info("Starting query (ID: " + queryID + ").");

		try {
			this.executionPlanLock.lock();
			setExecutionPlan(optimizer().preStartOptimization(queryToStart,
					this.executionPlan));
			queryToStart.start();
			if (isRunning()) {
				for (IPhysicalOperator curRoot : queryToStart.getRoots()) {
					// this also works for cyclic plans,
					// since if an operator is already open, the
					// following sources will not be called any more.
					if (curRoot.isSink()) {
						((ISink<?>) curRoot).open();
					} else {
						throw new IllegalArgumentException(
								"Open cannot be called on a a source");
					}
				}
			}
			getLogger().debug("Query " + queryID + " started.");
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_START, queryToStart));
		} catch (Exception e) {
			getLogger().warn(
					"Query not started. An Error during optimizing occurd (ID: "
							+ queryID + ").");
			return;
		} finally {
			this.executionPlanLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#stopQuery
	 * (int)
	 */
	@Override
	public void stopQuery(int queryID) {

		getLogger().info("Stop a query (ID: " + queryID + ").");

		Query queryToStop = (Query) this.plan.getQuery(queryID);
	
		try {
			this.executionPlanLock.lock();
			setExecutionPlan(optimizer().preStopOptimization(queryToStop,
					this.executionPlan));
			queryToStop.stop();
			if (isRunning()) {
				for (IPhysicalOperator curRoot : queryToStop.getRoots()) {
					// this also works for cyclic plans,
					// since if an operator is already open, the
					// following sources will not be called any more.
					if (curRoot.isSink()) {
						((ISink<?>) curRoot).close();
					} else {
						throw new IllegalArgumentException(
								"Close cannot be called on a a source");
					}
				}
			}
			getLogger().debug("Query " + queryID + " stopped.");
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_STOP, queryToStop));
		} catch (Exception e) {
			getLogger().warn(
					"Query not stopped. An Error while optimizing occurd (ID: "
							+ queryID + ")." + e.getMessage());
			throw new RuntimeException(e);
			//return;
		} finally {
			this.executionPlanLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.query.IQueryReoptimizeListener
	 * #reoptimize(de.uniol.inf.is.odysseus.planmanagement.query.IQuery)
	 */
	@Override
	public void reoptimize(IQuery sender) {
		getLogger().info(
				"Reoptimize request by query (ID: " + sender.getID() + ").");

		try {
			if (sender instanceof Query) {
				this.executionPlanLock.lock();
				setExecutionPlan(optimizer().reoptimize(this, (IQuery) sender,
						this.executionPlan));

				getLogger().debug("Query " + sender.getID() + " reoptimized.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_REOPTIMIZE, sender));
			} else {
				getLogger().warn(
						"Query not reoptimized. Query type is not supported.");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warn(
					"Query not reoptimized. An Error while optimizing occurd (ID: "
							+ sender.getID() + ").");
			return;
		} finally {
			this.executionPlanLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.plan.IPlanReoptimizeListener
	 * #
	 * reoptimizeRequest(de.uniol.inf.is.odysseus.planmanagement.plan.IPlan
	 * )
	 */
	@Override
	public void reoptimizeRequest(IPlan sender) {
		getLogger().info("Reoptimize request by plan.");

		if (sender instanceof Plan) {
			try {
				this.executionPlanLock.lock();
				setExecutionPlan(optimizer().reoptimize(this,
						this.executionPlan));
				getLogger().debug("Plan reoptimized.");
				firePlanModificationEvent(new PlanModificationEvent(this,
						PlanModificationEventType.PLAN_REOPTIMIZE, this.plan));
			} catch (Exception e) {
				getLogger()
						.warn("Plan not reoptimized. An Error while optimizing occurd.");
				return;
			} finally {
				this.executionPlanLock.unlock();
			}
		} else {
			getLogger().warn(
					"Plan not reoptimized. Plan type is not supported.");
			return;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable
	 * #getRegisteredQueries()
	 */
	@Override
	public ArrayList<IQuery> getRegisteredQueries() {
		return this.plan.getEdittableQueries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getRegisteredBufferPlacementStrategies()
	 */
	@Override
	public Set<String> getRegisteredBufferPlacementStrategies() {
		try {
			return optimizer().getRegisteredBufferPlacementStrategies();
		} catch (NoOptimizerLoadedException e) {
			getLogger().error(
					"Error while using optimizer. Getting BufferplacementStrategies. "
							+ e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getRegisteredSchedulingStrategyFactories()
	 */
	@Override
	public Set<String> getRegisteredSchedulingStrategyFactories() {

		try {
			return schedulerManager().getSchedulingStrategy();
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Getting SchedulingStrategyFactories. "
							+ e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getRegisteredSchedulerFactories()
	 */
	@Override
	public Set<String> getRegisteredSchedulerFactories() {

		try {
			return schedulerManager().getScheduler();
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Getting SchedulingFactories. "
							+ e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * setScheduler(java.lang.String, java.lang.String)
	 */
	@Override
	public void setScheduler(String scheduler, String schedulerStrategy) {
		try {
			schedulerManager().setActiveScheduler(scheduler, schedulerStrategy,
					this);
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Setting Scheduler. "
							+ e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getCurrentSchedulingStrategy()
	 */
	@Override
	public String getCurrentSchedulingStrategy() {
		try {
			return schedulerManager().getActiveSchedulingStrategy();
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Getting Active Scheduling Strategy. "
							+ e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getCurrentScheduler()
	 */
	@Override
	public String getCurrentScheduler() {
		try {
			return schedulerManager().getActiveScheduler();
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Getting Active Scheduler. "
							+ e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable#
	 * getEditableExecutionPlan()
	 */
	@Override
	public IExecutionPlan getEditableExecutionPlan() {
		return this.executionPlan;
	}

	@Override
	public OptimizationConfiguration getOptimizerConfiguration()
			throws NoOptimizerLoadedException {
		return this.optimizer().getConfiguration();
	}

	@Override
	public ISystemMonitor getDefaultSystemMonitor()
			throws NoSystemMonitorLoadedException {
		if (this.systemMonitorFactory == null) {
			throw new NoSystemMonitorLoadedException();
		}
		return this.defaultSystemMonitor;
	}

	@Override
	public ISystemMonitor newSystemMonitor(long period)
			throws NoSystemMonitorLoadedException {
		if (this.systemMonitorFactory == null) {
			throw new NoSystemMonitorLoadedException();
		}
		ISystemMonitor monitor = this.systemMonitorFactory.newSystemMonitor();
		monitor.initialize(period);
		return monitor;
	}

	@Override
	public void updateExecutionPlan() throws NoOptimizerLoadedException,
			QueryOptimizationException {
		getLogger().debug("Update Execution Plan.");
		// synchronize the process
		this.executionPlanLock.lock();
		try {
			setExecutionPlan(this.optimizer().preQueryMigrateOptimization(this,
					new OptimizationConfiguration(ParameterDoRestruct.FALSE)));
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}
		getLogger().debug("Finished updating Execution Plan.");
	}
	
	@Override
	public String getName() {
		return "Standard";
	}
}