/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionReaction;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.IExecutionSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.QueryAddException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.SLADictionary;
import de.uniol.inf.is.odysseus.core.server.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.core.server.util.SetOwnerVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.reloadlog.ReloadLog;

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

	private static final Logger LOG = LoggerFactory
			.getLogger(StandardExecutor.class);

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
	private final Map<IUser, List<IPhysicalQuery>> stoppedQueriesByAC = Maps
			.newHashMap();

	private Map<ILogicalQuery, QueryBuildConfiguration> queryBuildParameter = new HashMap<ILogicalQuery, QueryBuildConfiguration>();

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
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider#getInfos
	 * ()
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
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement.executor
	 * .configuration .ExecutionConfiguration)
	 */
	@Override
	protected void initializeIntern(ExecutionConfiguration configuration) {
		this.executionPlan.addReoptimizeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.configuration.
	 * IValueChangeListener
	 * #settingChanged(de.uniol.inf.is.odysseus.core.server.planmanagement
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
			QueryBuildConfiguration parameters)
			throws NoCompilerLoadedException, QueryParseException,
			OpenFailedException {
		LOG.debug("Translate Queries.");
		// translate query and build logical plans
		List<ILogicalQuery> queries = getCompiler().translateQuery(queryStr,
				parameters.getParserID(), user, getDataDictionary());
		LOG.trace("Number of queries: " + queries.size());
		String slaName = SLADictionary.getInstance().getUserSLA(user.getUser());
		SLA sla = SLADictionary.getInstance().getSLA(slaName);
		// create for each logical plan an intern query
		for (ILogicalQuery query : queries) {
			setQueryBuildParameters(query, parameters);
			query.setQueryText(queryStr);
			query.setUser(user);
			query.setParameter(SLA.class.getName(), sla);
			// this executor processes reoptimize requests
			if (query instanceof IPhysicalQuery) {
				((IPhysicalQuery) query).addReoptimizeListener(this);
			}
		}

		return queries;
	}

	private void setQueryBuildParameters(ILogicalQuery query,
			QueryBuildConfiguration parameters) {
		queryBuildParameter.put(query, parameters);

	}

	/**
	 * Optimize new queries and set the resulting execution plan. After setting
	 * the execution plan all new queries are stored in the global queries
	 * storage ({@link IExecutionPlan}).
	 * 
	 * @param newQueries
	 *            Queries to process.
	 * @throws NoOptimizerLoadedException
	 *             No optimizer is set.
	 * @throws QueryOptimizationException
	 *             An exception during optimization occurred.
	 */
	private Collection<IPhysicalQuery> addQueries(
			List<ILogicalQuery> newQueries, OptimizationConfiguration conf)
			throws NoOptimizerLoadedException, QueryOptimizationException {
		LOG.debug("Optimize Queries. Count:" + newQueries.size());
		Collection<IPhysicalQuery> optimizedQueries = new ArrayList<IPhysicalQuery>();
		if (newQueries.isEmpty()) {
			return optimizedQueries;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			optimizedQueries = getOptimizer().optimize(this,
					getExecutionPlan(), newQueries, conf, getDataDictionary());
			executionPlanChanged();

			// store optimized queries

			for (IPhysicalQuery optimizedQuery : optimizedQueries) {
				optimizedQuery.addReoptimizeListener(this);
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_ADDED, optimizedQuery));
				if (optimizedQuery.getLogicalQuery() != null) {
					// TODO: Bisher k�nnen nur Namen von Configuration
					// gespeichert werden
					// es sollten aber echte Configs speicherbar sein!
					getDataDictionary().addQuery(
							optimizedQuery.getLogicalQuery(),
							optimizedQuery.getSession(), conf.getName());
				}
			}

		} catch (Exception e) {
			throw new QueryOptimizationException(e);
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}

		LOG.info("Queries added (Count: " + newQueries.size() + ").");
		return optimizedQueries;
	}

	private static List<IPhysicalQuery> addQueries(
			ArrayList<IPhysicalQuery> newQueries, OptimizationConfiguration conf) {
		throw new RuntimeException(
				"Adding physical query plans is currently not implemented");
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
			LOG.error("Error while using optimizer. Getting BufferplacementStrategy. "
					+ e.getMessage());
		} finally {
			this.executionPlanLock.unlock();
		}
		return null;
	}

	// ----------------------------------------------------------------------------------------------
	// ADD QUERY
	// ----------------------------------------------------------------------------------------------

	// -----------
	// QUERYSTRING
	// -----------

	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, String buildConfigurationName)
			throws PlanManagementException {
		return addQuery(query, parserID, user, buildConfigurationName, null);
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, String buildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException {
		LOG.info("Start adding Queries. " + query + " for user "
				+ user.getUser().getName());
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		QueryBuildConfiguration params = buildAndValidateQueryBuildConfigurationFromSettings(
				buildConfigurationName, overwriteSetting);
		params.set(new ParameterParserID(parserID));
		return addQuery(query, parserID, user, params);
	}

	private Collection<Integer> addQuery(String query, String parserID,
			ISession user, QueryBuildConfiguration buildConfiguration)
			throws PlanManagementException {
		try {
			List<ILogicalQuery> newQueries = createQueries(query, user,
					buildConfiguration);
			Collection<IPhysicalQuery> addedQueries = addQueries(newQueries,
					new OptimizationConfiguration(buildConfiguration));
			reloadLog.queryAdded(query, buildConfiguration.getName(), parserID,
					user);
			LOG.info("Adding Queries. " + query + " for user "
					+ user.getUser().getName() + " done.");
			Collection<Integer> createdQueries = new ArrayList<Integer>();
			for (IPhysicalQuery p : addedQueries) {
				createdQueries.add(p.getID());
			}
			return createdQueries;
		} catch (QueryParseException | QueryOptimizationException
				| OpenFailedException e) {
			LOG.error("Could not add query '" + query + "'", e);
			throw e;
		}
	}

	// -----------
	// LOGICALPLAN
	// -----------

	@Override
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			String buildConfigurationName) throws PlanManagementException {
		return addQuery(logicalPlan, user, buildConfigurationName, null);
	}

	@Override
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			String buildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException {
		LOG.info("Start adding Queries.");
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		QueryBuildConfiguration params = buildAndValidateQueryBuildConfigurationFromSettings(
				buildConfigurationName, overwriteSetting);
		return addQuery(logicalPlan, user, params);
	}

	private Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			QueryBuildConfiguration params) throws PlanManagementException {
		try {
			ArrayList<ILogicalQuery> newQueries = new ArrayList<ILogicalQuery>();
			int prio = 0;
			if (params != null) {
				prio = params.getPriority();
			}
			ILogicalQuery query = new LogicalQuery(logicalPlan, prio);
			query.setUser(user);
			SetOwnerVisitor visitor = new SetOwnerVisitor(query);
			AbstractTreeWalker.prefixWalk(logicalPlan, visitor);
			newQueries.add(query);
			Collection<IPhysicalQuery> addedQueries = addQueries(newQueries,
					new OptimizationConfiguration(params));
			return addedQueries.iterator().next().getID();
		} catch (Exception e) {
			LOG.error("Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	// ------------
	// PHYSICALPLAN
	// ------------

	@Override
	public Integer addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String buildConfigurationName)
			throws PlanManagementException {
		return addQuery(physicalPlan, user, buildConfigurationName, null);
	}

	@Override
	public Integer addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String buildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException {
		LOG.info("Start adding Queries.");
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		try {
			QueryBuildConfiguration queryBuildConfiguration = buildAndValidateQueryBuildConfigurationFromSettings(
					buildConfigurationName, overwriteSetting);
			ArrayList<IPhysicalQuery> newQueries = new ArrayList<IPhysicalQuery>();

			IPhysicalQuery query = new PhysicalQuery(physicalPlan,
					queryBuildConfiguration.getDefaultRoot(),
					queryBuildConfiguration.getDefaultRootStrategy());
			query.setSession(user);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			List<IPhysicalQuery> added = addQueries(newQueries,
					new OptimizationConfiguration(queryBuildConfiguration));
			return added.get(0).getID();
		} catch (Exception e) {
			LOG.error("Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	// -------------------------------------------------------------------------------------------------
	// Query Translation Settings
	// -------------------------------------------------------------------------------------------------

	private QueryBuildConfiguration buildAndValidateQueryBuildConfigurationFromSettings(
			String buildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws QueryAddException {
		IQueryBuildConfiguration settings = getQueryBuildConfiguration(buildConfigurationName);
		if (settings == null) {
			throw new QueryAddException("Transformation Configuration "
					+ buildConfigurationName + " not found");
		}
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(
				settings.getConfiguration());

		// TODO: Funktioniert das so???
		if (overwriteSetting != null) {
			for (IQueryBuildSetting<?> overwrite : overwriteSetting) {
				for (IQueryBuildSetting<?> setting : settings
						.getConfiguration()) {
					if (overwrite.getClass() == setting.getClass()) {
						newSettings.remove(setting);
						newSettings.add(overwrite);
					}
				}
			}
		}

		QueryBuildConfiguration config = new QueryBuildConfiguration(
				newSettings.toArray(new IQueryBuildSetting<?>[0]),
				buildConfigurationName);
		config = validateBuildParameters(config);
		return config;
	}

	// -------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------

	@Override
	public void removeQuery(int queryID, ISession caller)
			throws PlanManagementException {
		LOG.info("Start remove a query (ID: " + queryID + ").");

		IPhysicalQuery queryToRemove = this.executionPlan.getQueryById(queryID);
		validateUserRight(queryToRemove, caller,
				ExecutorPermission.REMOVE_QUERY);
		if (queryToRemove != null && getOptimizer() != null) {
			try {
				executionPlanLock.lock();
				getOptimizer().beforeQueryRemove(queryToRemove,
						this.executionPlan, null, getDataDictionary());
				executionPlanChanged();
				stopQuery(queryToRemove.getID(), caller);
				LOG.info("Removing Query " + queryToRemove.getID());
				this.executionPlan.removeQuery(queryToRemove.getID());
				LOG.info("Removing Ownership " + queryToRemove.getID());
				queryToRemove.removeOwnerschip();
				if (queryToRemove.getLogicalQuery() != null) {
					queryBuildParameter.remove(queryToRemove.getLogicalQuery());
				}
				dataDictionary.removeClosedSources();
				dataDictionary.removeClosedSinks();
				LOG.debug("Query " + queryToRemove.getID() + " removed.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_REMOVE, queryToRemove));
				if (queryToRemove.getLogicalQuery() != null) {
					dataDictionary.removeQuery(queryToRemove.getLogicalQuery(),
							caller);
					this.reloadLog.removeQuery(queryToRemove.getLogicalQuery()
							.getQueryText());
				}
			} catch (Exception e) {
				LOG.warn("Query not removed. An Error while optimizing occurd (ID: "
						+ queryID + ").");
				throw new PlanManagementException(e);
			} finally {
				executionPlanLock.unlock();
			}
		}
	}

	@Override
	public boolean removeAllQueries(ISession caller) {
		boolean success = true;
		for (IPhysicalQuery q : executionPlan.getQueries()) {
			try {
				removeQuery(q.getID(), caller);
			} catch (Throwable throwable) {
				LOG.error("Exception during stopping query " + q.getID()
						+ " caller " + caller.getId(), throwable);
				success = false;
			}
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager
	 * #startQuery (int)
	 */
	@Override
	public void startQuery(int queryID, ISession caller) {
		IPhysicalQuery queryToStart = this.executionPlan.getQueryById(queryID);
		validateUserRight(queryToStart, caller, ExecutorPermission.START_QUERY);
		if (queryToStart.isOpened()) {
			LOG.info("Query (ID: " + queryID + ") is already started.");
			return;
		}

		if (admissionControl != null) {
			if (!admissionControl.canStartQuery(queryToStart)) {
				LOG.error("Could not start query since it will potencially overload the system");
				throw new RuntimeException(
						"Query due of admission control not started");
			}
		}
		LOG.info("Starting query (ID: " + queryID + ").");

		try {
			this.executionPlanLock.lock();
			getOptimizer().beforeQueryStart(queryToStart, this.executionPlan);
			executionPlanChanged();
			queryToStart.open();
			LOG.debug("Query " + queryID + " started.");
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_START, queryToStart));
		} catch (Exception e) {
			LOG.warn("Query not started. An Error during optimizing occurd (ID: "
					+ queryID + ").");
			throw new RuntimeException(
					"Query not started. An Error during optimizing occurd (ID: "
							+ queryID + ").", e);
		} finally {
			this.executionPlanLock.unlock();
		}
	}

	@Override
	public Collection<Integer> startAllClosedQueries(ISession user) {
		executionPlanLock.lock();
		List<Integer> started = new LinkedList<Integer>();
		for (IPhysicalQuery q : executionPlan.getQueries()) {
			if (!q.isOpened()) {
				startQuery(q.getID(), user);
				started.add(q.getID());
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
				query.isOwner(caller) ||
		// User has higher right
		usrMgmt.hasPermission(caller,
				ExecutorPermission.hasSuperAction(executorAction),
				ExecutorPermission.objectURI))) {
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
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager
	 * #stopQuery (int)
	 */
	@Override
	public void stopQuery(int queryID, ISession caller) {

		LOG.info("Stopping query (ID: " + queryID + ").");

		IPhysicalQuery queryToStop = this.executionPlan.getQueryById(queryID);
		validateUserRight(queryToStop, caller, ExecutorPermission.STOP_QUERY);
		try {
			this.executionPlanLock.lock();
			getOptimizer().beforeQueryStop(queryToStop, this.executionPlan);
			executionPlanChanged();
			if (isRunning()) {
				queryToStop.close();
				LOG.debug("Query " + queryID + " stopped.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_STOP, queryToStop));
			} else {
				throw new RuntimeException(
						"Scheduler not running. Query cannot be stopped");
			}
		} catch (Exception e) {
			LOG.warn("Query not stopped. An Error while optimizing occurd (ID: "
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.query.
	 * IQueryReoptimizeListener
	 * #reoptimize(de.uniol.inf.is.odysseus.core.server.
	 * planmanagement.query.IQuery)
	 */
	@Override
	public void reoptimize(IPhysicalQuery sender) {
		LOG.info("Reoptimize request by query (ID: " + sender.getID() + ").");

		try {
			this.executionPlanLock.lock();
			getOptimizer().reoptimize(sender, this.executionPlan);
			executionPlanChanged();

			LOG.debug("Query " + sender.getID() + " reoptimized.");
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_REOPTIMIZE, sender));
		} catch (Exception e) {
			LOG.warn("Query not reoptimized. An Error while optimizing occurd (ID: "
					+ sender.getID() + ").");
			return;
		} finally {
			this.executionPlanLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.plan.
	 * IPlanReoptimizeListener #
	 * reoptimizeRequest(de.uniol.inf.is.odysseus.core.
	 * server.planmanagement.plan.IPlan )
	 */
	@Override
	public void reoptimizeRequest(IExecutionPlan sender) {
		LOG.info("Reoptimize request by plan.");

		if (sender instanceof ExecutionPlan) {
			try {
				this.executionPlanLock.lock();
				getOptimizer().reoptimize(this.executionPlan);
				executionPlanChanged();
				LOG.debug("Plan reoptimized.");
				firePlanModificationEvent(new PlanModificationEvent(this,
						PlanModificationEventType.PLAN_REOPTIMIZE,
						this.executionPlan));
			} catch (Exception e) {
				LOG.warn("Plan not reoptimized. An Error while optimizing occurd.");
				return;
			} finally {
				this.executionPlanLock.unlock();
			}
		} else {
			LOG.warn("Plan not reoptimized. Plan type is not supported.");
			return;
		}

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
			LOG.error("Error while using optimizer. Getting BufferplacementStrategies. "
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
			LOG.error("Error while using schedulerManager. Getting SchedulingStrategyFactories. "
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
			LOG.error("Error while using schedulerManager. Getting SchedulingFactories. "
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
			LOG.error("Error while using schedulerManager. Setting Scheduler. "
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
			LOG.error("Error while using schedulerManager. Getting Active Scheduling Strategy. "
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
			LOG.error("Error while using schedulerManager. Getting Active Scheduler. "
					+ e.getMessage());
		}
		return null;
	}

	@Override
	public IScheduler getCurrentScheduler() {
		try {
			return getSchedulerManager().getActiveScheduler();
		} catch (SchedulerException e) {
			LOG.error("Error while using schedulerManager. Getting Active Scheduler. "
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
		overloadUserOccured(sender, null);
	}

	@Override
	public void underloadOccured(IAdmissionControl sender) {
		underloadUserOccured(sender, null);
	}

	@Override
	public void overloadUserOccured(IAdmissionControl sender, IUser user) {
		if (admissionReaction != null) {
			List<IPossibleExecution> possibilities = sender
					.getPossibleExecutions(user);
			IPossibleExecution execution = admissionReaction
					.react(possibilities);

			for (IPhysicalQuery query : execution.getStoppingQueries()) {
				try {
					stopQuery(query.getID(), query.getSession());

					IUser usr = query.getSession().getUser();
					if (stoppedQueriesByAC.containsKey(usr)) {
						stoppedQueriesByAC.get(usr).add(query);
					} else {
						List<IPhysicalQuery> queries = Lists.newArrayList();
						queries.add(query);
						stoppedQueriesByAC.put(usr, queries);
					}
				} catch (Throwable t) {
					LOG.error("Could not stop query {] by admission control",
							query.getID(), t);
				}
			}
		}
	}

	@Override
	public void underloadUserOccured(IAdmissionControl sender, IUser user) {
		if (!stoppedQueriesByAC.isEmpty()) {
			Collection<IPhysicalQuery> stoppedQueries = determineStoppedQueries(
					user, stoppedQueriesByAC);
			for (IPhysicalQuery stoppedQuery : stoppedQueries) {
				try {
					if (!stoppedQuery.isOpened()) {
						startQuery(stoppedQuery.getID(),
								stoppedQuery.getSession());
					}

					IUser usr = stoppedQuery.getSession().getUser();
					if (stoppedQueriesByAC.containsKey(usr)) {
						stoppedQueriesByAC.get(usr).remove(stoppedQuery);
					}
				} catch (Throwable t) {
					LOG.error(
							"Could not start query {} which was stopped by admission control",
							stoppedQuery.getID(), t);
				}
			}
		}
	}

	@Override
	public QueryBuildConfiguration getBuildConfigForQuery(ILogicalQuery query) {
		return queryBuildParameter.get(query);
	}

	@Override
	public Collection<Integer> getLogicalQueryIds() {
		Collection<Integer> result = new ArrayList<Integer>();
		for (IPhysicalQuery pq : getExecutionPlan().getQueries()) {
			result.add(pq.getID());
		}
		return result;
	}

	@Override
	public SDFSchema getOutputSchema(int queryId) {
		return getLogicalQueryById(queryId).getLogicalPlan().getOutputSchema();
	}

	private static List<IPhysicalQuery> determineStoppedQueries(IUser user,
			Map<IUser, List<IPhysicalQuery>> stoppedQueries) {
		if (user != null) {
			return stoppedQueries.containsKey(user) ? stoppedQueries.get(user)
					: Lists.<IPhysicalQuery> newArrayList();
		}

		List<IPhysicalQuery> queries = Lists.newArrayList();
		for (IUser usr : stoppedQueries.keySet()) {
			queries.addAll(stoppedQueries.get(usr));
		}
		return queries;
	}
}