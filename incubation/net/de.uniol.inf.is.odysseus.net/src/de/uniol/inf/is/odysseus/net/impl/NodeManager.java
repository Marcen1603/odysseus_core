package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManagerListener;

public final class NodeManager implements IOdysseusNodeManager {

	private static final Logger LOG = LoggerFactory.getLogger(NodeManager.class);

	private final Collection<IOdysseusNode> nodes = Lists.newLinkedList();
	private final Collection<IOdysseusNodeManagerListener> listeners = Lists.newLinkedList();

	@Override
	public void addNode(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		synchronized (nodes) {
			if (!nodes.contains(node)) {
				nodes.add(node);
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
		
		synchronized (nodes) {
			if (nodes.contains(node)) {
				nodes.remove(node);
				LOG.info("Node {} removed", node);
				
				fireNodeRemovedEvent(node);
			}
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
		return ImmutableList.copyOf(nodes);
	}

	@Override
	public void addListener(IOdysseusNodeManagerListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
			
			LOG.debug("Added node manager listener {}", listener);
		}

		// call nodeAdded for new listener for already existing nodes
		synchronized (nodes) {
			for (IOdysseusNode node : nodes) {
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

}
