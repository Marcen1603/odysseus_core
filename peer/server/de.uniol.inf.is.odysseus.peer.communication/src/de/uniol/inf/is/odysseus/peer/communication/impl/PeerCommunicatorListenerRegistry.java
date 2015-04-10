package de.uniol.inf.is.odysseus.peer.communication.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;

public final class PeerCommunicatorListenerRegistry {

	private static PeerCommunicatorListenerRegistry instance;

	private final Map<Class<? extends IMessage>, Collection<IPeerCommunicatorListener>> listenerMap = Maps.newHashMap();

	public static PeerCommunicatorListenerRegistry getInstance() {
		if (instance == null) {
			instance = new PeerCommunicatorListenerRegistry();
		}

		return instance;
	}

	public void add(IPeerCommunicatorListener listener, Class<? extends IMessage> messageType) {
		Preconditions.checkNotNull(listener, "Peer communicator listener to add to registry must not be null!");

		synchronized (listenerMap) {
			Collection<IPeerCommunicatorListener> listeners = listenerMap.get(messageType);
			if( listeners == null  ) {
				listeners = Lists.newArrayList();
				listenerMap.put(messageType, listeners);
			}
			listeners.add(listener);
		}
	}

	public void remove(IPeerCommunicatorListener listener, Class<? extends IMessage> messageType) {
		Preconditions.checkNotNull(listener, "Peer communicator listener to remove from registry must not be null!");

		synchronized (listenerMap) {
			Collection<IPeerCommunicatorListener> listeners = listenerMap.get(messageType);
			if( listeners != null ) {
				listeners.remove(listener);
				if( listeners.isEmpty() ) {
					listenerMap.remove(messageType);
				}
			}
		}
	}

	public ImmutableCollection<IPeerCommunicatorListener> getListeners(Class<? extends IMessage> messageType) {
		Collection<IPeerCommunicatorListener> listeners = null;
		synchronized (listenerMap) {
			listeners = listenerMap.get(messageType);
		}

		return listeners != null ? ImmutableList.copyOf(listeners) : ImmutableList.<IPeerCommunicatorListener>of();
	}
}
