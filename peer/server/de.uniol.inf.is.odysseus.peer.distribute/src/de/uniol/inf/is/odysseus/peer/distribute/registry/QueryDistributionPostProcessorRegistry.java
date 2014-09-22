package de.uniol.inf.is.odysseus.peer.distribute.registry;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

public final class QueryDistributionPostProcessorRegistry extends NamedInterfaceRegistry<IQueryDistributionPostProcessor> {

	private static QueryDistributionPostProcessorRegistry instance;

	private QueryDistributionPostProcessorRegistry() {
		
	}
	
	public static QueryDistributionPostProcessorRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryDistributionPostProcessorRegistry();
		}
		return instance;
	}
}
