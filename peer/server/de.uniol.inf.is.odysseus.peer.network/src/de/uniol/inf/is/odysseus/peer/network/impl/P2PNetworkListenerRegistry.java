package de.uniol.inf.is.odysseus.peer.network.impl;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkListener;

final class P2PNetworkListenerRegistry {

	private static P2PNetworkListenerRegistry instance;
	
	private final List<IP2PNetworkListener> listeners = Lists.newArrayList();

	private P2PNetworkListenerRegistry() {
		// do not instantiate this
	}
	
	public static P2PNetworkListenerRegistry getInstance() {
		if( instance == null ) {
			instance = new P2PNetworkListenerRegistry();
		}
		
		return instance;
	}
	
	public void add( IP2PNetworkListener listener ) {
		Preconditions.checkNotNull(listener, "P2P network listener must not be null!");
		
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void remove( IP2PNetworkListener listener ) {
		Preconditions.checkNotNull(listener, "P2P listener to remove must not be null!");
		
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	public ImmutableCollection<IP2PNetworkListener> getAllListeners() {
		synchronized( listeners ) {
			return ImmutableList.copyOf(listeners);
		}
	}
}
