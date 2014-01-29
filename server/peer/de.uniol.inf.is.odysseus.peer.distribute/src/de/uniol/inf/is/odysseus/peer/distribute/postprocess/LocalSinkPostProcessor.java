package de.uniol.inf.is.odysseus.peer.distribute.postprocess;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class LocalSinkPostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(LocalSinkPostProcessor.class);
	
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
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, ILogicalQuery query, QueryBuildConfiguration config) {
		LOG.debug("Begin post processing");
		
		Collection<ILogicalOperator> realSinks = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : allocationMap.keySet().toArray(new ILogicalQueryPart[0])) {
			LOG.debug("Determining real sinks from query part {}", queryPart);
			
			Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
			
			for( ILogicalOperator relativeSink : relativeSinks ) {
				if( isRealSink(relativeSink)) {
					LOG.debug("Found real sink {}", relativeSink);
					realSinks.add(relativeSink);
					
					queryPart.getOperatorsWriteable().remove(relativeSink);
				} else if( relativeSink.getSubscriptions().isEmpty() ) {
					LOG.debug("Found nonreal sink {}", relativeSink);
					
					RenameAO renameAO = new RenameAO(); 
					renameAO.setNoOp(true);
					renameAO.setOutputSchema(relativeSink.getOutputSchema());
					LOG.debug("Created local renameAO {}", renameAO);
					
					relativeSink.subscribeSink(renameAO, 0, 0, relativeSink.getOutputSchema());
					
					realSinks.add(renameAO);
				}
			}
		}
		
		if( !realSinks.isEmpty() ) {
			ILogicalQueryPart localSinkQueryPart = new LogicalQueryPart(realSinks);
			allocationMap.put(localSinkQueryPart, p2pNetworkManager.getLocalPeerID());
			LOG.debug("Created local query part {}", localSinkQueryPart);
		}
	}

	private static boolean isRealSink(ILogicalOperator relativeSink) {
		return relativeSink.isSinkOperator() && !relativeSink.isSourceOperator();
	}

}
