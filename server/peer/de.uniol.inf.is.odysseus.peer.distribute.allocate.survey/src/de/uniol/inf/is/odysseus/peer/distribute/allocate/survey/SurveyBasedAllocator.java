package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.SubPlanManipulator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		ID sharedQueryID = IDFactory.newContentID(P2PNetworkManagerService.get().getLocalPeerGroupID(), true);

		// copy --> original
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);

		Map<SubPlan, ILogicalQueryPart> subPlans = transformToSubPlans(queryPartsCopyMap.keySet());
		Map<String, List<SubPlan>> allocationMap = SurveyBasedAllocatorImpl.allocate(sharedQueryID, subPlans.keySet(), config);
		Map<PeerID, List<SubPlan>> allocationMapPeerID = transformToPeerIDMap(allocationMap);

		Map<PeerID, Collection<ILogicalQueryPart>> allocationMapParts = transformToLogicalQueryParts(allocationMapPeerID, subPlans, queryPartsCopyMap);
		return revertMap(allocationMapParts);
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

	private static Map<PeerID, List<SubPlan>> transformToPeerIDMap(Map<String, List<SubPlan>> allocationMap) {
		Map<PeerID, List<SubPlan>> subPlanMap = Maps.newHashMap();

		for (String peerName : allocationMap.keySet()) {

			PeerID peerID;
			if (isLocal(peerName)) {
				peerID = P2PNetworkManagerService.get().getLocalPeerID();
			} else {
				peerID = toID(peerName);
			}

			subPlanMap.put(peerID, allocationMap.get(peerName));
		}

		return subPlanMap;
	}

	private static boolean isLocal(String peerName) {
		return "local".equalsIgnoreCase(peerName) || P2PNetworkManagerService.get().getLocalPeerID().toString().equalsIgnoreCase(peerName);
	}

	private static PeerID toID(String text) {
		try {
			final URI id = new URI(text);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}

	private static Map<PeerID, Collection<ILogicalQueryPart>> transformToLogicalQueryParts(Map<PeerID, List<SubPlan>> allocationMapPeerID, Map<SubPlan, ILogicalQueryPart> subPlanMap, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<PeerID, Collection<ILogicalQueryPart>> partMap = Maps.newHashMap();
		
		for( PeerID peerID : allocationMapPeerID.keySet() ) {
			Collection<ILogicalQueryPart> queryPartsOfPeerID = Lists.newArrayList();
			
			for( SubPlan subPlan : allocationMapPeerID.get(peerID)) {
				ILogicalQueryPart copyLogicalQueryPart = subPlanMap.get(subPlan);
				ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(copyLogicalQueryPart);
				
				queryPartsOfPeerID.add(originalQueryPart);
			}
			
			partMap.put(peerID, queryPartsOfPeerID);
		}
		
		return partMap;
	}

	private static Map<ILogicalQueryPart, PeerID> revertMap(Map<PeerID, Collection<ILogicalQueryPart>> allocationMapParts) {
		Map<ILogicalQueryPart, PeerID> revertedMap = Maps.newHashMap();
		
		for( PeerID peerID : allocationMapParts.keySet() ) {
			for( ILogicalQueryPart part : allocationMapParts.get(peerID)) {
				revertedMap.put(part, peerID);
			}
		}
		
		return revertedMap;
	}
}
