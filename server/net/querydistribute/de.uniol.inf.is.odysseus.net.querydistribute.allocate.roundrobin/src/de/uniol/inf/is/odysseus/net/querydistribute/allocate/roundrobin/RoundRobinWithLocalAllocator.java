package de.uniol.inf.is.odysseus.net.querydistribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public class RoundRobinWithLocalAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		return "roundrobinwithlocal";
	}

	@Override
	protected List<IOdysseusNode> determineConsideredNodes(Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode) {
		List<IOdysseusNode> nodes = Lists.newArrayList();
		nodes.add(localNode);
		nodes.addAll(knownRemoteNodes);
		return nodes;
	}

}
