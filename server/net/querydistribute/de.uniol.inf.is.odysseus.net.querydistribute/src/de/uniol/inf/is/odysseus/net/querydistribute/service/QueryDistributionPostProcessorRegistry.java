package de.uniol.inf.is.odysseus.net.querydistribute.service;

import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.util.NamedInterfaceRegistry;

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
