package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalCost;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class Helper {

	private static final Logger LOG = LoggerFactory.getLogger(Helper.class);

	private static IPhysicalCostModel physicalCostModel;
	private static ILogicalCostModel logicalCostModel;
	private static IServerExecutor executor;
	private static ISession session;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		physicalCostModel = serv;
	}

	// called by OSGi-DS
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if (physicalCostModel == serv) {
			physicalCostModel = null;
		}
	}

	// called by OSGi-DS
	public static void bindLogicalCostModel(ILogicalCostModel serv) {
		logicalCostModel = serv;
	}

	// called by OSGi-DS
	public static void unbindLogicalCostModel(ILogicalCostModel serv) {
		if (logicalCostModel == serv) {
			logicalCostModel = null;
		}
	}

	public static boolean oneTargetNotInList(List<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {
		for (final LogicalSubscription subscription : subscriptions) {
			if (!operators.contains(subscription.getTarget())) {
				return true;
			}
		}
		return false;
	}

	public static ISession getActiveSession() {
		if (session == null || !session.isValid()) {
			session = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return session;
	}

	public static IPhysicalQuery getPhysicalQuery(ILogicalQuery query, String transCfgName) {
		IQueryBuildConfigurationTemplate settings = executor.getQueryBuildConfiguration(transCfgName);
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(settings.getConfiguration());
		QueryBuildConfiguration config = new QueryBuildConfiguration(newSettings.toArray(new IQueryBuildSetting<?>[0]), transCfgName);
		config.getTransformationConfiguration().setVirtualTransformation(true);

		LogicalQueryHelper.replaceStreamAOs(LogicalQueryHelper.getAllOperators(query));
		return executor.getCompiler().transform(query, config.getTransformationConfiguration(), getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()));
	}

	public static List<ILogicalQuery> getLogicalQuery(String pqlStatement) {
		List<IExecutorCommand> commands = executor.getCompiler().translateQuery(pqlStatement, "PQL", getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()), Context.empty());
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

	public static boolean allSourcesAvailable(ILogicalOperator logicalPlan) {
		List<ILogicalOperator> sources = Lists.newArrayList();
		for (ILogicalOperator op : LogicalQueryHelper.getAllOperators(logicalPlan)) {
			if (op instanceof AccessAO || op instanceof StreamAO) {
				sources.add(op);
			}
		}

		boolean sourcesAvailable = true;

		if (!sources.isEmpty()) {
			LOG.debug("Sub query contains {} source operators", sources.size());
			LOG.debug("Checking if all sources are available");

			ISession session = getActiveSession();

			for (ILogicalOperator source : sources) {
				String sourceName = session.getUser().getName() + "." + source.getName();
				if (!DataDictionaryProvider.getDataDictionary(session.getTenant()).containsViewOrStream(sourceName, session)) {
					LOG.error("Source '{}' is not available here.", sourceName);
					sourcesAvailable = false;
					break;
				}
			}
		}
		return sourcesAvailable;
	}

	public static void insertDummyAOs(Collection<ILogicalQueryPart> queryParts, Map<ILogicalOperator, DetailCost> estimationMap) {
		Preconditions.checkNotNull(queryParts, "Query part list to insert dummy AOs  must not be null!");

		for (ILogicalQueryPart queryPart : queryParts) {

			Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
			for (ILogicalOperator relativeSink : relativeSinks) {
				for (LogicalSubscription subscription : relativeSink.getSubscriptions()) {
					if (queryPart.getOperators().contains(subscription.getTarget())) {
						continue;
					}

					ILogicalOperator srcOfAcceptor = subscription.getTarget();
					LogicalSubscription removingSubscription = RestructHelper.determineSubscription(relativeSink, srcOfAcceptor);

					relativeSink.unsubscribeSink(removingSubscription);

					DummyAO dummySender = new DummyAO();
					dummySender.setOutputSchema(relativeSink.getOutputSchema());

					DummyAO dummyReceiver = new DummyAO();
					dummyReceiver.setOutputSchema(relativeSink.getOutputSchema());
					dummyReceiver.setSchema(relativeSink.getOutputSchema().getAttributes());
					if (estimationMap.containsKey(relativeSink)) {
						double datarate = estimationMap.get(relativeSink).getDatarate();
						if( !Double.isInfinite(datarate)) {
							dummyReceiver.setDataRate(datarate);
						}
						double windowSize = estimationMap.get(relativeSink).getWindowSize();
						if(!Double.isInfinite(windowSize)) {
							dummyReceiver.setIntervalLength(windowSize);
						}
					} else {
						LOG.error("Could not get operator estimation for operator {}", relativeSink);
						dummyReceiver.setDataRate(1);
						dummyReceiver.setIntervalLength(1);
					}

					relativeSink.subscribeSink(dummySender, 0, removingSubscription.getSourceOutPort(), relativeSink.getOutputSchema());
					dummyReceiver.subscribeSink(srcOfAcceptor, removingSubscription.getSinkInPort(), 0, dummyReceiver.getOutputSchema());

					dummySender.connectWithDummySink(dummyReceiver);
					dummyReceiver.connectWithDummySource(dummySender);
				}
			}
		}
	}

	public static Map<ILogicalOperator, DetailCost> determineOperatorCostEstimations(Collection<ILogicalOperator> operators) {
		LOG.debug("Generating operator cost estimations");

		ILogicalCost logicalCost = logicalCostModel.estimateCost(operators);
		Map<ILogicalOperator, DetailCost> logicalOperatorEstimationMap = Maps.newHashMap();
		for (ILogicalOperator logicalOperator : logicalCost.getOperators()) {
			logicalOperatorEstimationMap.put(logicalOperator, logicalCost.getDetailCost(logicalOperator));
		}

		if (LOG.isDebugEnabled()) {
			for (ILogicalOperator op : logicalOperatorEstimationMap.keySet()) {
				LOG.debug("{} --> {}", op, logicalOperatorEstimationMap.get(op));
			}
		}
		return logicalOperatorEstimationMap;
	}
}
