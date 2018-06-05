package de.uniol.inf.is.odysseus.net.communication.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;

public final class OdysseusNodeCommunicatorListenerRegistry {

	private static OdysseusNodeCommunicatorListenerRegistry instance;

	private final Map<Class<? extends IMessage>, Collection<IOdysseusNodeCommunicatorListener>> listenerMap = Maps.newHashMap();

	public static OdysseusNodeCommunicatorListenerRegistry getInstance() {
		if (instance == null) {
			instance = new OdysseusNodeCommunicatorListenerRegistry();
		}

		return instance;
	}

	public void add(IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType) {
		Preconditions.checkNotNull(listener, "Odysseus node communicator listener to add to registry must not be null!");

		synchronized (listenerMap) {
			Collection<IOdysseusNodeCommunicatorListener> listeners = listenerMap.get(messageType);
			if( listeners == null  ) {
				listeners = Lists.newArrayList();
				listenerMap.put(messageType, listeners);
			}
			listeners.add(listener);
		}
	}

	public void remove(IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType) {
		Preconditions.checkNotNull(listener, "Odysseus node communicator listener to remove from registry must not be null!");

		synchronized (listenerMap) {
			Collection<IOdysseusNodeCommunicatorListener> listeners = listenerMap.get(messageType);
			if( listeners != null ) {
				listeners.remove(listener);
				if( listeners.isEmpty() ) {
					listenerMap.remove(messageType);
				}
			}
		}
	}

	public ImmutableCollection<IOdysseusNodeCommunicatorListener> getListeners(Class<? extends IMessage> messageType) {
		Collection<IOdysseusNodeCommunicatorListener> listeners = null;
		synchronized (listenerMap) {
			listeners = listenerMap.get(messageType);
		}

		return listeners != null ? ImmutableList.copyOf(listeners) : ImmutableList.<IOdysseusNodeCommunicatorListener>of();
	}
}
