/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.ac.IAdmissionReaction;
import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.IExecutionSetting;
import de.uniol.inf.is.odysseus.planmanagement.executor.datastructure.PhysicalPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.QueryAddException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.reloadlog.ReloadLog;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPhysicalPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.sla.SLA;
import de.uniol.inf.is.odysseus.sla.SLADictionary;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.util.SetOwnerVisitor;

/**
 * StandardExecutor is the standard implementation of {@link IExecutor}. The
 * tasks of this object are:
 * 
 * - adding new queries - control scheduling, optimization and query processing
 * - send events of intern changes - providing execution informations
 * 
 * @author Wolf Bauer, Jonas Jacobi, Tobias Witt, Marco Grawunder
 */
public class StandardExecutor extends AbstractExecutor implements
		IAdmissionListener {

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

	private ReloadLog reloadLog;

	private IAdmissionControl admissionControl = null;

	public void bindAdmissionControl(IAdmissionControl control) {
		if (admissionControl != null)
			admissionControl.removeListener(this);

		admissionControl = control;
		if (admissionControl != null)
			admissionControl.addListener(this);
	}

	public void unbindAdmissionControl(IAdmissionControl control) {
		if (admissionControl == control) {
			if (admissionControl != null)
				admissionControl.removeListener(this);

			admissionControl = null;
		}
	}

	private IAdmissionReaction admissionReaction = null;

	public void bindAdmissionReaction(IAdmissionReaction reaction) {
		admissionReaction = reaction;
	}

	public void unbindAdmissionReaction(IAdmissionReaction reaction) {
		if (admissionReaction == reaction) {
			admissionReaction = null;
		}
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
		this.reloadLog = new ReloadLog();
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
		this.plan = new PhysicalPlan();
		// add ReoptimizeListener
		((PhysicalPlan) this.plan).addReoptimizeListener(this);
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
	private List<ILogicalQuery> createQueries(String queryStr, ISession user,
			String buildConfigName, QueryBuildConfiguration parameters)
			throws NoCompilerLoadedException, QueryParseException,
			OpenFailedException {
		getLogger().debug("Translate Queries.");
		// translate query and build logical plans
		List<ILogicalQuery> queries = getCompiler().translateQuery(queryStr,
				parameters.getParserID(), user, getDataDictionary());
		getLogger().trace("Number of queries: " + queries.size());
		String slaName = SLADictionary.getInstance().getCurrentSLA(user);
		SLA sla = SLADictionary.getInstance().getSLA(slaName);
		// create for each logical plan an intern query
		for (ILogicalQuery query : queries) {
			query.setBuildParameter(buildConfigName, parameters);
			query.setQueryText(queryStr);
			query.setUser(user);
			query.setSLA(sla);
			// this executor processes reoptimize requests
			if (query instanceof IPhysicalQuery) {
				((IPhysicalQuery) query).addReoptimizeListener(this);
			}
		}

		return queries;
	}

	/**
	 * Optimize new queries and set the resulting execution plan. After setting
	 * the execution plan all new queries are stored in the global queries
	 * storage ({@link IPhysicalPlan}).
	 * 
	 * @param newQueries
	 *            Queries to process.
	 * @throws NoOptimizerLoadedException
	 *             No optimizer is set.
	 * @throws QueryOptimizationException
	 *             An exception during optimization occurred.
	 */
	private List<IPhysicalQuery> addQueries(List<ILogicalQuery> newQueries,
			OptimizationConfiguration conf) throws NoOptimizerLoadedException,
			QueryOptimizationException {
		getLogger().debug("Optimize Queries. Count:" + newQueries.size());
		List<IPhysicalQuery> optimizedQueries = new ArrayList<IPhysicalQuery>();
		if (newQueries.isEmpty()) {
			return optimizedQueries;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			IExecutionPlan exep = getOptimizer().optimize(this, newQueries,
					optimizedQueries, conf, getDataDictionary());

			setExecutionPlan(exep);

			// store optimized queries

			for (IPhysicalQuery optimizedQuery : optimizedQueries) {
				this.plan.addQuery(optimizedQuery);
				optimizedQuery.addReoptimizeListener(this);
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_ADDED, optimizedQuery));
				if (optimizedQuery.getLogicalQuery() != null) {
					getDataDictionary().addQuery(
							optimizedQuery.getLogicalQuery(),
							optimizedQuery.getUser());
				}
			}

		} catch (Exception e) {
			throw new QueryOptimizationException(e);
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}

		getLogger().info("Queries added (Count: " + newQueries.size() + ").");
		return optimizedQueries;
	}

	private List<IPhysicalQuery> addQueries(ArrayList<IPhysicalQuery> newQueries,
			OptimizationConfiguration conf) {
		throw new RuntimeException("Adding physical query plans is currently not implemented");		
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

	@Override
	public synchronized Collection<ILogicalQuery> addQuery(String query,
			String parserID, ISession user, String buildConfiguartionName)
			throws PlanManagementException {
		getLogger().info(
				"Start adding Queries. " + query + " for user "
						+ user.getUser().getName());
		QueryBuildConfiguration buildConfiguration = buildAndValidateQueryBuildConfigurationFromSettings(buildConfiguartionName);
		buildConfiguration.set(new ParameterParserID(parserID));
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		validateBuildParameters(buildConfiguration);
		try {
			List<ILogicalQuery> newQueries = createQueries(query, user,
					buildConfiguartionName, buildConfiguration);
			addQueries(newQueries, new OptimizationConfiguration(
					buildConfiguration));
			reloadLog.queryAdded(query, buildConfiguartionName, parserID, user);
			getLogger().info(
					"Adding Queries. " + query + " for user "
							+ user.getUser().getName() + " done.");
			return newQueries;
		} catch (QueryParseException e) {
			getLogger().error("QueryAddError " + e.getMessage());
			throw e;
		} catch (QueryOptimizationException e) {
			getLogger().error("QueryOptimizationError " + e.getMessage());
			throw e;
		} catch (OpenFailedException e) {
			getLogger().error(
					"Query Init: Open Failed Exception " + e.getMessage());
			throw e;
		}
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
	public IPhysicalQuery addQuery(ILogicalOperator logicalPlan, ISession user,
			String buildConfigurationName) throws PlanManagementException {
		getLogger().info("Start adding Queries.");
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		try {
			QueryBuildConfiguration params = buildAndValidateQueryBuildConfigurationFromSettings(buildConfigurationName);
			ArrayList<ILogicalQuery> newQueries = new ArrayList<ILogicalQuery>();
			ILogicalQuery query = new Query(logicalPlan, params);
			query.setUser(user);
			SetOwnerVisitor visitor = new SetOwnerVisitor(query);
			AbstractTreeWalker.prefixWalk(logicalPlan, visitor);
			newQueries.add(query);
			List<IPhysicalQuery> addedQueries = addQueries(newQueries,
					new OptimizationConfiguration(params));
			return addedQueries.get(0);
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
	public IPhysicalQuery addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String buildConfigurationName)
			throws PlanManagementException {
		getLogger().info("Start adding Queries.");
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		try {
			QueryBuildConfiguration queryBuildConfiguration = buildAndValidateQueryBuildConfigurationFromSettings(buildConfigurationName);
			ArrayList<IPhysicalQuery> newQueries = new ArrayList<IPhysicalQuery>();
			IPhysicalQuery query = new PhysicalQuery(physicalPlan,
					queryBuildConfiguration);
			query.setUser(user);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			List<IPhysicalQuery> added = addQueries(newQueries, new OptimizationConfiguration(
					queryBuildConfiguration));
			return added.get(0);
		} catch (Exception e) {
			getLogger().error(
					"Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	private QueryBuildConfiguration buildAndValidateQueryBuildConfigurationFromSettings(
			String buildConfigurationName) throws QueryAddException {
		IQueryBuildConfiguration settings = getQueryBuildConfiguration(buildConfigurationName);
		if (settings == null) {
			throw new QueryAddException("Transformation Configuration "
					+ buildConfigurationName + " not found");
		}
		QueryBuildConfiguration config = new QueryBuildConfiguration(settings
				.getConfiguration().toArray(new IQueryBuildSetting<?>[0]));
		config = validateBuildParameters(config);
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#removeQuery
	 * (int)
	 */
	@Override
	public void removeQuery(int queryID, ISession caller)
			throws PlanManagementException {
		getLogger().info("Start remove a query (ID: " + queryID + ").");

		IPhysicalQuery queryToRemove = this.plan.getQuery(queryID);
		validateUserRight(queryToRemove, caller,
				ExecutorPermission.REMOVE_QUERY);
		if (queryToRemove != null && getOptimizer() != null) {
			try {
				executionPlanLock.lock();
				setExecutionPlan(getOptimizer().beforeQueryRemove(this,
						queryToRemove, this.executionPlan, null,
						getDataDictionary()));
				stopQuery(queryToRemove.getID(), caller);
				getLogger().info("Removing Query " + queryToRemove.getID());
				this.plan.removeQuery(queryToRemove.getID());
				getLogger().info("Removing Ownership " + queryToRemove.getID());
				queryToRemove.removeOwnerschip();
				dataDictionary.removeClosedSources();
				dataDictionary.removeClosedSinks();
				getLogger().debug(
						"Query " + queryToRemove.getID() + " removed.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_REMOVE, queryToRemove));
				if (queryToRemove.getLogicalQuery() != null) {
					dataDictionary.removeQuery(queryToRemove.getLogicalQuery(), caller);
					this.reloadLog.removeQuery(queryToRemove.getLogicalQuery().getQueryText());
				}
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

	@Override
	public void removeAllQueries() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#startQuery
	 * (int)
	 */
	@Override
	public void startQuery(int queryID, ISession caller) {
		IPhysicalQuery queryToStart = this.plan.getQuery(queryID);
		validateUserRight(queryToStart, caller, ExecutorPermission.START_QUERY);
		if (queryToStart.isOpened()) {
			getLogger().info("Query (ID: " + queryID + ") is already started.");
			return;
		}
		getLogger().info("Starting query (ID: " + queryID + ").");

		try {
			this.executionPlanLock.lock();
			setExecutionPlan(getOptimizer().beforeQueryStart(queryToStart,
					this.executionPlan));
			if (isRunning()) {
				queryToStart.open();
				getLogger().debug("Query " + queryID + " started.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_START, queryToStart));
			} else {
				throw new RuntimeException(
						"Scheduler not running. Query cannot be started");
			}
		} catch (Exception e) {
			getLogger().warn(
					"Query not started. An Error during optimizing occurd (ID: "
							+ queryID + ").");
			throw new RuntimeException(
					"Query not started. An Error during optimizing occurd (ID: "
							+ queryID + ").", e);
		} finally {
			this.executionPlanLock.unlock();
		}
	}

	@Override
	public List<IPhysicalQuery> startAllClosedQueries(ISession user) {
		executionPlanLock.lock();
		List<IPhysicalQuery> started = new LinkedList<IPhysicalQuery>();
		for (IPhysicalQuery q : plan.getQueries()) {
			if (!q.isOpened()) {
				startQuery(q.getID(), user);
				started.add(q);
			}
		}
		executionPlanLock.unlock();
		return started;
	}

	private void validateUserRight(IPhysicalQuery query, ISession caller,
			ExecutorPermission executorAction) {
		if (!(
		// User has right
		usrMgmt.hasPermission(caller, executorAction, "Query " + query.getID())
				||
				// User is owner
				query.getUser().equals(caller) ||
		// User has higher right
		usrMgmt.hasPermission(caller,
				ExecutorPermission.hasSuperAction(executorAction), null))) {
			throw new PermissionException("No Right to execute "
					+ executorAction + " on Query " + query.getID() + " for "
					+ caller.getUser().getName());
		}

	}

	private void validateUserRight(ISession caller,
			ExecutorPermission executorAction) {
		if (!(
		// User has right
		usrMgmt.hasPermission(caller, executorAction,
				ExecutorPermission.objectURI) ||
		// User has higher right
		usrMgmt.hasPermission(caller,
				ExecutorPermission.hasSuperAction(executorAction),
				ExecutorPermission.objectURI))) {
			throw new PermissionException("No Right to execute "
					+ executorAction + " for " + caller.getUser().getName());
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
	public void stopQuery(int queryID, ISession caller) {

		getLogger().info("Stopping query (ID: " + queryID + ").");

		IPhysicalQuery queryToStop = this.plan.getQuery(queryID);
		validateUserRight(queryToStop, caller, ExecutorPermission.STOP_QUERY);
		try {
			this.executionPlanLock.lock();
			setExecutionPlan(getOptimizer().beforeQueryStop(queryToStop,
					this.executionPlan));
			if (isRunning()) {
				queryToStop.close();
				getLogger().debug("Query " + queryID + " stopped.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_STOP, queryToStop));
			} else {
				throw new RuntimeException(
						"Scheduler not running. Query cannot be stopped");
			}
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
	public void reoptimize(IPhysicalQuery sender) {
		getLogger().info(
				"Reoptimize request by query (ID: " + sender.getID() + ").");

		try {
			if (sender instanceof IPhysicalQuery) {
				this.executionPlanLock.lock();
				setExecutionPlan(getOptimizer().reoptimize(this,
						(IPhysicalQuery) sender, this.executionPlan));

				getLogger().debug("Query " + sender.getID() + " reoptimized.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_REOPTIMIZE, sender));
			} else {
				getLogger().warn(
						"Query not reoptimized. Query type is not supported.");
				return;
			}
		} catch (Exception e) {
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
	public void reoptimizeRequest(IPhysicalPlan sender) {
		getLogger().info("Reoptimize request by plan.");

		if (sender instanceof PhysicalPlan) {
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
	public Collection<IPhysicalQuery> getQueries() {
		return Collections.unmodifiableCollection(this.plan.getQueries());
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
					schedulerStrategy, this.getExecutionPlan());
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
			setExecutionPlan(this.getOptimizer().beforeQueryMigration(this,
					new OptimizationConfiguration(ParameterDoRewrite.FALSE),
					getDataDictionary()));
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
	public IQueryBuildConfiguration getQueryBuildConfiguration(String name) {
		return queryBuildConfigs.get(name);
	}

	@Override
	public Map<String, IQueryBuildConfiguration> getQueryBuildConfigurations() {
		return queryBuildConfigs;
	}

	@Override
	public void overloadOccured(IAdmissionControl sender) {
		if (admissionReaction != null) {
			List<IPossibleExecution> possibilities = sender
					.getPossibleExecutions();
			IPossibleExecution execution = admissionReaction
					.react(possibilities);

			// Anfragen stoppen
			for (IPhysicalQuery query : execution.getStoppingQueries()) {
				// System.err.println("Stopping query : " + query);
				stopQuery(query.getID(), query.getUser());
			}
		}
	}

}