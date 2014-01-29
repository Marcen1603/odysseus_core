package de.uniol.inf.is.odysseus.peer.distribute.postprocess;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

// TODO javaDoc M.B.
public class CalcLatencyPostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(CalcLatencyPostProcessor.class);
	
	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		CalcLatencyPostProcessor.p2pNetworkManager = serv;
		
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		if(CalcLatencyPostProcessor.p2pNetworkManager == serv) {
			
			CalcLatencyPostProcessor.p2pNetworkManager = null;
			
		}
		
	}
	
	@Override
	public String getName() {
		
		return "calclatency";
		
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, 
			ISession caller, 
			Map<ILogicalQueryPart, PeerID> allocationMap, 
			ILogicalQuery query, QueryBuildConfiguration config) {
		
		CalcLatencyPostProcessor.log.debug("Begin post processing");
		
		Collection<ILogicalOperator> calcLatencys = Lists.newArrayList();
		
		for(ILogicalQueryPart queryPart : allocationMap.keySet()) {
			
			CalcLatencyPostProcessor.log.debug("Determining real sinks from query part {} to insert CalcLatenyAOs", queryPart);
			
			Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
			
			for(ILogicalOperator relativeSink : relativeSinks) {
				
				if(relativeSink.isSinkOperator() && !relativeSink.isSourceOperator()) {
					
					log.debug("Found real sink {}", relativeSink);
					calcLatencys.addAll(CalcLatencyPostProcessor.insertCalcLatency(relativeSink));
					
				} else if(relativeSink.getSubscriptions().isEmpty()) {
					
					log.debug("Found unreal sink {}", relativeSink);
					calcLatencys.add(CalcLatencyPostProcessor.attachCalcLatency(relativeSink));
					
				}
			}
		}
		
		if(!calcLatencys.isEmpty()) {
			
			ILogicalQueryPart calcLatencyPart = new LogicalQueryPart(calcLatencys);
			allocationMap.put(calcLatencyPart, p2pNetworkManager.getLocalPeerID());
			log.debug("Created local query part {}", calcLatencyPart);
			
		}
		
	}

	private static Collection<CalcLatencyAO> insertCalcLatency(ILogicalOperator relativeSink) {
		
		Collection<CalcLatencyAO> latAOs = Lists.newArrayList();
		
		for(LogicalSubscription subToSource : relativeSink.getSubscribedToSource().toArray(new LogicalSubscription[0])) {
		
			CalcLatencyAO latAO = new CalcLatencyAO();
			ILogicalOperator target = subToSource.getTarget();
			relativeSink.unsubscribeFromSource(subToSource);
			latAO.subscribeToSource(target, 0, subToSource.getSourceOutPort(), subToSource.getSchema());
			latAO.subscribeSink(relativeSink, subToSource.getSinkInPort(), 0, latAO.getOutputSchema());
			latAOs.add(latAO);
			
		}
		
		return latAOs;
		
	}

	private static CalcLatencyAO attachCalcLatency(ILogicalOperator relativeSink) {
		
		CalcLatencyAO latAO = new CalcLatencyAO();
		latAO.subscribeToSource(relativeSink, 0, 0, relativeSink.getOutputSchema());
		return latAO;
		
	}

}