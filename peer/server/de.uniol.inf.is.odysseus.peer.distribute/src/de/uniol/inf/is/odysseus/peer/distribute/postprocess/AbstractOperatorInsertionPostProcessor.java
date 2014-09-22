package de.uniol.inf.is.odysseus.peer.distribute.postprocess;

import java.util.Collection;
import java.util.List;
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
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryDistributionPostProcessorException;
import de.uniol.inf.is.odysseus.peer.distribute.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

// TODO javaDoc
public abstract class AbstractOperatorInsertionPostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(AbstractOperatorInsertionPostProcessor.class);
	
	// TODO javaDoc
	protected static boolean isRealSink(ILogicalOperator operator) {
		
		return operator.isSinkOperator() && !operator.isSourceOperator();
		
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, 
			ISession caller, 
			Map<ILogicalQueryPart, PeerID> allocationMap, 
			ILogicalQuery query, QueryBuildConfiguration config,
			List<String> parameters) throws QueryDistributionPostProcessorException {
		
		AbstractOperatorInsertionPostProcessor.log.debug("Begin post processing");
		
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		
		if(!P2PNetworkManagerService.getP2PNetworkManager().isPresent()) {
			
			AbstractOperatorInsertionPostProcessor.log.error("No peer network manager bound!");
			return;
			
		}
		
		for(ILogicalQueryPart queryPart : allocationMap.keySet()) {
			
			AbstractOperatorInsertionPostProcessor.log.debug("Determining real sinks from query part {} to insert operators", queryPart);
			
			Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
			
			for(ILogicalOperator relativeSink : relativeSinks) {
				
				if(AbstractOperatorInsertionPostProcessor.isRealSink(relativeSink)) {
					
					AbstractOperatorInsertionPostProcessor.log.debug("Found real sink {}", relativeSink);
					operators.addAll(this.insertOperator(relativeSink));
					
				} else if(relativeSink.getSubscriptions().isEmpty()) {
					
					AbstractOperatorInsertionPostProcessor.log.debug("Found unreal sink {}", relativeSink);
					operators.add(this.attachOperator(relativeSink));
					
				}
			}
		}
		
		if(!operators.isEmpty()) {
			
			ILogicalQueryPart newPart = new LogicalQueryPart(operators);
			allocationMap.put(newPart, P2PNetworkManagerService.getP2PNetworkManager().get().getLocalPeerID());
			log.debug("Created local query part {}", newPart);
			
		}
		
	}

	/**
	 * Inserts operators between a relative sink and each operator subscribed by the relative sink.
	 */
	protected Collection<ILogicalOperator> insertOperator(ILogicalOperator relativeSink) {
		
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		
		for(LogicalSubscription subToSource : relativeSink.getSubscribedToSource().toArray(new LogicalSubscription[0])) {
		
			ILogicalOperator operator = this.createOperator();
			operator.setOutputSchema(relativeSink.getOutputSchema());
			ILogicalOperator target = subToSource.getTarget();
			relativeSink.unsubscribeFromSource(subToSource);
			operator.subscribeToSource(target, 0, subToSource.getSourceOutPort(), subToSource.getSchema());
			operator.subscribeSink(relativeSink, subToSource.getSinkInPort(), 0, operator.getOutputSchema());
			operator.initialize();
			operators.add(operator);
			
		}
		
		return operators;
		
	}

	/**
	 * Inserts an operator after a real sink.
	 */
	protected ILogicalOperator attachOperator(ILogicalOperator realSink) {
		
		ILogicalOperator operator = this.createOperator();
		operator.setOutputSchema(realSink.getOutputSchema());
		operator.subscribeToSource(realSink, 0, 0, realSink.getOutputSchema());
		operator.initialize();
		return operator;
		
	}
	
	// TODO javaDoc
	protected abstract ILogicalOperator createOperator();
	
}
