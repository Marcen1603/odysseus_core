package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManagerListener;

public final class OdysseusNodeManagerListenerBinder {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeManagerListenerBinder.class);
	
	private static IOdysseusNodeManager nodeManager;
	private static Collection<IOdysseusNodeManagerListener> listenerCache = Lists.newArrayList(); // for the case, the listeners are bound before the manager
	
	// called by OSGi-DS
	public static void bindNodeManager( IOdysseusNodeManager manager ) {
		synchronized( listenerCache ) {
			nodeManager = manager;
			LOG.info("Bound node manager");
			
			if( !listenerCache.isEmpty() ) {
				LOG.debug("Emptying cache of {} listeners", listenerCache.size());
				
				for( IOdysseusNodeManagerListener listener : listenerCache ) {
					nodeManager.addListener(listener);
				}
				
				listenerCache.clear();
			}
		}
	}
	
	// called by OSGi-DS
	public static void unbindNodeManager( IOdysseusNodeManager manager ) {
		if( nodeManager == manager ) {
			nodeManager = null;
			LOG.info("Unbound node manager");
		}
	}
	
	// called by OSGi-DS
	public static void bindNodeManagerListener( IOdysseusNodeManagerListener listener ) {
		synchronized( listenerCache ) {
			LOG.info("Bound node manager listener {}", listener);
			
			if( nodeManager != null ) {
				nodeManager.addListener(listener);
				LOG.debug("...into node manager");
				
			} else {
				listenerCache.add(listener);
				LOG.debug("...into cache");
			}
		}
	}
	
	// called by OSGi-DS
	public static void unbindNodeManagerListener( IOdysseusNodeManagerListener listener ) {
		synchronized( listenerCache ) {
			LOG.info("Unbound node manager listener {}", listener);
			
			if( !listenerCache.isEmpty() ) {
				listenerCache.remove(listener);
			}
			
			if( nodeManager != null ) {
				nodeManager.removeListener(listener);
			}
		}
	}
}
