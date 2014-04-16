package de.uniol.inf.is.odysseus.peer.distribute.postprocessor.forcelocalsources;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;

/**
 * The force local sources post processor forces all source accesses to be executed on the local peer.
 * @author Michael Brand
 */
public class ForceLocalSourcesPostProcessor implements
		IQueryDistributionPostProcessor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ForceLocalSourcesPostProcessor.class);
	
	/**
	 * The bound Peer-To-Peer network manager for access to peer ids and names.
	 */
	private static IP2PNetworkManager p2pNetworkManager;

	/**
	 * Binds the Peer-To-Peer network manager. <br />
	 * Called by OSGi-DS.
	 * @param serv The Peer-To-Peer network manager to bind.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		p2pNetworkManager = serv;
		
	}

	/**
	 * Unbinds the Peer-To-Peer network manager, if <code>serv</code> is the one bound. <br />
	 * Called by OSGi-DS.
	 * @param serv The Peer-To-Peer network manager to unbind.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		if(ForceLocalSourcesPostProcessor.p2pNetworkManager == serv)
			ForceLocalSourcesPostProcessor.p2pNetworkManager = null;

	}
	
	@Override
	public String getName() {
		
		return "forcelocalsources";
		
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, ILogicalQuery query, QueryBuildConfiguration config) {
		
		ForceLocalSourcesPostProcessor.LOG.debug("Forcing all source accesses to be executed locally");

		for(ILogicalQueryPart part : allocationMap.keySet().toArray(new ILogicalQueryPart[0])) {
			
			Collection<ILogicalOperator> sources = ForceLocalSourcesPostProcessor.collectSourceAccesses(part.getOperators());
			
			if(!sources.isEmpty() ) {
				
				if(sources.size() == part.getOperators().size() ) {
					
					ForceLocalSourcesPostProcessor.LOG.debug("Forcing query part {} to be executed locally", part);
					allocationMap.put(part, p2pNetworkManager.getLocalPeerID());
					
				} else {
					
					ForceLocalSourcesPostProcessor.LOG.debug("Splitting query part {} into local and non-local", part);
					
					part.removeOperators(sources);
					ForceLocalSourcesPostProcessor.LOG.debug("Query part {} as the non-local one", part);
					
					ILogicalQueryPart localQueryPart = new LogicalQueryPart(sources);
					allocationMap.put(localQueryPart, p2pNetworkManager.getLocalPeerID());
					ForceLocalSourcesPostProcessor.LOG.debug("Query part {} as the local one", localQueryPart);
					
				}
				
			}
			
		}
		
	}

	/**
	 * Collects all instances of {@link AbstractAccessAO}. <br />
	 * Note: Following {@link RenameAO}s will be collected too.
	 * @param operators A collection of operators to collect from.
	 * @return A collection containing all instances of {@link AbstractAccessAO}.
	 */
	private static Collection<ILogicalOperator> collectSourceAccesses(Collection<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators, "Collection of operators must be not null!");
		
		Collection<ILogicalOperator> sources = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof AbstractAccessAO) {
				
				sources.add(operator);
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					if(subToSink.getTarget() instanceof RenameAO)
						sources.add(subToSink.getTarget());
					
				}
				
			}
			
		}
		
		return sources;
	}

}