package de.uniol.inf.is.odysseus.net.querydistribute.service;

import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.net.util.NamedInterfaceRegistry;

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
