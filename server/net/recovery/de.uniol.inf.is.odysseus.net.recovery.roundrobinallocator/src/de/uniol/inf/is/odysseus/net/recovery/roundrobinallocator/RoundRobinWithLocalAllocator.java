package de.uniol.inf.is.odysseus.net.recovery.roundrobinallocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

/**
 * The round robin allocator that includes the local node.
 * 
 * @author Simon Kuespert
 */
public class RoundRobinWithLocalAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {

		return "roundrobinwithlocal";

	}

	@Override
	protected List<OdysseusNodeID> determineConsideredNodeIDs(Collection<OdysseusNodeID> knownRemoteNodes,
			OdysseusNodeID localNodeID) {
		List<OdysseusNodeID> nodes = new ArrayList<OdysseusNodeID>();
		// don't change the order of peers !!!
		// Round Robin now first allocates all other peers and after that the
		// own peer
		nodes.add(localNodeID);
		nodes.addAll(knownRemoteNodes);
		return nodes;
	}

}