package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.INodeManager;
import de.uniol.inf.is.odysseus.net.INodeManagerListener;

public final class NodeManagerListenerBinder {

	private static INodeManager nodeManager;
	private static Collection<INodeManagerListener> listenerCache = Lists.newArrayList(); // for the case, the listeners are bound before the manager
	
	// called by OSGi-DS
	public static void bindNodeManager( INodeManager manager ) {
		synchronized( listenerCache ) {
			nodeManager = manager;
		
			for( INodeManagerListener listener : listenerCache ) {
				nodeManager.addListener(listener);
			}
			
			listenerCache.clear();
		}
	}
	
	// called by OSGi-DS
	public static void unbindNodeManager( INodeManager manager ) {
		if( nodeManager == manager ) {
			nodeManager = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindNodeManagerListener( INodeManagerListener listener ) {
		synchronized( listenerCache ) {
			if( nodeManager != null ) {
				nodeManager.addListener(listener);
			} else {
				listenerCache.add(listener);
			}
		}
	}
	
	// called by OSGi-DS
	public static void unbindNodeManagerListener( INodeManagerListener listener ) {
		synchronized( listenerCache ) {
			if( !listenerCache.isEmpty() ) {
				listenerCache.remove(listener);
			}
			
			if( nodeManager != null ) {
				nodeManager.removeListener(listener);
			}
		}
	}
}
