package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;

public final class PeerCommunicatorListenerRegistry {

	private static PeerCommunicatorListenerRegistry instance;

	private final Map<Integer, Collection<IPeerCommunicatorListener>> listenerMap = Maps.newHashMap();

	public static PeerCommunicatorListenerRegistry getInstance() {
		if (instance == null) {
			instance = new PeerCommunicatorListenerRegistry();
		}

		return instance;
	}

	public void add(int id, IPeerCommunicatorListener listener) {
		Preconditions.checkNotNull(listener, "Peer communicator listener to add to registry must not be null!");

		synchronized (listenerMap) {
			Collection<IPeerCommunicatorListener> listeners = listenerMap.get(id);
			if( listeners == null  ) {
				listeners = Lists.newArrayList();
				listenerMap.put(id, listeners);
			}
			listeners.add(listener);
		}
	}

	public void remove(int id, IPeerCommunicatorListener listener) {
		Preconditions.checkNotNull(listener, "Peer communicator listener to remove from registry must not be null!");

		synchronized (listenerMap) {
			Collection<IPeerCommunicatorListener> listeners = listenerMap.get(id);
			if( listeners != null ) {
				listeners.remove(listener);
				if( listeners.isEmpty() ) {
					listenerMap.remove(id);
				}
			}
		}
	}

	public ImmutableCollection<IPeerCommunicatorListener> getListeners(int id) {
		Collection<IPeerCommunicatorListener> listeners = null;
		synchronized (listenerMap) {
			listeners = listenerMap.get(id);
		}

		return listeners != null ? ImmutableList.copyOf(listeners) : ImmutableList.<IPeerCommunicatorListener>of();
	}
}
