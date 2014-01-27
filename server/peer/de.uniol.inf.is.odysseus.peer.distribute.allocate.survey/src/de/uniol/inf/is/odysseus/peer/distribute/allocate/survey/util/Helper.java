package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class Helper {

	private static final Logger LOG = LoggerFactory.getLogger(Helper.class);

	@SuppressWarnings("rawtypes")
	private static OperatorCostModel costModel;
	private static IServerExecutor executor;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindCostModel(ICostModel<?> serv) {
		costModel = (OperatorCostModel<?>)serv;
	}

	// called by OSGi-DS
	public static void unbindCostModel(ICostModel<?> serv) {
		if (costModel == serv) {
			costModel = null;
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
		return UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
	}

	public static IPhysicalQuery getPhysicalQuery(ILogicalQuery query, String transCfgName) {
		IQueryBuildConfigurationTemplate settings = executor.getQueryBuildConfiguration(transCfgName);
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(settings.getConfiguration());
		QueryBuildConfiguration config = new QueryBuildConfiguration(newSettings.toArray(new IQueryBuildSetting<?>[0]), transCfgName);
		config.getTransformationConfiguration().setVirtualTransformation(true);

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
		for(ILogicalOperator op : LogicalQueryHelper.getAllOperators(logicalPlan)) {
			if(op instanceof AccessAO || op instanceof StreamAO) {
				sources.add(op);
			}
		}
		
		boolean sourcesAvailable = true;
		
		if(!sources.isEmpty()) {
			LOG.debug("Sub query contains {} source operators", sources.size());
			LOG.debug("Checking if all sources are available");
			
			ISession session = getActiveSession();
		
			for(ILogicalOperator source : sources) {
				String sourceName = session.getUser().getName() + "." + source.getName();
				if (!DataDictionaryProvider.getDataDictionary(session.getTenant())
						.containsViewOrStream(sourceName, session)) {
					sourcesAvailable = false;
					break;
				}
			}				
		}
		return sourcesAvailable;
	}
	
	public static void insertDummyAOs(Collection<ILogicalQueryPart> queryParts, Map<ILogicalOperator, OperatorEstimation<?>> estimationMap) {
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
					if( estimationMap.containsKey(relativeSink)) {
						dummyReceiver.setDataRate(estimationMap.get(relativeSink).getDataStream().getDataRate());
						dummyReceiver.setIntervalLength(estimationMap.get(relativeSink).getDataStream().getIntervalLength());
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
	
	@SuppressWarnings("unchecked")
	public static Map<ILogicalOperator, OperatorEstimation<?>> determineOperatorCostEstimations( Collection<ILogicalOperator> operators, String transCfgName ) {
		LOG.debug("Generating operator cost estimations");
		
		Map<String, ILogicalOperator> logicalOperatorIDMap = createLogicalOperatorIDMap(operators);
		
		IPhysicalQuery physicalQuery = getPhysicalQuery(wrapInLogicalQuery(operators), transCfgName);
		OperatorCost<IPhysicalOperator> costs = (OperatorCost<IPhysicalOperator>)costModel.estimateCost(physicalQuery.getPhysicalChilds(), false);
		Map<IPhysicalOperator, OperatorEstimation<IPhysicalOperator>> physicalOperatorEstimationMap = costs.getOperatorEstimations();
		
		Map<ILogicalOperator, OperatorEstimation<?>> logicalOperatorEstimationMap = Maps.newHashMap();
		for( IPhysicalOperator physicalOperator : physicalOperatorEstimationMap.keySet() ) {
			String idStr = physicalOperator.getParameterInfos().get("Id");
			if( !Strings.isNullOrEmpty(idStr)) {
				ILogicalOperator logicalOperator = logicalOperatorIDMap.get(idStr);
				
				if( logicalOperator != null ) {
					logicalOperatorEstimationMap.put(logicalOperator, physicalOperatorEstimationMap.get(physicalOperator));
				}
			}
		}
		
		if( LOG.isDebugEnabled()) {
			for( ILogicalOperator op : logicalOperatorEstimationMap.keySet() ) {
				LOG.debug("{} --> {}", op, logicalOperatorEstimationMap.get(op));
			}
		}
		return logicalOperatorEstimationMap;
	}

	private static Map<String, ILogicalOperator> createLogicalOperatorIDMap(Collection<ILogicalOperator> operators) {
		Map<String, ILogicalOperator> logicalOperatorIDMap = Maps.newHashMap();
		long currentID = 0;
		for( ILogicalOperator logicalOperator : operators ) {
			String idString = "'" + String.valueOf(currentID) + "'";
			logicalOperator.addParameterInfo("Id", idString);
			logicalOperatorIDMap.put(idString, logicalOperator);
			currentID++;
		}
		
		return logicalOperatorIDMap;
	}
	
	public static ILogicalQuery wrapInLogicalQuery(Collection<ILogicalOperator> ops) {
		
		Collection<ILogicalOperator> allOperators = LogicalQueryHelper.getAllOperators(ops.iterator().next());
		Collection<ILogicalOperator> sinks = LogicalQueryHelper.getSinks(allOperators);

		ILogicalOperator sinkOp;
		if( sinks.size() > 1 ) {
			TopAO topAO = new TopAO();
			int inputPort = 0;
			for (ILogicalOperator sink : sinks) {
				topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
			}
			sinkOp = topAO;
		} else {
			sinkOp = sinks.iterator().next();
		}

		ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(sinkOp, true);
		logicalQuery.setName("TmpQuery");
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setUser(getActiveSession());
		return logicalQuery;
	}
}
