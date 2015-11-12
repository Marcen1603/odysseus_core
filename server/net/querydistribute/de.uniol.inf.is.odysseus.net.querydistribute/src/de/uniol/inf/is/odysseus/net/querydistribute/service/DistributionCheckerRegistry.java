package de.uniol.inf.is.odysseus.net.querydistribute.service;

import de.uniol.inf.is.odysseus.net.querydistribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.net.util.NamedInterfaceRegistry;

public final class DistributionCheckerRegistry extends NamedInterfaceRegistry<IDistributionChecker> {

	private static DistributionCheckerRegistry instance;
	
	private DistributionCheckerRegistry() {
		
	}
	
	public static DistributionCheckerRegistry getInstance() {
		if( instance == null ) {
			instance = new DistributionCheckerRegistry();
		}
		return instance;
	}
}
