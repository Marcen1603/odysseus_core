package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManagerListener;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

public final class OdysseusNodeManager implements IOdysseusNodeManager {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeManager.class);

	private final Map<OdysseusNodeID, IOdysseusNode> nodeMap = Maps.newHashMap();
	private final Collection<IOdysseusNodeManagerListener> listeners = Lists.newLinkedList();

	@Override
	public void addNode(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		synchronized (nodeMap) {
			if (!nodeMap.containsKey(node.getID())) {
				nodeMap.put(node.getID(), node);
				LOG.info("Node {} added", node);

				fireNodeAddedEvent(node);
			} else {
				LOG.error("Node {} was already added to the node manager", node);
			}
		}
	}

	private void fireNodeAddedEvent(IOdysseusNode node) {
		synchronized (listeners) {
			LOG.debug("Fire node add event to listeners");

			for (IOdysseusNodeManagerListener listener : listeners) {
				try {
					listener.nodeAdded(this, node);
				} catch (Throwable t) {
					LOG.error("Got exception from NodeManagerListener on node add", t);
				}
			}
		}
	}

	@Override
	public void removeNode(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		boolean removed = false;
		synchronized (nodeMap) {
			if (nodeMap.containsKey(node.getID())) {
				nodeMap.remove(node.getID());
				removed = true;
			}
		}

		if (removed) {
			LOG.info("Node {} removed", node);
			fireNodeRemovedEvent(node);
		}
	}

	private void fireNodeRemovedEvent(IOdysseusNode node) {
		synchronized (listeners) {
			LOG.debug("Fire node remove event to listeners");

			for (IOdysseusNodeManagerListener listener : listeners) {
				try {
					listener.nodeRemoved(this, node);
				} catch (Throwable t) {
					LOG.error("Got exception from NodeManagerListener on node remove", t);
				}
			}
		}
	}

	@Override
	public ImmutableCollection<IOdysseusNode> getNodes() {
		return ImmutableList.copyOf(nodeMap.values());
	}

	@Override
	public void addListener(IOdysseusNodeManagerListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);

			LOG.debug("Added node manager listener {}", listener);
		}

		// call nodeAdded for new listener for already existing nodes
		synchronized (nodeMap) {
			for (IOdysseusNode node : nodeMap.values()) {
				listener.nodeAdded(this, node);
			}
		}
	}

	@Override
	public void removeListener(IOdysseusNodeManagerListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			listeners.remove(listener);

			LOG.debug("Added node manager listener {}", listener);
		}
	}

	@Override
	public boolean existsNode(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		return existsNode(node.getID());
	}

	@Override
	public Optional<IOdysseusNode> getNode(OdysseusNodeID nodeID) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");

		IOdysseusNode node = null;
		synchronized (nodeMap) {
			node = nodeMap.get(nodeID);
		}

		if (node == null) {
			try {
				IOdysseusNode localNode = getLocalNode();
				if (localNode.getID().equals(nodeID)) {
					return Optional.of(localNode);
				}
			} catch (OdysseusNetException e) {
			}
			return Optional.absent();
		}

		return Optional.fromNullable(node);
	}

	@Override
	public boolean existsNode(OdysseusNodeID nodeID) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");
		
		return getNode(nodeID).isPresent();
	}

	@Override
	public ImmutableCollection<OdysseusNodeID> getNodeIDs() {
		synchronized (nodeMap) {
			return ImmutableList.copyOf(nodeMap.keySet());
		}
	}

	@Override
	public IOdysseusNode getLocalNode() throws OdysseusNetException {
		Optional<IOdysseusNetStartupManager> optStartupManager = OdysseusNetStartupManager.getInstance();
		while(!optStartupManager.isPresent()) {
			// It is possible start the OdysseusNetStartupManager is not activated! Example is, if stuff gets recovered after a crash.
			waitSomeTime();
		}
		if (!optStartupManager.get().isStarted()) {
			throw new OdysseusNetException("OdysseusNet must be started to get local node");
		}
		return optStartupManager.get().getLocalOdysseusNode();
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}
	
	@Override
	public boolean isLocalNode(OdysseusNodeID nodeID) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");
		
		Optional<IOdysseusNode> optNode = getNode(nodeID);
		if( optNode.isPresent() ) {
			return optNode.get().isLocal();
		}
		
		return false;
	}
}
