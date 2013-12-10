package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;

public final class PeerCommunicatorListenerRegistry {

	private static PeerCommunicatorListenerRegistry instance;

	private final Collection<IPeerCommunicatorListener> listeners = Lists.newArrayList();

	public static PeerCommunicatorListenerRegistry getInstance() {
		if (instance == null) {
			instance = new PeerCommunicatorListenerRegistry();
		}

		return instance;
	}

	public void add(IPeerCommunicatorListener listener) {
		Preconditions.checkNotNull(listener, "Peer communicator listener to add to registry must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void remove(IPeerCommunicatorListener listener) {
		Preconditions.checkNotNull(listener, "Peer communicator listener to remove from registry must not be null!");

		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	public ImmutableCollection<IPeerCommunicatorListener> getAll() {
		synchronized (listeners) {
			return ImmutableList.copyOf(listeners);
		}
	}
}
