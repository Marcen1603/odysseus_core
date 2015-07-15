package de.uniol.inf.is.odysseus.net.discovery.impl;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.discovery.INodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.INodeDiscovererManager;

public class NodeDiscovererBinder {

	private static final Collection<INodeDiscoverer> CACHE = Lists.newArrayList();
	private static INodeDiscovererManager discovererManager;

	// called by OSGi-DS
	public static void bindNodeDiscoverer(INodeDiscoverer serv) {
		synchronized( CACHE ) {
			if( discovererManager == null ) {
				CACHE.add(serv);
			} else {
				discovererManager.add(serv);
			}
		}
	}

	// called by OSGi-DS
	public static void unbindNodeDiscoverer(INodeDiscoverer serv) {
		synchronized( CACHE ) {
			CACHE.remove(serv);
			
			if( discovererManager != null ) {
				discovererManager.remove(serv);
			}
		}
	}

	// called by OSGi-DS
	public static void bindNodeDiscovererManager(INodeDiscovererManager serv) {
		synchronized( CACHE ) {
			discovererManager = serv;
			
			for( INodeDiscoverer discoverer : CACHE) {
				discovererManager.add(discoverer);
			}
			
			CACHE.clear();
		}
	}

	// called by OSGi-DS
	public static void unbindNodeDiscovererManager(INodeDiscovererManager serv) {
		synchronized( CACHE ) {
			if (discovererManager == serv) {
				discovererManager = null;
				
				CACHE.clear();
			}			
		}

	}
}