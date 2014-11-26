package de.uniol.inf.is.odysseus.peer.recovery.registry;

import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;

public final class RecoveryStrategyRegistry extends NamedInterfaceRegistry<IRecoveryStrategy> {

	private static RecoveryStrategyRegistry instance;

	private RecoveryStrategyRegistry() {
		
	}
	
	public static RecoveryStrategyRegistry getInstance() {
		if( instance == null ) {
			instance = new RecoveryStrategyRegistry();
		}
		return instance;
	}
}
