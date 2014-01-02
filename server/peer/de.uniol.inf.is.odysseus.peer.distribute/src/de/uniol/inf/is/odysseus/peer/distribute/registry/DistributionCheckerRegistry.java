package de.uniol.inf.is.odysseus.peer.distribute.registry;

import de.uniol.inf.is.odysseus.peer.distribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

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
