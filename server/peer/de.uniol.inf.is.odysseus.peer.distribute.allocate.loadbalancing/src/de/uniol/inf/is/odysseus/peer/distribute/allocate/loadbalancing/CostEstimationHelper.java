package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.IOperatorGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public final class CostEstimationHelper {

	private static final Logger LOG = LoggerFactory.getLogger(CostEstimationHelper.class);
	
	private static IPhysicalCostModel costModel;
	private static IServerExecutor serverExecutor;
	private static ISession activeSession;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}

	// called by OSGi-DS
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		costModel = serv;
	}

	// called by OSGi-DS
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if (costModel == serv) {
			costModel = null;
		}
	}
	
	public static Map<ILogicalQueryPart, IPhysicalCost> determineQueryPartCosts(Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration transCfg) {
		LOG.debug("Estimating query part costs");
		
		LogicalQueryHelper.disconnectQueryParts(queryParts, new IOperatorGenerator() {
			
			@Override
			public void beginDisconnect(ILogicalQueryPart sourceQueryPart, ILogicalOperator sourceOperator, ILogicalQueryPart sinkQueryPart, ILogicalOperator sinkOperator) {
				
			}
			
			@Override
			public ILogicalOperator createSourceofSink(ILogicalQueryPart sinkQueryPart, ILogicalOperator sink) {
				return new DummyAO();
			}
			
			@Override
			public ILogicalOperator createSinkOfSource(ILogicalQueryPart sinkQueryPart, ILogicalOperator source) {
				return new DummyAO();
			}

			@Override
			public void endDisconnect() {
				
			}
		});

		Map<ILogicalQueryPart, IPhysicalCost> result = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			ILogicalOperator topAO = LogicalQueryHelper.appendTopAO(queryPart);
			ILogicalQuery logicalQuery = wrapInLogicalQuery(topAO, "logical query");
			IPhysicalQuery physicalQuery = getPhysicalQuery(logicalQuery, transCfg);
			
			IPhysicalCost costs = costModel.estimateCost(physicalQuery.getPhysicalChilds());
			
			result.put(queryPart, costs);
			LOG.debug("Query part {} costs {}", queryPart, costs);
		}
		return result;
	}
	
	private static ILogicalQuery wrapInLogicalQuery(ILogicalOperator plan, String name) {
		final ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(plan, true);
		logicalQuery.setName(name);
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setUser(getActiveSession());
		return logicalQuery;
	}

	private static IPhysicalQuery getPhysicalQuery(ILogicalQuery query, QueryBuildConfiguration transCfg) {
		return serverExecutor.getCompiler().transform(query, transCfg.getTransformationConfiguration(), getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()));
	}

	private static ISession getActiveSession() {
		if( activeSession == null && !activeSession.isValid()) {
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		} 
		return activeSession;
	}	
}
