package de.uniol.inf.is.odysseus.peer.distribute.registry;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

public final class QueryDistributionPreProcessorRegistry extends NamedInterfaceRegistry<IQueryDistributionPreProcessor> {

	private static QueryDistributionPreProcessorRegistry instance;

	private QueryDistributionPreProcessorRegistry() {
		
	}
	
	public static QueryDistributionPreProcessorRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryDistributionPreProcessorRegistry();
		}
		return instance;
	}
}
