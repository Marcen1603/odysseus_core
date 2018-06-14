package de.uniol.inf.is.odysseus.net.querydistribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public class RoundRobinAllocator extends AbstractRoundRobinAllocator {

	private static final long serialVersionUID = -5528354964652549480L;

	@Override
	public String getName() {
		return "roundrobin";
	}

	@Override
	protected List<IOdysseusNode> determineConsideredNodes(Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode) {
		List<IOdysseusNode> nodes = Lists.newArrayList(knownRemoteNodes);
		return nodes;
	}

}
