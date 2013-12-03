package de.uniol.inf.is.odysseus.peer.distribute.service;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartitionerRegistry;

public class QueryPartitionerService {

	// called by OSGi-DS
	public void bindQueryPartitioner( IQueryPartitioner partitioner ) {
		QueryPartitionerRegistry.getInstance().add(partitioner);
	}
	
	// called by OSGi-DS
	public void unbindQueryPartitioner( IQueryPartitioner partitioner ) {
		QueryPartitionerRegistry.getInstance().remove(partitioner);
	}
}
