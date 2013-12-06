package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.CostModelService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.ServerExecutorService;

public class Helper {
	private static final Logger log = LoggerFactory.getLogger(Helper.class);

	public static String getId(ILogicalOperator operator) {
		String id = operator.getParameterInfos().get("id".toUpperCase());
		if (id != null) {
			return id.replace("'", "");
		}

		return null;
	}

	/**
	 * Determines if the target {@link ILogicalOperator} of any
	 * {@link LogicalSubscription} is not in a given collection of
	 * {@link IlogicalOperator}s.
	 * 
	 * @see LogicalSubscription#getTarget()
	 * @param operators
	 *            The given collection of {@link IlogicalOperator}s.
	 * @param subscriptions
	 *            A collection of {@link LogicalSubscription}s to be checked.
	 * @return true, if any target {@link ILogicalOperator} of
	 *         <code>subscriptions</code> is not in <code>operators</code>.
	 */
	public static boolean oneTargetNotInList(List<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {

		for (final LogicalSubscription subscription : subscriptions) {

			if (!operators.contains(subscription.getTarget()))
				return true;

		}
		return false;
	}

	public static ISession getActiveSession() {
		return UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
	}

	public static List<ILogicalOperator> allSubscriptions(ILogicalOperator target) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		if (target.getSubscriptions().isEmpty()) {
			operators.add(target);
		} else {
			for (LogicalSubscription sub : target.getSubscriptions()) {
				operators.addAll(allSubscriptions(sub.getTarget()));
			}
		}
		return operators;
	}

	public static ILogicalQuery wrapInLogicalQuery(ILogicalOperator plan, String name, String pqlText) {
		final ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(plan, true);
		logicalQuery.setName(name);
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setUser(getActiveSession());
		logicalQuery.setQueryText(pqlText);
		return logicalQuery;
	}

	public static ILogicalQuery copyQuery(ILogicalQuery query) {
		String pqlStatement = PQLGeneratorService.get().generatePQLStatement(query.getLogicalPlan());
		return Helper.getLogicalQuery(pqlStatement).get(0);
	}

	public static boolean isSourceOperator(ILogicalOperator op) {
		return op instanceof AccessAO || op instanceof StreamAO;
	}

	public static void addOperatorIds(ILogicalOperator query) {
		Collection<ILogicalOperator> operators = DistributionHelper.collectOperators(query);
		int id = 0;
		for (ILogicalOperator op : operators) {
			Map<String, String> params = op.getParameterInfos();
			params.put("id".toUpperCase(), "'" + id + "'");
			id++;
		}
	}

	public static ID convertToID(String elem) {
		try {
			final URI id = new URI(elem);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			log.error("Could not set id", ex);
			return null;
		}
	}

	// basierend auf einer tiefensuche
	public static void iterateThroughPlan(ILogicalOperator operator, List<ILogicalOperator> visitedOperators, Function<ILogicalOperator, Void> func) {
		if (visitedOperators.contains(operator))
			return;

		visitedOperators.add(operator);
		func.apply(operator);

		for (LogicalSubscription sub : operator.getSubscriptions()) {
			iterateThroughPlan(sub.getTarget(), visitedOperators, func);
		}
		for (LogicalSubscription sub : operator.getSubscribedToSource()) {
			iterateThroughPlan(sub.getTarget(), visitedOperators, func);
		}
	}

	public static IPhysicalQuery getPhysicalQuery(ILogicalQuery query, String transCfgName) {
		IQueryBuildConfigurationTemplate settings = ServerExecutorService.getServerExecutor().getQueryBuildConfiguration(transCfgName);
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(settings.getConfiguration());
		QueryBuildConfiguration config = new QueryBuildConfiguration(newSettings.toArray(new IQueryBuildSetting<?>[0]), transCfgName);
		config.getTransformationConfiguration().setVirtualTransformation(true);

		return ServerExecutorService.getServerExecutor().getCompiler().transform(query, config.getTransformationConfiguration(), getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()));
	}

	public static List<IPhysicalQuery> getPhysicalQuery(String pqlStatement, String transCfgName) {
		IQueryBuildConfigurationTemplate settings = ServerExecutorService.getServerExecutor().getQueryBuildConfiguration(transCfgName);
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(settings.getConfiguration());
		QueryBuildConfiguration config = new QueryBuildConfiguration(newSettings.toArray(new IQueryBuildSetting<?>[0]), transCfgName);
		config.getTransformationConfiguration().setVirtualTransformation(true);

		List<IPhysicalQuery> query = ServerExecutorService.getServerExecutor().getCompiler().translateAndTransformQuery(pqlStatement, "PQL", getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()), config.getTransformationConfiguration(), null);
		return query;
	}

	public static List<ILogicalQuery> getLogicalQuery(String pqlStatement) {
		List<IExecutorCommand> commands = ServerExecutorService.getServerExecutor().getCompiler().translateQuery(pqlStatement, "PQL", getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()), null);
		List<ILogicalQuery> list = Lists.newArrayList();
		for (IExecutorCommand q : commands) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();
				final List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(), operators);
				list.add(query);
			}
		}
		return list;
	}

	public static double getFreeMemory() {
		return new Long(Runtime.getRuntime().freeMemory()).doubleValue();
	}

	public static double getUsedMemory() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}

	public static double getMaxMemory() {
		return new Long(Runtime.getRuntime().maxMemory()).doubleValue();
	}

	@SuppressWarnings("unchecked")
	public static double getCpuCostTotal() {
		ICost<IPhysicalOperator> cost = null;
		for (IPhysicalQuery q : ServerExecutorService.getServerExecutor().getExecutionPlan().getQueries()) {
			if (cost == null)
				cost = CostModelService.get().estimateCost(q.getPhysicalChilds(), false);
			else {
				cost.merge(CostModelService.get().estimateCost(q.getPhysicalChilds(), false));
			}
		}

		if (cost == null) {
			return 0;
		}

		if (cost instanceof OperatorCost) {
			OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);
			return c.getCpuCost();
		}

		throw new RuntimeException("Did not expect this implementation of ICost: " + cost.getClass().getName());
	}

	public static ILogicalQuery transformToQueryWithTopAO(List<SubPlan> queryParts, String name) {
		final Collection<ILogicalOperator> sinks = collectSinks(queryParts);
		final TopAO topAO = generateTopAO(sinks);

		final ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(topAO, true);
		logicalQuery.setName(name);
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setUser(getActiveSession());
		logicalQuery.setQueryText(PQLGeneratorService.get().generatePQLStatement(topAO));

		return logicalQuery;
	}

	public static ILogicalQuery transformToQueryWithTopAO(ILogicalOperator operator, String name) {
		return transformToQueryWithTopAO(Lists.newArrayList(new SubPlan(operator)), name);
	}

	public static ILogicalQuery transformToQuery(ILogicalOperator operator, String name) {
		return wrapInLogicalQuery(operator, name, PQLGeneratorService.get().generatePQLStatement(operator));
	}

	private static Collection<ILogicalOperator> collectSinks(List<SubPlan> queryParts) {
		final Collection<ILogicalOperator> result = Lists.newArrayList();
		for (SubPlan queryPart : queryParts) {
			List<ILogicalOperator> sinks = queryPart.getSinks();
			for (ILogicalOperator sink : sinks) {
				if (sink.getSubscriptions().isEmpty())
					result.add(sink);
			}
			result.addAll(queryPart.getJxtaSinks());
		}
		return result;
	}

	private static TopAO generateTopAO(final Collection<ILogicalOperator> sinks) {
		final TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks) {
			topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
		}
		return topAO;
	}

	public static boolean containsJxtaOperators(ILogicalOperator logicalPlan) {
		SubPlan subplan = new SubPlan(DistributionHelper.collectOperators(logicalPlan).toArray(new ILogicalOperator[0]));
		return (!(subplan.findOperatorsByType(JxtaReceiverAO.class).isEmpty() && subplan.findOperatorsByType(JxtaSenderAO.class).isEmpty()));
	}

	public static boolean allSourcesAvailable(ILogicalOperator logicalPlan) {
		List<ILogicalOperator> sources = Lists.newArrayList();
		for (ILogicalOperator op : DistributionHelper.collectOperators(logicalPlan)) {
			if (op instanceof AccessAO || op instanceof StreamAO) {
				sources.add(op);
			}
		}

		boolean sourcesAvailable = true;

		if (!sources.isEmpty()) {
			log.debug("Sub query contains {} source operators", sources.size());
			log.debug("Checking if all sources are available");

			ISession session = getActiveSession();

			for (ILogicalOperator source : sources) {
				String sourceName = session.getUser().getName() + "." + source.getName();
				if (!DataDictionaryProvider.getDataDictionary(session.getTenant()).containsViewOrStream(sourceName, session)) {
					sourcesAvailable = false;
					break;
				}
			}
		}
		return sourcesAvailable;
	}
}
