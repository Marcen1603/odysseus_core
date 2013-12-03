package de.uniol.inf.is.odysseus.peer.distribute.registry;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;

public final class QueryPartitionerRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartitionerRegistry.class);
	
	private static QueryPartitionerRegistry instance;
	
	private final Map<String, IQueryPartitioner> partitionerMap = Maps.newHashMap();

	public static QueryPartitionerRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartitionerRegistry();
		}
		return instance;
	}
	
	public void add( IQueryPartitioner partitioner ) {
		Preconditions.checkNotNull(partitioner, "Query partitioner to add to registry must not be null!");
		Preconditions.checkArgument(!contains(partitioner.getName()), "Query partitioner %s already registered", partitioner.getClass());
		
		partitionerMap.put(partitioner.getName().toUpperCase(), partitioner);
		LOG.debug("Query partitioner added : {}", partitioner.getName().toUpperCase());
	}
	
	public void remove( IQueryPartitioner partitioner ) {
		Preconditions.checkNotNull(partitioner, "Query partitioner to remove from registry must not be null!");
		
		String partitionerName = partitioner.getName().toUpperCase();
		if( partitionerMap.containsKey(partitionerName)) {
			partitionerMap.remove(partitionerName);
			LOG.debug("Query partitioner removed : {}", partitionerName);
		} else {
			LOG.warn("Tried to remove query partitioner which was not registered before: {}", partitionerName);
		}
	}
	
	public boolean contains( String name) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of query partitioner must not be null or empty!");
		
		return partitionerMap.containsKey(name.toUpperCase());
	}
	
	public ImmutableCollection<String> getNames() {
		return ImmutableList.copyOf(partitionerMap.keySet());
	}
	
	public IQueryPartitioner get( String name ) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of query partitioner to get must not be null or empty!");
		
		return partitionerMap.get(name.toUpperCase());
	}
}
