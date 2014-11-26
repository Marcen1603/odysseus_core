package de.uniol.inf.is.odysseus.peer.recovery.registry;

import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;

public final class RecoveryStrategyManagerRegistry extends NamedInterfaceRegistry<IRecoveryStrategyManager> {

	private static RecoveryStrategyManagerRegistry instance;

	private RecoveryStrategyManagerRegistry() {
		
	}
	
	public static RecoveryStrategyManagerRegistry getInstance() {
		if( instance == null ) {
			instance = new RecoveryStrategyManagerRegistry();
		}
		return instance;
	}
}
