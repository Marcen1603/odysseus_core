package de.uniol.inf.is.odysseus.net.discovery.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.discovery.INodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.INodeDiscovererManager;

public final class NodeDiscovererManager implements INodeDiscovererManager {

	private static final Logger LOG = LoggerFactory.getLogger(NodeDiscovererManager.class);
	private final Map<String, INodeDiscoverer> discovererMap = Maps.newHashMap();
	
	@Override
	public void add( INodeDiscoverer discoverer ) {
		Preconditions.checkNotNull(discoverer, "discoverer must not be null!");
		
		String discovererName = determineName(discoverer);
		synchronized( discovererMap ) {
			if( !discovererMap.containsKey(discovererName)) {
				discovererMap.put(discovererName, discoverer);
				LOG.info("Added node discoverer {}", discovererName);
				
			} else {
				LOG.error("NodeDiscoverer '{}' already added to discoverer manager", discovererName);
			}
		}
	}
	
	@Override
	public void remove( INodeDiscoverer discoverer ) {
		String discovererName = determineName(discoverer);
		
		synchronized (discovererMap) {
			if( discovererMap.containsKey(discovererName)) {
				discovererMap.remove(discovererName);
				LOG.info("Removed node discoverer {}", discovererName);
			} else {
				LOG.warn("Tried to remove non-added node discoverer {}", discovererName);
			}
		}
	}
	
	private static final String determineName( INodeDiscoverer discoverer ) {
		return discoverer.getClass().getSimpleName();
	}
	
	@Override
	public ImmutableCollection<String> getNames() {
		synchronized (discovererMap) {
			return ImmutableList.copyOf(discovererMap.keySet());
		}
	}
	
	@Override
	public boolean existsName( String discovererName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(discovererName), "discovererName must not be null or empty!");
		
		synchronized (discovererMap) {	
			return discovererMap.containsKey(discovererName);
		}
	}

	@Override
	public Optional<INodeDiscoverer> get( String discovererName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(discovererName), "discovererName must not be null or empty!");
		
		synchronized( discovererMap ) {
			return Optional.fromNullable(discovererMap.get(discovererName));
		}
	}
}
