package de.uniol.inf.is.odysseus.net.discovery.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.discovery.INodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.INodeDiscovererManager;

public class NodeDiscovererBinder {

	private static final Logger LOG = LoggerFactory.getLogger(NodeDiscovererBinder.class);
	
	private static final Collection<INodeDiscoverer> CACHE = Lists.newArrayList();
	private static INodeDiscovererManager discovererManager;

	// called by OSGi-DS
	public static void bindNodeDiscoverer(INodeDiscoverer serv) {
		synchronized( CACHE ) {
			LOG.info("Bound node discoverer {}", serv);
			
			if( discovererManager == null ) {
				CACHE.add(serv);
				LOG.debug("...into cache");
				
			} else {
				discovererManager.add(serv);
				LOG.debug("...into discoverer manager");
				
			}
		}
	}

	// called by OSGi-DS
	public static void unbindNodeDiscoverer(INodeDiscoverer serv) {
		synchronized( CACHE ) {
			CACHE.remove(serv);
			LOG.info("Unbound node discoverer {}", serv);
			
			if( discovererManager != null ) {
				discovererManager.remove(serv);
			}
		}
	}

	// called by OSGi-DS
	public static void bindNodeDiscovererManager(INodeDiscovererManager serv) {
		synchronized( CACHE ) {
			discovererManager = serv;
			LOG.info("Bound discoverer manager");
			
			if( !CACHE.isEmpty() ) {
				LOG.debug("Emptying cache of {} discoverers", CACHE.size());
				
				for( INodeDiscoverer discoverer : CACHE) {
					discovererManager.add(discoverer);
				}
				
				CACHE.clear();
			}
		}
	}

	// called by OSGi-DS
	public static void unbindNodeDiscovererManager(INodeDiscovererManager serv) {
		synchronized( CACHE ) {
			if (discovererManager == serv) {
				discovererManager = null;
				LOG.info("Unbound discoverer manager");
				
				CACHE.clear();
			}			
		}

	}
}