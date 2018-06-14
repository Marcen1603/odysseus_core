package de.uniol.inf.is.odysseus.net.querydistribute.allocate.roundrobin;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;

public abstract class AbstractRoundRobinAllocator implements IQueryPartAllocator {

	private static final long serialVersionUID = -5122320619851079877L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractRoundRobinAllocator.class);
	
	private IOdysseusNode lastAllocatedNode;

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		LOG.debug("Begin round robin allocation");

		List<IOdysseusNode> nodes = determineConsideredNodes(knownRemoteNodes, localNode);

		if (LOG.isDebugEnabled()) {
			logNodes(nodes);
		}

		return roundRobinImpl(queryParts, nodes);
	}

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> reallocate(Map<ILogicalQueryPart, IOdysseusNode> previousAllocationMap, Collection<IOdysseusNode> faultynodes, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		LOG.debug("Begin round robin reallocation");

		List<IOdysseusNode> nodes = determineConsideredNodes(knownRemoteNodes, localNode);
		nodes.removeAll(faultynodes);
		if (LOG.isDebugEnabled()) {
			logNodes(nodes);
		}

		return roundRobinImpl(previousAllocationMap.keySet(), nodes);
	}

	private Map<ILogicalQueryPart, IOdysseusNode> roundRobinImpl(Collection<ILogicalQueryPart> queryParts, List<IOdysseusNode> nodes) throws QueryPartAllocationException {
		if (nodes == null || nodes.isEmpty()) {
			throw new QueryPartAllocationException("There are no peers left to be considered for round robin in reallocation");
		}

		boolean lastNodeAdded = false;
		if( !nodes.contains(lastAllocatedNode)) {
			nodes.add(lastAllocatedNode);
			lastNodeAdded = true;
		}
		
		Collections.sort(nodes, new Comparator<IOdysseusNode>() {
			@Override
			public int compare(IOdysseusNode o1, IOdysseusNode o2) {
				if (o1 == null) {
					if (o2 == null) {
						return 0;
					}
					return 1;
				}
				if (o2 == null) {
					return -1;
				}
				return o1.getID().compareTo(o2.getID());
			}
		});

		int nodeIndex = nodes.indexOf(lastAllocatedNode);
		if( lastNodeAdded ) {
			nodes.remove(lastAllocatedNode);
			if( nodeIndex == nodes.size() ) {
				nodeIndex = 0;
			}
		}

		Map<ILogicalQueryPart, IOdysseusNode> allocationMap = Maps.newHashMap();

		for (ILogicalQueryPart queryPart : queryParts) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Allocating query part {}", queryPart);
				if (!queryPart.getAvoidingQueryParts().isEmpty()) {
					LOG.debug("Avoiding query parts");
					for (ILogicalQueryPart avoidedQueryPart : queryPart.getAvoidingQueryParts()) {
						LOG.debug("\t{}", avoidedQueryPart);
					}
				}
			}

			Collection<IOdysseusNode> nonAllowedNodes = determineAvoidedNodes(queryPart, allocationMap);
			if (!nonAllowedNodes.isEmpty()) {
				LOG.debug("Avoiding nodes for this query part:");
				logNodes(nonAllowedNodes);
			}

			int tries = 0;
			int tempNodeIndex = nodeIndex;
			IOdysseusNode chosenNode = null;

			do {
				if (tries >= nodes.size()) {
					// checked all possibilities
					chosenNode = null;
					break;
				}
				tempNodeIndex = (tempNodeIndex + 1) % nodes.size();
				chosenNode = nodes.get(tempNodeIndex);
				tries++;

			} while (nonAllowedNodes.contains(chosenNode));

			if (chosenNode == null) {
				LOG.debug("Could not allocate to a node which is not avoided...");
				nodeIndex = (nodeIndex + 1) % nodes.size();
				chosenNode = nodes.get(nodeIndex);
			} else {
				LOG.debug("Could allocate to non-avoided node");
				nodeIndex = tempNodeIndex;
			}

			LOG.debug("Allocated to node {} for query part {}", chosenNode, queryPart);
			allocationMap.put(queryPart, chosenNode);
			lastAllocatedNode = chosenNode;
		}

		LOG.debug("RoundRobinAllocation finished");
		return allocationMap;
	}

	private static void logNodes(Collection<IOdysseusNode> nodes) {
		if (nodes == null || nodes.isEmpty()) {
			LOG.debug("\t<empty>");
			return;
		}

		for (IOdysseusNode node : nodes) {
			LOG.debug("\t{}", node.getName());
		}
	}

	private static Collection<IOdysseusNode> determineAvoidedNodes(ILogicalQueryPart queryPart, Map<ILogicalQueryPart, IOdysseusNode> allocationMap) {
		Collection<IOdysseusNode> avoidedNodes = Lists.newArrayList();

		for (ILogicalQueryPart avoidedPart : queryPart.getAvoidingQueryParts()) {
			IOdysseusNode avoidedNode = allocationMap.get(avoidedPart);
			if (avoidedNode != null && !avoidedNodes.contains(avoidedNode)) {
				avoidedNodes.add(avoidedNode);
			}
		}

		return avoidedNodes;
	}

	protected abstract List<IOdysseusNode> determineConsideredNodes(Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode);
}
