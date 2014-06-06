package de.uniol.inf.is.odysseus.peer.distribute.postprocessor.forcelocal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryDistributionPostProcessorException;

public class ForceLocalPostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ForceLocalPostProcessor.class);
	
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
		return "forcelocal";
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters ) throws QueryDistributionPostProcessorException {
		LOG.debug("Forcing operators with 'local' as destination name (if any) to be locally executed");

		for( ILogicalQueryPart part : allocationMap.keySet().toArray(new ILogicalQueryPart[0]) ) {
			
			Collection<ILogicalOperator> localOperators = collectLocalOperators(part.getOperators());
			if( !localOperators.isEmpty() ) {
				
				if( localOperators.size() == part.getOperators().size() ) {
					LOG.debug("Forcing query part {} to be executed locally", part);
					allocationMap.put(part, p2pNetworkManager.getLocalPeerID());
				} else {
					LOG.debug("Splitting query part {} into local and non-local", part);
					
					part.removeOperators(localOperators);
					LOG.debug("Query part {} as the non-local one", part);
					
					ILogicalQueryPart localQueryPart = new LogicalQueryPart(localOperators);
					allocationMap.put(localQueryPart, p2pNetworkManager.getLocalPeerID());
					LOG.debug("Query part {} as the local one", localQueryPart);
				}
			}
		}
	}

	private static Collection<ILogicalOperator> collectLocalOperators(Collection<ILogicalOperator> operators) {
		Collection<ILogicalOperator> localOperators = Lists.newArrayList();
		for( ILogicalOperator operator : operators ) {
			if( isDestinationNameLocal(operator)) {
				localOperators.add(operator);
			}
		}
		return localOperators;
	}

	private static boolean isDestinationNameLocal(ILogicalOperator operator) {
		return !Strings.isNullOrEmpty(operator.getDestinationName()) && operator.getDestinationName().equalsIgnoreCase("local");
	}
}
