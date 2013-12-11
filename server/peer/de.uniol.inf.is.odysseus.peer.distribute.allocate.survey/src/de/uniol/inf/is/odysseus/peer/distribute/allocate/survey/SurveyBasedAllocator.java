package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.SubPlanManipulator;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		ID sharedQueryID = IDFactory.newContentID(P2PNetworkManagerService.get().getLocalPeerGroupID(), true);

		// copy --> original
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = copyQueryParts(queryParts);

		Map<SubPlan, ILogicalQueryPart> subPlans = transformToSubPlans(queryPartsCopyMap.keySet());
		Map<String, List<SubPlan>> allocationMap = SurveyBasedAllocatorImpl.allocate(sharedQueryID, subPlans.keySet(), config);
		Map<PeerID, List<SubPlan>> allocationMapPeerID = transformToPeerIDMap(allocationMap);

		Map<PeerID, Collection<ILogicalQueryPart>> allocationMapParts = transformToLogicalQueryParts(allocationMapPeerID, subPlans, queryPartsCopyMap);
		return revertMap(allocationMapParts);
	}

	private static Map<ILogicalQueryPart, ILogicalQueryPart> copyQueryParts(Collection<ILogicalQueryPart> queryParts) {
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		for (ILogicalQueryPart part : queryParts) {
			operators.addAll(part.getOperators());
		}

		// copy --> original
		Map<ILogicalOperator, ILogicalOperator> operatorCopyMap = createOperatorCopyMap(operators);
		createSubscriptionsInCopies(operatorCopyMap);

		Map<ILogicalQueryPart, ILogicalQueryPart> map = Maps.newHashMap();

		for (ILogicalQueryPart queryPart : queryParts) {
			Collection<ILogicalOperator> copyOperatorsOfCopyPart = Lists.newArrayList();

			for (ILogicalOperator queryPartOperator : queryPart.getOperators()) {
				copyOperatorsOfCopyPart.add(getCopyOfMap(queryPartOperator, operatorCopyMap));
			}

			ILogicalQueryPart copyQueryPart = new LogicalQueryPart(copyOperatorsOfCopyPart);
			map.put(copyQueryPart, queryPart);
		}

		return map;
	}

	private static Map<ILogicalOperator, ILogicalOperator> createOperatorCopyMap(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, ILogicalOperator> copyMap = Maps.newHashMap();
		for (ILogicalOperator operator : operators) {
			copyMap.put(operator.clone(), operator);
		}
		return copyMap;
	}

	private static void createSubscriptionsInCopies(Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		for (ILogicalOperator copyOperator : operatorCopyMap.keySet()) {
			ILogicalOperator originalOperator = operatorCopyMap.get(copyOperator);

			for (LogicalSubscription sub : originalOperator.getSubscriptions()) {
				ILogicalOperator originalTarget = sub.getTarget();
				ILogicalOperator copyTarget = getCopyOfMap(originalTarget, operatorCopyMap);

				copyOperator.subscribeSink(copyTarget, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
			}
		}
	}

	private static ILogicalOperator getCopyOfMap(ILogicalOperator originalOperator, Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		for (ILogicalOperator copy : operatorCopyMap.keySet()) {
			if (operatorCopyMap.get(copy).equals(originalOperator)) {
				return copy;
			}
		}

		throw new RuntimeException("Could not find the copy of " + originalOperator);
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
