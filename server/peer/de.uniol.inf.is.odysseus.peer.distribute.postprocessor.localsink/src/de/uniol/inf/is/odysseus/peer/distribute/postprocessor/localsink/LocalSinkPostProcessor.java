package de.uniol.inf.is.odysseus.peer.distribute.postprocessor.localsink;


import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.postprocess.AbstractOperatorInsertionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class LocalSinkPostProcessor extends AbstractOperatorInsertionPostProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(LocalSinkPostProcessor.class);
	
	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	@Override
	public String getName() {
		
		return "localsink";
		
	}
	
	@Override
	public void postProcess(IServerExecutor serverExecutor, 
			ISession caller, 
			Map<ILogicalQueryPart, PeerID> allocationMap, 
			ILogicalQuery query, QueryBuildConfiguration config) {
		
		LocalSinkPostProcessor.log.debug("Begin post processing");
		
		if(LocalSinkPostProcessor.p2pNetworkManager == null) {
			
			LocalSinkPostProcessor.log.error("No peer network manager bound!");
			return;
			
		}
		
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		
		for(ILogicalQueryPart queryPart : allocationMap.keySet()) {
			
			LocalSinkPostProcessor.log.debug("Determining real sinks from query part {} to insert operators", queryPart);
			
			Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
			Collection<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
			
			for(ILogicalOperator relativeSink : relativeSinks) {
				
				if(AbstractOperatorInsertionPostProcessor.isRealSink(relativeSink)) {
					
					LocalSinkPostProcessor.log.debug("Found real sink {}", relativeSink);
					operators.addAll(this.insertOperator(relativeSink));
					operatorsToRemove.add(relativeSink);
					
				} else if(relativeSink.getSubscriptions().isEmpty()) {
					
					LocalSinkPostProcessor.log.debug("Found unreal sink {}", relativeSink);
					operators.add(this.attachOperator(relativeSink));
					
				}
			}
			
			queryPart.removeOperators(operatorsToRemove);
			
		}
		
		if(!operators.isEmpty()) {
			
			ILogicalQueryPart newPart = new LogicalQueryPart(operators);
			allocationMap.put(newPart, LocalSinkPostProcessor.p2pNetworkManager.getLocalPeerID());
			LocalSinkPostProcessor.log.debug("Created local query part {}", newPart);
			
		}
		
	}
	
	@Override
	protected Collection<ILogicalOperator> insertOperator(ILogicalOperator relativeSink) {
		
		Collection<ILogicalOperator> operators = Lists.newArrayList(relativeSink);
		return operators;
		
	}
	
	@Override
	protected ILogicalOperator createOperator() {
		
		RenameAO renameAO = new RenameAO(); 
		renameAO.setNoOp(true);
		return renameAO;
		
	}

}
