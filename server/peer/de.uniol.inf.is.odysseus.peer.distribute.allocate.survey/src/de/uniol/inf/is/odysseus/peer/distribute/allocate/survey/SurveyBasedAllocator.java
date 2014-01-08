package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.SubPlanManipulator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class SurveyBasedAllocator implements IQueryPartAllocator {

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
		return "survey";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		// copy --> original
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);

		Map<SubPlan, ILogicalQueryPart> subPlans = transformToSubPlans(queryPartsCopyMap.keySet());
		Map<SubPlan, PeerID> allocationMap = SurveyBasedAllocatorImpl.allocate(subPlans.keySet(), config);
		Map<ILogicalQueryPart, PeerID> allocationMapParts = transformToLogicalQueryParts(allocationMap, subPlans, queryPartsCopyMap);
		
		return allocationMapParts;
	}

	private static Map<SubPlan, ILogicalQueryPart> transformToSubPlans(Collection<ILogicalQueryPart> queryParts) {
		Map<SubPlan, ILogicalQueryPart> partMap = Maps.newHashMap();

		for (ILogicalQueryPart queryPart : queryParts) {
			SubPlan subPlan = new SubPlan(queryPart.getOperators().toArray(new ILogicalOperator[0]));
			partMap.put(subPlan, queryPart);
		}

		SubPlanManipulator.insertDummyAOs(partMap.keySet());

		return partMap;
	}

	private static Map<ILogicalQueryPart, PeerID> transformToLogicalQueryParts(Map<SubPlan, PeerID> allocationMapPeerID, Map<SubPlan, ILogicalQueryPart> subPlanMap, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<ILogicalQueryPart, PeerID> partMap = Maps.newHashMap();
		
		for( SubPlan subPlan : allocationMapPeerID.keySet() ) {
			
			ILogicalQueryPart copyLogicalQueryPart = subPlanMap.get(subPlan);
			ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(copyLogicalQueryPart);
			
			partMap.put(originalQueryPart, allocationMapPeerID.get(subPlan));
		}
		
		return partMap;
	}
}
