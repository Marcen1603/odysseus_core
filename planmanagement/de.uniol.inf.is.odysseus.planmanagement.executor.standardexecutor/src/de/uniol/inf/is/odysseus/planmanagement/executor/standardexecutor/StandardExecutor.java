package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.datastructure.Plan;
import de.uniol.inf.is.odysseus.planmanagement.executor.datastructure.Query;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.MultiQueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.QueryAddException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.MultiQueryEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.PlanEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.QueryEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.MultiQueryStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.PlanStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.QueryStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.EditableExecutionPlan;

public class StandardExecutor extends AbstractExecutor implements
		IAdvancedExecutor {

	@SuppressWarnings("unused")
	private void activate() {
		Iterator<String> iter;
		if (getRegisteredBufferPlacementStrategies() != null
				&& (iter = getRegisteredBufferPlacementStrategies().iterator()).hasNext()) {
			this.configuration.set(new SettingBufferPlacementStrategy(iter.next()));
		} else {
			this.configuration.set(new SettingBufferPlacementStrategy(null));
		}
	}

	@Override
	public void setDefaultBufferPlacementStrategy(String strategy) {
		this.configuration.set(new SettingBufferPlacementStrategy(strategy));
	}
	
	private AbstractPlanModificationEvent<?> processMultiQueryEvents(
			MultiQueryEndEvent eventArgs) {
		ArrayList<IEditableQuery> queries = eventArgs.getValue();

		if (queries != null && queries.size() > 0) {
			if (eventArgs.getID() == MultiQueryEndEvent.QUERIES_ADD) {
				for (IEditableQuery optimizedQuery : queries) {
					this.plan.addQuery(optimizedQuery);
				}

				this.logger.debug("Quies added (Count: " + queries.size()
						+ ").");

				return new MultiQueryPlanModificationEvent(this,
						MultiQueryPlanModificationEvent.QUERIES_ADD,
						new ArrayList<IQuery>(queries));
			}
		}
		return null;
	}

	private AbstractPlanModificationEvent<?> processPlanEvents(
			PlanEndEvent eventArgs) {
		if (eventArgs.getID() == PlanEndEvent.PLAN_REOPTIMIZE) {
			this.logger.debug("Plan reoptimized.");

			return new PlanModificationEvent(this,
					PlanModificationEvent.PLAN_REOPTIMIZE, this.plan);
		}
		return null;
	}

	private AbstractPlanModificationEvent<?> processQueryEvents(
			QueryEndEvent eventArgs) throws SchedulerException,
			OpenFailedException {

		String returnType = "";

		if (eventArgs.getID() == QueryEndEvent.QUERY_REMOVE) {
			this.plan.removeQuery(eventArgs.getValue().getID());
			eventArgs.getValue().removeOwnerschip();
			this.logger.debug("Query removed.");
			returnType = QueryPlanModificationEvent.QUERY_REMOVE;
		} else if (eventArgs.getID() == QueryEndEvent.QUERY_REOPTIMIZE) {
			this.logger.debug("Query reoptimized.");
			returnType = QueryPlanModificationEvent.QUERY_REOPTIMIZE;
		} else if (eventArgs.getID() == QueryEndEvent.QUERY_START) {
			eventArgs.getValue().start();
			if (isRunning()) {
				eventArgs.getValue().getSealedRoot().open();
			}
			this.logger.debug("Query started.");
			returnType = QueryPlanModificationEvent.QUERY_START;
		} else if (eventArgs.getID() == QueryEndEvent.QUERY_STOP) {
			eventArgs.getValue().stop();
			if (isRunning()) {
				eventArgs.getValue().getSealedRoot().close();
			}
			this.logger.debug("Query stopped.");
			returnType = QueryEndEvent.QUERY_STOP;
		}

		if (returnType != null && returnType.length() > 0) {
			return new QueryPlanModificationEvent(this, returnType, eventArgs
					.getValue());
		}
		return null;
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public String getInfos() {
		String infos = "Executor: " + this;

		infos += AppEnv.LINE_SEPERATOR + AppEnv.LINE_SEPERATOR + "Optimizer: ";
		try {
			infos += AppEnv.LINE_SEPERATOR + optimizer().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPERATOR + e.getMessage();
		}

		infos += AppEnv.LINE_SEPERATOR + AppEnv.LINE_SEPERATOR
				+ "SchedulerManager: ";
		try {
			infos += AppEnv.LINE_SEPERATOR + schedulerManager().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPERATOR + e.getMessage();
		}

		infos += AppEnv.LINE_SEPERATOR + AppEnv.LINE_SEPERATOR + "Compiler: ";

		try {
			infos += AppEnv.LINE_SEPERATOR + compiler().getInfos();
		} catch (Exception e) {
			infos += "not set. " + AppEnv.LINE_SEPERATOR + e.getMessage();
		}

		return infos;
	}

	@Override
	protected void initializeIntern(ExecutionConfiguration configuration) {
		this.plan = new Plan();
		((Plan) this.plan).addReoptimizeListener(this);
		this.executionPlan = new EditableExecutionPlan();
	}

	@Override
	public void settingChanged(AbstractExecutionSetting<?> newValueContainer) {
	}

	private ArrayList<IEditableQuery> createQueries(String query,
			String parserID, QueryBuildParameter parameters)
			throws NoCompilerLoadedException, QueryParseException,
			OpenFailedException {
		this.logger.debug("Tranlate Queries.");
		ArrayList<IEditableQuery> newQueries = new ArrayList<IEditableQuery>();
		Query newQuery = null;
		Collection<ILogicalOperator> logicalPlan = compiler().translateQuery(
				query, parserID);

		for (ILogicalOperator planOp : logicalPlan) {
			newQuery = new Query(parserID, parameters);
			newQuery.setLogicalPlan(planOp);
			newQuery.addReoptimizeListener(this);
			newQueries.add(newQuery);
		}

		return newQueries;
	}

	private void addQueries(ArrayList<IEditableQuery> newQueries)
			throws NoOptimizerLoadedException, QueryOptimizationException {
		this.logger.debug("Optimize Queries. Count:" + newQueries.size());
		optimizer().optimizeStartEvent(
				new MultiQueryStartEvent(this,
						MultiQueryStartEvent.QUERIES_ADD, newQueries,
						ParameterDoRestruct.TRUE));
	}

	private ArrayList<Integer> getQuerieIDs(ArrayList<IEditableQuery> newQueries) {
		ArrayList<Integer> newIDs = new ArrayList<Integer>();

		for (IEditableQuery query : newQueries) {
			newIDs.add(query.getID());
		}

		return newIDs;
	}

	private QueryBuildParameter getBuildParameter(
			AbstractQueryBuildParameter<?>... parameters) {		
		QueryBuildParameter params = new QueryBuildParameter(parameters);
		if (params.getTransformationConfiguration() == null) {
			params.set(new ParameterTransformationConfiguration(
					this.configuration
							.getSettingDefaultTransformationConfiguration()
							.getValue()));
		}

		if (params.getBufferPlacementStrategy() == null) {
			params.set(new ParameterBufferPlacementStrategy(
					 this.getBufferPlacementStrategy((String)this.configuration.get(
							SettingBufferPlacementStrategy.class).getValue())));
		}
		
		return params;
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException {
		this.logger.info("Start adding Queries.");
		try {
			QueryBuildParameter params = getBuildParameter(parameters);
			ArrayList<IEditableQuery> newQueries = createQueries(query,
					parserID, params);
			addQueries(newQueries);
			return getQuerieIDs(newQueries);
		} catch (Exception e) {
			this.logger.error("Error adding Queries. Details: "
					+ e.getMessage());
			throw new QueryAddException(e);
		}
	}

	@Override
	public int addQuery(ILogicalOperator logicalPlan,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException {
		this.logger.info("Start adding Queries.");
		try {
			QueryBuildParameter params = getBuildParameter(parameters);
			ArrayList<IEditableQuery> newQueries = new ArrayList<IEditableQuery>();
			Query query = new Query(logicalPlan, params);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			addQueries(newQueries);
			return query.getID();
		} catch (Exception e) {
			this.logger.error("Error adding Queries. Details: "
					+ e.getMessage());
			throw new QueryAddException(e);
		}
	}

	@Override
	public int addQuery(IPhysicalOperator physicalPlan,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException {
		this.logger.info("Start adding Queries.");
		try {
			QueryBuildParameter params = getBuildParameter(parameters);
			ArrayList<IEditableQuery> newQueries = new ArrayList<IEditableQuery>();
			Query query = new Query(physicalPlan, params);
			query.addReoptimizeListener(this);
			newQueries.add(query);
			addQueries(newQueries);
			return query.getID();
		} catch (Exception e) {
			this.logger.error("Error adding Queries. Details: "
					+ e.getMessage());
			throw new QueryAddException(e);
		}
	}

	@Override
	public void removeQuery(int queryID) throws PlanManagementException {
		this.logger.info("Start remove a query (ID: " + queryID + ").");

		Query removedQuery = (Query) this.plan.getQuery(queryID);

		if (removedQuery != null && optimizer() != null) {
			try {
				optimizer().optimizeStartEvent(
						new QueryStartEvent(this, QueryStartEvent.QUERY_REMOVE,
								removedQuery));
			} catch (QueryOptimizationException e) {
				this.logger
						.warn("Query not removed. An Error while optimizing occurd (ID: "
								+ queryID + ").");
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public void startQuery(int queryID) {
		this.logger.info("Start a query (ID: " + queryID + ").");

		Query queryToStart = (Query) this.plan.getQuery(queryID);

		try {
			optimizer().optimizeStartEvent(
					new QueryStartEvent(this, QueryStartEvent.QUERY_START,
							queryToStart));
		} catch (Exception e) {
			this.logger
					.warn("Query not started. An Error while optimizing occurd (ID: "
							+ queryID + ").");
			return;
		}
	}

	@Override
	public void stopQuery(int queryID) {
		this.logger.info("Stop a query (ID: " + queryID + ").");

		Query queryToStop = (Query) this.plan.getQuery(queryID);

		try {
			optimizer().optimizeStartEvent(
					new QueryStartEvent(this, QueryStartEvent.QUERY_STOP,
							queryToStop));
		} catch (Exception e) {
			this.logger
					.warn("Query not stopped. An Error while optimizing occurd (ID: "
							+ queryID + ").");
			return;
		}
	}

	@Override
	public void reoptimizeRequest(IQuery sender) {
		this.logger.info("Reoptimize request by query (ID: " + sender.getID()
				+ ").");

		try {
			if (sender instanceof Query) {
				optimizer().optimizeStartEvent(
						new QueryStartEvent(this,
								QueryStartEvent.QUERY_REOPTIMIZE,
								(Query) sender));
			} else {
				this.logger
						.warn("Query not reoptimized. Query is not supported.");
				return;
			}
		} catch (Exception e) {
			this.logger
					.warn("Query not reoptimized. An Error while optimizing occurd (ID: "
							+ sender.getID() + ").");
			return;
		}
	}

	@Override
	public void reoptimizeRequest(IPlan sender) {
		this.logger.info("Reoptimize request by plan.");

		try {
			if (sender instanceof Plan) {
				optimizer().optimizeStartEvent(
						new PlanStartEvent(this,
								PlanStartEvent.PLAN_REOPTIMIZE, (Plan) sender));
			} else {
				this.logger
						.warn("Query not reoptimized. Query is not supported.");
				return;
			}

		} catch (Exception e) {
			this.logger
					.warn("Plan not reoptimized. An Error while optimizing occurd.");
			return;
		}
	}

	@Override
	public void optimizeEndEvent(AbstractEndEvent<?> eventArgs)
			throws QueryOptimizationException {

		if (eventArgs.getNewExecutionPlan() != null) {
			AbstractPlanModificationEvent<?> newEventArgs = null;

			try {
				if (eventArgs instanceof MultiQueryEndEvent) {
					newEventArgs = processMultiQueryEvents((MultiQueryEndEvent) eventArgs);
				} else if (eventArgs instanceof PlanEndEvent) {
					newEventArgs = processPlanEvents((PlanEndEvent) eventArgs);
				} else if (eventArgs instanceof QueryEndEvent) {
					newEventArgs = processQueryEvents((QueryEndEvent) eventArgs);
				}

				if (!this.executionPlan.equals(eventArgs.getNewExecutionPlan())) {
					setExecutionPlan(eventArgs.getNewExecutionPlan());
				}

				if (newEventArgs != null) {
					firePlanModificationEvent(newEventArgs);
				}
			} catch (Exception e) {
				this.logger.error("Error while setting new execution plan. "
						+ e.getMessage());
				fireErrorEvent(new ErrorEvent(this, ErrorEvent.ERROR,
						"Error while setting new execution plan. "
								+ e.getMessage()));
			}
		}
	}

	@Override
	public ArrayList<IEditableQuery> getRegisteredQueries() {
		return this.plan.getEdittableQueries();
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategies() {
		try {
			return optimizer().getRegisteredBufferPlacementStrategies();
		} catch (NoOptimizerLoadedException e) {
			this.logger
					.error("Error while using optimizer. Getting BufferplacementStrategies. "
							+ e.getMessage());
		}
		return null;
	}
	
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy){
		try {
			return optimizer().getBufferPlacementStrategy(strategy);
		} catch (NoOptimizerLoadedException e) {
			this.logger
					.error("Error while using optimizer. Getting BufferplacementStrategy. "
							+ e.getMessage());
		}
		return null;		
	}
	
	@Override
	public Set<String> getRegisteredSchedulingStrategyFactories() {
		
		try {
			return schedulerManager().getSchedulingStrategy();
		} catch (SchedulerException e) {
			this.logger
			.error("Error while using schedulerManager. Getting SchedulingStrategyFactories. "
					+ e.getMessage());
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulerFactories() {
		
		try {
			return schedulerManager().getScheduler();
		} catch (SchedulerException e) {
			this.logger
			.error("Error while using schedulerManager. Getting SchedulingFactories. "
					+ e.getMessage());
		}
		return null;
	}
	
	@Override
	public void setScheduler(String scheduler, String schedulerStrategy) {
		try {
			schedulerManager().setActiveScheduler(scheduler, schedulerStrategy, this);
		} catch (SchedulerException e) {
			this.logger
			.error("Error while using schedulerManager. Setting Scheduler. "
					+ e.getMessage());
		}
	}
	
	@Override
	public String getCurrentSchedulingStrategy() {
		try {
			return schedulerManager().getActiveSchedulingStrategy();
		} catch (SchedulerException e) {
			this.logger
			.error("Error while using schedulerManager. Getting Active Scheduling Strategy. "
					+ e.getMessage());
		}
		return null;
	}
	
	@Override
	public String getCurrentScheduler() {
		try {
			return schedulerManager().getActiveScheduler();
		} catch (SchedulerException e) {
			this.logger
			.error("Error while using schedulerManager. Getting Active Scheduler. "
					+ e.getMessage());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable#getEditableExecutionPlan()
	 */
	@Override
	public IEditableExecutionPlan getEditableExecutionPlan() {
		return this.executionPlan;
	}
}