package de.uniol.inf.is.odysseus.peer.distribute.registry;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;

public final class QueryPartAllocatorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartAllocatorRegistry.class);
	
	private static QueryPartAllocatorRegistry instance;
	
	private final Map<String, IQueryPartAllocator> allocatorMap = Maps.newHashMap();

	public static QueryPartAllocatorRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartAllocatorRegistry();
		}
		return instance;
	}
	
	public void add( IQueryPartAllocator allocator ) {
		Preconditions.checkNotNull(allocator, "Query part allocator to add to registry must not be null!");
		Preconditions.checkArgument(!contains(allocator.getName()), "Query part allocator %s already registered", allocator.getClass());
		
		allocatorMap.put(allocator.getName().toUpperCase(), allocator);
		LOG.debug("Query part allocator added : {}", allocator.getName().toUpperCase());
	}
	
	public void remove( IQueryPartAllocator allocator ) {
		Preconditions.checkNotNull(allocator, "Query part allocator to remove from registry must not be null!");
		
		String allocatorName = allocator.getName().toUpperCase();
		if( allocatorMap.containsKey(allocatorName)) {
			allocatorMap.remove(allocatorName);
			LOG.debug("Query part allocator removed : {}", allocatorName);
		} else {
			LOG.warn("Tried to remove query part allocator which was not registered before: {}", allocatorName);
		}
	}
	
	public boolean contains( String name) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of query part allocator must not be null or empty!");
		
		return allocatorMap.containsKey(name.toUpperCase());
	}
	
	public ImmutableCollection<String> getNames() {
		return ImmutableList.copyOf(allocatorMap.keySet());
	}
	
	public IQueryPartAllocator get( String name ) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of query part allocator to get must not be null or empty!");
		
		return allocatorMap.get(name.toUpperCase());
	}
}
