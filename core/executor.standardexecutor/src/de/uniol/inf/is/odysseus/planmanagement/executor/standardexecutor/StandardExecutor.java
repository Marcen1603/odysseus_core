package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorAction;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.IExecutionSetting;
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
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.usermanagement.AccessControl;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
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

	// ----------------------------------------------------------------------------------------
	// Logging
	// ----------------------------------------------------------------------------------------

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(StandardExecutor.class);
		}
		return _logger;
	}

	// ----------------------------------------------------------------------------------------
	// OSGI-Framework
	// ----------------------------------------------------------------------------------------

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 */
	public void activate() {
		// store buffer placement strategy in the configuration
		Iterator<String> iter;
		if (getRegisteredBufferPlacementStrategiesIDs() != null
				&& (iter = getRegisteredBufferPlacementStrategiesIDs()
						.iterator()).hasNext()) {
			this.configuration.set(new ParameterBufferPlacementStrategy(
					getBufferPlacementStrategy(iter.next())));
		} else {
			this.configuration.set(new ParameterBufferPlacementStrategy());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.IInfoProvider#getInfos()
	 */
	@Override
	public String getInfos() {
		String infos = "Executor: " + this;

		infos += AppEnv.LINE_SEPARATOR + AppEnv.LINE_SEPARATOR + "Optimizer: ";
		try {
			infos += AppEnv.LINE_SEPARATOR + getOptimizer().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPARATOR + e.getMessage();
		}

		infos += AppEnv.LINE_SEPARATOR + AppEnv.LINE_SEPARATOR
				+ "SchedulerManager: ";
		try {
			infos += AppEnv.LINE_SEPARATOR + getSchedulerManager().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPARATOR + e.getMessage();
		}

		infos += AppEnv.LINE_SEPARATOR + AppEnv.LINE_SEPARATOR + "Compiler: ";

		try {
			infos += AppEnv.LINE_SEPARATOR + getCompiler().getInfos();
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
	public void settingChanged(IExecutionSetting<?> newValueContainer) {
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
	private List<IQuery> createQueries(String queryStr, User user, IDataDictionary dd,
			QueryBuildConfiguration parameters)
			throws NoCompilerLoadedException, QueryParseException,
			OpenFailedException {
		getLogger().debug("Translate Queries.");
		// translate query and build logical plans
		List<IQuery> queries = getCompiler().translateQuery(queryStr,
				parameters.getParserID(), user, dd);
		getLogger().trace("Number of queries: " + queries.size());
		// create for each logical plan an intern query
		for (IQuery query : queries) {
			query.setBuildParameter(parameters);
			query.setQueryText(queryStr);
			query.setUser(user);
			query.setDataDictionary(dd);
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
	private void addQueries(List<IQuery> newQueries,
			OptimizationConfiguration conf) throws NoOptimizerLoadedException,
			QueryOptimizationException {
		getLogger().debug("Optimize Queries. Count:" + newQueries.size());
		if (newQueries.isEmpty()) {
			return;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			IExecutionPlan exep = getOptimizer().optimize(this,
					newQueries, conf);

			setExecutionPlan(exep);


			

			// store optimized queries

			for (IQuery optimizedQuery : newQueries) {
				this.plan.addQuery(optimizedQuery);
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_ADDED, optimizedQuery));
			}
			

		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
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
	 * {@link IQueryBuildSetting}. If some parameter not set default settings
	 * are used.
	 * 
	 * @param parameters
	 *            Parameter for creating a {@link QueryBuildConfiguration}
	 *            object.
	 * @return {@link QueryBuildConfiguration} with some assured parameters.
	 */
	@SuppressWarnings("rawtypes")
	private QueryBuildConfiguration validateBuildParameters(
			IQueryBuildSetting... parameters) {
		return validateBuildParameters(new QueryBuildConfiguration(parameters));
	}
	
	private QueryBuildConfiguration validateBuildParameters(
			QueryBuildConfiguration params) {
		if (params.getTransformationConfiguration() == null) {
			throw new RuntimeException(
					"No transformation configuration set. Abort query execution.");
		}
		// Parameter can be delayed as String --> Replace with strategy
		ParameterBufferPlacementStrategy bufferPlacement = params
				.getBufferPlacementParameter();
		if (bufferPlacement != null && bufferPlacement.getValue() == null
				&& bufferPlacement.getName() != null) {
			bufferPlacement = new ParameterBufferPlacementStrategy(
					getBufferPlacementStrategy(bufferPlacement.getName()));
			params.set(bufferPlacement);
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
	@Override
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy) {
		try {
			this.executionPlanLock.lock();
			return getOptimizer().getBufferPlacementStrategy(strategy);
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
	 * .lang.String, java.lang.String, de.uniol.inf.is.odysseus.planmanagement
	 * .query.querybuiltparameter.AbstractQueryBuildParameter<?>[])
	 */
	private Collection<IQuery> addQuery(String query, User user, IDataDictionary dd,
			QueryBuildConfiguration parameters) throws PlanManagementException {
		getLogger().info("Start adding Queries. " + query+ "for user "+user.getUsername());
		validateUserRight(user, ExecutorAction.ADD_QUERY);
		validateBuildParameters(parameters);
		try {
			List<IQuery> newQueries = createQueries(query, user, dd, parameters);
			addQueries(newQueries, new OptimizationConfiguration(parameters
					.values().toArray(new Setting[0])));
			return newQueries;
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			e.printStackTrace();
			throw new QueryAddException(e);
		}finally{
			getLogger().info("Adding Queries. " + query+ "for user "+user.getUsername()+" done.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(java
	 * .lang.String, java.lang.String, de.uniol.inf.is.odysseus.planmanagement
	 * .query.querybuiltparameter.AbstractQueryBuildParameter<?>[])
	 */
	@Override
	public Collection<IQuery> addQuery(String query, User user, IDataDictionary dd,
			@SuppressWarnings("rawtypes") IQueryBuildSetting... parameters) throws PlanManagementException {
		return addQuery(query, user, dd, validateBuildParameters(parameters));
	}

	// TODO: REMOVE SYNCHRONIZED!
	@Override
	public synchronized Collection<IQuery> addQuery(String query, String parserID,
			User user, IDataDictionary dd, @SuppressWarnings("rawtypes") IQueryBuildSetting... parameters)
			throws PlanManagementException {
		QueryBuildConfiguration conf = new QueryBuildConfiguration(parameters);
		conf.set(new ParameterParserID(parserID));
		return addQuery(query, user, dd, conf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#addQuery(de
	 * .uniol.inf.is.odysseus.base.ILogicalOperator, de.uniol.inf.is.odysseus
	 * .planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter
	 * <?>[])
	 */
	@Override
	public IQuery addQuery(ILogicalOperator logicalPlan, User user, IDataDictionary dd,
			@SuppressWarnings("rawtypes") IQueryBuildSetting... parameters) throws PlanManagementException {
		getLogger().info("Start adding Queries.");
		validateUserRight(user, ExecutorAction.ADD_QUERY);
		try {
			QueryBuildConfiguration params = validateBuildParameters(parameters);
			ArrayList<IQuery> newQueries = new ArrayList<IQuery>();
			Query query = new Query(logicalPlan, params);
			query.setUser(user);
			query.setDataDictionary(dd);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			addQueries(newQueries, new OptimizationConfiguration(parameters));
			return query;
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
	 * .uniol.inf.is.odysseus.base.IPhysicalOperator, de.uniol.inf.is.odysseus
	 * .planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter
	 * <?>[])
	 */
	@Override
	public IQuery addQuery(List<IPhysicalOperator> physicalPlan, User user,
			@SuppressWarnings("rawtypes") IQueryBuildSetting... parameters) throws PlanManagementException {
		getLogger().info("Start adding Queries.");
		validateUserRight(user, ExecutorAction.ADD_QUERY);
		try {
			QueryBuildConfiguration params = validateBuildParameters(parameters);
			ArrayList<IQuery> newQueries = new ArrayList<IQuery>();
			Query query = new Query(physicalPlan, params);
			query.setUser(user);
			// TODO: Korrekt so oder braucht man immer ein DD?
			query.setDataDictionary(null);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			addQueries(newQueries, getOptimizationParameters(parameters));
			return query;
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	private OptimizationConfiguration getOptimizationParameters(
			@SuppressWarnings("rawtypes") IQueryBuildSetting[] parameters) {
		OptimizationConfiguration conf = new OptimizationConfiguration(
				parameters);
		return conf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#removeQuery
	 * (int)
	 */
	@Override
	public void removeQuery(int queryID, User caller) throws PlanManagementException {
		getLogger().info("Start remove a query (ID: " + queryID + ").");

		Query queryToRemove = (Query) this.plan.getQuery(queryID);
		validateUserRight(queryToRemove, caller, ExecutorAction.REMOVE_QUERY);		
		if (queryToRemove != null && getOptimizer() != null) {
			try {
				getLogger().info(
						"Try to aquire executionPlanLock (Currently "
								+ executionPlanLock.getHoldCount() + ").");
				executionPlanLock.lock();
				getLogger().info(
						"Try to aquire executionPlanLock (Currently "
								+ executionPlanLock.getHoldCount() + "). done");
				setExecutionPlan(getOptimizer().beforeQueryRemove(
						this, queryToRemove, this.executionPlan));
				stopQuery(queryToRemove.getID(), caller);
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
	public void startQuery(int queryID, User caller) {
		Query queryToStart = (Query) this.plan.getQuery(queryID);
		validateUserRight(queryToStart,caller, ExecutorAction.START_QUERY);
		if (queryToStart.isActive()) {
			getLogger().info("Query (ID: " + queryID + ") is already started.");
			return;
		}
		getLogger().info("Starting query (ID: " + queryID + ").");

		try {
			this.executionPlanLock.lock();
			setExecutionPlan(getOptimizer().beforeQueryStart(queryToStart,
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

	private void validateUserRight(Query query, User caller,
			ExecutorAction executorAction) {
		if (!(
		// User has right 
		AccessControl.hasPermission(executorAction,  "Query "+query.getID(), caller)||
		// User is owner
		query.getUser().equals(caller)||
		// User has higher right
		AccessControl.hasPermission(
				ExecutorAction.hasSuperAction(executorAction), ExecutorAction.alias, caller))){
			throw new HasNoPermissionException("No Right to execute "+executorAction+" on Query "+query.getID()+" for "+caller.getUsername());
		}
		
	}

	private void validateUserRight(User caller,	ExecutorAction executorAction) {
		if (!(
		// User has right 
		AccessControl.hasPermission(executorAction,  ExecutorAction.alias, caller)||
		// User has higher right
		AccessControl.hasPermission(
				ExecutorAction.hasSuperAction(executorAction), ExecutorAction.alias, caller))){
			throw new HasNoPermissionException("No Right to execute "+executorAction+" for "+caller.getUsername());
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
	public void stopQuery(int queryID, User caller) {

		getLogger().info("Stop a query (ID: " + queryID + ").");

		Query queryToStop = (Query) this.plan.getQuery(queryID);
		validateUserRight(queryToStop,caller, ExecutorAction.STOP_QUERY);
		try {
			this.executionPlanLock.lock();
			setExecutionPlan(getOptimizer().beforeQueryStop(queryToStop,
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
			// return;
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
				setExecutionPlan(getOptimizer().reoptimize(this,
						(IQuery) sender, this.executionPlan));

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
	 * @see de.uniol.inf.is.odysseus.planmanagement.plan.IPlanReoptimizeListener
	 * # reoptimizeRequest(de.uniol.inf.is.odysseus.planmanagement.plan.IPlan )
	 */
	@Override
	public void reoptimizeRequest(IPlan sender) {
		getLogger().info("Reoptimize request by plan.");

		if (sender instanceof Plan) {
			try {
				this.executionPlanLock.lock();
				setExecutionPlan(getOptimizer().reoptimize(this,
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
	public List<IQuery> getQueries() {
		return Collections.unmodifiableList(this.plan.getQueries());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getRegisteredBufferPlacementStrategies()
	 */
	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs() {
		try {
			return getOptimizer().getRegisteredBufferPlacementStrategies();
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
	public Set<String> getRegisteredSchedulingStrategies() {

		try {
			return getSchedulerManager().getSchedulingStrategy();
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
	public Set<String> getRegisteredSchedulers() {

		try {
			return getSchedulerManager().getScheduler();
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
			getSchedulerManager().setActiveScheduler(scheduler,
					schedulerStrategy, this);
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
	public String getCurrentSchedulingStrategyID() {
		try {
			return getSchedulerManager().getActiveSchedulingStrategyID();
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
	public String getCurrentSchedulerID() {
		try {
			return getSchedulerManager().getActiveSchedulerID();
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Getting Active Scheduler. "
							+ e.getMessage());
		}
		return null;
	}

	@Override
	public IScheduler getCurrentScheduler() {
		try {
			return getSchedulerManager().getActiveScheduler();
		} catch (SchedulerException e) {
			getLogger().error(
					"Error while using schedulerManager. Getting Active Scheduler. "
							+ e.getMessage());
		}
		return null;
	}

	@Override
	public OptimizationConfiguration getOptimizerConfiguration()
			throws NoOptimizerLoadedException {
		return this.getOptimizer().getConfiguration();
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
			setExecutionPlan(this.getOptimizer().beforeQueryMigration(
					this,
					new OptimizationConfiguration(ParameterDoRewrite.FALSE)));
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

	@Override
	public Collection<String> getQueryBuildConfigurationNames() {
		return queryBuildConfigs.keySet();	
	}

	@Override
	public List<IQueryBuildSetting<?>> getQueryBuildConfiguration(String name) {
		return queryBuildConfigs.get(name);	
	}
}