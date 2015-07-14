package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.INode;
import de.uniol.inf.is.odysseus.net.INodeManager;
import de.uniol.inf.is.odysseus.net.INodeManagerListener;

public final class NodeManager implements INodeManager {

	private static final Logger LOG = LoggerFactory.getLogger(NodeManager.class);

	private final Collection<INode> nodes = Lists.newLinkedList();
	private final Collection<INodeManagerListener> listeners = Lists.newLinkedList();

	@Override
	public void addNode(INode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		synchronized (nodes) {
			if (!nodes.contains(node)) {
				nodes.add(node);
				fireNodeAddedEvent(node);
			} else {
				LOG.error("Node {} was already added to the node manager", node);
			}
		}
	}

	private void fireNodeAddedEvent(INode node) {
		synchronized (listeners) {
			for (INodeManagerListener listener : listeners) {
				try {
					listener.nodeAdded(this, node);
				} catch (Throwable t) {
					LOG.error("Got exception from NodeManagerListener on node add", t);
				}
			}
		}
	}

	@Override
	public void removeNode(INode node) {
		synchronized (nodes) {
			if (nodes.contains(node)) {
				nodes.remove(node);
				fireNodeRemovedEvent(node);
			}
		}
	}

	private void fireNodeRemovedEvent(INode node) {
		synchronized (listeners) {
			for (INodeManagerListener listener : listeners) {
				try {
					listener.nodeRemoved(this, node);
				} catch (Throwable t) {
					LOG.error("Got exception from NodeManagerListener on node remove", t);
				}
			}
		}
	}

	@Override
	public ImmutableCollection<INode> getNodes() {
		return ImmutableList.copyOf(nodes);
	}

	@Override
	public void addListener(INodeManagerListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}

		// call nodeAdded for new listener for already existing nodes
		synchronized (nodes) {
			for (INode node : nodes) {
				listener.nodeAdded(this, node);
			}
		}
	}

	@Override
	public void removeListener(INodeManagerListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

}
