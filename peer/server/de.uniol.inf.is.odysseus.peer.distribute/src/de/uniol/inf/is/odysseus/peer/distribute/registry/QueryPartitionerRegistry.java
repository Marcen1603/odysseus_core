package de.uniol.inf.is.odysseus.peer.distribute.registry;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

public final class QueryPartitionerRegistry extends NamedInterfaceRegistry<IQueryPartitioner> {

	private static QueryPartitionerRegistry instance;

	private QueryPartitionerRegistry() {
		
	}
	
	public static QueryPartitionerRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartitionerRegistry();
		}
		return instance;
	}

}
