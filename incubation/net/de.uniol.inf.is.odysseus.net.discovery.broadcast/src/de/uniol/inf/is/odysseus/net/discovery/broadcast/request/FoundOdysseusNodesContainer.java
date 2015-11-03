package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;

public class FoundOdysseusNodesContainer {

	private final IOdysseusNodeManager nodeManager;
	private final List<IOdysseusNode> foundOdysseusNodes = Lists.newArrayList();
	private final List<IOdysseusNode> potenciallyLostNodes = Lists.newArrayList();

	public FoundOdysseusNodesContainer(IOdysseusNodeManager nodeManager) {
		Preconditions.checkNotNull(nodeManager, "nodeManager must not be null!");

		this.nodeManager = nodeManager;
	}

	// called from broadcast discoverer
	public void addFoundNode(IOdysseusNode newNode) {
		Preconditions.checkNotNull(newNode, "newNode must not be null!");

		synchronized (foundOdysseusNodes) {
			if( !foundOdysseusNodes.contains(newNode)) {
				foundOdysseusNodes.add(newNode);
			}
		}
	}

	// called from broadcast sender
	public void processFoundNodes() {
		List<IOdysseusNode> newNodes = Lists.newArrayList();
		synchronized (foundOdysseusNodes) {
			newNodes.addAll(foundOdysseusNodes);
			foundOdysseusNodes.clear();
		}

		Collection<IOdysseusNode> oldNodes = Lists.newArrayList(nodeManager.getNodes());

		for (IOdysseusNode newNode : newNodes) {
			if (!oldNodes.contains(newNode)) {
				// newNode is completely new!
				nodeManager.addNode(newNode);
				oldNodes.remove(newNode);
			}
		}

		// list oldNodes contains nodes now which are not found now
		for (IOdysseusNode oldNode : oldNodes) {
			if (!BroadcastDiscoveryPlugIn.getOdysseusNodeConnectionManager().isConnected(oldNode)) {
				if (!potenciallyLostNodes.contains(oldNode)) {
					// maybe lost... give node last chance to next process
					// invocation
					potenciallyLostNodes.add(oldNode);

				} else {
					// was not found last time --> remove the node now!
					potenciallyLostNodes.remove(oldNode);
					nodeManager.removeNode(oldNode);
				}
			}
		}

	}

}
