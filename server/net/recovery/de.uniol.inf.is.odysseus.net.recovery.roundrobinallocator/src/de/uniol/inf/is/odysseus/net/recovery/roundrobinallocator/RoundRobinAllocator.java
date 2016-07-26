package de.uniol.inf.is.odysseus.net.recovery.roundrobinallocator;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

/**
 * The round robin allocator that excludes the local node.
 * 
 * @author Simon Kuespert
 */
public class RoundRobinAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		return "roundrobin";
	}

	@Override
	protected List<OdysseusNodeID> determineConsideredNodeIDs(Collection<OdysseusNodeID> knownRemoteNodes,
			OdysseusNodeID localNodeID) {
		List<OdysseusNodeID> nodes = Lists.newArrayList(knownRemoteNodes);
		return nodes;
	}

}