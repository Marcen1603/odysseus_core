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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryFunction;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
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
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDistribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterQueryName;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQueryStarter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ACQueryParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.SLADictionary;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.core.server.util.SetOwnerVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.reloadlog.ReloadLog;

/**
 * StandardExecutor is the standard implementation of {@link IExecutor}. The
 * tasks of this object are:
 * 
 * - adding new queries - control scheduling, optimization and query processing
 * - send events of intern changes - providing execution informations -
 * providing and executing admission control reactions if possible
 * 
 * @author Wolf Bauer, Jonas Jacobi, Tobias Witt, Marco Grawunder, Dennis
 *         Geesen, Timo Michelsen (AC)
 */
public class StandardExecutor extends AbstractExecutor implements IQueryStarter {

	private static final Logger LOG = LoggerFactory
			.getLogger(StandardExecutor.class);

	private static StandardExecutor instance;

	private ReloadLog reloadLog;

	private Map<ILogicalQuery, QueryBuildConfiguration> queryBuildParameter = new HashMap<ILogicalQuery, QueryBuildConfiguration>();

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
		// Iterator<String> iter;
		// if (getRegisteredBufferPlacementStrategiesIDs() != null
		// && (iter = getRegisteredBufferPlacementStrategiesIDs()
		// .iterator()).hasNext()) {
		// this.configuration.set(new ParameterBufferPlacementStrategy(
		// getBufferPlacementStrategy(iter.next())));
		// } else {
		// this.configuration.set(new ParameterBufferPlacementStrategy());
		// }
		this.reloadLog = new ReloadLog();

		instance = this;
	}

	public void deactivate() {
		// Remove all queries
		LOG.debug("Removing all queries before shutdown");
		// TODO: What if queries are stored persistently?
		this.removeAllQueries(UserManagementProvider.getSessionmanagement()
				.loginSuperUser(null));
		instance = null;
	}

	public static StandardExecutor getInstance() {
		return instance;
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
	private List<IExecutorCommand> createQueries(String queryStr,
			ISession user, QueryBuildConfiguration parameters, Context context)
			throws NoCompilerLoadedException, QueryParseException,
			OpenFailedException {
		LOG.debug("Translating query into logical plans");
		// translate query and build logical plans
		IMetaAttribute metaAttribute = MetadataRegistry
				.getMetadataType(parameters.getTransformationConfiguration()
						.getDefaultMetaTypeSet());
		List<IExecutorCommand> commands = getCompiler().translateQuery(
				queryStr, parameters.getParserID(), user,
				getDataDictionary(user), context, metaAttribute);
		LOG.trace("Number of commands: " + commands.size());
		LOG.debug("Translation done.");
		annotateQueries(commands, queryStr, user, parameters);

		List<ILogicalQuery> queries = new LinkedList<>();

		// Splitt queries and commands
		Iterator<IExecutorCommand> iter = commands.iterator();
		while (iter.hasNext()) {
			IExecutorCommand cmd = iter.next();
			if (cmd instanceof CreateQueryCommand) {
				iter.remove();
				queries.add(((CreateQueryCommand) cmd).getQuery());
			}
		}

		executePreTransformationHandlers(user, parameters, queries, context);

		// Distribution handler only for queries
		if (parameters.get(ParameterDoDistribute.class).getValue()) {
			queries = distributeQueries(parameters, user, queries);
		}

		// Wrap queries again
		for (ILogicalQuery q : queries) {
			commands.add(new CreateQueryCommand(q, user));
		}

		if (parameters.get(ParameterDoDistribute.class).getValue()) {
			// Distributor could change queries, so they need to be annotated
			// again
			annotateQueries(commands, queryStr, user, parameters);
		}

		return commands;
	}

	private void executePreTransformationHandlers(ISession user,
			QueryBuildConfiguration parameters, List<ILogicalQuery> queries,
			Context context) throws QueryParseException {
		PreTransformationHandlerParameter preTransformationHandlerParameter = parameters
				.get(PreTransformationHandlerParameter.class);
		if (preTransformationHandlerParameter != null
				&& preTransformationHandlerParameter.hasPairs()) {
			List<PreTransformationHandlerParameter.Pair> pairs = preTransformationHandlerParameter
					.getPairs();

			for (ILogicalQuery query : queries) {
				for (PreTransformationHandlerParameter.Pair pair : pairs) {
					String handlerName = pair.name;
					List<String> handlerParameters = pair.parameters;

					try {
						IPreTransformationHandler handler = getPreTransformationHandler(handlerName);
						try {
							handler.preTransform(this, user, query, parameters,
									handlerParameters, context);
						} catch (Throwable t) {
							throw new QueryParseException(
									"PreTransformationHandler called '"
											+ handlerName
											+ "' throwed an exception", t);
						}
					} catch (InstantiationException | IllegalAccessException e) {
						throw new QueryParseException(
								"Could not use preTransformationHandler '"
										+ handlerName + "'", e);
					}
				}
			}
		}
	}

	private void annotateQueries(List<IExecutorCommand> queries,
			String queryStr, ISession user, QueryBuildConfiguration parameters) {
		String slaName = SLADictionary.getInstance().getUserSLA(user.getUser());
		SLA sla = SLADictionary.getInstance().getSLA(slaName);
		// create for each logical plan an intern query
		for (IExecutorCommand q : queries) {
			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();
				setQueryBuildParameters(query, parameters);

				ParameterQueryName queryName = parameters
						.get(ParameterQueryName.class);
				if (queryName != null && queryName.getValue() != null
						&& queryName.getValue().length() > 0) {
					query.setName(queryName.getValue());
				}

				if (Strings.isNullOrEmpty(query.getQueryText())) {
					query.setQueryText(queryStr);
				}

				if (Strings.isNullOrEmpty(query.getParserId())) {
					query.setParserId(parameters.getParserID());
				}

				query.setUser(user);
				query.setParameter(SLA.class.getName(), sla);

				if (parameters.getPriority() > 0) {
					query.setPriority(parameters.getPriority());
				}
				
				query.setParameter(ACQueryParameter.class.getSimpleName(), parameters.get(ACQueryParameter.class));

				// // this executor processes reoptimize requests
				// if (query instanceof IPhysicalQuery) {
				// ((IPhysicalQuery) query).addReoptimizeListener(this);
				// }
			}
		}
	}

	private List<ILogicalQuery> distributeQueries(
			QueryBuildConfiguration parameters, ISession caller,
			List<ILogicalQuery> queries) {
		LOG.debug("Beginning distribution of queries");
		if (hasQueryDistributor()) {
			LOG.debug("Using new way to distribute (peer)");

			// distribution is async. If there are local queries, the
			// distributer will call the executor explictly
			getQueryDistributor().distribute(this, caller, queries, parameters);
			return Lists.newArrayList();
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
			List<IExecutorCommand> newCommands, OptimizationConfiguration conf,
			ISession session) throws NoOptimizerLoadedException {
		LOG.debug("Starting Optimization of logical queries...");
		Collection<IPhysicalQuery> optimizedQueries = new ArrayList<IPhysicalQuery>();

		List<ILogicalQuery> newQueries = new LinkedList<>();

		// Execute commands
		for (IExecutorCommand cmd : newCommands) {
			// extract query and go the standard way
			if (cmd instanceof CreateQueryCommand) {
				newQueries.add(((CreateQueryCommand) cmd).getQuery());
			} else {
				// execute command
				LOG.debug("Executing " + cmd);
				// Remark: AddQueryCommand returns a set of query ids, that were
				// added
				// These ids must be returned to the first caller (i.e. calling
				// odysseus script)
				cmd.execute(getDataDictionary(cmd.getCaller()),
						(IUserManagementWritable) UserManagementProvider
								.getUsermanagement(true), this);
				Collection<Integer> result = cmd.getCreatedQueryIds();
				if (result != null) {
					for (Integer qId : result) {
						optimizedQueries.add(executionPlan.getQueryById(qId));
					}
				}
			}
		}

		// Only go on if there are queries left
		if (newQueries.isEmpty()) {
			return optimizedQueries;
		}

		// synchronize the process
		this.executionPlanLock.lock();
		try {
			// optimize queries and set resulting execution plan
			LOG.debug("Starting optimization and transformation for "
					+ newQueries.size() + " logical queries...");
			optimizedQueries = getOptimizer().optimize(this,
					getExecutionPlan(), newQueries, conf,
					getDataDictionary(session));
			LOG.debug("Optimization and transformation for  "
					+ newQueries.size() + " logical queries done.");
			LOG.debug("Changing execution plan for optimized queries...");
			executionPlanChanged(PlanModificationEventType.QUERY_ADDED,
					optimizedQueries);
			LOG.debug("Execution plan changed.");

			// store optimized queries

			for (IPhysicalQuery optimizedQuery : optimizedQueries) {
				optimizedQuery.addReoptimizeListener(this);
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_ADDED, optimizedQuery));
				if (optimizedQuery.getLogicalQuery() != null) {
					// TODO: Bisher k�nnen nur Namen von Configuration
					// gespeichert werden
					// es sollten aber echte Configs speicherbar sein!
					getDataDictionary(session).addQuery(
							optimizedQuery.getLogicalQuery(),
							optimizedQuery.getSession(), conf.getName());
				}
			}

		} catch (Exception e) {
			LOG.error("ERROR IN ADD QUERY ", e);
			throw e;
		} finally {
			// end synchronize of the process
			this.executionPlanLock.unlock();
		}
		LOG.debug("Optimization of logical queries done");
		return optimizedQueries;
	}

	private List<IPhysicalQuery> addQueries(
			ArrayList<IPhysicalQuery> newQueries,
			OptimizationConfiguration conf, ISession session) {
		// Work in Progress
		for (IPhysicalQuery q : newQueries) {
			if (q.getLogicalQuery() == null) {
				ILogicalQuery emptyLogicalQuery = new LogicalQuery();
				emptyLogicalQuery.setParserId("");
				emptyLogicalQuery.setQueryText("");
				emptyLogicalQuery.setName(q.getName());
				emptyLogicalQuery.setUser(q.getSession());
				emptyLogicalQuery.setLogicalPlan(new TopAO(), true);
				q.setLogicalQuery(emptyLogicalQuery);
			}
		}
		// throw new
		// RuntimeException("Adding physical query plans is currently not implemented");

		// synchronize the process
		this.executionPlanLock.lock();
		LOG.debug("Changing execution plan for optimized queries...");
		try {
			executionPlanChanged(PlanModificationEventType.QUERY_ADDED,
					newQueries);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.debug("Execution plan changed.");

		for (IPhysicalQuery query : newQueries) {
			query.addReoptimizeListener(this);

			if (query.getLogicalQuery() != null) {
				getDataDictionary(session).addQuery(query.getLogicalQuery(),
						query.getSession(), conf.getName());
			}
			// add the queries by themselves instead in bulk, the rcp-view
			// doesn't update properly otherwise
			getExecutionPlan().addQuery(query);
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_ADDED, query));
		}
		// TODO: maybe the physical plan could be optimized further,
		// in which case it should be run through the QuerySharing-Optimizer
		// For now, just leave the operators alone and add the queries to the
		// execution-plan as is.
		// Adding the new Queries to the execution-plan would normally be done
		// by the optimizer,
		// but since we don't need the transformation of a logical query in this
		// case, we do it here instead.

		this.executionPlanLock.unlock();

		LOG.debug("Optimization of logical queries done");
		return newQueries;

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
	public IBufferPlacementStrategy getBufferPlacementStrategy(String stratID,
			ISession session) {
		// TODO: Check rights
		return getBufferPlacementStrategy(stratID);
	};

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
			ISession user, String buildConfigurationName, Context context)
			throws PlanManagementException {
		return addQuery(query, parserID, user, buildConfigurationName, context,
				null);
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, Context context) throws PlanManagementException {
		return addQuery(query, parserID, user, "Standard", context, null);
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, String buildConfigurationName, Context context,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException {
		LOG.debug("Adding textual query using " + parserID + " for user "
				+ user.getUser().getName() + "...");
		LOG.debug("Adding following query: " + query);
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		QueryBuildConfiguration params = buildAndValidateQueryBuildConfigurationFromSettings(
				buildConfigurationName, overwriteSetting, context);
		params.set(new ParameterParserID(parserID));
		return addQuery(query, parserID, user, params, context);
	}

	private Collection<Integer> addQuery(String query, String parserID,
			ISession user, QueryBuildConfiguration buildConfiguration,
			Context context) throws PlanManagementException {
		try {
			List<IExecutorCommand> newQueries = createQueries(query, user,
					buildConfiguration, context);

			if (newQueries != null && !newQueries.isEmpty()) {
				Collection<IPhysicalQuery> addedQueries = addQueries(
						newQueries, new OptimizationConfiguration(
								buildConfiguration), user);
				reloadLog.queryAdded(query, buildConfiguration.getName(),
						parserID, user);
				Collection<Integer> createdQueries = new ArrayList<Integer>();
				for (IPhysicalQuery p : addedQueries) {
					createdQueries.add(p.getID());
				}
				LOG.info("Adding textual query using " + parserID
						+ " for user " + user.getUser().getName() + " done.");
				// LOG.debug("Added the following query: " + query);
				return createdQueries;
			}
			LOG.info("Adding textual query using " + parserID + " for user "
					+ user.getUser().getName() + " done.");
			return Lists.newArrayList();
		} catch (Exception e) {
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
		LOG.info("Start adding a logical query plan!");
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		QueryBuildConfiguration params = buildAndValidateQueryBuildConfigurationFromSettings(
				buildConfigurationName, overwriteSetting, Context.empty());
		return addQuery(logicalPlan, user, params);
	}

	private Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			QueryBuildConfiguration params) throws PlanManagementException {
		try {
			ArrayList<IExecutorCommand> newQueries = new ArrayList<>();
			int prio = 0;
			if (params != null) {
				prio = params.getPriority();
			}
			ILogicalQuery query = new LogicalQuery(logicalPlan, prio);
			setQueryName(params, query);
			query.setUser(user);
			SetOwnerVisitor visitor = new SetOwnerVisitor(query);
			AbstractTreeWalker.prefixWalk(logicalPlan, visitor);
			CreateQueryCommand cmd = new CreateQueryCommand(query, user);
			newQueries.add(cmd);
			setQueryBuildParameters(query, params);

			annotateQueries(newQueries, "", user, params);
			Collection<IPhysicalQuery> addedQueries = addQueries(newQueries,
					new OptimizationConfiguration(params), user);
			return addedQueries.iterator().next().getID();
		} catch (Exception e) {
			LOG.error("Error adding Queries ", e);
			throw new QueryAddException(e);
		}
	}

	private void setQueryName(QueryBuildConfiguration params,
			ILogicalQuery query) {
		if (params != null) {
			ParameterQueryName queryName = params.get(ParameterQueryName.class);
			if (queryName != null && queryName.getValue() != null
					&& queryName.getValue().length() > 0) {
				query.setName(queryName.getValue());
			}
		}
	}

	// ------------
	// PHYSICALPLAN
	// ------------

	@Override
	public Integer addIdenticalQuery(Integer idOfRunningQuery, ILogicalQuery q,
			ISession user, String confName) {
		IPhysicalQuery oldQuery = this.getExecutionPlan().getQueryById(
				idOfRunningQuery);
		List<IPhysicalOperator> oldOps = new ArrayList<IPhysicalOperator>();
		oldOps.addAll(oldQuery.getAllOperators());
		IPhysicalQuery newQuery = new PhysicalQuery(oldOps);
		newQuery.setLogicalQueryAndAdoptItsID(q);
		newQuery.setSession(user);
		newQuery.addReoptimizeListener(this);
		this.executionPlanLock.lock();
		List<IPhysicalQuery> queries = new ArrayList<IPhysicalQuery>();
		queries.add(newQuery);
		LOG.debug("Adding identical Query");
		getDataDictionary(user).addQuery(newQuery.getLogicalQuery(),
				newQuery.getSession(), confName);
		getExecutionPlan().addQuery(newQuery);
		firePlanModificationEvent(new QueryPlanModificationEvent(this,
				PlanModificationEventType.QUERY_ADDED, newQuery));
		this.executionPlanLock.unlock();
		return newQuery.getID();
	}

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
		LOG.info("Start adding a physical query plan!");
		validateUserRight(user, ExecutorPermission.ADD_QUERY);
		try {
			QueryBuildConfiguration queryBuildConfiguration = buildAndValidateQueryBuildConfigurationFromSettings(
					buildConfigurationName, overwriteSetting, null);
			ArrayList<IPhysicalQuery> newQueries = new ArrayList<IPhysicalQuery>();

			IPhysicalQuery query = new PhysicalQuery(physicalPlan);
			query.setSession(user);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			List<IPhysicalQuery> added = addQueries(newQueries,
					new OptimizationConfiguration(queryBuildConfiguration),
					user);
			return added.get(0).getID();
		} catch (Exception e) {
			LOG.error("Error adding Queries. Details: " + e.getMessage());
			throw new QueryAddException(e);
		}
	}

	// -------------------------------------------------------------------------------------------------
	// Deliver Schema information
	// -------------------------------------------------------------------------------------------------

	@Override
	public SDFSchema determineOutputSchema(String query, String parserID,
			ISession user, int port, Context context) {
		if (context == null) {
			context = Context.empty();
		}
		context.put("tempQuery", true);
		// FIXME: Add metaAttribute
		List<IExecutorCommand> commands = getCompiler().translateQuery(query,
				parserID, user, getDataDictionary(user), context, null);
		context.remove("tempQuery");
		if (commands.size() != 1) {
			throw new IllegalArgumentException(
					"Method can only be called for one query statement!");
		}
		IExecutorCommand cmd = commands.get(0);
		if (cmd instanceof CreateQueryCommand) {
			CreateQueryCommand qCmd = (CreateQueryCommand) cmd;
			ILogicalOperator root = qCmd.getQuery().getLogicalPlan();
			return root.getOutputSchema(port);
		}
		return null;
	}

	// -------------------------------------------------------------------------------------------------
	// Query Translation Settings
	// -------------------------------------------------------------------------------------------------

	private QueryBuildConfiguration buildAndValidateQueryBuildConfigurationFromSettings(
			String buildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting, Context context)
			throws QueryAddException {
		if (buildConfigurationName == null) {
			buildConfigurationName = "Standard";
		}
		IQueryBuildConfigurationTemplate settings = getQueryBuildConfiguration(buildConfigurationName);

		// Could not happend
		if (settings == null) {
			throw new QueryAddException("Transformation Configuration "
					+ buildConfigurationName + " not found");
		}
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(
				settings.getConfiguration());

		if (overwriteSetting != null) {

			for (IQueryBuildSetting<?> overwrite : overwriteSetting) {
				for (IQueryBuildSetting<?> setting : settings
						.getConfiguration()) {
					if (overwrite.getClass() == setting.getClass()) {
						newSettings.remove(setting);
					}
					newSettings.add(overwrite);
				}
			}
		}

		if (context != null) {
			@SuppressWarnings("unchecked")
			List<String> activeRules = (ArrayList<String>) context
					.get("ACTIVATEREWRITERULE");
			@SuppressWarnings("unchecked")
			List<String> inactiveRules = (ArrayList<String>) context
					.get("DEACTIVATEREWRITERULE");

			Set<String> rulesToApply = new HashSet<>();

			if (activeRules != null) {
				// First check if all rules should be activated to deactivated
				for (String s : activeRules) {
					if (s.equalsIgnoreCase("all")) {
						rulesToApply.addAll(getRewriteRules());
					}
				}
			}

			if (inactiveRules != null) {
				for (String s : inactiveRules) {
					if (s.equalsIgnoreCase("all")) {
						rulesToApply.clear();
					}
				}
			}

			if (activeRules != null) {
				// Now, activate or deactive single rule (must be done as
				// second!)
				for (String s : activeRules) {
					if (getRewriteRules().contains(s)) {
						rulesToApply.add(s);
					}
				}
			}

			if (inactiveRules != null) {
				for (String s : inactiveRules) {
					if (getRewriteRules().contains(s)) {
						rulesToApply.remove(s);
					}
				}
			}

			RewriteConfiguration rewriteConfig;
			// Only if one of the sets had values, the rule base should change
			// else use default rule base
			if (activeRules != null || inactiveRules != null) {
				rewriteConfig = new RewriteConfiguration(rulesToApply);
			} else {
				rewriteConfig = new RewriteConfiguration(null);
			}

			// Remove possible contained RewriteConfigurations
			Iterator<IQueryBuildSetting<?>> iter = newSettings.iterator();
			while (iter.hasNext()) {
				if (iter.next() instanceof RewriteConfiguration) {
					iter.remove();
				}
			}

			newSettings.add(rewriteConfig);

		}

		QueryBuildConfiguration config = new QueryBuildConfiguration(
				newSettings.toArray(new IQueryBuildSetting<?>[0]),
				buildConfigurationName);
		config.setExecutor(this);
		config.getTransformationConfiguration().setOption(
				IServerExecutor.class.getName(), this);

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
		if (queryToRemove != null) {
			removeQuery(caller, queryToRemove);
		} else {
			LOG.warn("Query with id " + queryID + " could not be found");
		}
	}

	@Override
	public void removeQuery(String queryName, ISession caller)
			throws PlanManagementException {
		LOG.info("Start remove a query (Name: " + queryName + ").");
		IPhysicalQuery queryToRemove = this.executionPlan
				.getQueryByName(queryName);
		if (queryToRemove != null) {
			removeQuery(caller, queryToRemove);
		} else {
			LOG.warn("Query with name " + queryName + " could not be found");
		}

	}

	private void removeQuery(ISession caller, IPhysicalQuery queryToRemove) {
		if (queryToRemove != null && getOptimizer() != null) {
			validateUserRight(queryToRemove, caller,
					ExecutorPermission.REMOVE_QUERY);
			try {
				executionPlanLock.lock();
				getOptimizer().beforeQueryRemove(queryToRemove,
						this.executionPlan, null, getDataDictionary(caller));

				if (!(queryToRemove.getState() == QueryState.INACTIVE)) {
					stopQuery(queryToRemove.getID(), caller);
				}

				executionPlanChanged(PlanModificationEventType.QUERY_REMOVE,
						queryToRemove);
				LOG.info("Removing Query " + queryToRemove.getID());
				this.executionPlan.removeQuery(queryToRemove.getID());
				LOG.debug("Removing Ownership " + queryToRemove.getID());
				queryToRemove.removeOwnerschip();
				// A query can now be without owner, but connected to a source
				// we need to removed all subscriptions of the physical
				// operators
				// that have no owners (but are potentially still connected!)
				List<IPhysicalOperator> ops = queryToRemove.getPhysicalChilds();
				for (IPhysicalOperator p : ops) {
					List<IOperatorOwner> toRemove = new ArrayList<>();
					if (!p.hasOwner()) {
						if (p.isSink()) {
							((ISink<?>) p).unsubscribeFromAllSources();
						}
						if (p.isSource()) {
							((ISource<?>) p).unsubscribeFromAllSinks();
						}
						for (Entry<IOperatorOwner, Resource> id : p
								.getUniqueIds().entrySet()) {
							
							getDataDictionary(caller).removeOperator(
									id.getValue());
							toRemove.add(id.getKey());
						}
					} else { // Remove ids from query sharing with this removed
								// query
						for (Entry<IOperatorOwner, Resource> id : p
								.getUniqueIds().entrySet()) {
							if (id.getKey().getID() == queryToRemove.getID()) {
								getDataDictionary(caller).removeOperator(
										id.getValue());
								toRemove.add(id.getKey());
							}
						}

					}
					for (IOperatorOwner id : toRemove) {
						p.removeUniqueId(id);
					}
				}
				if (queryToRemove.getLogicalQuery() != null) {
					queryBuildParameter.remove(queryToRemove.getLogicalQuery());
				}
				if (getDataDictionary(caller) != null) {
					getDataDictionary(caller).removeClosedSources();
					getDataDictionary(caller).removeClosedSinks();
				}
				LOG.info("Query " + queryToRemove.getID() + " removed.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_REMOVE, queryToRemove));
				if (queryToRemove.getLogicalQuery() != null) {
					if (getDataDictionary(caller) != null) {
						getDataDictionary(caller).removeQuery(
								queryToRemove.getLogicalQuery(), caller);
						this.reloadLog.removeQuery(queryToRemove
								.getLogicalQuery().getQueryText());
					}
				}
			} catch (Exception e) {
				LOG.warn("Query not removed. An Error while removing occurd (ID: "
						+ (queryToRemove!=null?queryToRemove.getID():"NULL" )+ ").");
				throw new PlanManagementException(e);
			} finally {
				executionPlanLock.unlock();
			}
		}
	}

	@Override
	public boolean removeAllQueries(ISession caller) {
		boolean success = true;
		List<IPhysicalQuery> queries = new ArrayList<>(
				executionPlan.getQueries());
		for (IPhysicalQuery q : queries) {
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
		if (queryToStart != null) {
			startQuery(caller, queryToStart);
		} else {
			LOG.error("Query with ID " + queryID + " could not be found");
		}
	}

	@Override
	public void startQuery(String queryName, ISession caller) {
		IPhysicalQuery queryToStart = this.executionPlan
				.getQueryByName(queryName);
		if (queryToStart != null) {
			startQuery(caller, queryToStart);
		} else {
			LOG.error("Query with name " + queryName + " could not be found");
		}
	}

	private void startQuery(ISession caller, IPhysicalQuery queryToStart) {
		synchronized (queryToStart) {
			validateUserRight(queryToStart, caller,
					ExecutorPermission.START_QUERY);
			if (queryToStart.isOpened() || queryToStart.isStarting()) {
				LOG.info("Query (ID: " + queryToStart.getID()
						+ ") is already started.");
				return;
			}

			LOG.info("Starting query (ID: " + queryToStart.getID() + ")...");

			try {
				this.executionPlanLock.lock();
				getOptimizer().beforeQueryStart(queryToStart,
						this.executionPlan);
				executionPlanChanged(PlanModificationEventType.QUERY_START,
						queryToStart);
				queryToStart.start(this);
				LOG.info("Query " + queryToStart.getID() + " started.");
				firePlanModificationEvent(new QueryPlanModificationEvent(this,
						PlanModificationEventType.QUERY_START, queryToStart));
			} catch (Exception e) {
				LOG.warn(
						"Query not started. An Error during optimizing occurd (ID: "
								+ queryToStart.getID() + ").", e);
				throw new RuntimeException(
						"Query not started. An Error during optimizing occurd (ID: "
								+ queryToStart.getID() + "). " + e.getMessage(),
						e);
			} finally {
				this.executionPlanLock.unlock();
			}

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
		UserManagementProvider.getUsermanagement(true).hasPermission(caller,
				executorAction, "Query " + query.getID())
				||
				// User is owner
				query.isOwner(caller) ||
		// User has higher right
		UserManagementProvider.getUsermanagement(true).hasPermission(caller,
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
		UserManagementProvider.getUsermanagement(true).hasPermission(caller,
				executorAction, ExecutorPermission.objectURI) ||
		// User has higher right
		UserManagementProvider.getUsermanagement(true).hasPermission(caller,
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

		LOG.info("Stopping query (ID: " + queryID + ")....");

		IPhysicalQuery queryToStop = this.executionPlan.getQueryById(queryID);
		validateUserRight(queryToStop, caller, ExecutorPermission.STOP_QUERY);
		stopQuery(queryToStop);
	}

	public void stopQuery(String queryID, ISession caller) {

		LOG.info("Stopping query (ID: " + queryID + ")....");

		IPhysicalQuery queryToStop = this.executionPlan.getQueryByName(queryID);
		validateUserRight(queryToStop, caller, ExecutorPermission.STOP_QUERY);
		stopQuery(queryToStop);
	}

	// for internal stopping
	void stopQuery(int queryID) {
		IPhysicalQuery queryToStop = this.executionPlan.getQueryById(queryID);
		stopQuery(queryToStop);
	}

	private void stopQuery(IPhysicalQuery queryToStop) {
		// There are two ways, a query can be stopped. By a scheduler or by the
		// executor. Run into this method only once, else there will be a
		// deadlock!
		if (!queryToStop.isMarkedAsStopping()) {
			queryToStop.setAsStopping(true);

			try {
				this.executionPlanLock.lock();
				getOptimizer().beforeQueryStop(queryToStop, this.executionPlan);
				executionPlanChanged(PlanModificationEventType.QUERY_STOP,
						queryToStop);
				if (isRunning()) {
					if (queryToStop.getState() != QueryState.INACTIVE) {
						queryToStop.stop();
					}
					LOG.info("Query "
							+ queryToStop.getID()
							+ " stopped. Execution time "
							+ (System.currentTimeMillis() - queryToStop
									.getQueryStartTS()) + " ms");
					firePlanModificationEvent(new QueryPlanModificationEvent(
							this, PlanModificationEventType.QUERY_STOP,
							queryToStop));
				}
			} catch (Exception e) {
				LOG.warn("Query not stopped. An Error while optimizing occurd (ID: "
						+ queryToStop.getID() + ")." + e.getMessage());
				throw new RuntimeException(e);
				// return;
			} finally {
				this.executionPlanLock.unlock();
			}
		}
	}

	@Override
	public void done(PhysicalQuery physicalQuery) {
		stopQuery(physicalQuery);
	}

	@Override
	public void suspendQuery(int queryID, ISession caller)
			throws PlanManagementException {
		LOG.info("Suspending query (ID: " + queryID + ")....");

		IPhysicalQuery queryToSuspend = this.executionPlan
				.getQueryById(queryID);
		suspendQuery(caller, queryToSuspend);
	}

	@Override
	public void suspendQuery(String queryID, ISession caller)
			throws PlanManagementException {
		LOG.info("Suspending query (ID: " + queryID + ")....");

		IPhysicalQuery queryToSuspend = this.executionPlan
				.getQueryByName(queryID);
		suspendQuery(caller, queryToSuspend);
	}

	private void suspendQuery(ISession caller, IPhysicalQuery queryToSuspend) {
		try {
			QueryState.next(queryToSuspend.getState(), QueryFunction.SUSPEND);
			validateUserRight(queryToSuspend, caller,
					ExecutorPermission.SUSPEND_QUERY);
			executionPlanChanged(PlanModificationEventType.QUERY_SUSPEND,
					queryToSuspend);

			if (isRunning()) {
				queryToSuspend.suspend();
			}
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_SUSPEND, queryToSuspend));

		} catch (Exception e) {
			LOG.warn("Query not suspended. An Error while optimizing occurd (ID: "
					+ queryToSuspend.getID() + ")." + e.getMessage());
			throw new RuntimeException(e);
			// return;
		}
	}

	@Override
	public void resumeQuery(int queryID, ISession caller)
			throws PlanManagementException {
		LOG.info("Resuming query (ID: " + queryID + ")....");

		IPhysicalQuery queryToResume = this.executionPlan.getQueryById(queryID);
		resumeQuery(caller, queryToResume);
	}

	@Override
	public void resumeQuery(String queryID, ISession caller)
			throws PlanManagementException {
		LOG.info("Resuming query (ID: " + queryID + ")....");

		IPhysicalQuery queryToResume = this.executionPlan
				.getQueryByName(queryID);
		resumeQuery(caller, queryToResume);
	}

	private void resumeQuery(ISession caller, IPhysicalQuery queryToResume) {
		try {
			QueryState.next(queryToResume.getState(), QueryFunction.RESUME);
			validateUserRight(queryToResume, caller,
					ExecutorPermission.RESUME_QUERY);
			executionPlanChanged(PlanModificationEventType.QUERY_RESUME,
					queryToResume);

			if (isRunning()) {
				queryToResume.resume();
			}
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_RESUME, queryToResume));

		} catch (Exception e) {
			LOG.warn("Query not suspended. An Error while optimizing occurd (ID: "
					+ queryToResume.getID() + ")." + e.getMessage());
			throw new RuntimeException(e);
			// return;
		}
	}

	@Override
	public void partialQuery(int queryID, int sheddingFactor, ISession caller)
			throws PlanManagementException {
		LOG.info("Set query (ID: " + queryID + ") to patial....");

		IPhysicalQuery queryToPartial = this.executionPlan
				.getQueryById(queryID);
		partialQuery(sheddingFactor, caller, queryToPartial);
	}

	@Override
	public void partialQuery(String queryID, int sheddingFactor, ISession caller)
			throws PlanManagementException {
		LOG.info("Set query (ID: " + queryID + ") to patial....");

		IPhysicalQuery queryToPartial = this.executionPlan
				.getQueryByName(queryID);
		partialQuery(sheddingFactor, caller, queryToPartial);
	}

	private void partialQuery(int sheddingFactor, ISession caller,
			IPhysicalQuery queryToPartial) {
		try {
			if (sheddingFactor > 0) {
				QueryState.next(queryToPartial.getState(),
						QueryFunction.PARTIAL);
			} else {
				QueryState.next(queryToPartial.getState(), QueryFunction.FULL);
			}
			validateUserRight(queryToPartial, caller,
					ExecutorPermission.PARTIAL_QUERY);
			executionPlanChanged(PlanModificationEventType.QUERY_PARTIAL,
					queryToPartial);

			if (isRunning()) {
				queryToPartial.partial(sheddingFactor);
			}
			firePlanModificationEvent(new QueryPlanModificationEvent(this,
					PlanModificationEventType.QUERY_PARTIAL, queryToPartial));

		} catch (Exception e) {
			LOG.warn("Query not in partial mode. An Error while optimizing occurd (ID: "
					+ queryToPartial.getID() + ")." + e.getMessage());
			throw new RuntimeException(e);
			// return;
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
		LOG.info("Reoptimizing request by query (ID: " + sender.getID()
				+ ")...");

		try {
			this.executionPlanLock.lock();
			getOptimizer().reoptimize(sender, this.executionPlan);
			executionPlanChanged(PlanModificationEventType.PLAN_REOPTIMIZE,
					(IPhysicalQuery) null);

			LOG.info("Query " + sender.getID() + " reoptimized.");
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
				executionPlanChanged(PlanModificationEventType.PLAN_REOPTIMIZE,
						(IPhysicalQuery) null);
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
	public Set<String> getRegisteredBufferPlacementStrategiesIDs(
			ISession session) {
		// TODO: Check access rights
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
	public Set<String> getRegisteredSchedulingStrategies(ISession session) {
		// TODO: Check access rights
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
	public Set<String> getRegisteredSchedulers(ISession session) {
		// TODO: Check access rights
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
	public void setScheduler(String scheduler, String schedulerStrategy,
			ISession session) {
		// TODO: Check access rights
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
	public String getCurrentSchedulingStrategyID(ISession user) {
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
	public String getCurrentSchedulerID(ISession session) {
		// TODO: Check rights
		try {
			return getSchedulerManager().getActiveSchedulerID();
		} catch (SchedulerException e) {
			LOG.error("Error while using schedulerManager. Getting Active Scheduler. "
					+ e.getMessage());
		}
		return null;
	}

	@Override
	public IScheduler getCurrentScheduler(ISession session) {
		return getCurrentScheduler();

	};

	protected IScheduler getCurrentScheduler() {
		try {
			return getSchedulerManager().getActiveScheduler();
		} catch (SchedulerException e) {
			LOG.error("Error while using schedulerManager. Getting Active Scheduler. "
					+ e.getMessage());
		}
		return null;
	}

	@Override
	public OptimizationConfiguration getOptimizerConfiguration(ISession session)
			throws NoOptimizerLoadedException {
		// TODO: Check rights
		return getOptimizerConfiguration();
	};

	protected OptimizationConfiguration getOptimizerConfiguration()
			throws NoOptimizerLoadedException {
		return this.getOptimizer().getConfiguration();
	}

	@Override
	public ISystemMonitor getDefaultSystemMonitor(ISession session)
			throws NoSystemMonitorLoadedException {
		// TODO: Checks rights
		return getDefaultSystemMonitor();
	}

	protected ISystemMonitor getDefaultSystemMonitor()
			throws NoSystemMonitorLoadedException {
		if (this.systemMonitorFactory == null) {
			throw new NoSystemMonitorLoadedException();
		}
		return this.defaultSystemMonitor;
	}

	@Override
	public ISystemMonitor newSystemMonitor(long period, ISession session)
			throws NoSystemMonitorLoadedException {
		// TODO: Check Rights
		return newSystemMonitor(period);
	};

	protected ISystemMonitor newSystemMonitor(long period)
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
		return "StandardExecutor";
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames(ISession session) {
		// TODO: Check access rights
		return queryBuildConfigs.keySet();
	}

	@Override
	public IQueryBuildConfigurationTemplate getQueryBuildConfiguration(
			String name) {
		return queryBuildConfigs.get(name);
	}

	@Override
	public Map<String, IQueryBuildConfigurationTemplate> getQueryBuildConfigurations() {
		return queryBuildConfigs;
	}

	@Override
	public QueryBuildConfiguration getBuildConfigForQuery(ILogicalQuery query) {
		// TODO: Check access rights;
		// ISession caller = query.getUser();
		return queryBuildParameter.get(query);
	}

	@Override
	public Collection<Integer> getLogicalQueryIds(ISession session) {
		Collection<Integer> result = new ArrayList<Integer>();
		for (IPhysicalQuery pq : getExecutionPlan().getQueries()) {
			// TODO: Show queries of other users...
			if (pq.getSession().getUser() == session.getUser()) {
				result.add(pq.getID());
			}
		}
		return result;
	}

	@Override
	public SDFSchema getOutputSchema(int queryId, ISession session) {
		return getLogicalQueryById(queryId, session).getLogicalPlan()
				.getOutputSchema();
	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure sp,
			ISession caller) {
		getDataDictionary(caller).addStoredProcedure(sp, caller);
	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		return getDataDictionary(caller).getStoredProcedure(name, caller);
	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {
		getDataDictionary(caller).removeStoredProcedure(name, caller);
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		return getDataDictionary(caller).getStoredProcedures(caller);
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		return getDataDictionary(caller).containsStoredProcedure(name, caller);
	}

	@Override
	public Collection<String> getRewriteRules() {
		ICompiler compiler = getCompiler();
		if (compiler != null) {
			return getCompiler().getRewriteRules();
		} else {
			return null;
		}
	}

}
