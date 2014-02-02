package de.uniol.inf.is.odysseus.peer.distribute.postprocess.calclatency;

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
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * The {@link CalcLatencyPostProcessor} inserts a {@link CalcLatencyAO} for each sink within a query.
 * @author Michael Brand
 */
public class CalcLatencyPostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(CalcLatencyPostProcessor.class);
	
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
		
		if(!Activator.getP2PNetworkManager().isPresent()) {
			
			CalcLatencyPostProcessor.log.error("No peer network manager bound!");
			return;
			
		}
		
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
			allocationMap.put(calcLatencyPart, Activator.getP2PNetworkManager().get().getLocalPeerID());
			log.debug("Created local query part {}", calcLatencyPart);
			
		}
		
	}

	/**
	 * Inserts {@link CalcLatencyAO}s between a relative sink and each operator subscribed by the relative sink.
	 */
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

	/**
	 * Inserts a {@link CalcLatencyAO} after a real sink.
	 */
	private static CalcLatencyAO attachCalcLatency(ILogicalOperator realSink) {
		
		CalcLatencyAO latAO = new CalcLatencyAO();
		latAO.subscribeToSource(realSink, 0, 0, realSink.getOutputSchema());
		return latAO;
		
	}

}